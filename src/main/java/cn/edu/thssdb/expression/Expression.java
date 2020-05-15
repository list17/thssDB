package cn.edu.thssdb.expression;

public interface Expression {

    public boolean evaluate();
    public void assignValue(Variable.Value... values);
}
