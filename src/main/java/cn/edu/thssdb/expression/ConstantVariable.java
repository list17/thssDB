package cn.edu.thssdb.expression;

import cn.edu.thssdb.schema.Column;

public class ConstantVariable implements Variable{

    private Comparable value;

    public ConstantVariable(Comparable value) {
        this.value = value;
    }

    @Override
    public Type getType() {
        return Type.CONSTANT;
    }

    @Override
    public Column.FullName getVariableName() {
        return null;
    }

    @Override
    public Comparable evaluate() {
        return value;
    }

    @Override
    public void assignValue(Comparable value) {
        this.value = value;
    }

}
