package cn.edu.thssdb.schema;

import cn.edu.thssdb.exception.*;
import cn.edu.thssdb.index.BPlusTree;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.utils.Transaction;
import javafx.util.Pair;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Table implements Iterable<Row> {
    ReentrantReadWriteLock lock;
    private String root;
    private String tableName;
    private ArrayList<Column> columns;

    private BPlusTree<MultiEntry, Row> index;
    private HashMap<String, Integer> columnIndicesMap;  //根据列名定位索引(下标)
    private ArrayList<Integer> primaryIndices;   //主键索引(下标)

    /**
     * 实例化数据表
     * 为primary key建立索引
     *
     * @param tableRoot 数据表存放位置
     * @param columns   列属性
     * @param tableName 数据表名称
     */
    public Table(String tableRoot, String tableName, ArrayList<Column> columns) {
        // TODO
        // 设置该数据表的表名称, 数据库名称, 以及列属性.
        this.tableName = tableName;
        this.root = tableRoot;
        this.columns = columns;

        // 设定主键的位置, 并检查列名是否有重复.
        int attrSize = this.columns.size();
        this.primaryIndices = new ArrayList<Integer>();
        this.columnIndicesMap = new HashMap<>();

        boolean hasPrimary = false;
        for (int i = 0; i < attrSize; i++) {
            if (this.columnIndicesMap.containsKey(this.columns.get(i).getColumnFullName().name)) {
                //重名列的表非法.
                throw new SQLHandleException("Exception: duplicate column name.");
            }
            this.columnIndicesMap.put(this.columns.get(i).getColumnFullName().name, i);
            if (this.columns.get(i).isPrimary()) {
                hasPrimary = true;
                this.primaryIndices.add(i);
            }
        }
        if (!hasPrimary && columns.size() != 0) {
            // 不含主键的表非法.
            throw new SQLHandleException("Exception: no primary key.");
        }

        // 更新列名前缀
        this.updatePrefix();

        // 建立主键索引树.
        this.index = new BPlusTree<MultiEntry, Row>();

        //
        this.lock = new ReentrantReadWriteLock();
    }

    public Table(String databaseRoot, String tableName) {
        this(databaseRoot, tableName, new ArrayList<Column>());
    }

    /**
     * 获取该表的列属性复制.
     * @param withPrefix
     * @return
     */
    public ArrayList<Column> getCopiedColumns(boolean withPrefix) {
        ArrayList<Column> copy = new ArrayList<>();
        for (Column col: this.columns) {
            copy.add(col.getCopiedColumn(withPrefix));
        }
        if (withPrefix) {
            for (Column col: copy) {
                col.setPrefix(this.tableName);
            }
        }
        return copy;
    }

    /**
     * 设置每列名字的前缀, 并更新列索引哈希表.
     */
    public void updatePrefix() {
        //设置前缀
        for (Column col: this.columns) {
            col.setPrefix(this.tableName);
        }
    }

    /**
     * 清空每列名字的前缀, 并更新列索引哈希表.
     */
    public void clearPrefix() {
        //设置前缀为null
        for (Column col: this.columns) {
            col.setPrefix(null);
        }
    }


    public synchronized void recover(){
        this.deserialize();
    }

    public synchronized QueryTable getQueryTable(boolean withPrefix) {
        ArrayList<Column> queryColumns = this.getCopiedColumns(withPrefix);
        QueryTable queryTable = new QueryTable(this.tableName, queryColumns);

        // TODO: 复制数据到queryTable中.
        Iterator<Pair<MultiEntry, Row>> dataIterator = this.index.iterator();
        while (dataIterator.hasNext()) {
            queryTable.rows.add(dataIterator.next().getValue());
        }
        return queryTable;
    }

    /**
     * 根据主键寻找对应的行
     *
     * @param primaryKey 主键
     * @return 主键对应的行
     * @throws RuntimeException
     */
    public Row findRowByPrimaryKey(MultiEntry primaryKey) throws RuntimeException {
        if (!this.index.contains(primaryKey)) {
            // 主键不存在, 返回KeyNotExistException.
            throw new KeyNotExistException();
        } else {
            return this.index.get(primaryKey);
        }
    }

    public Row search(MultiEntry primaryKey) {
        if (!this.index.contains(primaryKey)) {
            return null;
        } else {
            return this.index.get(primaryKey);
        }
    }
    /**
     * 往该数据表插入一条新行.
     *
     * @param newRow 要插入的新行
     * @throws RuntimeException
     */
    public void insert(Row newRow, Transaction tx) throws SQLHandleException {
        // TODO

        if (newRow.getEntries().size() != this.columns.size()) {
            // 要插入的行不满足该表的列数量约束
            throw new ColumnMismatchException();
        } else {
            int columnCount = this.columns.size();
            for (int i = 0; i < columnCount; i++) {
                // 新插入的行某元素不属于该列定义的类型.
                if (!this.columns.get(i).isCompatible(newRow.getEntries().get(i))) {
                    throw new TypeMismatchException(this.columns.get(i).getType().toString());
                } else {
                    this.columns.get(i).transCompatible(newRow.getEntries().get(i));
                }
                // 新插入的行某元素违反了某些约束(如notNull)
                if (this.columns.get(i).isNotNull() && newRow.getEntries().get(i).value == null) {
                    throw new ConstraintViolatedException("NotNull");
                }
            }
            MultiEntry primaryKey = newRow.getMultiEntry(primaryIndices);
            if (this.index.contains(primaryKey)) {
                // 主键已经存在, 无法插入有重复主键的行, 抛出DuplicateKeyException.
                throw new DuplicateKeyException();
            }
            // 可以正确插入新行

            this.index.put(primaryKey, newRow);
            if (tx != null) {
                tx.addLog(newRow, null, this);
            }
        }
    }
    public void updateByIndicatingRow(Row oldRow, Row newRow, Transaction tx) throws RuntimeException {
        if (oldRow == null && newRow == null) {
            throw new SQLHandleException("Exception: illegal rollback.");
        } else if (oldRow == null) {
            this.insert(newRow, tx);
        } else if (newRow == null) {
            this.delete(oldRow.getMultiEntry(this.primaryIndices), tx);
        } else {
            this.update(oldRow.getMultiEntry(this.primaryIndices), newRow, tx);
        }

    }
    /**
     * 根据主键删除某行
     *
     * @param deleteKey 主键
     * @throws RuntimeException
     */
    public void delete(MultiEntry deleteKey, Transaction tx) throws RuntimeException {
        // TODO
        if (!this.index.contains(deleteKey)) {
            // 主键不存在, 返回KeyNotExistException.
            throw new KeyNotExistException();
        } else {
            Row oldRow = this.findRowByPrimaryKey(deleteKey);
            this.index.remove(deleteKey);
            if (tx != null) {
                tx.addLog(null, oldRow, this);
            }
        }
    }

    /**
     * 根据主键和数据更新数据表中的某一行
     *
     * @param updateKey 主键
     * @param newRow    新行
     * @throws RuntimeException
     */
    public void update(MultiEntry updateKey, Row newRow, Transaction tx){
        // TODO
        // 由于可能涉及到主键的更新, 所以这里我们选择删掉该主键然后再插入新的行
        Row locatedRow;
        try {
            locatedRow = this.findRowByPrimaryKey(updateKey);
            this.delete(updateKey, null);
        } catch (KeyNotExistException e) {
            throw e;
        }
        try {
            this.insert(newRow, null);
            if (tx != null) {
                tx.addLog(newRow, locatedRow, this);
            }
        } catch (RuntimeException e) {
            this.insert(locatedRow, null);
            throw e;
        }
    }

    /**
     * 序列化数据表, 存储到文件中.
     */
    public void serialize() {
        // TODO
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(this.root));
            Iterator rowIterator = this.iterator();
            objectOutputStream.writeObject(this.columns);

            // 写入数据行的个数以及所有数据行到文件中.
            objectOutputStream.writeObject(this.index.size());

            while (rowIterator.hasNext()) {
                objectOutputStream.writeObject(rowIterator.next());
            }
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLHandleException("Exception: Save table " + this.tableName + " failed.");
        }
    }

    public synchronized void deserialize(){
        // TODO
        try {
            this.index = new BPlusTree<MultiEntry, Row>();
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(this.root));
            this.columns = (ArrayList<Column>) objectInputStream.readObject();
            boolean hasPrimary = false;
            for (int i = 0; i < this.columns.size(); i++) {
                //重新更新主键下标和列名哈希表
                if (this.columns.get(i).isPrimary()) {
                    hasPrimary = true;
                    this.primaryIndices.add(i);
                }
                this.columnIndicesMap.put(this.columns.get(i).getColumnFullName().name, i);
            }
            if (!hasPrimary) {
                throw new SQLHandleException("Exception: no primary key.");
            }

            int rowSize = (int) objectInputStream.readObject();

            for (int i = 0; i < rowSize; i++) {
                Row loadRow = (Row) objectInputStream.readObject();
                this.index.put(loadRow.getMultiEntry(primaryIndices), loadRow);

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLHandleException("Exception: Load table " + this.tableName + " failed.");
        }
    }

    public synchronized String getRoot() {
        return this.root;
    }

    public synchronized String getTableName() {
        return this.tableName;
    }

    public HashMap<String, Integer> getColumnIndicesMap() {
        return this.columnIndicesMap;
    }
    public ArrayList<Integer> getPrimaryIndices() {
        return this.primaryIndices;
    }

    private class TableIterator implements Iterator<Row> {
        private Iterator<Pair<MultiEntry, Row>> iterator;

        TableIterator(Table table) {
            this.iterator = table.index.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Row next() {
            return iterator.next().getValue();
        }
    }

    @Override
    public Iterator<Row> iterator() {
        return new TableIterator(this);
    }
}
