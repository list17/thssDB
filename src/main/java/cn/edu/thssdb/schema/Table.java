package cn.edu.thssdb.schema;

import cn.edu.thssdb.exception.*;
import cn.edu.thssdb.index.BPlusTree;
import javafx.util.Pair;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Table implements Iterable<Row> {
  ReentrantReadWriteLock lock;
  private String databaseName;
  public String tableName;
  public ArrayList<Column> columns;
  public BPlusTree<Entry, Row> index;
  private int primaryIndex;

  /**
   * 实例化数据表
   * 为primary key建立索引
   * @param databaseName 数据库名称
   * @param columns 列属性
   * @param tableName 数据表名称
   */
  public Table(String databaseName, String tableName, Column[] columns) {
    // TODO
    // 设置该数据表的表名称, 数据库名称, 以及列属性.
    this.tableName = tableName;
    this.databaseName = databaseName;
    this.columns = new ArrayList<>(Arrays.asList(columns));

    // 设定主键的位置, 并初始化索引树
    int attrSize = this.columns.size();
    for (int i = 0; i < attrSize; i++) {
      if (this.columns.get(i).isPrimary()) {
        this.primaryIndex = i;
        break;
      }
    }
    this.index = new BPlusTree<Entry, Row>();

    //
    this.lock = new ReentrantReadWriteLock();
  }

  private void recover() {
    // TODO
  }

  /**
   * 根据主键寻找对应的行
   * @param primaryKey 主键
   * @return 主键对应的行
   * @throws RuntimeException
   */
  public Row findRowByPrimaryKey(Entry primaryKey) throws RuntimeException {
    if (!this.index.contains(primaryKey)) {
      // 主键不存在, 返回KeyNotExistException.
      throw new KeyNotExistException();
    } else {
      return this.index.get(primaryKey);
    }
  }

  /**
   * 往该数据表插入一条新行.
   * @param newRow 要插入的新行
   * @throws RuntimeException
   */
  public void insert(Row newRow) throws RuntimeException {
    // TODO
    Entry primaryKey = newRow.getEntries().get(this.primaryIndex);
    if (this.index.contains(primaryKey)) {
      // 主键已经存在, 无法插入有重复主键的行, 抛出DuplicateKeyException.
      throw new DuplicateKeyException();
    } else if (newRow.getEntries().size() != this.columns.size()) {
      // 要插入的行不满足该表的列约束
      throw new ColumnMismatchException();
    } else {
      int columnCount = this.columns.size();
      for (int i = 0; i < columnCount; i++) {
        // 新插入的行某元素不属于该列定义的类型.
        if (this.columns.get(i).getType() != newRow.getEntries().get(i).getType()) {
          throw new TypeMismatchException(this.columns.get(i).getType().toString());
        }
        // 新插入的行某元素违反了某些约束(如notNull)
        if (this.columns.get(i).isNotNull() && newRow.getEntries().get(i).value == null) {
          throw new ConstraintViolatedException("NotNull");
        }
      }
      // 可以正确插入新行
      this.index.put(primaryKey, newRow);
    }
  }

  /**
   * 根据主键删除某行
   * @param deleteKey 主键
   * @throws RuntimeException
   */
  public void delete(Entry deleteKey) throws RuntimeException {
    // TODO
    if (!this.index.contains(deleteKey)) {
      // 主键不存在, 返回KeyNotExistException.
      throw new KeyNotExistException();
    } else {
      this.index.remove(deleteKey);
    }
  }

  /**
   * 根据主键和数据更新数据表中的某一行
   * @param updateKey 主键
   * @param newRow 新行
   * @throws RuntimeException
   */
  public void update(Entry updateKey, Row newRow) throws RuntimeException {
    // TODO
    // 由于可能涉及到主键的更新, 所以这里我们选择删掉该主键然后再插入新的行
    this.delete(updateKey);
    this.insert(newRow);
  }

  /**
   * 序列化数据表, 存储到文件中.
   */
  public void serialize() throws IOException {
    // TODO
    String fileName = "data/" + this.databaseName + "/" + this.tableName;
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileName));
    Iterator rowIterator = this.iterator();

    // 写入数据行的个数以及所有数据行到文件中.
    objectOutputStream.writeObject(this.index.size());

    while (rowIterator.hasNext()) {
      objectOutputStream.writeObject(rowIterator.next());
    }
    objectOutputStream.close();
  }

  public ArrayList<Row> deserialize() throws IOException, ClassNotFoundException {
    // TODO
    this.index = new BPlusTree<Entry, Row>();
    String fileName = "data/" + this.databaseName + "/" + this.tableName;
    ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileName));
    int rowSize = (int) objectInputStream.readObject();

    for (int i = 0; i < rowSize; i++) {
      Row loadRow = (Row) objectInputStream.readObject();
      this.index.put(loadRow.getEntries().get(this.primaryIndex), loadRow);
    }
    return null;
  }

  private class TableIterator implements Iterator<Row> {
    private Iterator<Pair<Entry, Row>> iterator;

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
