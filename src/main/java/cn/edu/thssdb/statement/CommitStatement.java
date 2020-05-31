package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.utils.TransactionManager;

public class CommitStatement implements Statement{
    @Override
    public QueryTable execute(Manager manager, Long sessionId, String command) throws SQLHandleException {
        TransactionManager tm = TransactionManager.getInstance();

        if (!tm.getFlag()) {
            throw new SQLHandleException("Please commit in a transaction!");
        }
        else {
            // 打印scripts
            tm.getTX().output(manager, sessionId);

            // 删除事务
            tm.destroyTransaction();

            // 退出事务态
            tm.changeFlag();
        }

        return null;
    }

}
