package cn.edu.thssdb.statement;

import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Manager;

public class RevokeStatement implements Statement{
    private String auth_level;
    private String database_name;
    private String username;

    public RevokeStatement(String auth_level, String database_name, String username) {
        this.auth_level = auth_level;
        this.database_name = database_name;
        this.username = username;
    }

    @Override
    public QueryTable execute(Manager manager, Long sessionId, String command) {
        return null;
    }
}