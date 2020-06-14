package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.utils.Global;
import cn.edu.thssdb.utils.UserManager;

public class DropDatabaseStatement implements Statement {
    private String name;

    public DropDatabaseStatement(String name) {
        this.name = name;
    }

    @Override
    public QueryTable execute(Manager manager, Long sessionId, String command) throws SQLHandleException {
        UserManager um = UserManager.getInstance();
        String cur_db_name = manager.getSessionCurrentDatabase(sessionId).getName();
        String cur_user = um.getCurUsername(sessionId);

        if (!manager.checkDatabaseExist(name)) {
            throw new SQLHandleException("Database " + name + " does not exist.");
        }

        if (!um.checkWritable(name, sessionId) && !cur_user.equals(Global.DEFAULT_USER)) {
            throw new SQLHandleException("Current user has no write authority on database " + name);
        }

        manager.deleteDatabase(this.name);

        um.deAuthCreate(cur_db_name);
        return null;
    }
}
