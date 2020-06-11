package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.exception.UserManageException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.utils.UserManager;

public class CreateUserStatement implements Statement{
    private String username;
    private String password;

    public CreateUserStatement(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public QueryTable execute(Manager manager, Long sessionId, String command) {
        UserManager um = UserManager.getInstance();

        if (!um.checkUserExist(username)) {
            um.createUser(username, password);
        }
        else {
            throw new SQLHandleException("User " + username + " already exists.");
        }
        return null;
    }
}
