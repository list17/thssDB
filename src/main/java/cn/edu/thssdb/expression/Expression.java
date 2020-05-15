package cn.edu.thssdb.expression;

import cn.edu.thssdb.exception.ExpressionHandleException;

import java.util.ArrayList;

public interface Expression {

    public boolean evaluate() throws ExpressionHandleException;
    public ArrayList<Variable> getAllVariables();
}
