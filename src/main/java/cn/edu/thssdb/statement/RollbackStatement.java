package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.utils.TransactionManager;

public class RollbackStatement implements Statement{
    @Override
    public QueryTable execute(Manager manager, Long sessionId) throws SQLHandleException {
        return null;
    }
}
