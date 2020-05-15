package cn.edu.thssdb.expression;

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
    public boolean evaluate() {
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
    public void assignValue(Variable.Value... values) {
        this.leftExpression.assignValue(values);
        this.rightExpression.assignValue(values);
    }
}
