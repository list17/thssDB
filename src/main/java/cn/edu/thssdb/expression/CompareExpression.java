package cn.edu.thssdb.expression;

import cn.edu.thssdb.exception.ExpressionHandleException;
import cn.edu.thssdb.schema.Column;
import cn.edu.thssdb.schema.Entry;

import java.util.ArrayList;
import java.util.Collections;

public class CompareExpression implements Expression {
    public enum Operator {
        EQ,
        GE,
        LE,
        NE,
        GT,
        LT;
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
            throw new ExpressionHandleException(ExpressionHandleException.ErrorCode.VALUE_OF_VARIABLE_NOT_FOUND);
        }
        if (leftValue.getClass() == String.class && rightValue.getClass() != String.class) {
            throw new ExpressionHandleException(ExpressionHandleException.ErrorCode.TYPE_NOT_MATCH);
        } else if (leftValue.getClass() != String.class && rightValue.getClass() == String.class) {
            throw new ExpressionHandleException(ExpressionHandleException.ErrorCode.TYPE_NOT_MATCH);
        }
        boolean result = false;

        switch (this.op) {
            case EQ:
                result = Entry.equals(leftValue, rightValue);
                break;
            case GT:
                result = (Entry.compareTo(leftValue, rightValue) > 0);
                break;
            case GE:
                result = (Entry.compareTo(leftValue, rightValue) >= 0);
                break;
            case LT:
                result = (Entry.compareTo(leftValue, rightValue) < 0);
                break;
            case LE:
                result = (Entry.compareTo(leftValue, rightValue) <= 0);
                break;
            case NE:
                result = !Entry.equals(leftValue, rightValue);
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public ArrayList<Variable> getAllVariables() {
        ArrayList<Variable> result = new ArrayList<>();
        if (this.leftVariable.getType() == Variable.Type.VARIABLE) {
            result.add(this.leftVariable);
        }
        if (this.rightVariable.getType() == Variable.Type.VARIABLE) {
            result.add(this.rightVariable);
        }
        return result;
    }

    @Override
    public ArrayList<Comparable> tryToGetPrimaryValue(ArrayList<Column.FullName> primaryKeys) {
        // 返回空数组代表这个搜索条件不是根据主键直接索引
        // 否则返回的数组代表主键某个属性的取值.
        int variableNum = primaryKeys.size();
        if (this.op != Operator.EQ) {
            return new ArrayList<>();
        }
        if (this.leftVariable.getType() == Variable.Type.CONSTANT
                && this.rightVariable.getType() == Variable.Type.CONSTANT) {
            return new ArrayList<>();
        } else if (this.leftVariable.getType() == Variable.Type.VARIABLE
                && this.rightVariable.getType() == Variable.Type.VARIABLE) {
            return new ArrayList<>();
        } else if (this.leftVariable.getType() == Variable.Type.VARIABLE) {
            for (int i = 0; i < variableNum; i++) {
                if (primaryKeys.get(i).equals(this.leftVariable.getVariableName())) {
                    ArrayList<Comparable> values = new ArrayList<Comparable>();
                    Collections.addAll(values, new Comparable[variableNum]);
                    values.set(i, this.rightVariable.evaluate());
                    return values;
                }
            }
            return new ArrayList<>();
        } else {
            for (int i = 0; i < variableNum; i++) {
                if (primaryKeys.get(i).equals(this.rightVariable.getVariableName())) {
                    ArrayList<Comparable> values = new ArrayList<>();
                    Collections.addAll(values, new Comparable[variableNum]);
                    values.set(i, this.leftVariable.evaluate());
                    return values;
                }
            }
            return new ArrayList<>();
        }
    }
}
