package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Manager;

public class UseDatabaseStatement implements Statement {
    String name;

    public UseDatabaseStatement(String name) {
        this.name = name;
    }

    @Override
    public QueryTable execute(Manager manager, Long sessionId, String command) throws SQLHandleException {

        manager.switchDatabase(sessionId, this.name);

//        WriteScript ws = new WriteScript();
//        ws.output(manager, sessionId, command);

        return null;
    }
}
