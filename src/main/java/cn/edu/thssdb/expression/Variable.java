package cn.edu.thssdb.expression;

import cn.edu.thssdb.exception.ExpressionHandleException;
import cn.edu.thssdb.schema.Column;

public interface Variable {

    public Type getType();

    public Column.FullName getVariableName();

    public Comparable evaluate() throws ExpressionHandleException;

    public void assignValue(Comparable value);

    public enum Type {
        CONSTANT,
        VARIABLE;
    }
}
