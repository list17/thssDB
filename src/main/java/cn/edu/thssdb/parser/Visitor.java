package cn.edu.thssdb.parser;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.expression.*;
import cn.edu.thssdb.schema.Column;
import cn.edu.thssdb.statement.*;
import cn.edu.thssdb.type.ColumnType;

import java.util.ArrayList;

public class Visitor extends SQLBaseVisitor<Object> {

    private enum ColumnConstraint {
        NOT_NULL,
        PRIMARY_KEY
    }

    @Override
    public Object visitParse(SQLParser.ParseContext ctx) {
        SQLParser.Sql_stmt_listContext sql_stmt_listContext = ctx.sql_stmt_list();
        if (sql_stmt_listContext == null) {
            return new ArrayList<>();
        }
        return visit(sql_stmt_listContext);
    }

    @Override
    public Object visitSql_stmt_list(SQLParser.Sql_stmt_listContext ctx) {
        ArrayList<Statement> statements = new ArrayList<>();
        for (SQLParser.Sql_stmtContext sql_stmtContext : ctx.sql_stmt())
            statements.add((Statement) visit(sql_stmtContext));
        return statements;
    }

    @Override
    public Object visitSql_stmt(SQLParser.Sql_stmtContext ctx) {
        return super.visitSql_stmt(ctx);
    }

    @Override
    public Object visitStart_trans_stmt(SQLParser.Start_trans_stmtContext ctx) {
        return new StartTransactionStatement();
    }

    @Override
    public Object visitCommit_stmt(SQLParser.Commit_stmtContext ctx) {
        return new CommitStatement();
    }

    @Override
    public Object visitRollback_stmt(SQLParser.Rollback_stmtContext ctx) {
        return new RollbackStatement();
    }

    @Override
    public Object visitShutdown_stmt(SQLParser.Shutdown_stmtContext ctx) {
        return new ShutdownStatement();
    }

    @Override
    public Object visitCreate_db_stmt(SQLParser.Create_db_stmtContext ctx) {
        return new CreateDatabaseStatement((String) visit(ctx.database_name()));
    }

    @Override
    public Object visitDrop_db_stmt(SQLParser.Drop_db_stmtContext ctx) {
        return new DropDatabaseStatement((String) visit(ctx.database_name()));
    }

    @Override
    public Object visitCreate_user_stmt(SQLParser.Create_user_stmtContext ctx) {
        String username = (String) visit(ctx.user_name());
        String password = (String) visit(ctx.password());
        return new CreateUserStatement(username, password);
    }

    @Override
    public Object visitDrop_user_stmt(SQLParser.Drop_user_stmtContext ctx) {
        String username = (String) visit(ctx.user_name());
        return new DropUserStatement(username);
    }

    @Override
    public Object visitCreate_table_stmt(SQLParser.Create_table_stmtContext ctx) {
        String name = (String) visit(ctx.table_name());
        ArrayList<ColumnDefinition> columnDefinitions = new ArrayList<>();
        for (SQLParser.Column_defContext column_defContext : ctx.column_def()) {
            columnDefinitions.add((ColumnDefinition) visit(column_defContext));
        }
        if (ctx.table_constraint() != null) {
            ConstraintColumnStatement constraintColumnStatement = (ConstraintColumnStatement) visit(ctx.table_constraint());
            columnDefinitions.add(constraintColumnStatement);
        }
        return new CreateTableStatement(name, columnDefinitions);
    }

    @Override
    public Object visitShow_meta_stmt(SQLParser.Show_meta_stmtContext ctx) {
        return new SelectStatement(
                new ArrayList<Column.FullName>() {
                    {
                        add(new Column.FullName("*"));
                    }
                },
                new SourceTable((String) visit(ctx.table_name()), new ArrayList<SourceTable.JoinOperator>()),
                new UnaryExpression(true)
        );
    }

    @Override
    public Object visitGrant_stmt(SQLParser.Grant_stmtContext ctx) {
        ArrayList<String> authLevels = new ArrayList<>();
        for (SQLParser.Auth_levelContext auth_levelContext : ctx.auth_level()) {
            authLevels.add((String) visit(auth_levelContext));
        }
        return super.visitGrant_stmt(ctx);
    }

    @Override
    public Object visitRevoke_stmt(SQLParser.Revoke_stmtContext ctx) {
        // todo
        return super.visitRevoke_stmt(ctx);
    }

    @Override
    public Object visitUse_db_stmt(SQLParser.Use_db_stmtContext ctx) {
        return new UseDatabaseStatement((String) visit(ctx.database_name()));
    }

    @Override
    public Object visitDelete_stmt(SQLParser.Delete_stmtContext ctx) {
        String name = (String) visit(ctx.table_name());
        if (ctx.multiple_condition() != null) {
            Expression expression = (Expression) visit(ctx.multiple_condition());
            return new DeleteStatement(name, expression);
        } else {
            return new DeleteStatement(name, new UnaryExpression(true));
        }
    }

    @Override
    public Object visitDrop_table_stmt(SQLParser.Drop_table_stmtContext ctx) {
        return new DropTableStatement((String) visit(ctx.table_name()));
    }

    @Override
    public Object visitShow_db_stmt(SQLParser.Show_db_stmtContext ctx) {
        return new ShowDatabaseStatement();
    }

    @Override
    public Object visitQuit_stmt(SQLParser.Quit_stmtContext ctx) {
        return super.visitQuit_stmt(ctx);
    }

    @Override
    public Object visitShow_table_stmt(SQLParser.Show_table_stmtContext ctx) {
        return new ShowTableStatement((String) visit(ctx.database_name()));
    }

    @Override
    public Object visitInsert_stmt(SQLParser.Insert_stmtContext ctx) {
        String name = (String) visit(ctx.table_name());
        ArrayList<String> columns = new ArrayList<>();
        ArrayList<ArrayList<ConstantVariable>> rows = new ArrayList<>();
        for (SQLParser.Column_nameContext column_nameContext : ctx.column_name()) {
            columns.add((String) visit(column_nameContext));
        }
        for (SQLParser.Value_entryContext value_entryContext : ctx.value_entry()) {
            rows.add((ArrayList<ConstantVariable>) visit(value_entryContext));
        }
        return new InsertStatement(name, columns, rows);
    }

    @Override
    public Object visitValue_entry(SQLParser.Value_entryContext ctx) {
        ArrayList<ConstantVariable> row = new ArrayList<>();
        for (SQLParser.Literal_valueContext literal_valueContext : ctx.literal_value()) {
            row.add((ConstantVariable) visit(literal_valueContext));
        }
        return row;
    }

    @Override
    public Object visitSelect_stmt(SQLParser.Select_stmtContext ctx) {
        ArrayList<Column.FullName> selectedColumns = new ArrayList<>();
        for (SQLParser.Result_columnContext result_columnContext : ctx.result_column()) {
            Object object = visit(result_columnContext);
            if (object == null) {
                selectedColumns.clear();
                break;
            } else {
                selectedColumns.add((Column.FullName) object);
            }
        }
        String sourceTablename = null;
        String alias = null;
        ArrayList<SourceTable.JoinOperator> operators = new ArrayList<>();
        for (int i = 0; i < ctx.table_query().size(); i++) {
            TableQueryStatement tableQueryStatement = (TableQueryStatement) visit(ctx.table_query(i));
            if (i == 0) {
                sourceTablename = tableQueryStatement.getTable_name();
                alias = tableQueryStatement.getAlias();
            } else {
                operators.add(new SourceTable.JoinOperator(tableQueryStatement.getTable_name(), new UnaryExpression(true)));
            }
            operators.addAll(tableQueryStatement.getOperators());
        }
        if (sourceTablename == null) {
            throw new SQLHandleException("Source table missed");
        }
        SourceTable sourceTable = new SourceTable(sourceTablename, operators, alias);
        Expression expression = null;
        if (ctx.multiple_condition() != null) {
            expression = (Expression) visit(ctx.multiple_condition());
        } else {
            expression = new UnaryExpression(true);
        }
        return new SelectStatement(selectedColumns, sourceTable, expression);
    }

    @Override
    public Object visitCreate_view_stmt(SQLParser.Create_view_stmtContext ctx) {
        return super.visitCreate_view_stmt(ctx);
    }

    @Override
    public Object visitDrop_view_stmt(SQLParser.Drop_view_stmtContext ctx) {
        return super.visitDrop_view_stmt(ctx);
    }

    @Override
    public Object visitUpdate_stmt(SQLParser.Update_stmtContext ctx) {
        String name = (String) visit(ctx.table_name());
        String column_name = (String) visit(ctx.column_name());
        ConstantVariable variable = (ConstantVariable) visit(ctx.literal_value());
        Expression expression = (Expression) visit(ctx.multiple_condition());
        return new UpdateStatement(name, column_name, variable, expression);
    }

    @Override
    public Object visitColumn_def(SQLParser.Column_defContext ctx) {
        String name = (String) visit(ctx.column_name());
        ColumnTypeStatement columnTypeStatement = (ColumnTypeStatement) visit(ctx.type_name());
        String type = columnTypeStatement.getName().toUpperCase();
        ColumnType columnType;
        int max_length = -1;
        switch (type) {
            case "INT":
                columnType = ColumnType.INT;
                break;
            case "LONG":
                columnType = ColumnType.LONG;
                break;
            case "FLOAT":
                columnType = ColumnType.FLOAT;
                break;
            case "DOUBLE":
                columnType = ColumnType.DOUBLE;
                break;
            case "VARCHAR":
                columnType = ColumnType.STRING;
                max_length = columnTypeStatement.getMax_length();
                break;
            default:
                throw new SQLHandleException("Unknown type in table definition statement");
        }
        boolean not_null = false;
        boolean primary_key = false;
        for (SQLParser.Column_constraintContext constraint : ctx.column_constraint()) {
            switch ((ColumnConstraint) visit(constraint)) {
                case NOT_NULL:
                    not_null = true;
                    break;
                case PRIMARY_KEY:
                    primary_key = true;
                    not_null = true;
                    break;
                default:
                    break;
            }
        }
        Column column = new Column(name, columnType, primary_key, not_null, max_length);
        return new ColumnStatement(column);
    }

    @Override
    public Object visitTypeInt(SQLParser.TypeIntContext ctx) {
        return new ColumnTypeStatement(ctx.getText());
    }

    @Override
    public Object visitTypeLong(SQLParser.TypeLongContext ctx) {
        return new ColumnTypeStatement(ctx.getText());
    }

    @Override
    public Object visitTypeFloat(SQLParser.TypeFloatContext ctx) {
        return new ColumnTypeStatement(ctx.getText());
    }

    @Override
    public Object visitTypeDouble(SQLParser.TypeDoubleContext ctx) {
        return new ColumnTypeStatement(ctx.getText());
    }

    @Override
    public Object visitTypeString(SQLParser.TypeStringContext ctx) {
        return new ColumnTypeStatement(ctx.T_STRING().getText(), (ConstantVariable) visit(ctx.literal_value()));
    }


    @Override
    public Object visitInt_value(SQLParser.Int_valueContext ctx) {
        return Integer.parseInt(ctx.getText());
    }

    @Override
    public Object visitColumn_constraint(SQLParser.Column_constraintContext ctx) {
        if (ctx.K_PRIMARY() != null && ctx.K_KEY() != null) {
            return ColumnConstraint.PRIMARY_KEY;
        } else if (ctx.K_NOT() != null && ctx.K_NULL() != null) {
            return ColumnConstraint.NOT_NULL;
        }
        return null;
    }

    @Override
    public Object visitLogicalExpression(SQLParser.LogicalExpressionContext ctx) {
        return new LogicalExpression(
                (Expression) visit(ctx.left),
                (LogicalExpression.Operator) visit(ctx.logicalOperator()),
                (Expression) visit(ctx.right)
        );
    }

    @Override
    public Object visitCompareExpression(SQLParser.CompareExpressionContext ctx) {
        return new CompareExpression(
                (Variable) visit(ctx.left),
                (CompareExpression.Operator) visit(ctx.comparator()),
                (Variable) visit(ctx.right)
        );
    }

    @Override
    public Object visitStringConstant(SQLParser.StringConstantContext ctx) {
        return new ConstantVariable(ctx.getText());
    }

    @Override
    public Object visitDecimalConstant(SQLParser.DecimalConstantContext ctx) {
        return new ConstantVariable(Long.parseLong(ctx.getText()));
    }

    @Override
    public Object visitRealConstant(SQLParser.RealConstantContext ctx) {
        return new ConstantVariable(Double.parseDouble(ctx.getText()));
    }

    @Override
    public Object visitTrueConstant(SQLParser.TrueConstantContext ctx) {
        return new UnaryExpression(true);
    }

    @Override
    public Object visitFalseConstant(SQLParser.FalseConstantContext ctx) {
        return new UnaryExpression(false);
    }


    @Override
    public Object visitColumnVariable(SQLParser.ColumnVariableContext ctx) {
        return new ColumnVariable((Column.FullName) visit(ctx.column_full_name()));
    }

    @Override
    public Object visitConstantVariable(SQLParser.ConstantVariableContext ctx) {
        return visit(ctx.literal_value());
    }

    @Override
    public Object visitBracketExpression(SQLParser.BracketExpressionContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public Object visitTable_constraint(SQLParser.Table_constraintContext ctx) {
        ArrayList<String> columnTypeStatements = new ArrayList<>();
        for (SQLParser.Column_nameContext column_nameContext : ctx.column_name()) {
            columnTypeStatements.add((String) visit(column_nameContext));
        }
        return new ConstraintColumnStatement(columnTypeStatements);
    }

    @Override
    public Object visitResultArbitraryColumn(SQLParser.ResultArbitraryColumnContext ctx) {
        return new Column.FullName("*");
    }

    @Override
    public Object visitResultTableArbitraryColumn(SQLParser.ResultTableArbitraryColumnContext ctx) {
        return new Column.FullName((String) visit(ctx.table_name()), "*");
    }

    @Override
    public Object visitResultColumnFull(SQLParser.ResultColumnFullContext ctx) {
        return visit(ctx.column_full_name());
    }

    @Override
    public Object visitTable_query(SQLParser.Table_queryContext ctx) {
        ArrayList<SourceTable.JoinOperator> joinOperators = new ArrayList<>();
        TableFullName tableFullName = (TableFullName) visit(ctx.table_full_name(0));
        for (int i = 1; i < ctx.table_full_name().size(); i++) {
            TableFullName tableFullName1 = (TableFullName) visit(ctx.table_full_name(i));
            Expression expression = (Expression) visit(ctx.multiple_condition(i - 1));
            joinOperators.add(new SourceTable.JoinOperator(tableFullName1.getName(), expression, tableFullName1.getAlias()));
        }
        return new TableQueryStatement(tableFullName.getName(), joinOperators, tableFullName.getAlias());
    }

    @Override
    public Object visitAuth_level(SQLParser.Auth_levelContext ctx) {
        return super.visitAuth_level(ctx);
    }

    @Override
    public Object visitColumn_full_name(SQLParser.Column_full_nameContext ctx) {
        String column_name = (String) visit(ctx.column_name());
        if (ctx.table_name() != null) {
            return new Column.FullName((String) visit(ctx.table_name()), column_name);
        } else {
            return new Column.FullName(column_name);
        }
    }

    @Override
    public Object visitDatabase_name(SQLParser.Database_nameContext ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitTable_full_name(SQLParser.Table_full_nameContext ctx) {
        if (ctx.K_AS() != null)
            return new TableFullName((String) visit(ctx.table_name()), (String) visit(ctx.table_alias_name()));
        else
            return new TableFullName((String) visit(ctx.table_name()));
    }

    @Override
    public Object visitTable_alias_name(SQLParser.Table_alias_nameContext ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitTable_name(SQLParser.Table_nameContext ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitUser_name(SQLParser.User_nameContext ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitColumn_name(SQLParser.Column_nameContext ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitView_name(SQLParser.View_nameContext ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitPassword(SQLParser.PasswordContext ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitAndOperator(SQLParser.AndOperatorContext ctx) {
        return LogicalExpression.Operator.AND;
    }

    @Override
    public Object visitOrOperator(SQLParser.OrOperatorContext ctx) {
        return LogicalExpression.Operator.OR;
    }

    @Override
    public Object visitEqualOperator(SQLParser.EqualOperatorContext ctx) {
        return CompareExpression.Operator.EQ;
    }

    @Override
    public Object visitGreateThanOperator(SQLParser.GreateThanOperatorContext ctx) {
        return CompareExpression.Operator.GT;
    }

    @Override
    public Object visitLessThanOperator(SQLParser.LessThanOperatorContext ctx) {
        return CompareExpression.Operator.LT;
    }

    @Override
    public Object visitLessEqualOperator(SQLParser.LessEqualOperatorContext ctx) {
        return CompareExpression.Operator.LE;
    }

    @Override
    public Object visitGreatEqualOperator(SQLParser.GreatEqualOperatorContext ctx) {
        return CompareExpression.Operator.GE;
    }

    @Override
    public Object visitNotEqualOperator(SQLParser.NotEqualOperatorContext ctx) {
        return CompareExpression.Operator.NE;
    }

}
