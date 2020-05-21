package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Database;
import cn.edu.thssdb.schema.Manager;

public class DropTableStatement implements Statement{
    String name;

    public DropTableStatement(String name) {
        this.name = name;
    }

    @Override
    public QueryTable execute(Manager manager, Long sessionId) throws SQLHandleException {
        Database database = manager.getSessionCurrentDatabase(sessionId);
        database.dropTable(this.name);
        return null;
    }
}
