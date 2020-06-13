package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.FileWriteException;
import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.exception.UserManageException;
import cn.edu.thssdb.expression.ConstantVariable;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.*;
import cn.edu.thssdb.utils.*;

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
        UserManager um = UserManager.getInstance();
        ValueInstance vi = ValueInstance.getInstance();
        String cur_db_name = manager.getSessionCurrentDatabase(sessionId).getName();
        String cur_user = um.getCurUsername(sessionId);

        if (!vi.getIsInit() && !um.checkWritable(cur_db_name, sessionId) && !cur_user.equals(Global.DEFAULT_USER)) {
            throw new SQLHandleException("Current user has no write authority on database " + cur_db_name);
        }

        Table table = manager.getSessionCurrentDatabase(sessionId).getTable(name);
        HashMap<String, Integer> columnPos = table.getColumnIndicesMap();
        for (ArrayList<ConstantVariable> row : rows) {
            if (row.size() > columnPos.size()) {
                throw new SQLHandleException("The number of values is more than the columns");
               }
            if (columns.size() > columnPos.size()) {
                throw new SQLHandleException("The number of values is more than the table columns");
            }
            if (columns.size() != 0 && row.size() != columns.size()) {
                throw new SQLHandleException("The number of values is not the same with the columns");
            }
        }

        TransactionManager tm = TransactionManager.getInstance();

        if (!vi.getIsInit()) { // 非初始化
            if (tm.getFlag(sessionId)) { // 事务态
                tm.setLockX(sessionId, this.name);
            } else { // 非事务态
                if (tm.setLockXSingle(sessionId, this.name)) {

                } else {
                    throw new SQLHandleException("Statement on this table is blocked now");
                }
            }
        }
        tm.setSession(sessionId);

        try {
            for (ArrayList<ConstantVariable> row : rows) {
                Entry[] entries = new Entry[columnPos.size()];
                if (this.columns.size() == 0) {
                    // 没有指定属性
                    int i;
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
                    for(String column : columns){
                        int index = columnPos.get(column);
                        if (index != -1) {
                            entries[index] = new Entry(row.get(columns.indexOf(column)).evaluate());
                        }
                    }
                }
                Row row1 = new Row(entries);
                table.insert(row1, tm.getTX());
            }

            if (!vi.getIsInit()) {
                if (tm.getFlag(sessionId)) { // 事务态
                    tm.getTX().addScript(command);
                } else { // 非事务态
                    WriteScript ws = new WriteScript();
                    ws.output(manager, sessionId, command);
                }
            }
        } catch (FileWriteException e) {

        } finally {
            if (!vi.getIsInit()) {
                if (tm.getFlag(sessionId)) { // 事务态

                } else { // 非事务态
                    tm.releaseTXLock(sessionId);
                    tm.destroyTransaction(sessionId);
                }
            }
        }
        return null;
    }
}
