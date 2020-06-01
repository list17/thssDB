package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.schema.Column;

import java.util.ArrayList;

public class ConstraintColumnStatement implements ColumnDefinition{

    private ArrayList<String> column_types;

    public ConstraintColumnStatement(ArrayList<String> column_types){
        this.column_types = column_types;
    }
    @Override
    public void attach(ArrayList<Column> columns) throws SQLHandleException {
        for (String column_type : column_types) {
            boolean found = false;
            for (Column column : columns) {
                if (column.getName().equals(column_type)) {
                    found = true;
                    column.setPrimary();
                    column.setNotNull();
                    break;
                }
            }
            if (!found)
                throw new SQLHandleException("Cannot find the column in primary key constraint");
        }
    }
}
