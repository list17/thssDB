package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Column;
import cn.edu.thssdb.schema.Database;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.utils.TransactionManager;
import cn.edu.thssdb.utils.WriteScript;

import java.util.ArrayList;

public class CreateTableStatement implements Statement{
    private String name;
    private ArrayList<ColumnDefinition> columnDefinitions;

    public CreateTableStatement(String name, ArrayList<ColumnDefinition> columnDefinitions) {
        this.name = name;
        this.columnDefinitions = columnDefinitions;
    }

    @Override
    public QueryTable execute(Manager manager, Long sessionId, String command) throws SQLHandleException {
        Database database = manager.getSessionCurrentDatabase(sessionId);
        if(database == null)
            throw new SQLHandleException("No database selected");
        ArrayList<Column> columns = new ArrayList<>();
        for(ColumnDefinition columnDefinition : this.columnDefinitions)
            columnDefinition.attach(columns);
        database.createTable(this.name, columns);

        TransactionManager tm = TransactionManager.getInstance();

        if (tm.getFlag()) { // 事务态
            tm.getTX().addScript(command);
        }
        else { // 非事务态
            WriteScript ws = new WriteScript();
            ws.output(manager, sessionId, command);
        }
        return null;
    }
}
