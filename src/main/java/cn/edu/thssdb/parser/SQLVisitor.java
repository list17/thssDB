// Generated from /home/list/Documents/数据库原理/大作业框架及具体要求/ThssDB/src/main/java/cn/edu/thssdb/parser/SQL.g4 by ANTLR 4.8
package cn.edu.thssdb.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SQLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SQLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SQLParser#parse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParse(SQLParser.ParseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#sql_stmt_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_stmt_list(SQLParser.Sql_stmt_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#sql_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_stmt(SQLParser.Sql_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#create_db_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_db_stmt(SQLParser.Create_db_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#drop_db_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_db_stmt(SQLParser.Drop_db_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#create_user_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_user_stmt(SQLParser.Create_user_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#drop_user_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_user_stmt(SQLParser.Drop_user_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#create_table_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table_stmt(SQLParser.Create_table_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#show_meta_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShow_meta_stmt(SQLParser.Show_meta_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#grant_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrant_stmt(SQLParser.Grant_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#revoke_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevoke_stmt(SQLParser.Revoke_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#use_db_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUse_db_stmt(SQLParser.Use_db_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#delete_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelete_stmt(SQLParser.Delete_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#drop_table_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_table_stmt(SQLParser.Drop_table_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#show_db_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShow_db_stmt(SQLParser.Show_db_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#quit_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuit_stmt(SQLParser.Quit_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#show_table_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShow_table_stmt(SQLParser.Show_table_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#insert_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_stmt(SQLParser.Insert_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#value_entry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue_entry(SQLParser.Value_entryContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#select_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_stmt(SQLParser.Select_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#create_view_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_view_stmt(SQLParser.Create_view_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#drop_view_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_view_stmt(SQLParser.Drop_view_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#update_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_stmt(SQLParser.Update_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#column_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_def(SQLParser.Column_defContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#type_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_name(SQLParser.Type_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#column_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_constraint(SQLParser.Column_constraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicalExpression}
	 * labeled alternative in {@link SQLParser#multiple_condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalExpression(SQLParser.LogicalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CompareExpression}
	 * labeled alternative in {@link SQLParser#multiple_condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompareExpression(SQLParser.CompareExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnVariable}
	 * labeled alternative in {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnVariable(SQLParser.ColumnVariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConstantVariable}
	 * labeled alternative in {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantVariable(SQLParser.ConstantVariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BracketExpression}
	 * labeled alternative in {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracketExpression(SQLParser.BracketExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#table_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_constraint(SQLParser.Table_constraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ResultArbitraryColumn}
	 * labeled alternative in {@link SQLParser#result_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResultArbitraryColumn(SQLParser.ResultArbitraryColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ResultTableArbitraryColumn}
	 * labeled alternative in {@link SQLParser#result_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResultTableArbitraryColumn(SQLParser.ResultTableArbitraryColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ResultColumnFull}
	 * labeled alternative in {@link SQLParser#result_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResultColumnFull(SQLParser.ResultColumnFullContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#table_query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_query(SQLParser.Table_queryContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#auth_level}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAuth_level(SQLParser.Auth_levelContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#column_full_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_full_name(SQLParser.Column_full_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#database_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatabase_name(SQLParser.Database_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#table_full_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_full_name(SQLParser.Table_full_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#table_alias_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_alias_name(SQLParser.Table_alias_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#table_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_name(SQLParser.Table_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#user_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUser_name(SQLParser.User_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#column_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_name(SQLParser.Column_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#view_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitView_name(SQLParser.View_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#password}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPassword(SQLParser.PasswordContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringConstant}
	 * labeled alternative in {@link SQLParser#literal_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringConstant(SQLParser.StringConstantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DecimalConstant}
	 * labeled alternative in {@link SQLParser#literal_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimalConstant(SQLParser.DecimalConstantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RealConstant}
	 * labeled alternative in {@link SQLParser#literal_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRealConstant(SQLParser.RealConstantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TrueConstant}
	 * labeled alternative in {@link SQLParser#literal_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrueConstant(SQLParser.TrueConstantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FalseConstant}
	 * labeled alternative in {@link SQLParser#literal_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalseConstant(SQLParser.FalseConstantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndOperator}
	 * labeled alternative in {@link SQLParser#logicalOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndOperator(SQLParser.AndOperatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrOperator}
	 * labeled alternative in {@link SQLParser#logicalOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrOperator(SQLParser.OrOperatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EqualOperator}
	 * labeled alternative in {@link SQLParser#comparator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualOperator(SQLParser.EqualOperatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code GreateThanOperator}
	 * labeled alternative in {@link SQLParser#comparator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGreateThanOperator(SQLParser.GreateThanOperatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LessThanOperator}
	 * labeled alternative in {@link SQLParser#comparator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLessThanOperator(SQLParser.LessThanOperatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LessEqualOperator}
	 * labeled alternative in {@link SQLParser#comparator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLessEqualOperator(SQLParser.LessEqualOperatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code GreatEqualOperator}
	 * labeled alternative in {@link SQLParser#comparator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGreatEqualOperator(SQLParser.GreatEqualOperatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotEqualOperator}
	 * labeled alternative in {@link SQLParser#comparator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotEqualOperator(SQLParser.NotEqualOperatorContext ctx);
}