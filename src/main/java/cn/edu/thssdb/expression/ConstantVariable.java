package cn.edu.thssdb.expression;

public class ConstantVariable implements Variable{

    private Comparable value;

    public ConstantVariable(Comparable value) {
        this.value = value;
    }

    @Override
    public void assignValue(Variable.Value... values) { }

    @Override
    public Comparable evaluate() {
        return value;
    }
}
