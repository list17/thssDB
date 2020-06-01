package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Column;
import cn.edu.thssdb.schema.Database;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.utils.TransactionManager;
import cn.edu.thssdb.utils.ValueInstance;
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

        TransactionManager tm = TransactionManager.getInstance();
        ValueInstance vi = ValueInstance.getInstance();

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
            database.createTable(this.name, columns);
        } catch (Exception e) {

        } finally {
            if (!vi.getIsInit()) {
                if (tm.getFlag(sessionId)) { // 事务态
                    tm.getTX().addScript(command);
                } else { // 非事务态
                    tm.releaseTXLock(sessionId);
                    tm.destroyTransaction(sessionId);
                    WriteScript ws = new WriteScript();
                    ws.output(manager, sessionId, command);
                }
            }
        }

        return null;
    }
}
