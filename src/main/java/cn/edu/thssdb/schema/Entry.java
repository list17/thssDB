package cn.edu.thssdb.schema;

import cn.edu.thssdb.type.ColumnType;

import java.io.Serializable;

public class Entry implements Comparable<Entry>, Serializable {
    private static final long serialVersionUID = -5809782578272943999L;
    public Comparable value;

    public static int compareTo(Comparable a, Comparable b) {
        if (a == null || b == null) {
            return -1;
        }
        Class thisClass = a.getClass();
        Class thatClass = b.getClass();
        Comparable thisValue = a;
        Comparable thatValue = b;
        if (thisClass == thatClass) {
            return a.compareTo(b);
        }

        if (thisClass != String.class && thatClass != String.class) {
            if (thisClass == Double.class || thisClass == Float.class) {
                thisValue = ((Number) a).doubleValue();
                thatValue = ((Number) b).doubleValue();
            } else if (thisClass == Long.class || thisClass == Integer.class) {
                if (thatClass == Double.class || thatClass == Float.class) {
                    thisValue = ((Number) a).doubleValue();
                    thatValue = ((Number) b).doubleValue();
                } else {
                    thisValue = ((Number) a).longValue();
                    thatValue = ((Number) b).longValue();
                }
            }
        }
        return thisValue.compareTo(thatValue);
    }

    public static boolean equals(Comparable a, Comparable b) {
        if (a == null || b == null) {
            return false;
        }
        Class thisClass = a.getClass();
        Class thatClass = b.getClass();
        Comparable thisValue = a;
        Comparable thatValue = b;
        if (thisClass == thatClass) {
            return a.equals(b);
        }
        
        if (thisClass != String.class && thatClass != String.class) {
            if (thisClass == Double.class || thisClass == Float.class) {
                thisValue = ((Number) a).doubleValue();
                thatValue = ((Number) b).doubleValue();
            } else if (thisClass == Long.class || thisClass == Integer.class) {
                if (thatClass == Double.class || thatClass == Float.class) {
                    thisValue = ((Number) a).doubleValue();
                    thatValue = ((Number) b).doubleValue();
                } else {
                    thisValue = ((Number) a).longValue();
                    thatValue = ((Number) b).longValue();
                }
            }
        }
        return thisValue.equals(thatValue);
    }

    public static Entry generateEntry(Comparable value) {
        Entry entry = new Entry(value);
        return entry;
    }

    public Entry(Comparable value) {
        this.value = value;
    }

    @Override
    public int compareTo(Entry e) {
        return Entry.compareTo(this.value, e.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        Entry e = (Entry) obj;
        return Entry.equals(this.value, e.value);
    }

    public String toString() {
        return value.toString();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
