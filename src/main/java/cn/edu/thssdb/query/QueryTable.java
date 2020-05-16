package cn.edu.thssdb.query;

import cn.edu.thssdb.schema.Column;
import cn.edu.thssdb.schema.Row;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class QueryTable implements Iterator<Row> {

    private String tableName;
    private ArrayList<Column> columns;
    private LinkedList<Row> rows;

    public QueryTable(String queryTableName, ArrayList<Column> columns) {
        // TODO
    }

    @Override
    public boolean hasNext() {
        // TODO
        return true;
    }

    @Override
    public Row next() {
        // TODO
        return null;
    }

}