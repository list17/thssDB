package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InsertStatement implements Statement{
    String name;
    ArrayList<String> columns;
    ArrayList<ArrayList<Comparable>> rows;

    public InsertStatement(String name, ArrayList<String> columns, ArrayList<ArrayList<Comparable>> rows) {
        this.name = name;
        this.columns = columns;
        this.rows = rows;
    }

    @Override
    public QueryTable execute(Manager manager, Long sessionId) throws SQLHandleException {
        Table table = manager.getSessionCurrentDatabase(sessionId).getTable(name);
        HashMap<String, Integer> columnPos = table.getColumnIndicesMap();
        for(ArrayList<Comparable> row : rows) {
            Row row1 = new Row();
            ArrayList<Entry> entries = new ArrayList<>();
            for(Map.Entry<String, Integer> entry : columnPos.entrySet()) {
                int index = columns.indexOf(entry.getKey());
                if(index != -1) {
                    entries.add(new Entry(row.get(index)));
                } else {
                    entries.add(new Entry(null));
                }
            }
            row1.appendEntries(entries);
            table.insert(row1);
        }
        return null;
    }
}
