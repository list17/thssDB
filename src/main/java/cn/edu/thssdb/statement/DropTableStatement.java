package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.FileWriteException;
import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Database;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.utils.*;

public class DropTableStatement implements Statement {
    String name;

    public DropTableStatement(String name) {
        this.name = name;
    }

    @Override
    public QueryTable execute(Manager manager, Long sessionId, String command) throws SQLHandleException {
        UserManager um = UserManager.getInstance();
        ValueInstance vi = ValueInstance.getInstance();
        String cur_db_name = manager.getSessionCurrentDatabase(sessionId).getName();
        String cur_user = um.getCurUsername(sessionId);

        if (!vi.getIsInit() && !um.checkWritable(cur_db_name, sessionId) && !cur_user.equals(Global.DEFAULT_USER)) {
            throw new SQLHandleException("Current user has no write authority on database " + cur_db_name);
        }

        Database database = manager.getSessionCurrentDatabase(sessionId);

        TransactionManager tm = TransactionManager.getInstance();

        if (!vi.getIsInit()) { // 非初始化
            if (tm.getFlag(sessionId)) { // 事务态
                tm.setLockX(sessionId, this.name);
            } else { // 非事务态
                if (tm.setLockXSingle(sessionId, this.name)) {

                } else {
                    throw new SQLHandleException("Statement on this table is blocked now");
                }
            }
        }
        tm.setSession(sessionId);

        try {
            database.dropTable(this.name);

            if (!vi.getIsInit()) {
                if (tm.getFlag(sessionId)) { // 事务态
                    tm.getTX().addScript(command);
                } else { // 非事务态
                    WriteScript ws = new WriteScript();
                    ws.output(manager, sessionId, command);
                }
            }
        } catch (FileWriteException e) {

        } finally {
            if (!vi.getIsInit()) {
                if (tm.getFlag(sessionId)) { // 事务态

                } else { // 非事务态
                    tm.releaseTXLock(sessionId);
                    tm.destroyTransaction(sessionId);
                }
            }
        }
        return null;
    }
}
