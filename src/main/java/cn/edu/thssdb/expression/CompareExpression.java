package cn.edu.thssdb.expression;

public class CompareExpression implements Expression {
    public enum Operator {
        EQ,
        GEQ,
        LEQ,
        NEQ,
        GE,
        LE;
    }

    private Variable leftVariable;
    private Variable rightVariable;
    private Operator op;

    public CompareExpression(Variable leftVariable, Operator op, Variable rightVariable) {
        this.leftVariable = leftVariable;
        this.rightVariable = rightVariable;
        this.op = op;
    }

    @Override
    public boolean evaluate() {
        Comparable leftValue = this.leftVariable.evaluate();
        Comparable rightValue = this.rightVariable.evaluate();
        if (leftValue == null || rightValue == null) {
            return false;
        }
        if (leftValue.getClass() != rightValue.getClass()) {
            return false;
        }
        boolean result = false;

        switch (this.op) {
            case EQ:
                result = (leftValue == rightValue);
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public void assignValue(Variable.Value... values) {
        this.leftVariable.assignValue(values);
        this.rightVariable.assignValue(values);
    }
}
