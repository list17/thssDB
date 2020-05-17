package cn.edu.thssdb.expression;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Column;
import cn.edu.thssdb.schema.Database;
import cn.edu.thssdb.schema.Row;
import cn.edu.thssdb.schema.Table;

import javax.management.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SourceTable {
    public static class JoinOperator {
        public String joinedTableName;
        public Expression expression;
        public String alias;

        public JoinOperator(String joinedTableName, Expression expression, String alias) {
            this.joinedTableName = joinedTableName;
            this.expression = expression;
            this.alias = alias;
        }
        public JoinOperator(String joinedTableName, Expression expression) {
            this.joinedTableName = joinedTableName;
            this.expression = expression;
        }
    }

    public String tableName;
    public ArrayList<JoinOperator> joinOps;
    public String alias;


    public SourceTable(String tableName, ArrayList<JoinOperator> joinOps, String alias) {
        this.tableName = tableName;
        this.joinOps = joinOps;
        this.alias = alias;
    }
    public SourceTable(String tableName, ArrayList<JoinOperator> joinOps) {
        this.tableName = tableName;
        this.joinOps = joinOps;
    }

    /**
     * 根据Join后得到的所有数据生成一个queryTable用于遍历.
     * 生成的queryTable, 其所有列名都强制加上源表的前缀.
     * @param database
     * @return
     */
    public QueryTable getQueryTable(Database database) throws SQLHandleException {
        QueryTable baseTable = database.getTable(tableName).getQueryTable(true);
        if (this.alias != null) {
            baseTable.setAlias(this.alias);
        }

        for (JoinOperator joinOp: joinOps) {
            QueryTable toJoinTable = database.getTable(joinOp.joinedTableName).getQueryTable(true);
            if (joinOp.alias != null) {
                toJoinTable.setAlias(joinOp.alias);
            }

            // 获取join后的表的列属性, 带前缀(便于辨识列), 然后建立临时表.
            ArrayList<Column> joinedColumn = new ArrayList<>();
            joinedColumn.addAll(baseTable.getCopiedColumns());
            joinedColumn.addAll(toJoinTable.getCopiedColumns());
            QueryTable joinedTable = new QueryTable(
                    baseTable.queryTableName + " JOIN " + toJoinTable.queryTableName,
                    joinedColumn);

            // 获取所有需要赋值的Variable.
            ArrayList<Variable> variables = joinOp.expression.getAllVariables();
            int variableNum = variables.size();
            ArrayList<Integer> assignIndices = new ArrayList<>();

            // 寻找这些变量在合成表含有的列中的位置. 找不到则抛出一个错误.
            for (int i = 0; i < variableNum; i++) {
                if (joinedTable.columnIndicesMap.containsKey(variables.get(i).getVariableName().toString())) {
                    assignIndices.add(joinedTable.columnIndicesMap
                            .get(variables.get(i).getVariableName().toString()));
                } else if (joinedTable.notConflictIndicesMap.containsKey(variables.get(i).getVariableName().name)) {
                    assignIndices.add(joinedTable.notConflictIndicesMap
                            .get(variables.get(i).getVariableName().name));
                } else {
                    throw new SQLHandleException("Exception: " +
                            variables.get(i).getVariableName().toString() +
                            " could not be found.");
                }
            }
            //开始join, 给每个变量赋值判断是否join.
            for (Row baseRow: baseTable.rows) {
                for (Row toJoinRow: toJoinTable.rows) {
                    Row joinedRow = new Row();
                    joinedRow.appendEntries(baseRow.getEntries());
                    joinedRow.appendEntries(toJoinRow.getEntries());

                    for (int i = 0; i < variableNum; i++) {
                        variables.get(i).assignValue(joinedRow.getEntries().get(assignIndices.get(i)).value);
                    }
                    if (joinOp.expression.evaluate()) {
                        joinedTable.rows.add(joinedRow);
                    }
                }
            }
            //最后将结果迭代到baseTable上
            baseTable = joinedTable;
        }
        return baseTable;
    }

}
