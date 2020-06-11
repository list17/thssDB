package cn.edu.thssdb.statement;

import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Manager;

public class DropUserStatement implements Statement{
    private String username;

    public DropUserStatement(String username) {
        this.username = username;
    }

    @Override
    public QueryTable execute(Manager manager, Long sessionId, String command) {
        return null;
    }
}
