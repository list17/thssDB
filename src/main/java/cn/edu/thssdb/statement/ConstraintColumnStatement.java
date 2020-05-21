package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.schema.Column;

import java.util.ArrayList;

public class ConstraintColumnStatement implements ColumnDefinition{

    private ArrayList<String> names;

    public ConstraintColumnStatement(ArrayList<String> names){
        this.names = names;
    }
    @Override
    public void attach(ArrayList<Column> columns) throws SQLHandleException {
        for (String name : names) {
            boolean found = false;
            for (Column column : columns) {
                if (column.getName().equals(name)) {
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
