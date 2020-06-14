package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.utils.Transaction;
import cn.edu.thssdb.utils.TransactionManager;

public class RollbackStatement implements Statement {
    @Override
    public QueryTable execute(Manager manager, Long sessionId, String command) throws SQLHandleException {
        TransactionManager tm = TransactionManager.getInstance();
        tm.setSession(sessionId);

        if (!tm.getFlag(sessionId)) {
            throw new SQLHandleException("Please rollback in a transaction!");
        } else {
            Transaction tx = tm.getTX();
            tx.clearScripts();
            tx.rollback();
        }
        return null;
    }
}
