package cn.edu.thssdb.utils;

import cn.edu.thssdb.exception.FileWriteException;
import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.schema.Manager;
import cn.edu.thssdb.schema.Row;
import cn.edu.thssdb.schema.Table;
import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static cn.edu.thssdb.utils.Global.NO_LOCK;

public class Transaction {
    private ArrayList<String> scrtipts;
    private ArrayList<Pair<Pair<Row, Row>, Table>> logs;
    private Long tx_session;
    private Boolean isBlocked;
    private int blockType;
    private String blockTable;
    private boolean DLFlag;

    public Transaction(Long tx_session) {
        this.tx_session = tx_session;
        this.isBlocked = false;
        this.DLFlag = false;
        this.blockType = NO_LOCK;
        this.blockTable = null;
        this.scrtipts = new ArrayList<String>();
        this.logs = new ArrayList<Pair<Pair<Row, Row>, Table>>();
    }

    // 正序添加script
    public void addScript(String script) {
        this.scrtipts.add(script);
    }

    public void clearScripts() {
        this.scrtipts.clear();
    }

    //逆序添加log
    public void addLog(Row now, Row ori, Table table) {
        Pair<Row, Row> log_row = new Pair<Row, Row>(now, ori);
        Pair<Pair<Row, Row>, Table> log = new Pair<Pair<Row, Row>, Table>(log_row, table);
        this.logs.add(0, log);
    }

    public void clearLogs() {
        this.logs.clear();
    }

    public void printLogs() {
        for (int i = 0; i < this.logs.size(); i++) {
            System.out.println(this.logs.get(i));
        }
    }

    public void rollback() {
        int len = this.logs.size();
        for (int i = 0; i < len; i++) {
            Pair<Pair<Row, Row>, Table> tmp_log = this.logs.get(0);

            tmp_log.getValue().updateByIndicatingRow(tmp_log.getKey().getKey(), tmp_log.getKey().getValue(), this);

            this.logs.remove(0);
        }
        this.scrtipts.clear();
    }

    // commit
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
        } catch (IOException e) {
            throw new FileWriteException("Write file *.script failed.");
        } finally {
            try {
                bw.close();
                fw.close();
            } catch (IOException e) {
                throw new FileWriteException("Close file *.script output-stream failed.");
            }
        }
    }

    public int getBlockType() {
        return this.blockType;
    }

    public String getBlockTable() {
        return this.blockTable;
    }

    public void setBlock(boolean b, String table, int type) {
        this.isBlocked = b;
        if (this.isBlocked) {

            this.blockType = type;
            this.blockTable = table;
            int a = 0;
            while (this.isBlocked) {
                // loop to block
                System.out.print("");
            }
            if (this.DLFlag) {
                this.clearScripts();
                this.rollback();
                TransactionManager tm = TransactionManager.getInstance();
                tm.releaseTXLock(this.tx_session);
                tm.removeBlock(this.tx_session);
                this.DLFlag = false;
                throw new SQLHandleException("Dead lock occurred, rollback all transactions.");
            }
        } else {
            this.blockTable = null;
            this.blockType = NO_LOCK;
        }

    }

    public void deadLockHandle(Long tx_session) {
        this.DLFlag = true;
        this.isBlocked = false;
    }
}
