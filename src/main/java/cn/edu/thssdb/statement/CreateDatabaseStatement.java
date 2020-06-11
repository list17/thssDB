package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.exception.UserManageException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.rpc.thrift.ConnectResp;
import cn.edu.thssdb.schema.Database;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.utils.User;
import cn.edu.thssdb.utils.UserManager;

public class CreateDatabaseStatement implements Statement{
    private String name;

    public  CreateDatabaseStatement(String name) {
        this.name = name;
    }

    @Override
    public QueryTable execute(Manager manager, Long sessionId, String command) throws SQLHandleException {
        manager.createDatabaseIfNotExists(this.name);

        UserManager um = UserManager.getInstance();
        String cur_user = um.getCurUsername(sessionId);

        um.authCreate(this.name, cur_user);

        return null;
    }
}
