package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Database;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.utils.TransactionManager;
import cn.edu.thssdb.utils.ValueInstance;
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

        TransactionManager tm = TransactionManager.getInstance();

        ValueInstance vi = ValueInstance.getInstance();

        if (!vi.getIsInit()) {
            if (tm.getFlag()) { // 事务态
                tm.getTX().addScript(command);
            } else { // 非事务态
                WriteScript ws = new WriteScript();
                ws.output(manager, sessionId, command);
            }
        }
        return null;
    }
}
