package cn.edu.thssdb.schema;

import cn.edu.thssdb.exception.DisconnectionException;
import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.exception.SQLThrowErrorListener;
import cn.edu.thssdb.exception.SessionLostException;
import cn.edu.thssdb.parser.SQLLexer;
import cn.edu.thssdb.parser.SQLParser;
import cn.edu.thssdb.parser.Visitor;
import cn.edu.thssdb.rpc.thrift.ConnectReq;
import cn.edu.thssdb.rpc.thrift.ExecuteStatementReq;
import cn.edu.thssdb.rpc.thrift.Status;
import cn.edu.thssdb.statement.Statement;
import cn.edu.thssdb.utils.Global;
import cn.edu.thssdb.rpc.thrift.ConnectResp;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
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
        this.loadFromScript();
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
            if(entry.getValue().status.currentDatabase != null && entry.getValue().status.currentDatabase.equals(databaseName)) {
                entry.getValue().status.currentDatabase = "";
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

    public void loadFromScript(){
        long sessionId = 12334545;
        ConnectResp resp = new ConnectResp();
        Status status = new Status();
        status.code = Global.SUCCESS_CODE;
        status.currentDatabase = "";
        resp.sessionId = sessionId;
        resp.status = status;
        this.addConnection(sessionId, resp);
        for (Map.Entry<String, Database> entry : this.databases.entrySet()){
            this.switchDatabase(sessionId, entry.getKey());
            // 找到这个数据库目录下的所有.script文件
            String[] list = new File(this.root + "/" + entry.getKey()).list();
            if (list != null) {
                for (String item : list) {
                    if (item.endsWith(".script")){
                        // 按行读取script文件并执行
                        Scanner scanner = null;
                        try {
                            scanner = new Scanner(new File(this.root + "/" + entry.getKey() + "/" + item));
                            while (scanner.hasNextLine()) {
                                String command = scanner.nextLine();
                                try{
                                    SQLThrowErrorListener listener = new SQLThrowErrorListener();
                                    SQLLexer lexer = new SQLLexer(CharStreams.fromString(command));
                                    lexer.removeErrorListeners();
                                    lexer.addErrorListener(listener);
                                    CommonTokenStream tokens = new CommonTokenStream(lexer);
                                    SQLParser parser = new SQLParser(tokens);
                                    parser.removeErrorListeners();
                                    parser.addErrorListener(listener);
                                    ParseTree tree = parser.parse();
                                    // 遍历语法树
                                    Visitor visitor = new Visitor();
                                    ArrayList<Statement> statements = (ArrayList<Statement>) visitor.visit(tree);
                                    // 执行语句
                                    for (Statement statement: statements){
                                        statement.execute(this, sessionId, command);
                                    }
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else
                throw new SQLHandleException("Recovery database from script failed");
        }
        this.removeConnection(sessionId);
    }

    public ArrayList<String> getAllDatabases() {
        return new ArrayList<String>(this.databases.keySet());
    }

    public void deleteAllDatabase() {
        for(Map.Entry<String, Database> entry : this.databases.entrySet()){
            this.deleteDatabase(entry.getKey());
        }
    }

    public Database getDatabase(String name) {
        return this.databases.get(name);
    }

    public String getRoot() {
        return this.root;
    }
}
