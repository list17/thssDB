package cn.edu.thssdb.query;

import cn.edu.thssdb.exception.ExpressionHandleException;
import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.expression.*;
import cn.edu.thssdb.rpc.thrift.ConnectResp;
import cn.edu.thssdb.rpc.thrift.Status;
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

    public Manager manager = new Manager();
    public Database database;
    public Table vtuberTable;
    public Table groupTable;

    @Before
    public void setUp() {
        System.out.println("----------setUp----------");
        manager.deleteAllDatabase();
        manager.createDatabaseIfNotExists("TestDatabase");
        ConnectResp connectResp = new ConnectResp();
        Status status = new Status();
        status.currentDatabase = "TestDatabase";
        connectResp.status = status;
        connectResp.sessionId = 1;
        manager.addConnection(connectResp.sessionId, connectResp);
        database = manager.getSessionCurrentDatabase(connectResp.sessionId);
        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column("ID", ColumnType.INT, true, true, 20));
        columns.add(new Column("Name", ColumnType.STRING, false, true, 20));
        columns.add(new Column("Group", ColumnType.STRING, false, true, 20));
        this.vtuberTable = database.createTable("Vtuber", columns);

        ArrayList<Column> columns2 = new ArrayList<>();
        columns2.add(new Column("Group", ColumnType.STRING, true, true, 20));
        columns2.add(new Column("Enterprise", ColumnType.STRING, false, false, 20));
        this.groupTable = database.createTable("Group", columns2);

        Row row = new Row();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(0));
        entries.add(Entry.generateEntry("Natsuiro Matsuri"));
        entries.add(Entry.generateEntry("Hololive 1st"));
        row.appendEntries(entries);
        this.vtuberTable.insert(row);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(1));
        entries.add(Entry.generateEntry("MurasakiShion"));
        entries.add(Entry.generateEntry("Hololive 2nd"));
        row.appendEntries(entries);
        this.vtuberTable.insert(row);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(2));
        entries.add(Entry.generateEntry("MinatoAqua"));
        entries.add(Entry.generateEntry("Hololive 2nd"));
        row.appendEntries(entries);
        this.vtuberTable.insert(row);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(3));
        entries.add(Entry.generateEntry("NakiriAyame"));
        entries.add(Entry.generateEntry("Hololive 2nd"));
        row.appendEntries(entries);
        this.vtuberTable.insert(row);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(4));
        entries.add(Entry.generateEntry("MononobeAlice"));
        entries.add(Entry.generateEntry("Nijisanji 2nd"));
        row.appendEntries(entries);
        this.vtuberTable.insert(row);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(5));
        entries.add(Entry.generateEntry("SasakiSaki"));
        entries.add(Entry.generateEntry("Nijisanji Gamer"));
        row.appendEntries(entries);
        this.vtuberTable.insert(row);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(6));
        entries.add(Entry.generateEntry("HonmaHimawari"));
        entries.add(Entry.generateEntry("Nijisanji Gamer"));
        row.appendEntries(entries);
        this.vtuberTable.insert(row);

        // groupTable

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry("Hololive 1st"));
        entries.add(Entry.generateEntry("Hololive"));
        row.appendEntries(entries);
        this.groupTable.insert(row);


        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry("Hololive 2nd"));
        entries.add(Entry.generateEntry("Hololive"));
        row.appendEntries(entries);
        this.groupTable.insert(row);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry("Hololive 4th"));
        entries.add(Entry.generateEntry("Hololive"));
        row.appendEntries(entries);
        this.groupTable.insert(row);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry("Nijisanji 2nd"));
        entries.add(Entry.generateEntry("Nijisanji"));
        row.appendEntries(entries);
        this.groupTable.insert(row);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry("Nijisanji Gamer"));
        entries.add(Entry.generateEntry("Nijisanji"));
        row.appendEntries(entries);
        this.groupTable.insert(row);

    }

    @Test
    public void testSelect() throws SQLHandleException, ExpressionHandleException {
        System.out.println("----------testSelect----------");
        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM Vtuber\n" +
                "----------");
        ArrayList<Column.FullName> selectedColumns = new ArrayList<>();
        SourceTable sourceTable = new SourceTable("Vtuber"
                , new ArrayList<SourceTable.JoinOperator>());

        Expression expression = new UnaryExpression(true);

        Statement selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        QueryTable resultTable = selectStatement.execute(this.manager, this.database);
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM Vtuber\n" +
                "WHERE ID == 1\n" +
                "----------");
        selectedColumns = new ArrayList<>();
        sourceTable = new SourceTable("Vtuber"
                , new ArrayList<SourceTable.JoinOperator>());

        expression = new CompareExpression(new ColumnVariable(new Column.FullName("ID"))
                , CompareExpression.Operator.EQ
                , new ConstantVariable(1));

        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, this.database);
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM Vtuber\n" +
                "WHERE ID >= 1\n" +
                "----------");
        selectedColumns = new ArrayList<>();
        sourceTable = new SourceTable("Vtuber"
                , new ArrayList<SourceTable.JoinOperator>());

        expression = new CompareExpression(new ColumnVariable(new Column.FullName("ID"))
                , CompareExpression.Operator.GEQ
                , new ConstantVariable(1));

        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, this.database);
        resultTable.display();


        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM Vtuber\n" +
                "WHERE ID > 1\n" +
                "----------");
        selectedColumns = new ArrayList<>();
        sourceTable = new SourceTable("Vtuber"
                , new ArrayList<SourceTable.JoinOperator>());

        expression = new CompareExpression(new ColumnVariable(new Column.FullName("ID"))
                , CompareExpression.Operator.GE
                , new ConstantVariable(1));

        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, this.database);
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM Vtuber\n" +
                "WHERE ID != 1\n" +
                "----------");
        selectedColumns = new ArrayList<>();
        sourceTable = new SourceTable("Vtuber"
                , new ArrayList<SourceTable.JoinOperator>());

        expression = new CompareExpression(new ColumnVariable(new Column.FullName("ID"))
                , CompareExpression.Operator.NEQ
                , new ConstantVariable(1));

        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, this.database);
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT Name, Group \n" +
                "FROM Vtuber\n" +
                "----------");
        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("Name"));
        selectedColumns.add(new Column.FullName("Group"));
        sourceTable = new SourceTable("Vtuber"
                , new ArrayList<SourceTable.JoinOperator>());

        expression = new UnaryExpression(true);

        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, this.database);
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM Vtuber JOIN Group ON Vtuber.Group == Group.Group\n" +
                "----------");
        ArrayList<SourceTable.JoinOperator> joinOps = new ArrayList<>();
        joinOps.add(new SourceTable.JoinOperator("Group"
                , new CompareExpression(new ColumnVariable(new Column.FullName("Vtuber", "Group"))
                                        , CompareExpression.Operator.EQ
                                        , new ColumnVariable(new Column.FullName("Group", "Group")))));
        sourceTable = new SourceTable("Vtuber"
                , joinOps);

        selectedColumns = new ArrayList<>();
        expression = new UnaryExpression(true);


        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, this.database);
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT Name, Vtuber.Group, Enterprise \n" +
                "FROM Vtuber JOIN Group ON Vtuber.Group == Group.Group\n" +
                "----------");
        joinOps = new ArrayList<>();
        joinOps.add(new SourceTable.JoinOperator("Group"
                , new CompareExpression(new ColumnVariable(new Column.FullName("Vtuber", "Group"))
                , CompareExpression.Operator.EQ
                , new ColumnVariable(new Column.FullName("Group", "Group")))));
        sourceTable = new SourceTable("Vtuber"
                , joinOps);

        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("Name"));
        selectedColumns.add(new Column.FullName("Vtuber", "Group"));
        selectedColumns.add(new Column.FullName("Enterprise"));
        expression = new UnaryExpression(true);


        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, this.database);
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT Name, Vtuber.Group, Enterprise \n" +
                "FROM Vtuber JOIN Group ON Vtuber.Group == Group.Group\n" +
                "WHERE Enterprise == \"Hololive\"\n" +
                "----------");
        joinOps = new ArrayList<>();
        joinOps.add(new SourceTable.JoinOperator("Group"
                , new CompareExpression(new ColumnVariable(new Column.FullName("Vtuber", "Group"))
                , CompareExpression.Operator.EQ
                , new ColumnVariable(new Column.FullName("Group", "Group")))));
        sourceTable = new SourceTable("Vtuber"
                , joinOps);

        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("Name"));
        selectedColumns.add(new Column.FullName("Vtuber", "Group"));
        selectedColumns.add(new Column.FullName("Enterprise"));
        expression = new CompareExpression(new ColumnVariable(new Column.FullName("Enterprise")),
                                            CompareExpression.Operator.EQ,
                                            new ConstantVariable("Hololive"));


        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, this.database);
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT Name, Vtuber.Group, Enterprise \n" +
                "FROM Vtuber JOIN Group ON Vtuber.Group == Group.Group\n" +
                "WHERE Enterprise == \"Hololive\" AND Vtuber.Group == \"Hololive 1st\"\n" +
                "----------");
        joinOps = new ArrayList<>();
        joinOps.add(new SourceTable.JoinOperator("Group"
                , new CompareExpression(new ColumnVariable(new Column.FullName("Vtuber", "Group"))
                , CompareExpression.Operator.EQ
                , new ColumnVariable(new Column.FullName("Group", "Group")))));
        sourceTable = new SourceTable("Vtuber"
                , joinOps);

        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("Name"));
        selectedColumns.add(new Column.FullName("Vtuber", "Group"));
        selectedColumns.add(new Column.FullName("Enterprise"));
        expression = new CompareExpression(new ColumnVariable(new Column.FullName("Enterprise")),
                CompareExpression.Operator.EQ,
                new ConstantVariable("Hololive"));
        expression = new LogicalExpression(expression,
                                            LogicalExpression.Operator.AND,
                                            new CompareExpression(new ColumnVariable(new Column.FullName("Vtuber.Group")),
                                                                    CompareExpression.Operator.EQ,
                                                                    new ConstantVariable("Hololive 1st")));


        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, this.database);
        resultTable.display();

    }
}
