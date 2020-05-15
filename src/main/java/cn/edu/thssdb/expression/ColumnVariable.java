package cn.edu.thssdb.expression;

import cn.edu.thssdb.exception.ExpressionHandleException;
import cn.edu.thssdb.schema.Column;
import cn.edu.thssdb.schema.Row;

import java.util.HashMap;

public class ColumnVariable implements Variable {

    private Column.FullName name;
    private Comparable value;

    public ColumnVariable(Column.FullName name) {
        this.name = name;
    }

    @Override
    public Type getType() {
        return Type.VARIABLE;
    }

    @Override
    public void assignValue(Comparable value) {
        this.value = value;
    }

    @Override
    public Comparable evaluate() throws ExpressionHandleException {
        // 每次求值后都会清空赋予的值.
        if (value != null) {
            Comparable result = this.value;
            this.value = null;
            return result;
        } else {
            // 还没有赋值就求值就会报错.
            throw new ExpressionHandleException(ExpressionHandleException.ErrorCode.VALUE_OF_VARIABLE_NOT_FOUND);
        }
    }

    @Override
    public Column.FullName getVariableName() {
        return this.name;
    }
}
