package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.utils.Transaction;
import cn.edu.thssdb.utils.TransactionManager;
import cn.edu.thssdb.utils.WriteScript;

public class StartTransactionStatement implements Statement{
    @Override
    public QueryTable execute(Manager manager, Long sessionId, String command) throws SQLHandleException {
        TransactionManager tm = TransactionManager.getInstance();

        if (tm.getFlag(sessionId)) {
            throw new SQLHandleException("Please start transaction out of a transaction!");
        }
        else {
            // 新建事务
            tm.createTransaction(sessionId);

            // 进入事务态
            tm.changeFlag(sessionId);
        }
        return null;
    }
}
