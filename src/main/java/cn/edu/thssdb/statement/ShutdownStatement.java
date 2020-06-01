package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.Database;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.utils.TransactionManager;
import cn.edu.thssdb.utils.WriteScript;

import javax.xml.crypto.Data;
import java.io.File;
import java.util.ArrayList;

public class ShutdownStatement implements Statement{
    @Override
    public QueryTable execute(Manager manager, Long sessionId, String command) throws SQLHandleException {
        ArrayList<String> database_names = manager.getAllDatabases();

        TransactionManager tm = TransactionManager.getInstance();

        if (tm.getFlag(sessionId)) { // 事务态
            tm.getTX().output(manager, sessionId);
        }

        for (String name : database_names) {
            // 持久化储存
            Database database = manager.getDatabase(name);
            database.saveData();
            // 删除script
            try{
                File file = new File("./data/" + name + "/" + name + ".script");
                if(file.delete()){
                    System.out.println("Delete files" + name + ".script successfully");
                }
                else{
                    System.out.println("Delete files" + name + ".script failed");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        // 退出
        System.exit(0);

        return null;
    }
}
