package cn.edu.thssdb.query;

import cn.edu.thssdb.exception.ExpressionHandleException;
import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.expression.*;
import cn.edu.thssdb.rpc.thrift.ConnectResp;
import cn.edu.thssdb.rpc.thrift.Status;
import cn.edu.thssdb.schema.*;
import cn.edu.thssdb.statement.*;
import cn.edu.thssdb.type.ColumnType;
import cn.edu.thssdb.utils.UserManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

//import com.sun.corba.se.impl.orbutil.closure.Constant;

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
        manager.createDatabaseIfNotExists("TESTDATABASE");
        connectResp = new ConnectResp();
        Status status = new Status();
        status.currentDatabase = "TESTDATABASE";
        connectResp.status = status;
        connectResp.sessionId = 1;
        manager.addConnection(connectResp.sessionId, connectResp);
        database = manager.getSessionCurrentDatabase(connectResp.sessionId);
        UserManager um = UserManager.getInstance();
        um.addSessionUser(connectResp.sessionId, "root");
        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column("ID", ColumnType.INT, true, true, 20));
        columns.add(new Column("Name", ColumnType.STRING, false, true, 20));
        columns.add(new Column("GROUP", ColumnType.STRING, false, true, 20));
        this.vtuberTable = database.createTable("VTUBER", columns);

        ArrayList<Column> columns2 = new ArrayList<>();
        columns2.add(new Column("GROUP", ColumnType.STRING, true, true, 20));
        columns2.add(new Column("Enterprise", ColumnType.STRING, false, false, 20));
        this.groupTable = database.createTable("GROUP", columns2);

        ArrayList<Column> columns3 = new ArrayList<>();
        columns3.add(new Column("ID_1", ColumnType.INT, true, true, 20));
        columns3.add(new Column("ID_2", ColumnType.INT, true, true, 20));
        this.multiTable = database.createTable("MULTI", columns3);

        Row row = new Row();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(0));
        entries.add(Entry.generateEntry("Natsuiro Matsuri"));
        entries.add(Entry.generateEntry("Hololive 1st"));
        row.appendEntries(entries);
        this.vtuberTable.insert(row, null);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(1));
        entries.add(Entry.generateEntry("MurasakiShion"));
        entries.add(Entry.generateEntry("Hololive 2nd"));
        row.appendEntries(entries);
        this.vtuberTable.insert(row, null);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(2));
        entries.add(Entry.generateEntry("MinatoAqua"));
        entries.add(Entry.generateEntry("Hololive 2nd"));
        row.appendEntries(entries);
        this.vtuberTable.insert(row, null);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(3));
        entries.add(Entry.generateEntry("NakiriAyame"));
        entries.add(Entry.generateEntry("Hololive 2nd"));
        row.appendEntries(entries);
        this.vtuberTable.insert(row, null);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(4));
        entries.add(Entry.generateEntry("MononobeAlice"));
        entries.add(Entry.generateEntry("Nijisanji 2nd"));
        row.appendEntries(entries);
        this.vtuberTable.insert(row, null);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(5));
        entries.add(Entry.generateEntry("SasakiSaki"));
        entries.add(Entry.generateEntry("Nijisanji Gamer"));
        row.appendEntries(entries);
        this.vtuberTable.insert(row, null);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(6));
        entries.add(Entry.generateEntry("HonmaHimawari"));
        entries.add(Entry.generateEntry("Nijisanji Gamer"));
        row.appendEntries(entries);
        this.vtuberTable.insert(row, null);

        // groupTable

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry("Hololive 1st"));
        entries.add(Entry.generateEntry("Hololive"));
        row.appendEntries(entries);
        this.groupTable.insert(row, null);


        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry("Hololive 2nd"));
        entries.add(Entry.generateEntry("Hololive"));
        row.appendEntries(entries);
        this.groupTable.insert(row, null);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry("Hololive 4th"));
        entries.add(Entry.generateEntry("Hololive"));
        row.appendEntries(entries);
        this.groupTable.insert(row, null);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry("Nijisanji 2nd"));
        entries.add(Entry.generateEntry("Nijisanji"));
        row.appendEntries(entries);
        this.groupTable.insert(row, null);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry("Nijisanji Gamer"));
        entries.add(Entry.generateEntry("Nijisanji"));
        row.appendEntries(entries);
        this.groupTable.insert(row, null);

        //
        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(1));
        entries.add(Entry.generateEntry(1));
        row.appendEntries(entries);
        this.multiTable.insert(row, null);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(1));
        entries.add(Entry.generateEntry(2));
        row.appendEntries(entries);
        this.multiTable.insert(row, null);

        row = new Row();
        entries = new ArrayList<Entry>();
        entries.add(Entry.generateEntry(1));
        entries.add(Entry.generateEntry(3));
        row.appendEntries(entries);
        this.multiTable.insert(row, null);

    }

    @Test
    public void testSelect() throws SQLHandleException, ExpressionHandleException {
        System.out.println("----------testSelect----------");
        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM VTUBER\n" +
                "----------");
        ArrayList<Column.FullName> selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("*"));
        SourceTable sourceTable = new SourceTable("VTUBER"
                , new ArrayList<SourceTable.JoinOperator>());

        Expression expression = new UnaryExpression(true);

        Statement selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        QueryTable resultTable = selectStatement.execute(this.manager, connectResp.sessionId, "test_command");
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM VTUBER\n" +
                "WHERE ID == 1\n" +
                "----------");
        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("*"));
        sourceTable = new SourceTable("VTUBER"
                , new ArrayList<SourceTable.JoinOperator>());

        expression = new CompareExpression(new ColumnVariable(new Column.FullName("ID"))
                , CompareExpression.Operator.EQ
                , new ConstantVariable(1));

        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId, "test_command");
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM VTUBER\n" +
                "WHERE ID >= 1\n" +
                "----------");
        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("*"));
        sourceTable = new SourceTable("VTUBER"
                , new ArrayList<SourceTable.JoinOperator>());

        expression = new CompareExpression(new ColumnVariable(new Column.FullName("ID"))
                , CompareExpression.Operator.GE
                , new ConstantVariable(1));

        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId, "test_command");
        resultTable.display();


        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM VTUBER\n" +
                "WHERE ID > 1\n" +
                "----------");
        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("*"));
        sourceTable = new SourceTable("VTUBER"
                , new ArrayList<SourceTable.JoinOperator>());

        expression = new CompareExpression(new ColumnVariable(new Column.FullName("ID"))
                , CompareExpression.Operator.GE
                , new ConstantVariable(1));

        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId, "test_command");
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM VTUBER\n" +
                "WHERE ID != 1\n" +
                "----------");
        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("*"));
        sourceTable = new SourceTable("VTUBER"
                , new ArrayList<SourceTable.JoinOperator>());

        expression = new CompareExpression(new ColumnVariable(new Column.FullName("ID"))
                , CompareExpression.Operator.NE
                , new ConstantVariable(1));

        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId, "test_command");
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT Name, GROUP \n" +
                "FROM VTUBER\n" +
                "----------");
        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("Name"));
        selectedColumns.add(new Column.FullName("GROUP"));
        sourceTable = new SourceTable("VTUBER"
                , new ArrayList<SourceTable.JoinOperator>());

        expression = new UnaryExpression(true);

        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId, "test_command");
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM VTUBER JOIN GROUP ON VTUBER.GROUP == GROUP.GROUP\n" +
                "----------");
        ArrayList<SourceTable.JoinOperator> joinOps = new ArrayList<>();
        joinOps.add(new SourceTable.JoinOperator("GROUP"
                , new CompareExpression(new ColumnVariable(new Column.FullName("VTUBER", "GROUP"))
                , CompareExpression.Operator.EQ
                , new ColumnVariable(new Column.FullName("GROUP", "GROUP")))));
        sourceTable = new SourceTable("VTUBER"
                , joinOps);

        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("*"));
        expression = new UnaryExpression(true);


        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId, "test_command");
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT Name, VTUBER.GROUP, Enterprise \n" +
                "FROM VTUBER JOIN GROUP ON VTUBER.GROUP == GROUP.GROUP\n" +
                "----------");
        joinOps = new ArrayList<>();
        joinOps.add(new SourceTable.JoinOperator("GROUP"
                , new CompareExpression(new ColumnVariable(new Column.FullName("VTUBER", "GROUP"))
                , CompareExpression.Operator.EQ
                , new ColumnVariable(new Column.FullName("GROUP", "GROUP")))));
        sourceTable = new SourceTable("VTUBER"
                , joinOps);

        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("Name"));
        selectedColumns.add(new Column.FullName("VTUBER", "GROUP"));
        selectedColumns.add(new Column.FullName("Enterprise"));
        expression = new UnaryExpression(true);


        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId, "test_command");
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT Name, VTUBER.GROUP, Enterprise \n" +
                "FROM VTUBER JOIN GROUP ON VTUBER.GROUP == GROUP.GROUP\n" +
                "WHERE Enterprise == \"Hololive\"\n" +
                "----------");
        joinOps = new ArrayList<>();
        joinOps.add(new SourceTable.JoinOperator("GROUP"
                , new CompareExpression(new ColumnVariable(new Column.FullName("VTUBER", "GROUP"))
                , CompareExpression.Operator.EQ
                , new ColumnVariable(new Column.FullName("GROUP", "GROUP")))));
        sourceTable = new SourceTable("VTUBER"
                , joinOps);

        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("Name"));
        selectedColumns.add(new Column.FullName("VTUBER", "GROUP"));
        selectedColumns.add(new Column.FullName("Enterprise"));
        expression = new CompareExpression(new ColumnVariable(new Column.FullName("Enterprise")),
                CompareExpression.Operator.EQ,
                new ConstantVariable("Hololive"));


        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId, "test_command");
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT Name, VTUBER.GROUP, Enterprise \n" +
                "FROM VTUBER JOIN GROUP ON VTUBER.GROUP == GROUP.GROUP\n" +
                "WHERE Enterprise == \"Hololive\" AND VTUBER.GROUP == \"Hololive 1st\"\n" +
                "----------");
        joinOps = new ArrayList<>();
        joinOps.add(new SourceTable.JoinOperator("GROUP"
                , new CompareExpression(new ColumnVariable(new Column.FullName("VTUBER", "GROUP"))
                , CompareExpression.Operator.EQ
                , new ColumnVariable(new Column.FullName("GROUP", "GROUP")))));
        sourceTable = new SourceTable("VTUBER"
                , joinOps);

        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("Name"));
        selectedColumns.add(new Column.FullName("VTUBER", "GROUP"));
        selectedColumns.add(new Column.FullName("Enterprise"));
        expression = new CompareExpression(new ColumnVariable(new Column.FullName("Enterprise")),
                CompareExpression.Operator.EQ,
                new ConstantVariable("Hololive"));
        expression = new LogicalExpression(expression,
                LogicalExpression.Operator.AND,
                new CompareExpression(new ColumnVariable(new Column.FullName("VTUBER.GROUP")),
                        CompareExpression.Operator.EQ,
                        new ConstantVariable("Hololive 1st")));


        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId, "test_command");
        resultTable.display();


        System.out.println("----------\n" +
                "SELECT VTUBER.* \n" +
                "FROM VTUBER JOIN GROUP ON VTUBER.GROUP == GROUP.GROUP\n" +
                "----------");
        joinOps = new ArrayList<>();
        joinOps.add(new SourceTable.JoinOperator("GROUP"
                , new CompareExpression(new ColumnVariable(new Column.FullName("VTUBER", "GROUP"))
                , CompareExpression.Operator.EQ
                , new ColumnVariable(new Column.FullName("GROUP", "GROUP")))));
        sourceTable = new SourceTable("VTUBER"
                , joinOps);

        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("VTUBER", "*"));
        expression = new UnaryExpression(true);


        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId, "test_command");
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM VTUBER AS V JOIN GROUP AS G ON V.GROUP == G.GROUP\n" +
                "----------");
        joinOps = new ArrayList<>();
        joinOps.add(new SourceTable.JoinOperator("GROUP"
                , new CompareExpression(new ColumnVariable(new Column.FullName("V", "GROUP"))
                , CompareExpression.Operator.EQ
                , new ColumnVariable(new Column.FullName("G", "GROUP"))), "G"));
        sourceTable = new SourceTable("VTUBER"
                , joinOps, "V");

        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("*"));
        expression = new UnaryExpression(true);


        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId, "test_command");
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM VTUBER, GROUP\n" +
                "----------");
        joinOps = new ArrayList<>();
        joinOps.add(new SourceTable.JoinOperator("GROUP"
                , new UnaryExpression(true)));
        sourceTable = new SourceTable("VTUBER"
                , joinOps);

        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("*"));
        expression = new UnaryExpression(true);


        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId, "test_command");
        resultTable.display();

        try {
            System.out.println("----------\n" +
                    "SELECT * \n" +
                    "FROM VTUBER, GROUP\n" +
                    "WHERE ID == 9\n" +
                    "----------");
            joinOps = new ArrayList<>();
            joinOps.add(new SourceTable.JoinOperator("GROUP"
                    , new UnaryExpression(true)));
            sourceTable = new SourceTable("VTUBER"
                    , joinOps);

            selectedColumns = new ArrayList<>();
            selectedColumns.add(new Column.FullName("*"));
            expression = new CompareExpression(new ColumnVariable(new Column.FullName("ID")),
                    CompareExpression.Operator.EQ,
                    new ConstantVariable(9));

            selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
            resultTable = selectStatement.execute(this.manager, connectResp.sessionId, "test_command");
            resultTable.display();
        } catch (SQLHandleException e) {
            System.out.println(e.getMessage());
        } catch (ExpressionHandleException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("----------\n" +
                    "SELECT * \n" +
                    "FROM VTUBER, GROUP\n" +
                    "WHERE Company == \"Hololive\"\n" +
                    "----------");
            joinOps = new ArrayList<>();
            joinOps.add(new SourceTable.JoinOperator("GROUP"
                    , new UnaryExpression(true)));
            sourceTable = new SourceTable("VTUBER"
                    , joinOps);

            selectedColumns = new ArrayList<>();
            selectedColumns.add(new Column.FullName("*"));
            expression = new CompareExpression(new ColumnVariable(new Column.FullName("Company")),
                    CompareExpression.Operator.EQ,
                    new ConstantVariable("Hololive"));

            selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
            resultTable = selectStatement.execute(this.manager, connectResp.sessionId, "test_command");
            resultTable.display();
        } catch (SQLHandleException e) {
            System.out.println(e.getMessage());
        } catch (ExpressionHandleException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("----------\n" +
                    "SELECT * \n" +
                    "FROM VTUBER, GROUP\n" +
                    "WHERE Enterprise == 1\n" +
                    "----------");
            joinOps = new ArrayList<>();
            joinOps.add(new SourceTable.JoinOperator("GROUP"
                    , new UnaryExpression(true)));
            sourceTable = new SourceTable("VTUBER"
                    , joinOps);

            selectedColumns = new ArrayList<>();
            selectedColumns.add(new Column.FullName("*"));
            expression = new CompareExpression(new ColumnVariable(new Column.FullName("Enterprise")),
                    CompareExpression.Operator.EQ,
                    new ConstantVariable(1));

            selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
            resultTable = selectStatement.execute(this.manager, connectResp.sessionId, "test_command");
            resultTable.display();
        } catch (SQLHandleException e) {
            System.out.println(e.getMessage());
        } catch (ExpressionHandleException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("----------\n" +
                    "SELECT ABC \n" +
                    "FROM VTUBER, GROUP\n" +
                    "----------");
            joinOps = new ArrayList<>();
            joinOps.add(new SourceTable.JoinOperator("GROUP"
                    , new UnaryExpression(true)));
            sourceTable = new SourceTable("VTUBER"
                    , joinOps);

            selectedColumns = new ArrayList<>();
            selectedColumns.add(new Column.FullName("ABC"));
            expression = new UnaryExpression(true);

            selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
            resultTable = selectStatement.execute(this.manager, connectResp.sessionId, "test_command");
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
            resultTable = selectStatement.execute(this.manager, connectResp.sessionId, "test_command");
            resultTable.display();
        } catch (SQLHandleException e) {
            System.out.println(e.getMessage());
        } catch (ExpressionHandleException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("----------\n" +
                    "SELECT * \n" +
                    "FROM VTUBER, VUP\n" +
                    "----------");
            joinOps = new ArrayList<>();
            joinOps.add(new SourceTable.JoinOperator("VUP"
                    , new UnaryExpression(true)));
            sourceTable = new SourceTable("VTUBER"
                    , joinOps);

            selectedColumns = new ArrayList<>();
            selectedColumns.add(new Column.FullName("*"));
            expression = new UnaryExpression(true);

            selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
            resultTable = selectStatement.execute(this.manager, connectResp.sessionId, "test_command");
            resultTable.display();
        } catch (SQLHandleException e) {
            System.out.println(e.getMessage());
        } catch (ExpressionHandleException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("----------\n" +
                    "SELECT GROUP \n" +
                    "FROM VTUBER, GROUP\n" +
                    "----------");
            joinOps = new ArrayList<>();
            joinOps.add(new SourceTable.JoinOperator("GROUP"
                    , new UnaryExpression(true)));
            sourceTable = new SourceTable("VTUBER"
                    , joinOps);

            selectedColumns = new ArrayList<>();
            selectedColumns.add(new Column.FullName("GROUP"));
            expression = new UnaryExpression(true);

            selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
            resultTable = selectStatement.execute(this.manager, connectResp.sessionId, "test_command");
            resultTable.display();
        } catch (SQLHandleException e) {
            System.out.println(e.getMessage());
        } catch (ExpressionHandleException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM MULTI\n" +
                "----------");
        joinOps = new ArrayList<>();
        sourceTable = new SourceTable("MULTI"
                , joinOps);

        selectedColumns = new ArrayList<>();
        selectedColumns.add(new Column.FullName("*"));
        expression = new CompareExpression(new ColumnVariable(new Column.FullName("ID_1")),
                CompareExpression.Operator.EQ,
                new ConstantVariable(1));
        selectStatement = new SelectStatement(selectedColumns, sourceTable, expression);
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId, "test_command");
        resultTable.display();

        System.out.println("----------\n" +
                "SELECT * \n" +
                "FROM MULTI\n" +
                "----------");
        joinOps = new ArrayList<>();
        sourceTable = new SourceTable("MULTI"
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
        resultTable = selectStatement.execute(this.manager, connectResp.sessionId, "test_command");
        resultTable.display();
    }

    @Test
    public void testCreateDatabase() throws SQLHandleException {
        System.out.println("----------testCreateDatabase----------");
        manager.deleteAllDatabase();
        CreateDatabaseStatement createDatabaseStatement = new CreateDatabaseStatement("A");
        createDatabaseStatement.execute(manager, connectResp.sessionId, "test_command");
        Assert.assertThrows(SQLHandleException.class, () -> {
            CreateDatabaseStatement createDatabaseStatement2 = new CreateDatabaseStatement("A");
            createDatabaseStatement.execute(manager, connectResp.sessionId, "test_command");
        });
    }

    @Test
    public void testDrpoDatabase() throws SQLHandleException {
        System.out.println("----------testDrpoDatabase----------");
        manager.deleteAllDatabase();
        CreateDatabaseStatement createDatabaseStatement = new CreateDatabaseStatement("A");
        createDatabaseStatement.execute(manager, connectResp.sessionId, "test_command");
        UseDatabaseStatement useDatabaseStatement = new UseDatabaseStatement("A");
        useDatabaseStatement.execute(manager, connectResp.sessionId, "test_command");
        DropDatabaseStatement dropDatabaseStatement = new DropDatabaseStatement("A");
        dropDatabaseStatement.execute(manager, connectResp.sessionId, "test_command");
        Assert.assertThrows(SQLHandleException.class, () -> {
            DropDatabaseStatement dropDatabaseStatement2 = new DropDatabaseStatement("A");
            dropDatabaseStatement2.execute(manager, connectResp.sessionId, "test_command");
        });
    }

    @Test
    public void testUseDatabase() throws SQLHandleException {
        System.out.println("----------testUseDatabase----------");
        manager.deleteAllDatabase();
        CreateDatabaseStatement createDatabaseStatement = new CreateDatabaseStatement("A");
        createDatabaseStatement.execute(manager, connectResp.sessionId, "test_command");
        UseDatabaseStatement useDatabaseStatement = new UseDatabaseStatement("A");
        useDatabaseStatement.execute(manager, connectResp.sessionId, "test_command");
        Assert.assertThrows(SQLHandleException.class, () -> {
            UseDatabaseStatement useDatabaseStatement2 = new UseDatabaseStatement("b");
            useDatabaseStatement2.execute(manager, connectResp.sessionId, "test_command");
        });
    }

    @Test
    public void testCreateTable() throws SQLHandleException {
        System.out.println("----------testCreateTable----------");
        manager.deleteAllDatabase();
        CreateDatabaseStatement createDatabaseStatement = new CreateDatabaseStatement("A");
        createDatabaseStatement.execute(manager, connectResp.sessionId, "test_command");
        ArrayList<ColumnDefinition> columnDefinitions = new ArrayList<>();
        columnDefinitions.add(new ColumnStatement(new Column("A", ColumnType.STRING, true, true, 100)));
        CreateTableStatement createTableStatement = new CreateTableStatement("A", columnDefinitions);
        Assert.assertThrows(SQLHandleException.class, () -> {
            createTableStatement.execute(manager, connectResp.sessionId, "test_command");
        });
        UseDatabaseStatement useDatabaseStatement = new UseDatabaseStatement("A");
        useDatabaseStatement.execute(manager, connectResp.sessionId, "test_command");
        createTableStatement.execute(manager, connectResp.sessionId, "test_command");
    }

    @Test
    public void testDropTable() throws SQLHandleException {
        System.out.println("----------testDropTable----------");
        manager.deleteAllDatabase();
        CreateDatabaseStatement createDatabaseStatement = new CreateDatabaseStatement("A");
        createDatabaseStatement.execute(manager, connectResp.sessionId, "test_command");
        ArrayList<ColumnDefinition> columnDefinitions = new ArrayList<>();
        columnDefinitions.add(new ColumnStatement(new Column("A", ColumnType.STRING, true, true, 100)));
        CreateTableStatement createTableStatement = new CreateTableStatement("A", columnDefinitions);
        Assert.assertThrows(SQLHandleException.class, () -> {
            createTableStatement.execute(manager, connectResp.sessionId, "test_command");
        });
        UseDatabaseStatement useDatabaseStatement = new UseDatabaseStatement("A");
        useDatabaseStatement.execute(manager, connectResp.sessionId, "test_command");
        createTableStatement.execute(manager, connectResp.sessionId, "test_command");
        DropDatabaseStatement dropDatabaseStatement = new DropDatabaseStatement("A");
        dropDatabaseStatement.execute(manager, connectResp.sessionId, "test_command");
        Assert.assertThrows(SQLHandleException.class, () -> {
            dropDatabaseStatement.execute(manager, connectResp.sessionId, "test_command");
        });
    }

    @Test
    public void testShowMeta() throws SQLHandleException {
        System.out.println("----------testShowMeta----------");
        SelectStatement selectStatement = new SelectStatement(
                new ArrayList<Column.FullName>() {
                    {
                        add(new Column.FullName("*"));
                    }
                },
                new SourceTable("VTUBER", new ArrayList<SourceTable.JoinOperator>()),
                new UnaryExpression(true)
        );
        QueryTable table = selectStatement.execute(manager, connectResp.sessionId, "test_command");
        table.display();
    }

    @Test
    public void testShowTable() throws SQLHandleException {
        System.out.println("----------testShowTable----------");
        ShowTableStatement showTableStatement = new ShowTableStatement("TESTDATABASE");
        QueryTable table = showTableStatement.execute(manager, connectResp.sessionId, "test_command");
        table.display();
        Assert.assertThrows(SQLHandleException.class, () -> {
            ShowTableStatement showTableStatement2 = new ShowTableStatement("TestDatabase2");
            showTableStatement2.execute(manager, connectResp.sessionId, "test_command");
        });
    }

    @Test
    public void testShowDatabase() throws SQLHandleException {
        System.out.println("----------testShowDatabase----------");
        ShowDatabaseStatement showDatabaseStatement = new ShowDatabaseStatement();
        QueryTable table = showDatabaseStatement.execute(manager, connectResp.sessionId, "test_command");
        table.display();
        manager.deleteAllDatabase();
        System.out.println("If we delete all databases, the result of show databases is:");
        table = showDatabaseStatement.execute(manager, connectResp.sessionId, "test_command");
        table.display();
    }

    @Test
    public void testInsert() throws SQLHandleException {
        System.out.println("----------testInsert----------");
        InsertStatement insertStatement = new InsertStatement("VTUBER",
                new ArrayList<String>() {
                    {
                        add("ID");
                        add("Name");
                        add("GROUP");
                    }
                },
                new ArrayList<ArrayList<ConstantVariable>>() {
                    {
                        add(new ArrayList<ConstantVariable>() {
                            {
                                add(new ConstantVariable(10));
                                add(new ConstantVariable("zhengweixi"));
                                add(new ConstantVariable("THU"));
                            }
                        });
                    }
                });
        insertStatement.execute(manager, connectResp.sessionId, "test_command");
        SelectStatement selectStatement = new SelectStatement(
                new ArrayList<Column.FullName>() {
                    {
                        add(new Column.FullName("*"));
                    }
                },
                new SourceTable("VTUBER", new ArrayList<SourceTable.JoinOperator>()),
                new UnaryExpression(true)
        );
        QueryTable table = selectStatement.execute(manager, connectResp.sessionId, "test_command");
        table.display();
    }

    @Test
    public void testUpdate() throws SQLHandleException {
        System.out.println("----------testUpdate----------");
        UpdateStatement updateStatement = new UpdateStatement("VTUBER", "Name"
                , new ConstantVariable("Matsuri")
                , new CompareExpression(new ColumnVariable(new Column.FullName("ID")),
                CompareExpression.Operator.EQ,
                new ConstantVariable(0)));

        updateStatement.execute(manager, connectResp.sessionId, "test_command");
        SelectStatement selectStatement = new SelectStatement(
                new ArrayList<Column.FullName>() {
                    {
                        add(new Column.FullName("*"));
                    }
                },
                new SourceTable("VTUBER", new ArrayList<SourceTable.JoinOperator>()),
                new UnaryExpression(true)
        );
        QueryTable table = selectStatement.execute(manager, connectResp.sessionId, "test_command");
        table.display();
    }

    @Test
    public void testDelete() throws SQLHandleException {
        System.out.println("----------testUpdate----------");
        DeleteStatement deleteStatement = new DeleteStatement("VTUBER"
                , new CompareExpression(new ColumnVariable(new Column.FullName("ID")),
                CompareExpression.Operator.EQ,
                new ConstantVariable(0)));

        deleteStatement.execute(manager, connectResp.sessionId, "test_command");
        SelectStatement selectStatement = new SelectStatement(
                new ArrayList<Column.FullName>() {
                    {
                        add(new Column.FullName("*"));
                    }
                },
                new SourceTable("VTUBER", new ArrayList<SourceTable.JoinOperator>()),
                new UnaryExpression(true)
        );
        QueryTable table = selectStatement.execute(manager, connectResp.sessionId, "test_command");
        table.display();
    }
}
