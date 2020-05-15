package cn.edu.thssdb.statement;

import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.schema.Table;

public class SelectStatement implements Statement {

    public SelectStatement() {

    }

    @Override
    public Table execute(Manager manager) {

        return new Table("DB", "TB");
    }
}
