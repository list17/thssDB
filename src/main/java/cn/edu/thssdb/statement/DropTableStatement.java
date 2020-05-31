package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Database;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.utils.WriteScript;

public class DropTableStatement implements Statement{
    String name;

    public DropTableStatement(String name) {
        this.name = name;
    }

    @Override
    public QueryTable execute(Manager manager, Long sessionId, String command) throws SQLHandleException {
        Database database = manager.getSessionCurrentDatabase(sessionId);
        database.dropTable(this.name);

        WriteScript ws = new WriteScript();
        ws.output(manager, sessionId, command);
        return null;
    }
}
