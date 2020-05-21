package cn.edu.thssdb.schema;

import cn.edu.thssdb.exception.SQLHandleException;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Database {

    private String name;
    private String root;
    private HashMap<String, Table> tables;
    ReentrantReadWriteLock lock;

    public Database(String name, String databaseRoot) throws SQLHandleException {
        this.name = name;
        this.root = databaseRoot;
        this.tables = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
        recover();
    }

    public Database(String name, String databaseRoot, boolean withRecover) throws SQLHandleException {
        this.name = name;
        this.root = databaseRoot;
        this.tables = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
        if (withRecover) {
            recover();
        }
    }

    public Table createTable(String name, ArrayList<Column> columns) throws SQLHandleException {
        // TODO
        if (this.tables.containsKey(name))
            throw new SQLHandleException("Table " + name + " already exists.");
        String tableRoot = Paths.get(this.root, name).toString();

        Table table = new Table(tableRoot, name, columns);
        this.tables.put(name, table);
        return table;
    }

    public void dropTable(String name) throws SQLHandleException {
        // TODO
        if (!tables.containsKey(name)) {
            throw new SQLHandleException("Table " + name + " does not exist");
        }
        Table table = tables.get(name);
        if (new File(table.getRoot()).exists() && !new File(table.getRoot()).delete()) {
            throw new SQLHandleException("Fail to delete table " + name + ".");
        }
        this.tables.remove(name);
    }

    public Table getTable(String name) throws SQLHandleException {
        return this.tables.get(name);
    }

    public void recover() {
        this.tables.clear();
        String[] list = new File(this.root).list();
        if (list != null) {
            for (String item : list) {
                Table table = new Table(Paths.get(this.root, item).toString(), item);
                table.recover();
                this.tables.put(name, table);
            }
        } else
            throw new SQLHandleException("Load database failed");
    }

    public ArrayList<String> getAllTables() {
        return new ArrayList<String>(this.tables.keySet());
    }

    public String getRoot() {
        return this.root;
    }
}
