package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.exception.UserManageException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.utils.UserManager;

public class DropUserStatement implements Statement{
    private String username;

    public DropUserStatement(String username) {
        this.username = username;
    }

    @Override
    public QueryTable execute(Manager manager, Long sessionId, String command) {
        UserManager um = UserManager.getInstance();

        if (this.username.equals(um.getCurUsername(sessionId))) {
            throw new SQLHandleException("User " + username + " is the current user which cannot be dropped now.");
        }
        else if (um.checkUserExist(sessionId)) {
            um.dropUser(username);
        }
        else {
            throw new SQLHandleException("User " + username + " not exists.");
        }
        return null;
    }
}
