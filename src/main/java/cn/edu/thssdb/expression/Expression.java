package cn.edu.thssdb.expression;

import cn.edu.thssdb.exception.ExpressionHandleException;
import cn.edu.thssdb.schema.Column;

import java.util.ArrayList;

public interface Expression {

    public boolean evaluate() throws ExpressionHandleException;
    public ArrayList<Variable> getAllVariables();

    public ArrayList<Comparable> tryToGetPrimaryValue(ArrayList<Column.FullName> primaryKeys);
}
