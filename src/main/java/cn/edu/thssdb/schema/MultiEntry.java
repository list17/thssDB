package cn.edu.thssdb.schema;

import java.io.Serializable;
import java.util.ArrayList;

public class MultiEntry implements Comparable<MultiEntry> {
    public ArrayList<Entry> values;
    public int valueCount;

    public MultiEntry(ArrayList<Entry> values) {
        this.values = values;
        this.valueCount = this.values.size();
    }

    public MultiEntry() {
        this.values = new ArrayList<Entry>();
        this.valueCount = 0;
    }

    public MultiEntry(Entry... entries) {
        this.values = new ArrayList<Entry>();
        this.valueCount = 0;

        for (Entry entry: entries) {
            addEntry(entry);
        }
    }

    public void addEntry(Entry entry) {
        this.values.add(entry);
        this.valueCount++;
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
//            throw new DataHandleException(DataHandleException.ErrorCode.MULTIENTRY_LENGTH_MISMATCH);
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

    public String toString() {
        String str = "";
        for (Entry e: values) {
            str += e.toString();
        }
        return str;
    }

    @Override
    public int hashCode() {
        int code = 0;
        for (Entry e: values) {
            code += e.hashCode();
        }
        return code;
    }
}
