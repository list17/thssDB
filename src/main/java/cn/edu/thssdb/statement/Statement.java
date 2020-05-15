package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.schema.Table;

public interface Statement {

    public Table execute(Manager manager) throws SQLHandleException;

}
