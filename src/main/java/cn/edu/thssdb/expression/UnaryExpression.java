package cn.edu.thssdb.expression;

import cn.edu.thssdb.exception.ExpressionHandleException;
import cn.edu.thssdb.schema.Column;

import java.util.ArrayList;

public class UnaryExpression implements Expression {
    public boolean value;

    public UnaryExpression(boolean value) {
        this.value = value;
    }

    @Override
    public boolean evaluate() throws ExpressionHandleException {
        return this.value;
    }

    @Override
    public ArrayList<Variable> getAllVariables() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Comparable> tryToGetPrimaryValue(ArrayList<Column.FullName> primaryKeys) {
        return new ArrayList<>();
    }
}
