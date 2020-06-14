package cn.edu.thssdb.schema;

import cn.edu.thssdb.exception.ExpressionHandleException;

import java.util.ArrayList;

public class MultiEntry implements Comparable<MultiEntry> {
    public ArrayList<Entry> entries;
    public int valueCount;

    public MultiEntry(ArrayList<Entry> values) {
        this.entries = values;
        this.valueCount = this.entries.size();
    }

    public MultiEntry() {
        this.entries = new ArrayList<Entry>();
        this.valueCount = 0;
    }

    public MultiEntry(Entry... entries) {
        this.entries = new ArrayList<Entry>();
        this.valueCount = 0;

        for (Entry entry : entries) {
            addEntry(entry);
        }
    }

    public void addEntry(Entry entry) {
        this.entries.add(entry);
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
//            return -1;
            throw new ExpressionHandleException(ExpressionHandleException.ErrorCode.GENERAL);
        }
        for (int i = 0; i < this.valueCount; i++) {
            if (this.entries.get(i).equals(e.entries.get(i))) {
                continue;
            }
            return this.entries.get(i).compareTo(e.entries.get(i));
        }
        return this.entries.get(this.valueCount - 1).compareTo(e.entries.get(this.valueCount - 1));
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
            if (!this.entries.get(i).equals(e.entries.get(i))) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        String str = "";
        for (Entry e : entries) {
            str += e.toString();
        }
        return str;
    }

    @Override
    public int hashCode() {
        int code = 0;
        for (Entry e : entries) {
            code += e.hashCode();
        }
        return code;
    }
}
