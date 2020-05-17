package cn.edu.thssdb.schema;

import cn.edu.thssdb.type.ColumnType;

import java.io.Serializable;

public class Entry implements Comparable<Entry>, Serializable {
    private static final long serialVersionUID = -5809782578272943999L;
    public Comparable value;
    public ColumnType type;

    public static Entry generateEntry(Comparable value) {
        Entry entry = null;
        if (value.getClass() == int.class) {
            entry = new Entry(value, ColumnType.INT);
        } else if (value.getClass() == double.class) {
            entry = new Entry(value, ColumnType.DOUBLE);
        } else if (value.getClass() == float.class) {
            entry = new Entry(value, ColumnType.FLOAT);
        } else if (value.getClass() == long.class) {
            entry = new Entry(value, ColumnType.LONG);
        } else if (value.getClass() == String.class) {
            entry = new Entry(value, ColumnType.STRING);
        }
        return entry;
    }

    public Entry(Comparable value, ColumnType type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public int compareTo(Entry e) {
        return value.compareTo(e.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        Entry e = (Entry) obj;
        return value.equals(e.value);
    }

    public String toString() {
        return value.toString();
    }

    public ColumnType getType() {
        return this.type;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
