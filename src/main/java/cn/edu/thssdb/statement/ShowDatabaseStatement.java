package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Column;
import cn.edu.thssdb.schema.Entry;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.schema.Row;
import cn.edu.thssdb.type.ColumnType;

import java.util.ArrayList;

public class ShowDatabaseStatement implements Statement {

    public ShowDatabaseStatement() {
        super();
    }

    @Override
    public QueryTable execute(Manager manager, Long sessionId, String command) throws SQLHandleException {
        ArrayList<String> databaseArrayList = manager.getAllDatabases();
        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column("DATABASES", ColumnType.STRING, false, true, 100));
        QueryTable queryTable = new QueryTable("ShowDatabaseResult", columns);
        for (String name : databaseArrayList) {
            Entry[] entries = new Entry[]{new Entry(name)};
            queryTable.rows.add(new Row(entries));
        }
        return queryTable;
    }
}
