package cn.edu.thssdb.schema;

import cn.edu.thssdb.exception.DisconnectionException;
import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.exception.SessionLostException;
import cn.edu.thssdb.server.ThssDB;
import cn.edu.thssdb.utils.Global;
import cn.edu.thssdb.rpc.thrift.ConnectResp;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Manager {
    private HashMap<String, Database> databases;
    private HashMap<Long, ConnectResp> connections;
    private String root;
    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static Manager getInstance() {
        return Manager.ManagerHolder.INSTANCE;
    }

    public Manager() {
        this.databases = new HashMap<>();
        this.connections = new HashMap<>();
        // 这里需要之后添加解析参数修改 先默认为DEFAULT_SERVER_ROOT
        this.root = Global.DEFAULT_SERVER_ROOT;
        if (!new File(this.root).exists() && !new File(this.root).mkdir())
            throw new SQLHandleException("Cannot find root");
        this.loadDatabase();
    }

    public void createDatabaseIfNotExists(String databaseName){
        if (this.databases.containsKey(databaseName)) {
            throw new SQLHandleException("Database " + databaseName + " already exists.");
        } else {
            String new_root = Paths.get(root, databaseName).toString();
            if (!new File(new_root).mkdir())
                throw new SQLHandleException("Cannot create database");
            databases.put(databaseName, new Database(databaseName, new_root));
        }
    }

    public void deleteDatabase(String databaseName){
        // TODO
        if (!databases.containsKey(databaseName))
            throw new SQLHandleException("Database does not exist");
        Database database = databases.get(databaseName);
        if (!deleteRecursive(new File(database.getRoot())))
            throw new SQLHandleException("Failed to delete database");
        for(Map.Entry<Long, ConnectResp> entry : this.connections.entrySet()){
            if(entry.getValue().status.currentDatabase.equals(databaseName)) {
                entry.getValue().status.currentDatabase = null;
            }
        }
        databases.remove(databaseName);
    }

    public void switchDatabase(long sessionId, String databaseName){
        // TODO
        if(this.connections.containsKey(sessionId)){
            if(this.databases.containsKey(databaseName)){
                this.connections.get(sessionId).status.currentDatabase = databaseName;
            } else {
                throw new SQLHandleException("Database " + databaseName + " not exists");
            }
        }
        else throw new SessionLostException(sessionId);
    }

    public Database getSessionCurrentDatabase(long sessionId) {
        if(this.connections.containsKey(sessionId))
        {
            if(this.databases.containsKey(this.connections.get(sessionId).status.currentDatabase))
                return this.databases.get(this.connections.get(sessionId).status.currentDatabase);
            else if(this.connections.get(sessionId).status.currentDatabase.equals(""))
                throw new SQLHandleException("Use database first");
            else
                throw new SQLHandleException("Database " + this.connections.get(sessionId).status.currentDatabase + " not exists");
        }
        else
            throw new SessionLostException(sessionId);
    }

    private static class ManagerHolder {
        private static final Manager INSTANCE = new Manager();

        private ManagerHolder() {

        }
    }

    /**
     * delete the file under the folder recursively
     * @param path 待删除的目录
     * @return 是否成功删除
     */
    private static boolean deleteRecursive(File path) {
        boolean ret = true;
        if (path.isDirectory()){
            File[] files = path.listFiles();
            if (files != null)
                for (File f : files)
                    ret = ret && deleteRecursive(f);
        }
        return ret && path.delete();
    }

    public synchronized void addConnection(long sessionId, ConnectResp resp) {
        this.connections.put(sessionId, resp);
    }

    public synchronized void removeConnection(long sessionId) throws DisconnectionException {
        if(this.connections.containsKey(sessionId))
            this.connections.remove(sessionId);
        else throw new DisconnectionException(sessionId);
    }

    public ConnectResp getConnection(long sessionId) {
        if(this.connections.containsKey(sessionId))
            return this.connections.get(sessionId);
        else
            throw new SessionLostException(sessionId);
    }

    private void loadDatabase() {
        databases.clear();
        String[] list = new File(root).list();
        if (list != null) {
            for (String item : list)
                databases.put(item, new Database(item, Paths.get(root, item).toString()));
        } else
            throw new RuntimeException("List databases failed");
    }

    public void deleteAllDatabase() {
        for(Map.Entry<String, Database> entry : this.databases.entrySet()){
            this.deleteDatabase(entry.getKey());
        }
    }

    public String getRoot() {
        return this.root;
    }
}
