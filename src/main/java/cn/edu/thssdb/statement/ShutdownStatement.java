package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Database;
import cn.edu.thssdb.schema.Manager;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class ShutdownStatement implements Statement{
    @Override
    public QueryTable execute(Manager manager, Long sessionId) throws SQLHandleException {
        ArrayList<String> database_names = manager.getAllDatabases();
        for (String name : database_names) {
            Database database = manager.getDatabase(name);
            database.saveData();
        }
        return null;
    }
}
