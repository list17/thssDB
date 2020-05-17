package cn.edu.thssdb.query;

import cn.edu.thssdb.exception.ExpressionHandleException;
import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.expression.*;
import cn.edu.thssdb.schema.*;
import cn.edu.thssdb.statement.SelectStatement;
import cn.edu.thssdb.statement.Statement;
import cn.edu.thssdb.type.ColumnType;
import com.sun.corba.se.impl.orbutil.closure.Constant;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

public class statementExecuteTest {

    public Database database = new Database("TestDatabase", "", false);
    public Table table_Hololive;
    public Table table_NIJISANJI;

    @Before
    public void setUp() {
        System.out.println("----------setUp----------");
        Column[] columns = new Column[]{
                new Column("ID", ColumnType.INT, true, true, 20),
                new Column("Name", ColumnType.STRING, false, true, 20),
                new Column("Group", ColumnType.STRING, false, true, 20)
        };
        this.table_Hololive = database.createTable("Hololive", columns);
        columns = new Column[]{
                new Column("ID", ColumnType.INT, true, true, 20),
                new Column("Name", ColumnType.STRING, false, true, 20),
                new Column("Group", ColumnType.STRING, false, true, 20)
        };
        this.table_NIJISANJI = database.createTable("NIJISANJI", columns);

        Row row = new Row();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(1));
        entries.add(Entry.generateEntry("MurasakiShion"));
        entries.add(Entry.generateEntry("Season 2"));
        row.appendEntries(entries);
        this.table_Hololive.insert(row);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(2));
        entries.add(Entry.generateEntry("MinatoAqua"));
        entries.add(Entry.generateEntry("Season 2"));
        row.appendEntries(entries);
        this.table_Hololive.insert(row);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(3));
        entries.add(Entry.generateEntry("NakiriAyame"));
        entries.add(Entry.generateEntry("Season 2"));
        row.appendEntries(entries);
        this.table_Hololive.insert(row);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(1));
        entries.add(Entry.generateEntry("MononobeAlice"));
        entries.add(Entry.generateEntry("Season 2"));
        row.appendEntries(entries);
        this.table_NIJISANJI.insert(row);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(2));
        entries.add(Entry.generateEntry("SasakiSaki"));
        entries.add(Entry.generateEntry("Gamers"));
        row.appendEntries(entries);
        this.table_NIJISANJI.insert(row);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(3));
        entries.add(Entry.generateEntry("HonmaHimawari"));
        entries.add(Entry.generateEntry("Gamers"));
        row.appendEntries(entries);
        this.table_NIJISANJI.insert(row);
    }

    @Test
    public void testSelect() throws SQLHandleException, ExpressionHandleException {
        System.out.println("----------testSelect----------");
        ArrayList<Column.FullName> selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("ID"));
        SourceTable sourceTable = new SourceTable("Hololive"
                , new ArrayList<SourceTable.JoinOperator>());

        Expression expression = new CompareExpression(new ColumnVariable(new Column.FullName("ID"))
                , CompareExpression.Operator.EQ
                , new ConstantVariable(1));

        Statement selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        QueryTable resultTable = selectStatement.execute(this.database);

        System.out.println("---search ID == 1---");
        for (Row r: resultTable.rows) {
            System.out.println(r.toString());
        }

        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("ID"));
        sourceTable = new SourceTable("Hololive"
                , new ArrayList<SourceTable.JoinOperator>());

        expression = new CompareExpression(new ColumnVariable(new Column.FullName("ID"))
                , CompareExpression.Operator.GEQ
                , new ConstantVariable(2));

        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.database);

        System.out.println("---search ID >= 2---");
        for (Row r: resultTable.rows) {
            System.out.println(r.toString());
        }

        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("ID"));
        sourceTable = new SourceTable("Hololive"
                , new ArrayList<SourceTable.JoinOperator>());

        expression = new CompareExpression(new ColumnVariable(new Column.FullName("ID"))
                , CompareExpression.Operator.GE
                , new ConstantVariable(2));

        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.database);

        System.out.println("---search ID > 2---");
        for (Row r: resultTable.rows) {
            System.out.println(r.toString());
        }

        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("ID"));
        sourceTable = new SourceTable("Hololive"
                , new ArrayList<SourceTable.JoinOperator>());

        expression = new CompareExpression(new ColumnVariable(new Column.FullName("ID"))
                , CompareExpression.Operator.NEQ
                , new ConstantVariable(2));

        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.database);

        System.out.println("---search ID != 2---");
        for (Row r: resultTable.rows) {
            System.out.println(r.toString());
        }

        ArrayList<SourceTable.JoinOperator> joinOps = new ArrayList<>();
        joinOps.add(new SourceTable.JoinOperator("NIJISANJI"
                , new CompareExpression(new ColumnVariable(new Column.FullName("NIJISANJI", "ID"))
                                        , CompareExpression.Operator.EQ
                                        , new ColumnVariable(new Column.FullName("Hololive", "ID")))));
        sourceTable = new SourceTable("Hololive"
                , joinOps);

        selectedColumns = new ArrayList<>();
        expression = new CompareExpression(new ColumnVariable(new Column.FullName("Hololive", "ID"))
                , CompareExpression.Operator.GEQ
                , new ConstantVariable(1));


        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.database);

        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM Hololive JOIN NIJISANJI ON Hololive.ID == NIJISANJI.ID\n" +
                "----------");
        for (Row r: resultTable.rows) {
            System.out.println(r.toString());
        }
    }
}
