package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.rpc.thrift.ConnectResp;
import cn.edu.thssdb.schema.Database;
import cn.edu.thssdb.schema.Manager;

public class CreateDatabaseStatement implements Statement{
    private String name;

    public  CreateDatabaseStatement(String name) {
        this.name = name;
    }

    @Override
    public QueryTable execute(Manager manager, Long sessionId, String command) throws SQLHandleException {
        manager.createDatabaseIfNotExists(this.name);
        return null;
    }
}
