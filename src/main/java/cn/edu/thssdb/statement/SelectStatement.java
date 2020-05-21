package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.expression.Expression;
import cn.edu.thssdb.expression.SourceTable;
import cn.edu.thssdb.expression.Variable;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.*;
import javafx.util.Pair;

import javax.management.Query;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class SelectStatement implements Statement {

    private ArrayList<Column.FullName> selectedColumns;
    private SourceTable sourceTable;
    private Expression expression;

    public SelectStatement(ArrayList<Column.FullName> selectedColumns,
                           SourceTable sourceTable,
                           Expression expression) {
        this.selectedColumns = selectedColumns;
        this.sourceTable = sourceTable;
        this.expression = expression;
    }

    public Row permuteRow(Row rawRow, ArrayList<Integer> permute) {
        Row resultRow = new Row();
        for (Integer index: permute) {
            resultRow.appendEntry(rawRow.getEntries().get(index));
        }
        return resultRow;
    }

    @Override
    public QueryTable execute(Manager manager, Long sessionId) {
        Database database = manager.getSessionCurrentDatabase(sessionId);
        QueryTable resultTable;

        if (sourceTable.joinOps.isEmpty()) {
            // 没有join或者cartesian操作, 单表源, 可以尝试主键检索.
            Table baseTable = database.getTable(sourceTable.tableName);
            if (baseTable == null) {
                throw new SQLHandleException("Exception: table " + sourceTable.tableName + " could not be found.");
            }
            ArrayList<Integer> primaryIndices = baseTable.getPrimaryIndices();
            ArrayList<Column> baseColumns = baseTable.getCopiedColumns(true);
            HashMap<String, Integer> columnMap = baseTable.getColumnIndicesMap();

            //先获取选择后的列以及排列顺序. 然后建立结果空表.
            ArrayList<Column> resultColumns = new ArrayList<>();
            ArrayList<Integer> resultIndices = new ArrayList<>();
            this.setColumnsAndIndices(this.selectedColumns, baseTable, resultColumns, resultIndices);
            resultTable = new QueryTable("Result", resultColumns);

            // 查找出所有主键属性.
            ArrayList<Column.FullName> primaryKeys = new ArrayList<>();
            if (sourceTable.alias != null) {
                for (Column c: baseColumns) {
                    c.setPrefix(sourceTable.alias);
                }
            }
            for (int index: primaryIndices) {
                primaryKeys.add(baseColumns.get(index).getColumnFullName());
            }
            ArrayList<Comparable> primaryValues = this.expression.tryToGetPrimaryValue(primaryKeys);
            if (!primaryValues.isEmpty()) {
                boolean isPrimarySearch = true;
                for (Comparable value: primaryValues) {
                    if (value == null) {
                        isPrimarySearch = false;
                        break;
                    }
                }
                // 主键所有属性都被指定, 为主键检索.
                if (isPrimarySearch) {
                    MultiEntry searchKey = new MultiEntry();
                    for (Comparable value: primaryValues) {
                        searchKey.addEntry(Entry.generateEntry(value));
                    }
                    Row searchRow = baseTable.search(searchKey);
                    if (searchRow != null) {
                        resultTable.rows.add(this.permuteRow(searchRow, resultIndices));
                        return resultTable;
                    }
                }
            }
            // 否则将遍历搜索
            // 获取所有需要赋值的Variable, 以及赋值时需要索引到行的下标.
            ArrayList<Variable> variables = this.expression.getAllVariables();
            int variableNum = variables.size();
            ArrayList<Integer> assignIndices = new ArrayList<>();
            this.setAssignIndices(variables, baseTable, assignIndices);

            Iterator<Row> iterator = baseTable.iterator();
            while (iterator.hasNext()) {
                Row rawRow = iterator.next();
                for (int i = 0; i < variableNum; i++) {
                    variables.get(i).assignValue(rawRow.getEntries().get(assignIndices.get(i)).value);
                }
                if (this.expression.evaluate()) {
                    resultTable.rows.add(this.permuteRow(rawRow, resultIndices));
                }
            }
            return resultTable;
        } else {
            // 获取Join后的总源表
            QueryTable baseQueryTable = sourceTable.getQueryTable(database);
            ArrayList<Column> resultColumns = new ArrayList<>();
            ArrayList<Integer> resultIndices = new ArrayList<>();
            this.setColumnsAndIndices(this.selectedColumns, baseQueryTable, resultColumns, resultIndices);
            resultTable = new QueryTable("Result", resultColumns);

            // 同样遍历搜索
            // 获取所有需要赋值的Variable.
            ArrayList<Integer> assignIndices = new ArrayList<>();
            ArrayList<Variable> variables = this.expression.getAllVariables();
            this.setAssignIndices(variables, baseQueryTable, assignIndices);
            int variableNum = assignIndices.size();

            // 判断每一行是否满足条件
            for (Row rawRow: baseQueryTable.rows) {
                for (int i = 0; i < variableNum; i++) {
                    variables.get(i).assignValue(rawRow.getEntries().get(assignIndices.get(i)).value);
                }
                if (this.expression.evaluate()) {
                    resultTable.rows.add(this.permuteRow(rawRow, resultIndices));
                }
            }
            return resultTable;
        }
    }
    public void setColumnsAndIndices(ArrayList<Column.FullName> selectedColumns,
                                     Table baseTable,
                                     ArrayList<Column> resultColumns,
                                     ArrayList<Integer> resultIndices) throws SQLHandleException {

        resultColumns.clear();
        resultIndices.clear();
        ArrayList<Column> baseColumns = baseTable.getCopiedColumns(true);
        HashMap<String, Integer> columnMap = baseTable.getColumnIndicesMap();

        for (Column.FullName selectedCol: selectedColumns) {
            if (selectedCol.name.equals("*")) {
                if (selectedCol.prefix == null || selectedCol.equals(baseTable.getTableName())) {
                    ArrayList<Column> toAddColumns = baseTable.getCopiedColumns(false);
                    for (Column col: toAddColumns) {
                        resultIndices.add(columnMap.get(col.getColumnFullName().name));
                    }
                    resultColumns.addAll(toAddColumns);
                } else {
                    throw new SQLHandleException("Exception: some selected columns do not exist.");
                }
            } else if (!columnMap.containsKey(selectedCol.name)) {
                throw new SQLHandleException("Exception: some selected columns do not exist.");
            } else {
                int index = columnMap.get(selectedCol.name);
                resultIndices.add(index);
                resultColumns.add(baseColumns.get(index).getCopiedColumn(false));
            }
        }
    }
    public void setColumnsAndIndices(ArrayList<Column.FullName> selectedColumns,
                                                  QueryTable baseQueryTable,
                                                  ArrayList<Column> resultColumns,
                                                  ArrayList<Integer> resultIndices) throws SQLHandleException {
        //先获取选择后的列. 然后建立结果空表.(注意要查两个HashMap)
        ArrayList<Column> baseColumns = baseQueryTable.getCopiedColumns();
        resultColumns.clear();
        resultIndices.clear();

        for (Column.FullName selectedCol: selectedColumns) {
            if (selectedCol.name.equals("*")) {
                if (selectedCol.prefix == null) {
                    // 列全选
                    ArrayList<Column> toAddColumns = baseQueryTable.getCopiedColumns();
                    for (Column col: toAddColumns) {
                        resultIndices.add(baseQueryTable.columnIndicesMap.get(col.getColumnFullName().toString()));
                    }
                    resultColumns.addAll(toAddColumns);

                } else {
                    ArrayList<Column> toAddColumns = new ArrayList<>();
                    ArrayList<Integer> toAddIndices = new ArrayList<>();
                    for (Column col: baseColumns) {
                        if (col.getColumnFullName().prefix.equals(selectedCol.prefix)) {
                            toAddColumns.add(col.getCopiedColumn(true));
                            toAddIndices.add(baseQueryTable.columnIndicesMap.get(col.getColumnFullName().toString()));
                        }
                    }
                    if (toAddColumns.isEmpty()) {
                        throw new SQLHandleException("Exception: some selected columns do not exist.");
                    } else {
                        resultColumns.addAll(toAddColumns);
                        resultIndices.addAll(toAddIndices);
                    }

                }
            } else if (baseQueryTable.columnIndicesMap.containsKey(selectedCol.toString())) {
                int index = baseQueryTable.columnIndicesMap.get(selectedCol.toString());
                resultIndices.add(index);
                resultColumns.add(baseColumns.get(index).getCopiedColumn(true));
            } else if (baseQueryTable.notConflictIndicesMap.containsKey(selectedCol.name)) {
                int index = baseQueryTable.notConflictIndicesMap.get(selectedCol.name);
                resultIndices.add(index);
                resultColumns.add(baseColumns.get(index).getCopiedColumn(true));
            } else {
                throw new SQLHandleException("Exception: some selected columns do not exist.");
            }
        }
//        }
    }
    public void setAssignIndices(ArrayList<Variable> variables,
                                 Table baseTable,
                                 ArrayList<Integer> assignIndices) {
        int variableNum = variables.size();
        assignIndices.clear();

        // 寻找这些变量在源表列中的位置. 找不到则抛出一个错误.
        for (int i = 0; i < variableNum; i++) {
            if (baseTable.getColumnIndicesMap().containsKey(variables.get(i).getVariableName().name)) {
                assignIndices.add(baseTable.getColumnIndicesMap()
                        .get(variables.get(i).getVariableName().name));
            } else {
                throw new SQLHandleException("Exception: " +
                        variables.get(i).getVariableName().name +
                        " in the WHERE clause could not be found.");
            }
        }
    }

    public void setAssignIndices(ArrayList<Variable> variables,
                                 QueryTable baseQueryTable,
                                 ArrayList<Integer> assignIndices) {
        int variableNum = variables.size();
        assignIndices.clear();

        // 寻找这些变量在源表列中的位置. 找不到则抛出一个错误.
        for (int i = 0; i < variableNum; i++) {
            if (baseQueryTable.columnIndicesMap.containsKey(variables.get(i).getVariableName().toString())) {
                assignIndices.add(baseQueryTable.columnIndicesMap
                        .get(variables.get(i).getVariableName().toString()));
            } else if (baseQueryTable.notConflictIndicesMap.containsKey(variables.get(i).getVariableName().name)) {
                assignIndices.add(baseQueryTable.notConflictIndicesMap
                        .get(variables.get(i).getVariableName().name));
            } else {
                throw new SQLHandleException("Exception: " +
                        variables.get(i).getVariableName().toString() +
                        " could not be found.");
            }
        }
    }
}
