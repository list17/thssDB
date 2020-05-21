package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.expression.ConstantVariable;
import cn.edu.thssdb.expression.Expression;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Entry;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.schema.Row;

import java.util.ArrayList;

public class UpdateStatement implements Statement{
    private String name;
    private String column_name;
    private Expression expression;
    private ConstantVariable constantVariable;
    public UpdateStatement(String name, String column_name, ConstantVariable constantVariable, Expression expression) {
        this.name = name;
        this.column_name = column_name;
        this.constantVariable = constantVariable;
        this.expression = expression;
    }

    @Override
    public QueryTable execute(Manager manager, Long sessionId) throws SQLHandleException {
        // todo 根据expression找到这些行 将column_name列的值变为constantVariable
        return null;
    }
}
