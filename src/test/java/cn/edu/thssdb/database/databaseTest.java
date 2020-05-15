package cn.edu.thssdb.database;

import cn.edu.thssdb.exception.*;
import cn.edu.thssdb.rpc.thrift.ConnectResp;
import cn.edu.thssdb.rpc.thrift.Status;
import cn.edu.thssdb.schema.*;
import cn.edu.thssdb.type.ColumnType;
import cn.edu.thssdb.utils.Global;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class databaseTest {
    private Manager manager;

    @Before
    public void setUp() {
        System.out.println("----------setUp----------");
        manager = new Manager();
        manager.deleteAllDatabase();
    }

    @Test
    public void testCreateTable() {
        System.out.println("----------testCreateTable----------");
        manager.deleteAllDatabase();
        manager.createDatabaseIfNotExists("database");
        Database database = new Database("database", Paths.get(manager.getRoot(), "database").toString());
        Column[] columns = new Column[2];
        columns[0] = new Column("ID", ColumnType.INT, true, true, 20);
        columns[1] = new Column("name", ColumnType.STRING, false, true, 100);
        database.createTable("table", columns);

        Assert.assertThrows(SQLHandleException.class, ()->{
            database.createTable("table", columns);
        });
    }

    @Test
    public void testDeleteTable() {
        System.out.println("----------testDeleteTable----------");
        manager.deleteAllDatabase();
        manager.createDatabaseIfNotExists("database");
        Database database = new Database("database", Paths.get(manager.getRoot(), "database").toString());
        Column[] columns = new Column[2];
        columns[0] = new Column("ID", ColumnType.INT, true, true, 20);
        columns[1] = new Column("name", ColumnType.STRING, false, true, 100);
        database.createTable("table", columns);
        database.dropTable("table");

        Assert.assertThrows(SQLHandleException.class, ()->{
            database.dropTable("table");
        });
    }

    @Test
    public void testCreateDatabase() throws SQLHandleException {
        System.out.println("----------testCreateDatabase----------");
        manager.deleteAllDatabase();
        manager.createDatabaseIfNotExists("database");

        Assert.assertThrows(SQLHandleException.class, () -> {
            manager.createDatabaseIfNotExists("database");
        });
    }

    @Test
    public void testDeleteDatabase() throws SQLHandleException {
        System.out.println("----------testDeleteDatabase----------");
        manager.deleteAllDatabase();
        manager.createDatabaseIfNotExists("database");
        manager.deleteDatabase("database");

        Assert.assertThrows(SQLHandleException.class, () -> {
            manager.deleteDatabase("database3");
        });
    }

    @Test
    public void testSwitchDatabase() {
        System.out.println("----------testSwitchDatabase----------");
        manager.deleteAllDatabase();
        manager.createDatabaseIfNotExists("database");
        ConnectResp resp = new ConnectResp();
        Status status = new Status(Global.SUCCESS_CODE, "");
        resp.status = status;
        resp.sessionId = 1;
        manager.addConnection(1, resp);
        manager.switchDatabase(1, "database");

        Assert.assertThrows(SessionLostException.class, () -> {
            manager.switchDatabase(2, "database");
        });

        Assert.assertThrows(SQLHandleException.class, ()->{
            manager.switchDatabase(1, "database1");
        });
    }

    @Test
    public void testRecoverDatabase() {
        System.out.println("----------testRecoverDatabase----------");
        manager.deleteAllDatabase();
        manager.createDatabaseIfNotExists("database");
        Database database = new Database("database", Paths.get(manager.getRoot(), "database").toString());
        Column[] columns = new Column[2];
        columns[0] = new Column("ID", ColumnType.INT, true, true, 20);
        columns[1] = new Column("name", ColumnType.STRING, false, true, 100);
        ArrayList<Column> columnArrayList = (ArrayList<Column>) Arrays.asList(columns);
        Table table = new Table(Paths.get(database.getRoot(), "table").toString(), "table", columnArrayList);

        Row newRow_1 = new Row();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(1, ColumnType.INT));
        entries.add(new Entry("Shion", ColumnType.STRING));
        newRow_1.appendEntries(entries);
        table.insert(newRow_1);

        Row newRow_2 = new Row();
        entries = new ArrayList<Entry>();
        entries.add(new Entry(2, ColumnType.INT));
        entries.add(new Entry("Ayaka", ColumnType.STRING));
        newRow_2.appendEntries(entries);
        table.insert(newRow_2);
        table.serialize();

        database.recover();
    }
}
