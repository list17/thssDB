package cn.edu.thssdb.query;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.schema.Column;
import cn.edu.thssdb.schema.Row;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class QueryTable {

    public String queryTableName;
    public ArrayList<Column> columns;
    public LinkedList<Row> rows;
    public HashMap<String, Integer> columnIndicesMap;  //根据列名定位索引(下标)
    public HashMap<String, Integer> notConflictIndicesMap; //列名后缀不冲突的定位索引(下标)

    public QueryTable(String queryTableName, ArrayList<Column> columns) {
        // TODO
        this.queryTableName = queryTableName;
        this.columns = columns;
        this.rows = new LinkedList<>();
        this.columnIndicesMap = new HashMap<>();
        this.notConflictIndicesMap = new HashMap<>();

        int attrSize = columns.size();
        for (int i = 0; i < attrSize; i++) {
            if (columnIndicesMap.containsKey(this.columns.get(i).getName())) {
                //存在重名列.
                throw new SQLHandleException("Exception: duplicate column name.");
            }
            columnIndicesMap.put(this.columns.get(i).getName(), i);
            // 同时建立非冲突列名索引.
            if (notConflictIndicesMap.containsKey(this.columns.get(i).getColumnFullName().name)) {
                notConflictIndicesMap.remove(this.columns.get(i).getColumnFullName().name);
            } else {
                notConflictIndicesMap.put(this.columns.get(i).getColumnFullName().name, i);
            }
        }
    }

    public ArrayList<Column> getCopiedColumns() {
        ArrayList<Column> copy = new ArrayList<>();
        for (Column col: this.columns) {
            copy.add(col.getCopiedColumn(true));
        }
        // 此处不加QueryTable的前缀.
        return copy;
    }

    /**
     * 将QueryTable改为别名.
     * 注意不可以对合成的QueryTable(如Join操作或者Cartesian操作后得到的表)设置别名.
     * @param alias
     */
    public void setAlias(String alias) {
        this.queryTableName = alias;
        this.updatePrefix();
    }

    /**
     * 设置每列名字的前缀
     */
    public void updatePrefix() {
        //设置前缀
        for (Column col: this.columns) {
            col.setPrefix(this.queryTableName);
        }
        //清空Hash表, 重新建立映射
        this.columnIndicesMap.clear();
        int attrSize = this.columns.size();
        for (int i = 0; i < attrSize; i++) {
            columnIndicesMap.put(this.columns.get(i).getName(), i);
        }
    }

    /**
     * 清空每列名字的前缀
     */
    public void clearPrefix() {
        //设置前缀为null
        for (Column col: this.columns) {
            col.setPrefix(null);
        }
        //清空Hash表, 重新建立映射
        this.columnIndicesMap.clear();
        int attrSize = this.columns.size();
        for (int i = 0; i < attrSize; i++) {
            columnIndicesMap.put(this.columns.get(i).getName(), i);
        }
    }

    public void display() {
        String header = "";
        for (Column col: this.columns) {
            header += col.getName() + " ";
        }
        System.out.println(header);

        for (Row row: this.rows) {
            System.out.println(row.toString());
        }
    }

    public boolean hasNext() {
        // TODO
        return true;
    }

    public Row next() {
        // TODO
        return null;
    }

}