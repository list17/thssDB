package cn.edu.thssdb.utils;

import cn.edu.thssdb.exception.FileWriteException;
import cn.edu.thssdb.schema.Manager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteScript {
    public WriteScript(){

    }

    public void output(Manager manager, Long sessionId, String command) {
        String db_name = manager.getSessionCurrentDatabase(sessionId).getName();
        String path = "./data/" + db_name + "/" + db_name + ".script";

        FileWriter fw = null;
        BufferedWriter bw = null;

        try {
            fw = new FileWriter(path, true);
            bw = new BufferedWriter(fw);
            bw.write(command);
            bw.newLine();
            bw.flush();
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
