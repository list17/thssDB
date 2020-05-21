package cn.edu.thssdb.statement;

public class TableFullName {
    private String name = null;
    private String alias = null;
    boolean isFull = false;

    public TableFullName(String name, String alias) {
        this.name = name;
        this.alias = alias;
        isFull = true;
    }

    public TableFullName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }
}
