package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.schema.Column;

import java.util.ArrayList;

public interface ColumnDefinition {
    public void attach(ArrayList<Column> columns) throws SQLHandleException;
}
