package cn.edu.thssdb.table;

import cn.edu.thssdb.exception.*;
import cn.edu.thssdb.index.BPlusTree;
import cn.edu.thssdb.schema.Column;
import cn.edu.thssdb.schema.Entry;
import cn.edu.thssdb.schema.Row;
import cn.edu.thssdb.schema.Table;
import cn.edu.thssdb.type.ColumnType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

public class tableOperatorTest {
    private Table table;

    @Before
    public void setUp() {
        System.out.println("----------setUp----------");

        Column[] columns = new Column[2];
        columns[0] = new Column("ID", ColumnType.INT, true, true, 20);
        columns[1] = new Column("name", ColumnType.STRING, false, true, 100);
        this.table = new Table("database", "table", columns);
    }

    @Test
    public void testInsert() {
        System.out.println("----------testInsert----------");

        Row newRow = new Row();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(1, ColumnType.INT));
        entries.add(new Entry("Shion", ColumnType.STRING));
        newRow.appendEntries(entries);
        this.table.insert(newRow);

        newRow = new Row();
        entries = new ArrayList<Entry>();
        entries.add(new Entry(2, ColumnType.INT));
        entries.add(new Entry("Ayaka", ColumnType.STRING));
        newRow.appendEntries(entries);
        this.table.insert(newRow);

        // 重复主键
        Assert.assertThrows(DuplicateKeyException.class, () -> {
            Row errorRow = new Row();
            ArrayList<Entry> errorEntries = new ArrayList<Entry>();
            errorEntries.add(new Entry(1, ColumnType.INT));
            errorEntries.add(new Entry("Duplicate Primary Key", ColumnType.STRING));
            errorRow.appendEntries(errorEntries);
            this.table.insert(errorRow);
        });

        // 行的属性个数与列不匹配
        Assert.assertThrows(ColumnMismatchException.class, () -> {
            Row errorRow = new Row();
            ArrayList<Entry> errorEntries = new ArrayList<Entry>();
            errorEntries.add(new Entry(3, ColumnType.INT));
            errorRow.appendEntries(errorEntries);
            this.table.insert(errorRow);
        });

        // Entry的类型与列定义的不同
        Assert.assertThrows(TypeMismatchException.class, () -> {
            Row errorRow = new Row();
            ArrayList<Entry> errorEntries = new ArrayList<Entry>();
            errorEntries.add(new Entry(4, ColumnType.INT));
            errorEntries.add(new Entry(4396, ColumnType.INT));
            errorRow.appendEntries(errorEntries);
            this.table.insert(errorRow);
        });

        // 违反约束
        Assert.assertThrows(ConstraintViolatedException.class, () -> {
            Row errorRow = new Row();
            ArrayList<Entry> errorEntries = new ArrayList<Entry>();
            errorEntries.add(new Entry(5, ColumnType.INT));
            errorEntries.add(new Entry(null, ColumnType.STRING));
            errorRow.appendEntries(errorEntries);
            this.table.insert(errorRow);
        });
    }

    @Test
    public void testQuery() {
        System.out.println("----------testQuery----------");

        Row newRow_1 = new Row();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(1, ColumnType.INT));
        entries.add(new Entry("Shion", ColumnType.STRING));
        newRow_1.appendEntries(entries);
        this.table.insert(newRow_1);

        Row newRow_2 = new Row();
        entries = new ArrayList<Entry>();
        entries.add(new Entry(2, ColumnType.INT));
        entries.add(new Entry("Ayaka", ColumnType.STRING));
        newRow_2.appendEntries(entries);
        this.table.insert(newRow_2);


        Assert.assertEquals(newRow_1.toString(),
                this.table.findRowByPrimaryKey(newRow_1.getEntries().get(0)).toString());
        Assert.assertEquals(newRow_2.toString(),
                this.table.findRowByPrimaryKey(newRow_2.getEntries().get(0)).toString());
        Assert.assertThrows(KeyNotExistException.class, () -> {
            this.table.findRowByPrimaryKey(new Entry(3, ColumnType.INT));
        });
    }

    @Test
    public void testDelete() {
        System.out.println("----------testDelete----------");

        Row newRow = new Row();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(1, ColumnType.INT));
        entries.add(new Entry("Shion", ColumnType.STRING));
        newRow.appendEntries(entries);
        this.table.insert(newRow);

        newRow = new Row();
        entries = new ArrayList<Entry>();
        entries.add(new Entry(2, ColumnType.INT));
        entries.add(new Entry("Ayaka", ColumnType.STRING));
        newRow.appendEntries(entries);
        this.table.insert(newRow);

        // 验证该行确实被删除
        this.table.delete(new Entry(1, ColumnType.INT));
        Assert.assertThrows(KeyNotExistException.class, () -> {
            this.table.findRowByPrimaryKey(new Entry(1, ColumnType.INT));
        });

        // 不存在的行删除报错
        Assert.assertThrows(KeyNotExistException.class, () -> {
            this.table.delete(new Entry(3, ColumnType.INT));
        });

        // 验证该行确实被删除
        this.table.delete(new Entry(2, ColumnType.INT));
        Assert.assertThrows(KeyNotExistException.class, () -> {
            this.table.findRowByPrimaryKey(new Entry(2, ColumnType.INT));
        });
    }

    @Test
    public void testUpdate() {
        System.out.println("----------testUpdate----------");

        Row newRow = new Row();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(1, ColumnType.INT));
        entries.add(new Entry("Shion", ColumnType.STRING));
        newRow.appendEntries(entries);
        this.table.insert(newRow);

        newRow = new Row();
        entries = new ArrayList<Entry>();
        entries.add(new Entry(2, ColumnType.INT));
        entries.add(new Entry("Ayaka", ColumnType.STRING));
        newRow.appendEntries(entries);
        this.table.insert(newRow);

        Row updateRow = new Row();
        entries = new ArrayList<Entry>();
        entries.add(new Entry(1, ColumnType.INT));
        entries.add(new Entry("MurasakiShion", ColumnType.STRING));
        updateRow.appendEntries(entries);
        this.table.update(new Entry(1, ColumnType.INT), updateRow);
        Assert.assertEquals(updateRow.toString(),
                this.table.findRowByPrimaryKey(new Entry(1, ColumnType.INT)).toString());

        updateRow = new Row();
        entries = new ArrayList<Entry>();
        entries.add(new Entry(3, ColumnType.INT));
        entries.add(new Entry("MurasakiShion", ColumnType.STRING));
        updateRow.appendEntries(entries);
        this.table.update(new Entry(1, ColumnType.INT), updateRow);
        Assert.assertEquals(updateRow.toString(),
                this.table.findRowByPrimaryKey(new Entry(3, ColumnType.INT)).toString());

        // 想要修改的行不存在(出现在delete部分)
        Assert.assertThrows(KeyNotExistException.class, () -> {
            Row errorRow = new Row();
            ArrayList<Entry> errorEntries = new ArrayList<Entry>();
            errorEntries.add(new Entry(1, ColumnType.INT));
            errorEntries.add(new Entry("MurasakiShion", ColumnType.STRING));
            errorRow.appendEntries(errorEntries);
            this.table.update(new Entry(1, ColumnType.INT), errorRow);
        });

        // 修改后的行存在主键重复(出现在insert部分)
        Assert.assertThrows(DuplicateKeyException.class, () -> {
            Row errorRow = new Row();
            ArrayList<Entry> errorEntries = new ArrayList<Entry>();
            errorEntries.add(new Entry(2, ColumnType.INT));
            errorEntries.add(new Entry("MurasakiShion", ColumnType.STRING));
            errorRow.appendEntries(errorEntries);
            this.table.update(new Entry(3, ColumnType.INT), errorRow);
        });
        // 验证原来的行是否被意外删除.
        Row originRow = new Row();
        ArrayList<Entry> originEntries = new ArrayList<Entry>();
        originEntries.add(new Entry(3, ColumnType.INT));
        originEntries.add(new Entry("MurasakiShion", ColumnType.STRING));
        originRow.appendEntries(originEntries);
        Assert.assertEquals(originRow.toString(),
                this.table.findRowByPrimaryKey(new Entry(3, ColumnType.INT)).toString());


    }

    @Test
    public void testSerialize() {
        System.out.println("----------testSerialize----------");

        Row newRow_1 = new Row();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(1, ColumnType.INT));
        entries.add(new Entry("Shion", ColumnType.STRING));
        newRow_1.appendEntries(entries);
        this.table.insert(newRow_1);

        Row newRow_2 = new Row();
        entries = new ArrayList<Entry>();
        entries.add(new Entry(2, ColumnType.INT));
        entries.add(new Entry("Ayaka", ColumnType.STRING));
        newRow_2.appendEntries(entries);
        this.table.insert(newRow_2);

        try {
            this.table.serialize();
            this.table.deserialize();
            // 能够复原数据.
            Assert.assertEquals(newRow_1.toString(),
                    this.table.findRowByPrimaryKey(newRow_1.getEntries().get(0)).toString());
            Assert.assertEquals(newRow_2.toString(),
                    this.table.findRowByPrimaryKey(newRow_2.getEntries().get(0)).toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
