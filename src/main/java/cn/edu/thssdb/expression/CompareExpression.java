package cn.edu.thssdb.expression;

import cn.edu.thssdb.exception.ExpressionHandleException;

import java.util.ArrayList;

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
    public boolean evaluate() throws ExpressionHandleException {
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
    public ArrayList<Variable> getAllVariables() {
        ArrayList<Variable> result = new ArrayList<>();
        result.add(this.leftVariable);
        result.add(this.rightVariable);
        return result;
    }
}
