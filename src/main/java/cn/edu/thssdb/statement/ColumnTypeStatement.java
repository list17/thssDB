package cn.edu.thssdb.statement;

import cn.edu.thssdb.expression.ConstantVariable;

public class ColumnTypeStatement {
    private String name;
    private int max_length;

    public ColumnTypeStatement(String name, ConstantVariable max_length) {
        this.max_length = Integer.parseInt(max_length.evaluate().toString());
        this.name = name;
    }

    public ColumnTypeStatement(String name) {
        this.name = name;
        this.max_length = -1;
    }

    public String getName() {
        return name;
    }

    public int getMax_length() {
        return max_length;
    }
}
