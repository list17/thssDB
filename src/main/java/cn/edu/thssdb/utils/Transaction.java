package cn.edu.thssdb.utils;

import cn.edu.thssdb.exception.FileWriteException;
import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.schema.Manager;
import com.sun.javafx.iio.ios.IosDescriptor;
import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Transaction {
    private ArrayList<String> scrtipts;
    private ArrayList<Pair<String, String>> logs;
    private Long tx_session;

    public Transaction(Long tx_session) {
        this.tx_session = tx_session;
        this.scrtipts = new ArrayList<String>();
    }

    // 正序添加script
    public void addScript(String script) {
        this.scrtipts.add(script);
    }

    public void clearScripts() {
        this.scrtipts.clear();
    }

    //逆序添加log
    public void addLog(String now, String ori) {
        Pair<String, String> log = new Pair<String, String>(now, ori);
        this.logs.add(0, log);
    }

    public void rollback() {
        int len = this.logs.size();
        for (int i = 0; i < len; i++) {
            // TODO: recover data
            this.logs.remove(0);
            this.scrtipts.remove(this.scrtipts.size() - 1);
        }
    }

    public void output(Manager manager, Long sessionId) {
        String db_name = manager.getSessionCurrentDatabase(sessionId).getName();
        String path = "./data/" + db_name + "/" + db_name + ".script";

        FileWriter fw = null;
        BufferedWriter bw = null;

        try {
            fw = new FileWriter(path, true);
            bw = new BufferedWriter(fw);

            for (int i = 0; i < this.scrtipts.size(); i++) {
                bw.write(this.scrtipts.get(i));
                bw.newLine();
                bw.flush();
            }
            this.scrtipts = null;
        }
        catch (IOException e) {
            throw new FileWriteException("Write file *.script failed.");
        }
        finally {
            try {
                bw.close();
                fw.close();
            }
            catch (IOException e) {
                throw new FileWriteException("Close file *.script output-stream failed.");
            }
        }
    }
}
