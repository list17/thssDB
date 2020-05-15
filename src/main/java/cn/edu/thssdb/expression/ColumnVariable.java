package cn.edu.thssdb.expression;

import cn.edu.thssdb.schema.Column;
import cn.edu.thssdb.schema.Row;

public class ColumnVariable implements Variable {

    private String tableName;
    private String columnName;
    private int columnIndex;
    private Row row;

    public ColumnVariable(String tableName, String columnName, int columnIndex) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.columnIndex = columnIndex;
    }

    @Override
    public void assignValue(Variable.Value... values) {
        for (Variable.Value v: values) {
            if (v.tableName == this.tableName && v.columnName == this.columnName) {
                this.row = v.row;
                return;
            }
        }
    }

    @Override
    public Comparable evaluate() {
        return row.getEntries().get(this.columnIndex);
    }
}
