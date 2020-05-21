package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.expression.Expression;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Manager;

public class DeleteStatement implements Statement{

    String name;
    Expression expression;

    public DeleteStatement(String name, Expression expression) {
        this.name = name;
        this.expression = expression;
    }

    public DeleteStatement(String name) {
        this.name = name;
    }

    @Override
    public QueryTable execute(Manager manager, Long sessionId) throws SQLHandleException {
        // todo 根据expression找到这些行并删除 如果没有expression则全部删除
        return null;
    }
}
