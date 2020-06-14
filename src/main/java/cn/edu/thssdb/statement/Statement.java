package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Manager;

public interface Statement {

    public QueryTable execute(Manager manager, Long sessionId, String command) throws SQLHandleException;

}
