package cn.edu.thssdb.statement;

import cn.edu.thssdb.expression.SourceTable;

import java.util.ArrayList;

public class TableQueryStatement {
    private String table_name;
    private String alias;
    private ArrayList<SourceTable.JoinOperator> operators;

    public TableQueryStatement(String name, ArrayList<SourceTable.JoinOperator> operators, String alias) {
        this.table_name = name;
        this.alias = alias;
        this.operators = operators;
    }

    public String getTable_name() {
        return table_name;
    }

    public String getAlias() {
        return alias;
    }

    public ArrayList<SourceTable.JoinOperator> getOperators() {
        return operators;
    }
}
