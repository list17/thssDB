package cn.edu.thssdb.expression;

import cn.edu.thssdb.exception.ExpressionHandleException;

import java.util.ArrayList;

public class LogicalExpression implements Expression{
    public enum Operator {
        AND,
        OR;
    }

    private Expression leftExpression;
    private Expression rightExpression;
    private Operator op;

    public LogicalExpression(Expression leftExpression, Operator op, Expression rightExpression) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
        this.op = op;
    }

    @Override
    public boolean evaluate() throws ExpressionHandleException {
        boolean result = false;
        switch (this.op) {
            case AND:
                result = this.leftExpression.evaluate() && this.rightExpression.evaluate();
                break;
            case OR:
                result = this.leftExpression.evaluate() || this.rightExpression.evaluate();
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public ArrayList<Variable> getAllVariables() {
        ArrayList<Variable> result = this.leftExpression.getAllVariables();
        result.addAll(this.rightExpression.getAllVariables());
        return result;
    }
}
