package cn.edu.thssdb.utils;

import java.util.HashMap;

public class TransactionManager {
    private HashMap<Long, Transaction> TransactionMap;
    private Boolean Flag;

    public TransactionManager() {

    }

    public void changeFlag() {
        this.Flag = !this.Flag;
    }

}
