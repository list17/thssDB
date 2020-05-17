package cn.edu.thssdb.statement;

import cn.edu.thssdb.expression.Expression;
import cn.edu.thssdb.expression.SourceTable;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.*;

import javax.management.Query;
import java.lang.reflect.Array;
import java.util.ArrayList;

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

    @Override
    public QueryTable execute(Database database) {
        QueryTable resultTable;

        if (sourceTable.joinOps.isEmpty()) {
            // 没有join或者cartesian操作, 单表源, 可以尝试主键检索.
            Table baseTable = database.getTable(sourceTable.tableName);
            ArrayList<Integer> primaryIndices = baseTable.getPrimaryIndices();
            ArrayList<Column> baseColumns = baseTable.getCopiedColumns(true);
            ArrayList<Column.FullName> primaryKeys = new ArrayList<>();

            // 查找出所有主键属性.
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
                if (isPrimarySearch) {
                    MultiEntry searchKey = new MultiEntry();
                    for (Comparable value: primaryValues) {
                        searchKey.addEntry(Entry.generateEntry(value));
                    }
                    Row searchRow = baseTable.findRowByPrimaryKey(searchKey);
                    
                }
            }

        } else {

        }
        return null;
    }
}
