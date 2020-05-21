package cn.edu.thssdb.expression;

import cn.edu.thssdb.exception.ExpressionHandleException;
import cn.edu.thssdb.schema.Column;

import java.util.ArrayList;
import java.util.Collections;

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

    @Override
    public ArrayList<Comparable> tryToGetPrimaryValue(ArrayList<Column.FullName> primaryKeys) {
        int variableNum = primaryKeys.size();
        if (this.op != Operator.AND) {
            return new ArrayList<>();
        }
        ArrayList<Comparable> leftValues = this.leftExpression.tryToGetPrimaryValue(primaryKeys);
        ArrayList<Comparable> rightValues = this.rightExpression.tryToGetPrimaryValue(primaryKeys);

        if (leftValues.isEmpty() || rightValues.isEmpty()) {
            return new ArrayList<>();
        } else {
            ArrayList<Comparable> result = new ArrayList<>();
            Collections.addAll(result, new Comparable[variableNum]);

            for (int i = 0; i < variableNum; i++) {
                Comparable l = leftValues.get(i);
                Comparable r = rightValues.get(i);
                if (l != null) {
                    if (r != null && !(l.equals(r))) {
                        return new ArrayList<>();
                    } else {
                        result.set(i, l);
                    }
                } else if (r != null) {
                    result.set(i, r);
                }
            }
            return result;
        }
    }
}
