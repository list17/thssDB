package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.utils.Global;
import cn.edu.thssdb.utils.UserManager;

import java.util.ArrayList;

public class GrantStatement implements Statement {
    private ArrayList<String> auth_levels;
    private String database_name;
    private String username;

    public GrantStatement(ArrayList<String> auth_levels, String username, String database_name) {
        this.auth_levels = auth_levels;
        this.database_name = database_name;
        this.username = username;
    }

    @Override
    public QueryTable execute(Manager manager, Long sessionId, String command) {
        UserManager um = UserManager.getInstance();
        String cur_db_name = manager.getSessionCurrentDatabase(sessionId).getName();
        String cur_user = um.getCurUsername(sessionId);


        if (!um.checkUserExist(this.username)) {
            throw new SQLHandleException("User " + this.username + " does not exist.");
        }

        if (!manager.checkDatabaseExist(this.database_name)) {
            throw new SQLHandleException("Database " + this.database_name + " does not exist.");
        }

        if (cur_user.equals(this.username)) {
            throw new SQLHandleException(this.username + " cannot grant itself.");
        }

        if (um.checkAuthable(this.database_name, sessionId) || cur_user.equals(Global.DEFAULT_USER)) {
            for (String auth_level : this.auth_levels) {
                if (auth_level.equals("write")) {
                    um.authWrite(this.database_name, this.username);
                } else if (auth_level.equals("read")) {
                    um.authRead(this.database_name, this.username);
                }
            }
        } else {
            throw new SQLHandleException("Current user " + cur_user + " cannot grant on " + this.database_name + ".");
        }
        return null;
    }
}
