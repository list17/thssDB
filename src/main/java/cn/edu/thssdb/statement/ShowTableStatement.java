package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.*;
import cn.edu.thssdb.type.ColumnType;

import java.util.ArrayList;

public class ShowTableStatement implements Statement{
    private String name;

    public ShowTableStatement(String name) {
        this.name = name;
    }

    @Override
    public QueryTable execute(Manager manager, Long sessionId, String command) throws SQLHandleException {
        if(!manager.getAllDatabases().contains(this.name)) {
            throw new SQLHandleException("Database " + this.name + "does not exist.");
        }
        Database database = manager.getDatabase(this.name);
        ArrayList<String> databaseArrayList = database.getAllTables();
        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column("TABLES", ColumnType.STRING, false, true, 100));
        QueryTable queryTable = new QueryTable("ShowTableResult", columns);
        for(String name: databaseArrayList) {
            Entry[] entries = new Entry[] {new Entry(name)};
            queryTable.rows.add(new Row(entries));
        }
        return queryTable;
    }
}
