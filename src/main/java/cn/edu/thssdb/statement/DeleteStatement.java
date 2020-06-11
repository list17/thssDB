package cn.edu.thssdb.statement;

import cn.edu.thssdb.exception.FileWriteException;
import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.exception.UserManageException;
import cn.edu.thssdb.expression.Expression;
import cn.edu.thssdb.expression.Variable;
import cn.edu.thssdb.query.QueryTable;
import cn.edu.thssdb.schema.*;
import cn.edu.thssdb.type.ColumnType;
import cn.edu.thssdb.utils.*;

import java.util.ArrayList;
import java.util.Iterator;

public class DeleteStatement implements Statement{

    String name;
    Expression expression;

    public DeleteStatement(String name, Expression expression) {
        this.name = name;
        this.expression = expression;
    }

    public DeleteStatement(String name) {
        this.name = name;
    }

    @Override
    public QueryTable execute(Manager manager, Long sessionId, String command) throws SQLHandleException {
        UserManager um = UserManager.getInstance();
        ValueInstance vi = ValueInstance.getInstance();
        String cur_db_name = manager.getSessionCurrentDatabase(sessionId).getName();
        String cur_user = um.getCurUsername(sessionId);

        if (!vi.getIsInit() && !um.checkWritable(cur_db_name, sessionId) && !cur_user.equals(Global.DEFAULT_USER)) {
            throw new SQLHandleException("Current user has no write authority on database " + cur_db_name);
        }

        Database database = manager.getSessionCurrentDatabase(sessionId);
        Table baseTable = database.getTable(this.name);

        //事务管理
        TransactionManager tm = TransactionManager.getInstance();

        if (!vi.getIsInit()) { // 非初始化
            if (tm.getFlag(sessionId)) { // 事务态
                tm.setLockX(sessionId, this.name);
            } else { // 非事务态
                if (tm.setLockXSingle(sessionId, this.name)) {

                } else {
                    throw new SQLHandleException("Statement on this table is blocked now");
                }
            }
        }
        tm.setSession(sessionId);

        try {
            // 建立结果表, 存放更新了多少行数据.
            ArrayList<Column> resultColumn = new ArrayList<>();
            resultColumn.add(new Column("DeleteCount", ColumnType.INT, true, true, 0));
            QueryTable resultTable = new QueryTable("Result", resultColumn);
            int deleteCount = 0;

            if (baseTable == null) {
                throw new SQLHandleException("Exception: table " + this.name + " could not be found.");
            }

            ArrayList<Integer> primaryIndices = baseTable.getPrimaryIndices();
            ArrayList<Column> baseColumns = baseTable.getCopiedColumns(true);

            // 查找出所有主键属性.
            ArrayList<Column.FullName> primaryKeys = new ArrayList<>();
            for (int index : primaryIndices) {
                primaryKeys.add(baseColumns.get(index).getColumnFullName());
            }
            ArrayList<Comparable> primaryValues = this.expression.tryToGetPrimaryValue(primaryKeys);
            if (!primaryValues.isEmpty()) {
                boolean isPrimarySearch = true;
                for (Comparable value : primaryValues) {
                    if (value == null) {
                        isPrimarySearch = false;
                        break;
                    }
                }
                // 主键所有属性都被指定, 为主键检索.
                if (isPrimarySearch) {
                    MultiEntry searchKey = new MultiEntry();
                    for (Comparable value : primaryValues) {
                        searchKey.addEntry(Entry.generateEntry(value));
                    }
                    try {
                        Row deleteRow = baseTable.search(searchKey);
                        if (deleteRow != null) {
                            baseTable.delete(searchKey, tm.getTX());
                            resultTable.rows.add(new Row(1));

                            if (!vi.getIsInit()) {
                                if (tm.getFlag(sessionId)) { // 事务态
                                    tm.getTX().addScript(command);
                                } else { // 非事务态
                                    WriteScript ws = new WriteScript();
                                    ws.output(manager, sessionId, command);
                                }
                            }
                            return resultTable;
                        }
                    } catch (SQLHandleException e) {
                        throw e;
                    }
                }
            }
            // 否则将遍历搜索
            // 获取所有需要赋值的Variable, 以及赋值时需要索引到行的下标.
            ArrayList<Variable> variables = this.expression.getAllVariables();
            int variableNum = variables.size();
            ArrayList<Integer> assignIndices = new ArrayList<>();
            this.setAssignIndices(variables, baseTable, assignIndices);

            Iterator<Row> iterator = baseTable.iterator();
            while (iterator.hasNext()) {
                Row rawRow = iterator.next();
                for (int i = 0; i < variableNum; i++) {
                    variables.get(i).assignValue(rawRow.getEntries().get(assignIndices.get(i)).value);
                }
                if (this.expression.evaluate()) {
                    try {
                        // 提取出该行的主键, 使用update进行更新.
                        MultiEntry searchKey = rawRow.getMultiEntry(primaryIndices);
                        baseTable.delete(searchKey, tm.getTX());
                        deleteCount = deleteCount + 1;
                    } catch (SQLHandleException e) {
                        throw e;
                    }
                }
            }

            resultTable.rows.add(new Row(deleteCount));

            if (!vi.getIsInit()) {
                if (tm.getFlag(sessionId)) { // 事务态
                    tm.getTX().addScript(command);
                } else { // 非事务态
                    WriteScript ws = new WriteScript();
                    ws.output(manager, sessionId, command);
                }
            }
            return resultTable;
        } catch (FileWriteException e) {

        } finally {
            if (!vi.getIsInit()) {
                if (tm.getFlag(sessionId)) { // 事务态

                } else { // 非事务态
                    tm.releaseTXLock(sessionId);
                    tm.destroyTransaction(sessionId);
                }
            }
        }
        return null;
    }

    public void setAssignIndices(ArrayList<Variable> variables,
                                 Table baseTable,
                                 ArrayList<Integer> assignIndices) {
        int variableNum = variables.size();
        assignIndices.clear();

        // 寻找这些变量在源表列中的位置. 找不到则抛出一个错误.
        for (int i = 0; i < variableNum; i++) {
            if (baseTable.getColumnIndicesMap().containsKey(variables.get(i).getVariableName().name)) {
                assignIndices.add(baseTable.getColumnIndicesMap()
                        .get(variables.get(i).getVariableName().name));
            } else {
                throw new SQLHandleException("Exception: " +
                        variables.get(i).getVariableName().name +
                        " in the WHERE clause could not be found.");
            }
        }
    }
}
