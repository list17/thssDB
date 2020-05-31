package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.FileWriteException;
import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.expression.ConstantVariable;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.*;
import cn.edu.thssdb.utils.Transaction;
import cn.edu.thssdb.utils.TransactionManager;
import cn.edu.thssdb.utils.WriteScript;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InsertStatement implements Statement {
    String name;
    ArrayList<String> columns;
    ArrayList<ArrayList<ConstantVariable>> rows;

    public InsertStatement(String name, ArrayList<String> columns, ArrayList<ArrayList<ConstantVariable>> rows) {
        this.name = name;
        this.columns = columns;
        this.rows = rows;
    }

    @Override
    public QueryTable execute(Manager manager, Long sessionId, String command) throws SQLHandleException {
        Table table = manager.getSessionCurrentDatabase(sessionId).getTable(name);
        HashMap<String, Integer> columnPos = table.getColumnIndicesMap();
        for (ArrayList<ConstantVariable> row : rows) {
            Entry[] entries = new Entry[columnPos.size()];
            if (this.columns.size() == 0) {
                // 没有指定属性
                int i = 0;
                for (i = 0; i < row.size(); i++) {
                    entries[i] = new Entry(row.get(i).evaluate());
                }
                for (; i < columnPos.size(); i++) {
                    entries[i] = new Entry(null);
                }
            } else {
                for (int i = 0; i < columnPos.size(); i++) {
                    entries[i] = new Entry(null);
                }
                for (Map.Entry<String, Integer> entry : columnPos.entrySet()) {
                    int index = columns.indexOf(entry.getKey());
                    if (index != -1) {
                        entries[index] = new Entry(row.get(index).evaluate());
                    }
                }
            }
            Row row1 = new Row(entries);
            TransactionManager tm = TransactionManager.getInstance();
            table.insert(row1, tm.getTX());

            if (tm.getFlag()) { // 事务态
                tm.getTX().addScript(command);
            } else { // 非事务态
                WriteScript ws = new WriteScript();
                ws.output(manager, sessionId, command);
            }
        }
        return null;
    }
}
