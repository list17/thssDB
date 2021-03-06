package cn.edu.thssdb.database;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.exception.SessionLostException;
import cn.edu.thssdb.rpc.thrift.ConnectResp;
import cn.edu.thssdb.rpc.thrift.Status;
import cn.edu.thssdb.schema.*;
import cn.edu.thssdb.type.ColumnType;
import cn.edu.thssdb.utils.Global;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.ArrayList;

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
        manager.createDatabaseIfNotExists("TESTDATABASE");
        Database database = new Database("TESTDATABASE", Paths.get(manager.getRoot(), "TESTDATABASE").toString());
        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column("ID", ColumnType.INT, true, true, 20));
        columns.add(new Column("name", ColumnType.STRING, false, true, 100));
        database.createTable("TABLETEST", columns);

        Assert.assertThrows(SQLHandleException.class, () -> {
            database.createTable("TABLETEST", columns);
        });
    }

    @Test
    public void testDeleteTable() {
        System.out.println("----------testDeleteTable----------");
        manager.deleteAllDatabase();
        manager.createDatabaseIfNotExists("TESTDATABASE");
        Database database = new Database("TESTDATABASE", Paths.get(manager.getRoot(), "TESTDATABASE").toString());
        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column("ID", ColumnType.INT, true, true, 20));
        columns.add(new Column("name", ColumnType.STRING, false, true, 100));
        database.createTable("TABLETEST", columns);
        database.dropTable("TABLETEST");

        Assert.assertThrows(SQLHandleException.class, () -> {
            database.dropTable("TABLETEST");
        });
    }

    @Test
    public void testCreateDatabase() throws SQLHandleException {
        System.out.println("----------testCreateDatabase----------");
        manager.deleteAllDatabase();
        manager.createDatabaseIfNotExists("TESTDATABASE");

        Assert.assertThrows(SQLHandleException.class, () -> {
            manager.createDatabaseIfNotExists("TESTDATABASE");
        });
    }

    @Test
    public void testDeleteDatabase() throws SQLHandleException {
        System.out.println("----------testDeleteDatabase----------");
        manager.deleteAllDatabase();
        manager.createDatabaseIfNotExists("TESTDATABASE");
        manager.deleteDatabase("TESTDATABASE");

        Assert.assertThrows(SQLHandleException.class, () -> {
            manager.deleteDatabase("database3");
        });
    }

    @Test
    public void testSwitchDatabase() {
        System.out.println("----------testSwitchDatabase----------");
        manager.deleteAllDatabase();
        manager.createDatabaseIfNotExists("TESTDATABASE");
        ConnectResp resp = new ConnectResp();
        Status status = new Status(Global.SUCCESS_CODE, "");
        resp.status = status;
        resp.sessionId = 1;
        manager.addConnection(1, resp);
        manager.switchDatabase(1, "TESTDATABASE");

        Assert.assertThrows(SessionLostException.class, () -> {
            manager.switchDatabase(2, "TESTDATABASE");
        });

        Assert.assertThrows(SQLHandleException.class, () -> {
            manager.switchDatabase(1, "database1");
        });
    }

    @Test
    public void testRecoverDatabase() {
        System.out.println("----------testRecoverDatabase----------");
        manager.deleteAllDatabase();
        manager.createDatabaseIfNotExists("TESTDATABASE");
        Database database = new Database("TESTDATABASE", Paths.get(manager.getRoot(), "TESTDATABASE").toString());
        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column("ID", ColumnType.INT, true, true, 20));
        columns.add(new Column("name", ColumnType.STRING, false, true, 100));
        Table table = new Table(Paths.get(database.getRoot(), "TABLETEST").toString(), "TABLETEST", columns);

        Row newRow_1 = new Row();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(1));
        entries.add(new Entry("Shion"));
        newRow_1.appendEntries(entries);
        table.insert(newRow_1, null);

        Row newRow_2 = new Row();
        entries = new ArrayList<Entry>();
        entries.add(new Entry(2));
        entries.add(new Entry("Ayaka"));
        newRow_2.appendEntries(entries);
        table.insert(newRow_2, null);
        table.serialize();

        database.recover();
    }
}
