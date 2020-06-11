package cn.edu.thssdb.statement;

import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Manager;

public class CreateUserStatement implements Statement{
    private String username;
    private String password;

    public CreateUserStatement(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public QueryTable execute(Manager manager, Long sessionId, String command) {
        return null;
    }
}
