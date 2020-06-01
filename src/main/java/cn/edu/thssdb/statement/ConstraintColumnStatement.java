package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.schema.Column;

import java.util.ArrayList;

public class ConstraintColumnStatement implements ColumnDefinition{

    private ArrayList<ColumnTypeStatement> column_types;

    public ConstraintColumnStatement(ArrayList<ColumnTypeStatement> column_types){
        this.column_types = column_types;
    }
    @Override
    public void attach(ArrayList<Column> columns) throws SQLHandleException {
        for (ColumnTypeStatement column_type : column_types) {
            boolean found = false;
            for (Column column : columns) {
                if (column.getName().equals(column_type.getName())) {
                    found = true;
                    column.setPrimary();
                    break;
                }
            }
            if (!found)
                throw new SQLHandleException("Cannot find the column in primary key constraint");
        }
    }
}
