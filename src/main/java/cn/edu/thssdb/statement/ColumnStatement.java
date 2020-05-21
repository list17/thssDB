package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.schema.Column;

import java.util.ArrayList;

public class ColumnStatement implements ColumnDefinition{

    private Column column;

    public ColumnStatement(Column column) {
        this.column = column;
    }

    @Override
    public void attach(ArrayList<Column> columns) throws SQLHandleException {
        for (Column column: columns)
            if (column.getName().equals(column.getName()))
                throw new SQLHandleException("Duplicated column name");
        columns.add(column);
    }
}
