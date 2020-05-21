package cn.edu.thssdb.parser;

import cn.edu.thssdb.exception.SQLHandleException;
import cn.edu.thssdb.expression.*;
import cn.edu.thssdb.schema.Column;
import cn.edu.thssdb.schema.Table;
import cn.edu.thssdb.statement.*;
import cn.edu.thssdb.type.ColumnType;
import javafx.scene.control.Tab;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;

public class Visitor extends SQLBaseVisitor<Object>{

    private enum ColumnConstraint {
        NOT_NULL,
        PRIMARY_KEY
    }

    @Override
    public Object visitParse(SQLParser.ParseContext ctx) {
        SQLParser.Sql_stmt_listContext sql_stmt_listContext = ctx.sql_stmt_list();
        if(sql_stmt_listContext == null) {
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
    public Object visitCreate_db_stmt(SQLParser.Create_db_stmtContext ctx) {
        return new CreateDatabaseStatement((String) visit(ctx.database_name()));
    }

    @Override
    public Object visitDrop_db_stmt(SQLParser.Drop_db_stmtContext ctx) {
        return new DropDatabaseStatement((String) visit(ctx.database_name()));
    }

    @Override
    public Object visitCreate_user_stmt(SQLParser.Create_user_stmtContext ctx) {
        // todo
        return super.visitCreate_user_stmt(ctx);
    }

    @Override
    public Object visitDrop_user_stmt(SQLParser.Drop_user_stmtContext ctx) {
        // todo
        return super.visitDrop_user_stmt(ctx);
    }

    @Override
    public Object visitCreate_table_stmt(SQLParser.Create_table_stmtContext ctx) {
        String name = (String) visit(ctx.table_name());
        ArrayList<ColumnDefinition> columnDefinitions = new ArrayList<>();
        for(SQLParser.Column_defContext column_defContext :ctx.column_def()) {
            columnDefinitions.add((ColumnDefinition) visit(column_defContext));
        }
        for(SQLParser.Column_nameContext column_nameContext : ((SQLParser.Table_constraintContext) visit(ctx.table_constraint())).column_name()) {
            columnDefinitions.add((ColumnDefinition) visit(column_nameContext));
        }
        return new CreateTableStatement(name, columnDefinitions);
    }

    @Override
    public Object visitShow_meta_stmt(SQLParser.Show_meta_stmtContext ctx) {
        return new ShowTableStatement((String) visit(ctx.table_name()));
    }

    @Override
    public Object visitGrant_stmt(SQLParser.Grant_stmtContext ctx) {
        // todo
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
        return super.visitDelete_stmt(ctx);
    }

    @Override
    public Object visitDrop_table_stmt(SQLParser.Drop_table_stmtContext ctx) {
        return super.visitDrop_table_stmt(ctx);
    }

    @Override
    public Object visitShow_db_stmt(SQLParser.Show_db_stmtContext ctx) {
        return super.visitShow_db_stmt(ctx);
    }

    @Override
    public Object visitQuit_stmt(SQLParser.Quit_stmtContext ctx) {
        return super.visitQuit_stmt(ctx);
    }

    @Override
    public Object visitShow_table_stmt(SQLParser.Show_table_stmtContext ctx) {
        return super.visitShow_table_stmt(ctx);
    }

    @Override
    public Object visitInsert_stmt(SQLParser.Insert_stmtContext ctx) {
        return super.visitInsert_stmt(ctx);
    }

    @Override
    public Object visitValue_entry(SQLParser.Value_entryContext ctx) {
        return super.visitValue_entry(ctx);
    }

    @Override
    public Object visitSelect_stmt(SQLParser.Select_stmtContext ctx) {
        ArrayList<Column.FullName> selectedColumns = new ArrayList<>();
        for(SQLParser.Result_columnContext result_columnContext : ctx.result_column()) {
            Object object = visit(result_columnContext);
            if(object == null) {
                selectedColumns.clear();
                break;
            } else {
                selectedColumns.add((Column.FullName) object);
            }
        }
        String sourceTablename = null;
        ArrayList<SourceTable.JoinOperator> operators = new ArrayList<>();
        for (int i = 0; i < ctx.table_query().size(); i++) {
            TableQueryStatement tableQueryStatement = (TableQueryStatement) visit(ctx.table_query(i));
            if (i == 0) {
                sourceTablename = tableQueryStatement.getTable_name();
            } else {
                operators.add(new SourceTable.JoinOperator(tableQueryStatement.getTable_name(), new UnaryExpression(true)));
            }
            operators.addAll(tableQueryStatement.getOperators());
        }
        if(sourceTablename == null){
            throw new SQLHandleException("Source table missed");
        }
        SourceTable sourceTable = new SourceTable(sourceTablename, operators);
        return super.visitSelect_stmt(ctx);
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
        return super.visitUpdate_stmt(ctx);
    }

    @Override
    public Object visitColumn_def(SQLParser.Column_defContext ctx) {
        String name = (String) visit(ctx.column_name());
        String type = ((String) visit(ctx.type_name())).toUpperCase();
        ColumnType columnType;
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
            case "STRING":
                columnType = ColumnType.STRING;
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
        // todo maxlength
        Column column = new Column(name, columnType, primary_key, not_null, 100);
        return new ColumnStatement(column);
    }

    @Override
    public Object visitType_name(SQLParser.Type_nameContext ctx) {
        return ctx.getText();
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
        return super.visitStringConstant(ctx);
    }

    @Override
    public Object visitDecimalConstant(SQLParser.DecimalConstantContext ctx) {
        return super.visitDecimalConstant(ctx);
    }

    @Override
    public Object visitRealConstant(SQLParser.RealConstantContext ctx) {
        return super.visitRealConstant(ctx);
    }

    @Override
    public Object visitTrueConstant(SQLParser.TrueConstantContext ctx) {
        return super.visitTrueConstant(ctx);
    }

    @Override
    public Object visitFalseConstant(SQLParser.FalseConstantContext ctx) {
        return super.visitFalseConstant(ctx);
    }

    @Override
    public Object visitNullConstant(SQLParser.NullConstantContext ctx) {
        return super.visitNullConstant(ctx);
    }

    @Override
    public Object visitColumnVariable(SQLParser.ColumnVariableContext ctx) {
        return new ColumnVariable((Column.FullName) visit(ctx.column_full_name()));
    }

    @Override
    public Object visitConstantVariable(SQLParser.ConstantVariableContext ctx) {
        return new ConstantVariable()
    }

    @Override
    public Object visitBracketExpression(SQLParser.BracketExpressionContext ctx) {
        return super.visitBracketExpression(ctx);
    }

    @Override
    public Object visitTable_constraint(SQLParser.Table_constraintContext ctx) {
        return super.visitTable_constraint(ctx);
    }

    @Override
    public Object visitResult_column(SQLParser.Result_columnContext ctx) {
        Column.FullName column_fullname;
        if(ctx.column_full_name()!=null){
            column_fullname = (Column.FullName) visit(ctx.column_full_name());
        } else if (ctx.table_name() != null) {
            // todo 此语法暂不支持
            column_fullname = new Column.FullName((String) visit(ctx.table_name()), (String) visit(ctx.))
        } else {
            return null;
        }
        return super.visitResult_column(ctx);
    }

    @Override
    public Object visitTable_query(SQLParser.Table_queryContext ctx) {
        ArrayList<SourceTable.JoinOperator> joinOperators = new ArrayList<>();
        TableFullName tableFullName = (TableFullName) visit(ctx.getChild(0));
        for(int i = 2;i < ctx.getChildCount();) {
            TableFullName tableFullName1 = (TableFullName) visit(ctx.getChild(i));
            if(i + 1 < ctx.getChildCount() &&  (String) visit(ctx.getChild(i + 1)) == "ON") {
                Expression expression = (Expression) visit(ctx.getChild(i + 2));
                joinOperators.add(new SourceTable.JoinOperator(tableFullName1.getName(), expression, tableFullName1.getAlias()));
                i += 4;
            } else if (i + 1 < ctx.getChildCount() && (String) visit(ctx.getChild(i+1)) == "JOIN") {
                UnaryExpression unaryExpression = new UnaryExpression(true);
                joinOperators.add(new SourceTable.JoinOperator(tableFullName1.getName(), unaryExpression, tableFullName1.getAlias()));
                i += 2;
            }
        }
        return new TableQueryStatement(tableFullName.getName(), joinOperators, tableFullName.getAlias());
    }

    @Override
    public Object visitAuth_level(SQLParser.Auth_levelContext ctx) {
        return super.visitAuth_level(ctx);
    }

    @Override
    public Object visitLiteral_value(SQLParser.Literal_valueContext ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitColumn_full_name(SQLParser.Column_full_nameContext ctx) {
        String table_name = null;
        if(ctx.table_name() != null){
            table_name = (String) visit(ctx.table_name());
        }
        String column_name = (String) visit(ctx.column_name());
        return new Column.FullName(table_name, column_name);
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
