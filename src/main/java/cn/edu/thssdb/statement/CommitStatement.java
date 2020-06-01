package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.utils.TransactionManager;

public class CommitStatement implements Statement{
    @Override
    public QueryTable execute(Manager manager, Long sessionId, String command) throws SQLHandleException {
        TransactionManager tm = TransactionManager.getInstance();
        tm.setSession(sessionId);

        if (!tm.getFlag(sessionId)) {
            throw new SQLHandleException("Please commit in a transaction!");
        }
        else {
            // 释放事务的所有锁
            tm.releaseTXLock(sessionId);

            // 打印scripts
            tm.getTX().output(manager, sessionId);

            // 删除事务
            tm.destroyTransaction(sessionId);

            // 退出事务态
            tm.changeFlag(sessionId);

            // 尝试阻塞事务
            tm.continueBlockedTX();
        }

        return null;
    }

}
