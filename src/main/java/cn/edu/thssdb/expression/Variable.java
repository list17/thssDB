package cn.edu.thssdb.expression;

import cn.edu.thssdb.schema.Row;

public interface Variable {
    public void assignValue(Variable.Value... values);
    public Comparable evaluate();

    public class Value {
        public String tableName;
        public String columnName;
        public Row row;
    }
}
