package cn.edu.thssdb.table;

import cn.edu.thssdb.exception.*;
import cn.edu.thssdb.index.BPlusTree;
import cn.edu.thssdb.schema.*;
import cn.edu.thssdb.type.ColumnType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class tableOperatorTest {
    private Table table;

    @Before
    public void setUp() {
        System.out.println("----------setUp----------");

        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column("ID", ColumnType.INT, true, true, 20));
        columns.add(new Column("name", ColumnType.STRING, false, true, 100));

        this.table = new Table("data/database/table", "table", columns);
    }

    @Test
    public void testInsert() throws SQLHandleException {
        System.out.println("----------testInsert----------");

        Row newRow = new Row();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(1));
        entries.add(new Entry("Shion"));
        newRow.appendEntries(entries);
        this.table.insert(newRow);

        newRow = new Row();
        entries = new ArrayList<Entry>();
        entries.add(new Entry(2));
        entries.add(new Entry("Ayaka"));
        newRow.appendEntries(entries);
        this.table.insert(newRow);

        // 重复主键
        Assert.assertThrows(DuplicateKeyException.class, () -> {
            Row errorRow = new Row();
            ArrayList<Entry> errorEntries = new ArrayList<Entry>();
            errorEntries.add(new Entry(1));
            errorEntries.add(new Entry("Duplicate Primary Key"));
            errorRow.appendEntries(errorEntries);
            this.table.insert(errorRow);
        });

        // 行的属性个数与列不匹配
        Assert.assertThrows(ColumnMismatchException.class, () -> {
            Row errorRow = new Row();
            ArrayList<Entry> errorEntries = new ArrayList<Entry>();
            errorEntries.add(new Entry(3));
            errorRow.appendEntries(errorEntries);
            this.table.insert(errorRow);
        });

        // Entry的类型与列定义的不同
        Assert.assertThrows(TypeMismatchException.class, () -> {
            Row errorRow = new Row();
            ArrayList<Entry> errorEntries = new ArrayList<Entry>();
            errorEntries.add(new Entry(4));
            errorEntries.add(new Entry(4396));
            errorRow.appendEntries(errorEntries);
            this.table.insert(errorRow);
        });

        // 违反约束
        Assert.assertThrows(ConstraintViolatedException.class, () -> {
            Row errorRow = new Row();
            ArrayList<Entry> errorEntries = new ArrayList<Entry>();
            errorEntries.add(new Entry(5));
            errorEntries.add(new Entry(null));
            errorRow.appendEntries(errorEntries);
            this.table.insert(errorRow);
        });
    }

    @Test
    public void testQuery() throws SQLHandleException {
        System.out.println("----------testQuery----------");

        Row newRow_1 = new Row();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(1));
        entries.add(new Entry("Shion"));
        newRow_1.appendEntries(entries);
        this.table.insert(newRow_1);

        Row newRow_2 = new Row();
        entries = new ArrayList<Entry>();
        entries.add(new Entry(2));
        entries.add(new Entry("Ayaka"));
        newRow_2.appendEntries(entries);
        this.table.insert(newRow_2);

        Assert.assertEquals(newRow_1.toString(),
                this.table.findRowByPrimaryKey(newRow_1.getMultiEntry(this.table.getPrimaryIndices())).toString());
        Assert.assertEquals(newRow_2.toString(),
                this.table.findRowByPrimaryKey(newRow_2.getMultiEntry(this.table.getPrimaryIndices())).toString());

        Assert.assertThrows(KeyNotExistException.class, () -> {
            this.table.findRowByPrimaryKey(new MultiEntry(new Entry(3)));
        });
    }

    @Test
    public void testDelete() throws SQLHandleException {
        System.out.println("----------testDelete----------");

        Row newRow = new Row();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(1));
        entries.add(new Entry("Shion"));
        newRow.appendEntries(entries);
        this.table.insert(newRow);

        newRow = new Row();
        entries = new ArrayList<Entry>();
        entries.add(new Entry(2));
        entries.add(new Entry("Ayaka"));
        newRow.appendEntries(entries);
        this.table.insert(newRow);

        // 验证该行确实被删除
        this.table.delete(new MultiEntry(new Entry(1)));
        Assert.assertThrows(KeyNotExistException.class, () -> {
            this.table.findRowByPrimaryKey(new MultiEntry(new Entry(1)));
        });

        // 不存在的行删除报错
        Assert.assertThrows(KeyNotExistException.class, () -> {
            this.table.delete(new MultiEntry(new Entry(3)));
        });

        // 验证该行确实被删除
        this.table.delete(new MultiEntry(new Entry(2)));
        Assert.assertThrows(KeyNotExistException.class, () -> {
            this.table.findRowByPrimaryKey(new MultiEntry(new Entry(2)));
        });
    }

    @Test
    public void testUpdate() throws SQLHandleException {
        System.out.println("----------testUpdate----------");

        Row newRow = new Row();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(1));
        entries.add(new Entry("Shion"));
        newRow.appendEntries(entries);
        this.table.insert(newRow);

        newRow = new Row();
        entries = new ArrayList<Entry>();
        entries.add(new Entry(2));
        entries.add(new Entry("Ayaka"));
        newRow.appendEntries(entries);
        this.table.insert(newRow);

        Row updateRow = new Row();
        entries = new ArrayList<Entry>();
        entries.add(new Entry(1));
        entries.add(new Entry("MurasakiShion"));
        updateRow.appendEntries(entries);
        this.table.update(new MultiEntry(new Entry(1)), updateRow);
        Assert.assertEquals(updateRow.toString(),
                this.table.findRowByPrimaryKey(new MultiEntry(new Entry(1))).toString());

        updateRow = new Row();
        entries = new ArrayList<Entry>();
        entries.add(new Entry(3));
        entries.add(new Entry("MurasakiShion"));
        updateRow.appendEntries(entries);
        this.table.update(new MultiEntry(new Entry(1)), updateRow);
        Assert.assertEquals(updateRow.toString(),
                this.table.findRowByPrimaryKey(new MultiEntry(new Entry(3))).toString());

        // 想要修改的行不存在(出现在delete部分)
        Assert.assertThrows(KeyNotExistException.class, () -> {
            Row errorRow = new Row();
            ArrayList<Entry> errorEntries = new ArrayList<Entry>();
            errorEntries.add(new Entry(1));
            errorEntries.add(new Entry("MurasakiShion"));
            errorRow.appendEntries(errorEntries);
            this.table.update(new MultiEntry(new Entry(1)), errorRow);
        });

        // 修改后的行存在主键重复(出现在insert部分)
        Assert.assertThrows(DuplicateKeyException.class, () -> {
            Row errorRow = new Row();
            ArrayList<Entry> errorEntries = new ArrayList<Entry>();
            errorEntries.add(new Entry(2));
            errorEntries.add(new Entry("MurasakiShion"));
            errorRow.appendEntries(errorEntries);
            this.table.update(new MultiEntry(new Entry(3)), errorRow);
        });

        // 验证原来的行是否被意外删除.
        Row originRow = new Row();
        ArrayList<Entry> originEntries = new ArrayList<Entry>();
        originEntries.add(new Entry(3));
        originEntries.add(new Entry("MurasakiShion"));
        originRow.appendEntries(originEntries);
        Assert.assertEquals(originRow.toString(),
                this.table.findRowByPrimaryKey(new MultiEntry(new Entry(3))).toString());


    }

    @Test
    public void testSerialize() throws SQLHandleException {
        System.out.println("----------testSerialize----------");

        Row newRow_1 = new Row();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(1));
        entries.add(new Entry("Shion"));
        newRow_1.appendEntries(entries);
        this.table.insert(newRow_1);

        Row newRow_2 = new Row();
        entries = new ArrayList<Entry>();
        entries.add(new Entry(2));
        entries.add(new Entry("Ayaka"));
        newRow_2.appendEntries(entries);
        this.table.insert(newRow_2);

        try {
            this.table.serialize();
            this.table.deserialize();
            // 能够复原数据.
            Assert.assertEquals(newRow_1.toString(),
                    this.table.findRowByPrimaryKey(newRow_1.getMultiEntry(this.table.getPrimaryIndices())).toString());
            Assert.assertEquals(newRow_2.toString(),
                    this.table.findRowByPrimaryKey(newRow_2.getMultiEntry(this.table.getPrimaryIndices())).toString());
            
        } catch (SQLHandleException e) {
            e.printStackTrace();
        }
    }
}
