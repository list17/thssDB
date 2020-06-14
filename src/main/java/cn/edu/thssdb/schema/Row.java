package cn.edu.thssdb.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

public class Row implements Serializable {

    private static final long serialVersionUID = -6962743336266728091L;
    protected ArrayList<Entry> entries;

    public Row() {
        this.entries = new ArrayList<>();
    }

    public Row(Comparable... values) {
        this.entries = new ArrayList<>();
        for (Comparable v : values) {
            this.entries.add(Entry.generateEntry(v));
        }
    }

    public Row(Entry[] entries) {
        this.entries = new ArrayList<>(Arrays.asList(entries));
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }

    public void appendEntries(ArrayList<Entry> entries) {
        this.entries.addAll(entries);
    }

    public void appendEntry(Entry entry) {
        this.entries.add(entry);
    }

    public MultiEntry getMultiEntry(ArrayList<Integer> indices) {
        ArrayList<Entry> selectedEntries = new ArrayList<Entry>();
        for (Integer i : indices) {
            selectedEntries.add(this.entries.get(i));
        }
        return new MultiEntry(selectedEntries);
    }

    public MultiEntry getMultiEntry(int... indices) {
        ArrayList<Entry> selectedEntries = new ArrayList<Entry>();
        for (int i : indices) {
            selectedEntries.add(this.entries.get(i));
        }
        return new MultiEntry(selectedEntries);
    }

    public Row getCopiedRow() {
        ArrayList<Entry> copiedEntries = new ArrayList<>();
        for (Entry e : this.entries) {
            copiedEntries.add(Entry.generateEntry(e.value));
        }
        Row copiedRow = new Row();
        copiedRow.appendEntries(copiedEntries);
        return copiedRow;
    }

    public String toString() {
        if (entries == null)
            return "EMPTY";
        StringJoiner sj = new StringJoiner(", ");
        for (Entry e : entries)
            if (e.value != null)
                sj.add(e.toString());
        return sj.toString();
    }
}
