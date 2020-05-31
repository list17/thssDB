package cn.edu.thssdb.utils;

import cn.edu.thssdb.exception.FileWriteException;
import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.schema.Manager;
import com.sun.javafx.iio.ios.IosDescriptor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Transaction {
    private ArrayList<String> scrtipts;
    private Long sessionId;

    public Transaction(Long sessionId) {
        this.sessionId = sessionId;
    }

    public void addScript(String script) {
        this.scrtipts.add(script);
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
