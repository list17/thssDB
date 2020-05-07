package cn.edu.thssdb.schema;

import java.io.Serializable;
import java.util.ArrayList;

public class MultiEntry implements Comparable<MultiEntry>, Serializable {
    private static final long serialVersionUID = -5809782578272943989L;
    public ArrayList<Entry> values;
    public int valueCount;

    public MultiEntry(ArrayList<Entry> values) {
        this.values = values;
        this.valueCount = this.values.size();
    }

    /**
     * 多列entry的比较
     *
     * @param e
     * @return
     */
    @Override
    public int compareTo(MultiEntry e) {
        if (e.valueCount != this.valueCount) {
            throw new NumberFormatException();
        }
        for (int i = 0; i < this.valueCount; i++) {
            if (this.values.get(i).equals(e.values.get(i))) {
                continue;
            }
            return this.values.get(i).compareTo(e.values.get(i));
        }
        return this.values.get(this.valueCount - 1).compareTo(e.values.get(this.valueCount - 1));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        MultiEntry e = (MultiEntry) obj;
        if (this.valueCount != e.valueCount)
            return false;
        for (int i = 0; i < this.valueCount; i++) {
            if (!this.values.get(i).equals(e.values.get(i))) {
                return false;
            }
        }
        return true;
    }

//    public String toString() {
//        return value.toString();
//    }
//
//    @Override
//    public int hashCode() {
//        return value.hashCode();
//    }
}
