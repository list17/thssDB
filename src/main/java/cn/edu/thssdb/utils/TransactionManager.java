package cn.edu.thssdb.utils;

import java.util.HashMap;
import java.util.Random;

public class TransactionManager {
    private static TransactionManager instance = null;

    private final Random random = new Random();

    private HashMap<Long, Transaction> transactionMap;
    private Boolean Flag; // false: 非事务态，true：事务态，需要存scripts、logs
    private Long cur_tx_session;

    private TransactionManager() {
        this.Flag = false;
        this.transactionMap = new HashMap<>();
    }

    public static TransactionManager getInstance() {
        if (instance == null) {
            synchronized (TransactionManager.class) {
                if (instance == null) {
                    instance = new TransactionManager();
                }
            }
        }
        return instance;
    }

    public Long createTransaction() {
        Long tx_session = random.nextLong();
        Transaction tx = new Transaction(tx_session);

        this.cur_tx_session = tx_session;

        transactionMap.put(tx_session, tx) ;
        return tx_session;
    }

    public void destroyTransaction() {
        this.transactionMap.remove(this.cur_tx_session);
    }

    public Transaction getTX() {
        return this.transactionMap.get(this.cur_tx_session);
    }

    public void changeFlag() {
        this.Flag = !this.Flag;
    }

    public boolean getFlag() {
        return this.Flag;
    }
}
