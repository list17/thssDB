package cn.edu.thssdb.schema;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.server.ThssDB;
import cn.edu.thssdb.utils.Global;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Manager {
    private HashMap<String, Database> databases;
    private String root;
    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static Manager getInstance() {
        return Manager.ManagerHolder.INSTANCE;
    }

    public Manager() {
        this.databases = new HashMap<>();
        // 这里需要之后添加解析参数修改 先默认为DEFAULT_SERVER_ROOT
        this.root = Global.DEFAULT_SERVER_ROOT;
    }

    private void createDatabaseIfNotExists(String databaseName) throws SQLHandleException {
        if (this.databases.containsKey(databaseName)) {
            throw new SQLHandleException("Database " + databaseName + " already exists.");
        } else {
            String new_root = Paths.get(root, databaseName).toString();
            if (!new File(new_root).mkdir())
                throw new SQLHandleException("Cannot create database");
            databases.put(databaseName, new Database(databaseName, new_root));
        }

    }

    private void deleteDatabase() {
        // TODO
    }

    public void switchDatabase() {
        // TODO
    }

    private static class ManagerHolder {
        private static final Manager INSTANCE = new Manager();

        private ManagerHolder() {

        }
    }
}
