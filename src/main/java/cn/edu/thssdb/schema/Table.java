package cn.edu.thssdb.schema;

import cn.edu.thssdb.index.BPlusTree;
import javafx.util.Pair;

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
    this.tableName = tableName;
    this.databaseName = databaseName;
    this.columns = new ArrayList<>(Arrays.asList(columns));

  }

  private void recover() {
    // TODO
  }

  public void insert(Row newRow) throws SQLException {
    // TODO
    this.lock.writeLock().lock();
    try {

    }
    finally {
      this.lock.writeLock().unlock();
    }

  }

  public void delete() {
    // TODO
  }

  public void update() {
    // TODO
  }

  private void serialize() {
    // TODO
  }

  private ArrayList<Row> deserialize() {
    // TODO
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
