package cn.edu.thssdb.utils;

import cn.edu.thssdb.schema.Table;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import static cn.edu.thssdb.utils.Global.NO_LOCK;
import static cn.edu.thssdb.utils.Global.LOCK_S;
import static cn.edu.thssdb.utils.Global.LOCK_X;

public class TransactionManager {
    private static TransactionManager instance = null;

    private final Random random = new Random();

    // 事务的session表
    private HashMap<Long, Transaction> transactionMap;

    /*
    * 表对应的锁
    * Pair.key: 持有该表共享锁的事务列表 lockS
    * Pair.value: 持有该表排它锁的事务 lockX
    */
    private HashMap<String, Pair<ArrayList<Long>, ArrayList<Long>>> tableLocks;

    /*
    * 事务拥有的锁
    * Pair.key: 事务持有lockS的table列表
    * Pair.value: 事务持有lockX的table列表
    */
//    private HashMap<Transaction, Pair<ArrayList<String>, ArrayList<String>>> txLocks;

    // 阻塞事务列表
    private ArrayList<Long> blockedTXs;

    private HashMap<Long, Boolean> Flags; // false: 非事务态，true：事务态，需要存scripts、logs
    private Long cur_tx_session;

    private TransactionManager() {
        this.Flags = new HashMap<Long, Boolean>();

        // 事务表
        this.transactionMap = new HashMap<Long, Transaction>();

        // 锁表
        this.tableLocks = new HashMap<String, Pair<ArrayList<Long>, ArrayList<Long>>>();
//        this.txLocks = new HashMap<Transaction, Pair<ArrayList<String>, ArrayList<String>>>();

        // 阻塞列表
        this.blockedTXs = new ArrayList<Long>();
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

    public Long createTransaction(Long tx_session) {
        Transaction tx = new Transaction(tx_session);

        this.cur_tx_session = tx_session;

        transactionMap.put(tx_session, tx) ;
        return tx_session;
    }

    public void destroyTransaction(Long tx_session) {
        this.cur_tx_session = tx_session;
        this.transactionMap.remove(tx_session);
    }

    public boolean checkTransaction(Long tx_session) {
        this.cur_tx_session = tx_session;
        return this.transactionMap.get(tx_session) != null;
    }

    public void blockTX(Long tx_session, String table, int type) {
        Transaction tx = this.transactionMap.get(tx_session);
        tx.setBlock(true, table, type);
    }

    public void continueBlockedTX() {
        int size = this.blockedTXs.size();
        for (int i = 0; i < size; i++) {
            Long tx_session = this.blockedTXs.get(i);
            this.cur_tx_session = tx_session;
            Transaction tx = this.transactionMap.get(tx_session);
            if (tx == null) {
                return;
            }
            if (resetLock(tx_session, tx.getBlockTable(), tx.getBlockType())) {
                tx.setBlock(false, null, NO_LOCK);
            }
        }
    }

    public boolean resetLock(Long tx_session, String table, int type) {
        if (type == LOCK_S) {
            int lockType = checkTableHasLock(table);
            Pair<ArrayList<Long>, ArrayList<Long>> txList = this.tableLocks.get(table);
            if (txList == null) {
                ArrayList<Long> tmpS = new ArrayList<Long>();
                ArrayList<Long> tmpX = new ArrayList<Long>();
                Pair<ArrayList<Long>, ArrayList<Long>> tmpPair = new Pair<ArrayList<Long>, ArrayList<Long>>(tmpS, tmpX);
                txList = tmpPair;
                this.tableLocks.put(table, tmpPair);
            }

            if (lockType == 0) {
                txList.getKey().add(tx_session);
                return true;
            } else if (lockType == 1) {
                if (!txList.getKey().contains(tx_session)) {
                    txList.getKey().add(tx_session);
                    return true;
                }
            } else {
                if (txList.getValue().contains(tx_session)) {
                    return true;
                }
            }
        }
        else if (type == LOCK_X) {
            int lockType = checkTableHasLock(table);
            Pair<ArrayList<Long>, ArrayList<Long>> txList = this.tableLocks.get(table);
            if (txList == null) {
                ArrayList<Long> tmpS = new ArrayList<Long>();
                ArrayList<Long> tmpX = new ArrayList<Long>();
                Pair<ArrayList<Long>, ArrayList<Long>> tmpPair = new Pair<ArrayList<Long>, ArrayList<Long>>(tmpS, tmpX);
                txList = tmpPair;
                this.tableLocks.put(table, tmpPair);
            }

            if (lockType == 0) {
                txList.getValue().add(tx_session);
                return true;
            } else if (lockType == 1) {
                if (txList.getKey().contains(tx_session) && txList.getKey().size() == 1) {
                    txList.getKey().remove(tx_session);
                    txList.getValue().add(tx_session);
                    return true;
                }
            } else {
                if (txList.getValue().contains(tx_session)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int checkTableHasLock(String table) {
        Pair<ArrayList<Long>, ArrayList<Long>> txList = this.tableLocks.get(table);
        if (txList == null) {
            ArrayList<Long> tmpS = new ArrayList<Long>();
            ArrayList<Long> tmpX = new ArrayList<Long>();
            Pair<ArrayList<Long>, ArrayList<Long>> tmpPair = new Pair<ArrayList<Long>, ArrayList<Long>>(tmpS, tmpX);
            txList = tmpPair;
            this.tableLocks.put(table, tmpPair);
        }

        int num_s = txList.getKey().size();
        int num_x = txList.getValue().size();
//        System.out.println("check lock" + table);
//        System.out.println("s " + num_s);
//        System.out.println("x " + num_x);

        if (num_x == 1) {
            return LOCK_X;
        } else if (num_s > 0) {
            return LOCK_S;
        } else {
            return NO_LOCK;
        }
    }

    public void setLockS(Long tx_session, String table) {
        this.cur_tx_session = tx_session;
        int lockType = checkTableHasLock(table);
        Pair<ArrayList<Long>, ArrayList<Long>> txList = this.tableLocks.get(table);
        if (txList == null) {
            ArrayList<Long> tmpS = new ArrayList<Long>();
            ArrayList<Long> tmpX = new ArrayList<Long>();
            Pair<ArrayList<Long>, ArrayList<Long>> tmpPair = new Pair<ArrayList<Long>, ArrayList<Long>>(tmpS, tmpX);
            txList = tmpPair;
            this.tableLocks.put(table, tmpPair);
        }

//        System.out.println(tx_session);
//        System.out.println("type" + lockType);

        if (lockType == NO_LOCK) {
            txList.getKey().add(tx_session);
            return;
        } else if (lockType == LOCK_S) {
            if (!txList.getKey().contains(tx_session)) {
                txList.getKey().add(tx_session);
            }
            return;
        } else {
            if (txList.getValue().contains(tx_session)) {
                return;
            }
        }
        this.blockedTXs.add(tx_session);
        this.blockTX(tx_session, table, LOCK_S);
        return;
    }

    public void setLockX(Long tx_session, String table) {
        this.cur_tx_session = tx_session;

        int lockType = checkTableHasLock(table);
        Pair<ArrayList<Long>, ArrayList<Long>> txList = this.tableLocks.get(table);
        if (txList == null) {
            ArrayList<Long> tmpS = new ArrayList<Long>();
            ArrayList<Long> tmpX = new ArrayList<Long>();
            Pair<ArrayList<Long>, ArrayList<Long>> tmpPair = new Pair<ArrayList<Long>, ArrayList<Long>>(tmpS, tmpX);
            txList = tmpPair;
            this.tableLocks.put(table, tmpPair);
        }

        if (lockType == NO_LOCK) {
            txList.getValue().add(tx_session);
            return;
        } else if (lockType == LOCK_S) {
            if (txList.getKey().contains(tx_session) && txList.getKey().size() == 1) {
                txList.getKey().remove(tx_session);
                txList.getValue().add(tx_session);
                return;
            }
        } else {
            if (txList.getValue().contains(tx_session)) {
                return;
            }
        }
        this.blockedTXs.add(tx_session);
        this.blockTX(tx_session, table ,LOCK_X);
        return;
    }


    public boolean setLockSSingle(Long tx_session, String table) {
        this.cur_tx_session = tx_session;
        this.createTransaction(tx_session);

        int lockType = checkTableHasLock(table);
        Pair<ArrayList<Long>, ArrayList<Long>> txList = this.tableLocks.get(table);
        if (txList == null) {
            ArrayList<Long> tmpS = new ArrayList<Long>();
            ArrayList<Long> tmpX = new ArrayList<Long>();
            Pair<ArrayList<Long>, ArrayList<Long>> tmpPair = new Pair<ArrayList<Long>, ArrayList<Long>>(tmpS, tmpX);
            txList = tmpPair;
            this.tableLocks.put(table, tmpPair);
        }

//        System.out.println("SLSS type" + lockType);

        if (lockType == NO_LOCK) {
            txList.getKey().add(tx_session);
            return true;
        } else if (lockType == LOCK_S) {
            if (!txList.getKey().contains(tx_session)) {
                txList.getKey().add(tx_session);
            }
            return true;
        } else {
            if (txList.getValue().contains(tx_session)) {
                return true;
            }
        }
        return false;
    }

    public boolean setLockXSingle(Long tx_session, String table) {
        this.cur_tx_session = tx_session;
        this.createTransaction(tx_session);

        int lockType = checkTableHasLock(table);
        Pair<ArrayList<Long>, ArrayList<Long>> txList = this.tableLocks.get(table);
        if (txList == null) {
            ArrayList<Long> tmpS = new ArrayList<Long>();
            ArrayList<Long> tmpX = new ArrayList<Long>();
            Pair<ArrayList<Long>, ArrayList<Long>> tmpPair = new Pair<ArrayList<Long>, ArrayList<Long>>(tmpS, tmpX);
            txList = tmpPair;
            this.tableLocks.put(table, tmpPair);
        }

        if (lockType == NO_LOCK) {
            txList.getValue().add(tx_session);
            return true;
        } else if (lockType == LOCK_S) {
            if (txList.getKey().contains(tx_session) && txList.getKey().size() == 1) {
                txList.getKey().remove(tx_session);
                txList.getValue().add(tx_session);
                return true;
            }
        } else {
            if (txList.getValue().contains(tx_session)) {
                return true;
            }
        }
        return false;
    }

    public void releaseTXLock(Long tx_session) {
        this.cur_tx_session = tx_session;

        for (HashMap.Entry<String, Pair<ArrayList<Long>, ArrayList<Long>>> entry : tableLocks.entrySet()) {
            Pair<ArrayList<Long>, ArrayList<Long>> tmpPair = entry.getValue();

            // 注意输出里的remove也会remove
            tmpPair.getKey().remove(tx_session);
            tmpPair.getValue().remove(tx_session);
//            System.out.println(entry.getKey());
//            System.out.println("s " + tmpPair.getKey().remove(tx_session));
//            System.out.println("x " + tmpPair.getValue().remove(tx_session));
        }
    }

    public void releaseLock(Transaction tx, String table) {
        // TODO: remove single record
    }

    public Transaction getTX() {
        return this.transactionMap.get(this.cur_tx_session);
    }

    public void initFlag(Long tx_session) {
        this.cur_tx_session = tx_session;
        this.Flags.put(tx_session, false);
    }

    public void changeFlag(Long tx_session) {
        this.cur_tx_session = tx_session;
        this.Flags.replace(tx_session, !this.Flags.get(tx_session));
    }

    public boolean getFlag(Long tx_session) {
        this.cur_tx_session = tx_session;
        if (this.Flags.get(tx_session) == null) {
//            System.out.println("init flags");
            this.initFlag(tx_session);
        }
        return this.Flags.get(tx_session);
    }

    public void setSession(Long tx_session) {
        this.cur_tx_session = tx_session;
    }
}
