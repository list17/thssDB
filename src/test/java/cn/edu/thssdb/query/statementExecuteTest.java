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
    public Table multiTable;
    public ConnectResp connectResp;

    @Before
    public void setUp() {
        System.out.println("----------setUp----------");
        manager.deleteAllDatabase();
        manager.createDatabaseIfNotExists("TestDatabase");
        connectResp = new ConnectResp();
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

        ArrayList<Column> columns3 = new ArrayList<>();
        columns3.add(new Column("ID_1", ColumnType.INT, true, true, 20));
        columns3.add(new Column("ID_2", ColumnType.INT, true, true, 20));
        this.multiTable = database.createTable("Multi", columns3);

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

        //
        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(1));
        entries.add(Entry.generateEntry(1));
        row.appendEntries(entries);
        this.multiTable.insert(row);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(1));
        entries.add(Entry.generateEntry(2));
        row.appendEntries(entries);
        this.multiTable.insert(row);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(1));
        entries.add(Entry.generateEntry(3));
        row.appendEntries(entries);
        this.multiTable.insert(row);

    }

    @Test
    public void testSelect() throws SQLHandleException, ExpressionHandleException {
        System.out.println("----------testSelect----------");
        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM Vtuber\n" +
                "----------");
        ArrayList<Column.FullName> selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("*"));
        SourceTable sourceTable = new SourceTable("Vtuber"
                , new ArrayList<SourceTable.JoinOperator>());

        Expression expression = new UnaryExpression(true);

        Statement selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        QueryTable resultTable = selectStatement.execute(this.manager, connectResp.sessionId);
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM Vtuber\n" +
                "WHERE ID == 1\n" +
                "----------");
        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("*"));
        sourceTable = new SourceTable("Vtuber"
                , new ArrayList<SourceTable.JoinOperator>());

        expression = new CompareExpression(new ColumnVariable(new Column.FullName("ID"))
                , CompareExpression.Operator.EQ
                , new ConstantVariable(1));

        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId);
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM Vtuber\n" +
                "WHERE ID >= 1\n" +
                "----------");
        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("*"));
        sourceTable = new SourceTable("Vtuber"
                , new ArrayList<SourceTable.JoinOperator>());

        expression = new CompareExpression(new ColumnVariable(new Column.FullName("ID"))
                , CompareExpression.Operator.GE
                , new ConstantVariable(1));

        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId);
        resultTable.display();


        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM Vtuber\n" +
                "WHERE ID > 1\n" +
                "----------");
        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("*"));
        sourceTable = new SourceTable("Vtuber"
                , new ArrayList<SourceTable.JoinOperator>());

        expression = new CompareExpression(new ColumnVariable(new Column.FullName("ID"))
                , CompareExpression.Operator.GE
                , new ConstantVariable(1));

        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId);
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM Vtuber\n" +
                "WHERE ID != 1\n" +
                "----------");
        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("*"));
        sourceTable = new SourceTable("Vtuber"
                , new ArrayList<SourceTable.JoinOperator>());

        expression = new CompareExpression(new ColumnVariable(new Column.FullName("ID"))
                , CompareExpression.Operator.NE
                , new ConstantVariable(1));

        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId);
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
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId);
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
        selectedColumns.add(new Column.FullName("*"));
        expression = new UnaryExpression(true);


        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId);
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
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId);
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
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId);
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
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId);
        resultTable.display();


        System.out.println("----------\n" +
                "SELECT Vtuber.* \n" +
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
        selectedColumns.add(new Column.FullName("Vtuber", "*"));
        expression = new UnaryExpression(true);


        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId);
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM Vtuber AS V JOIN Group AS G ON V.Group == G.Group\n" +
                "----------");
        joinOps = new ArrayList<>();
        joinOps.add(new SourceTable.JoinOperator("Group"
                , new CompareExpression(new ColumnVariable(new Column.FullName("V", "Group"))
                , CompareExpression.Operator.EQ
                , new ColumnVariable(new Column.FullName("G", "Group"))), "G"));
        sourceTable = new SourceTable("Vtuber"
                , joinOps, "V");

        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("*"));
        expression = new UnaryExpression(true);


        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId);
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM Vtuber, Group\n" +
                "----------");
        joinOps = new ArrayList<>();
        joinOps.add(new SourceTable.JoinOperator("Group"
                , new UnaryExpression(true)));
        sourceTable = new SourceTable("Vtuber"
                , joinOps);

        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("*"));
        expression = new UnaryExpression(true);


        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId);
        resultTable.display();

        try {
            System.out.println("----------\n" +
                    "SELECT * \n" +
                    "FROM Vtuber, Group\n" +
                    "WHERE ID == 9\n" +
                    "----------");
            joinOps = new ArrayList<>();
            joinOps.add(new SourceTable.JoinOperator("Group"
                    , new UnaryExpression(true)));
            sourceTable = new SourceTable("Vtuber"
                    , joinOps);

            selectedColumns = new ArrayList<>();
            selectedColumns.add(new Column.FullName("*"));
            expression = new CompareExpression(new ColumnVariable(new Column.FullName("ID")),
                                                CompareExpression.Operator.EQ,
                                                new ConstantVariable(9));

            selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
            resultTable = selectStatement.execute(this.manager, connectResp.sessionId);
            resultTable.display();
        } catch (SQLHandleException e) {
            System.out.println(e.getMessage());
        } catch (ExpressionHandleException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("----------\n" +
                    "SELECT * \n" +
                    "FROM Vtuber, Group\n" +
                    "WHERE Company == \"Hololive\"\n" +
                    "----------");
            joinOps = new ArrayList<>();
            joinOps.add(new SourceTable.JoinOperator("Group"
                    , new UnaryExpression(true)));
            sourceTable = new SourceTable("Vtuber"
                    , joinOps);

            selectedColumns = new ArrayList<>();
            selectedColumns.add(new Column.FullName("*"));
            expression = new CompareExpression(new ColumnVariable(new Column.FullName("Company")),
                                                CompareExpression.Operator.EQ,
                                                new ConstantVariable("Hololive"));

            selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
            resultTable = selectStatement.execute(this.manager, connectResp.sessionId);
            resultTable.display();
        } catch (SQLHandleException e) {
            System.out.println(e.getMessage());
        } catch (ExpressionHandleException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("----------\n" +
                    "SELECT * \n" +
                    "FROM Vtuber, Group\n" +
                    "WHERE Enterprise == 1\n" +
                    "----------");
            joinOps = new ArrayList<>();
            joinOps.add(new SourceTable.JoinOperator("Group"
                    , new UnaryExpression(true)));
            sourceTable = new SourceTable("Vtuber"
                    , joinOps);

            selectedColumns = new ArrayList<>();
            selectedColumns.add(new Column.FullName("*"));
            expression = new CompareExpression(new ColumnVariable(new Column.FullName("Enterprise")),
                    CompareExpression.Operator.EQ,
                    new ConstantVariable(1));

            selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
            resultTable = selectStatement.execute(this.manager, connectResp.sessionId);
            resultTable.display();
        } catch (SQLHandleException e) {
            System.out.println(e.getMessage());
        } catch (ExpressionHandleException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("----------\n" +
                    "SELECT ABC \n" +
                    "FROM Vtuber, Group\n" +
                    "----------");
            joinOps = new ArrayList<>();
            joinOps.add(new SourceTable.JoinOperator("Group"
                    , new UnaryExpression(true)));
            sourceTable = new SourceTable("Vtuber"
                    , joinOps);

            selectedColumns = new ArrayList<>();
            selectedColumns.add(new Column.FullName("ABC"));
            expression = new UnaryExpression(true);

            selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
            resultTable = selectStatement.execute(this.manager, connectResp.sessionId);
            resultTable.display();
        } catch (SQLHandleException e) {
            System.out.println(e.getMessage());
        } catch (ExpressionHandleException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("----------\n" +
                    "SELECT * \n" +
                    "FROM VUP\n" +
                    "----------");
            joinOps = new ArrayList<>();
            sourceTable = new SourceTable("VUP"
                    , joinOps);

            selectedColumns = new ArrayList<>();
            selectedColumns.add(new Column.FullName("*"));
            expression = new UnaryExpression(true);

            selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
            resultTable = selectStatement.execute(this.manager, connectResp.sessionId);
            resultTable.display();
        } catch (SQLHandleException e) {
            System.out.println(e.getMessage());
        } catch (ExpressionHandleException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("----------\n" +
                    "SELECT * \n" +
                    "FROM Vtuber, VUP\n" +
                    "----------");
            joinOps = new ArrayList<>();
            joinOps.add(new SourceTable.JoinOperator("VUP"
                    , new UnaryExpression(true)));
            sourceTable = new SourceTable("Vtuber"
                    , joinOps);

            selectedColumns = new ArrayList<>();
            selectedColumns.add(new Column.FullName("*"));
            expression = new UnaryExpression(true);

            selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
            resultTable = selectStatement.execute(this.manager, connectResp.sessionId);
            resultTable.display();
        } catch (SQLHandleException e) {
            System.out.println(e.getMessage());
        } catch (ExpressionHandleException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("----------\n" +
                    "SELECT Group \n" +
                    "FROM Vtuber, Group\n" +
                    "----------");
            joinOps = new ArrayList<>();
            joinOps.add(new SourceTable.JoinOperator("Group"
                    , new UnaryExpression(true)));
            sourceTable = new SourceTable("Vtuber"
                    , joinOps);

            selectedColumns = new ArrayList<>();
            selectedColumns.add(new Column.FullName("Group"));
            expression = new UnaryExpression(true);

            selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
            resultTable = selectStatement.execute(this.manager, connectResp.sessionId);
            resultTable.display();
        } catch (SQLHandleException e) {
            System.out.println(e.getMessage());
        } catch (ExpressionHandleException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM Multi\n" +
                "----------");
        joinOps = new ArrayList<>();
        sourceTable = new SourceTable("Multi"
                , joinOps);

        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("*"));
        expression = new CompareExpression(new ColumnVariable(new Column.FullName("ID_1")),
                CompareExpression.Operator.EQ,
                new ConstantVariable(1));
        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId);
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM Multi\n" +
                "----------");
        joinOps = new ArrayList<>();
        sourceTable = new SourceTable("Multi"
                , joinOps);

        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("*"));
        expression = new CompareExpression(new ColumnVariable(new Column.FullName("ID_1")),
                CompareExpression.Operator.EQ,
                new ConstantVariable(1));
        expression = new LogicalExpression(expression,
                LogicalExpression.Operator.AND,
                new CompareExpression(new ColumnVariable(new Column.FullName("ID_2")),
                        CompareExpression.Operator.EQ,
                        new ConstantVariable(2)));

        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId);
        resultTable.display();
    }
}
