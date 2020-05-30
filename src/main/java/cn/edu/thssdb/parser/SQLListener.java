// Generated from /home/list/Documents/数据库原理/大作业框架及具体要求/ThssDB/src/main/java/cn/edu/thssdb/parser/SQL.g4 by ANTLR 4.8
package cn.edu.thssdb.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SQLParser}.
 */
public interface SQLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SQLParser#parse}.
	 * @param ctx the parse tree
	 */
	void enterParse(SQLParser.ParseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#parse}.
	 * @param ctx the parse tree
	 */
	void exitParse(SQLParser.ParseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#sql_stmt_list}.
	 * @param ctx the parse tree
	 */
	void enterSql_stmt_list(SQLParser.Sql_stmt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#sql_stmt_list}.
	 * @param ctx the parse tree
	 */
	void exitSql_stmt_list(SQLParser.Sql_stmt_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#sql_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSql_stmt(SQLParser.Sql_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#sql_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSql_stmt(SQLParser.Sql_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#start_trans_stmt}.
	 * @param ctx the parse tree
	 */
	void enterStart_trans_stmt(SQLParser.Start_trans_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#start_trans_stmt}.
	 * @param ctx the parse tree
	 */
	void exitStart_trans_stmt(SQLParser.Start_trans_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#commit_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCommit_stmt(SQLParser.Commit_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#commit_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCommit_stmt(SQLParser.Commit_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#rollback_stmt}.
	 * @param ctx the parse tree
	 */
	void enterRollback_stmt(SQLParser.Rollback_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#rollback_stmt}.
	 * @param ctx the parse tree
	 */
	void exitRollback_stmt(SQLParser.Rollback_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#create_db_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_db_stmt(SQLParser.Create_db_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#create_db_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_db_stmt(SQLParser.Create_db_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#drop_db_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDrop_db_stmt(SQLParser.Drop_db_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#drop_db_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDrop_db_stmt(SQLParser.Drop_db_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#create_user_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_user_stmt(SQLParser.Create_user_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#create_user_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_user_stmt(SQLParser.Create_user_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#drop_user_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDrop_user_stmt(SQLParser.Drop_user_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#drop_user_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDrop_user_stmt(SQLParser.Drop_user_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#create_table_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table_stmt(SQLParser.Create_table_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#create_table_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table_stmt(SQLParser.Create_table_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#show_meta_stmt}.
	 * @param ctx the parse tree
	 */
	void enterShow_meta_stmt(SQLParser.Show_meta_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#show_meta_stmt}.
	 * @param ctx the parse tree
	 */
	void exitShow_meta_stmt(SQLParser.Show_meta_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#grant_stmt}.
	 * @param ctx the parse tree
	 */
	void enterGrant_stmt(SQLParser.Grant_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#grant_stmt}.
	 * @param ctx the parse tree
	 */
	void exitGrant_stmt(SQLParser.Grant_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#revoke_stmt}.
	 * @param ctx the parse tree
	 */
	void enterRevoke_stmt(SQLParser.Revoke_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#revoke_stmt}.
	 * @param ctx the parse tree
	 */
	void exitRevoke_stmt(SQLParser.Revoke_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#use_db_stmt}.
	 * @param ctx the parse tree
	 */
	void enterUse_db_stmt(SQLParser.Use_db_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#use_db_stmt}.
	 * @param ctx the parse tree
	 */
	void exitUse_db_stmt(SQLParser.Use_db_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#delete_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDelete_stmt(SQLParser.Delete_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#delete_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDelete_stmt(SQLParser.Delete_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#drop_table_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDrop_table_stmt(SQLParser.Drop_table_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#drop_table_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDrop_table_stmt(SQLParser.Drop_table_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#show_db_stmt}.
	 * @param ctx the parse tree
	 */
	void enterShow_db_stmt(SQLParser.Show_db_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#show_db_stmt}.
	 * @param ctx the parse tree
	 */
	void exitShow_db_stmt(SQLParser.Show_db_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#quit_stmt}.
	 * @param ctx the parse tree
	 */
	void enterQuit_stmt(SQLParser.Quit_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#quit_stmt}.
	 * @param ctx the parse tree
	 */
	void exitQuit_stmt(SQLParser.Quit_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#show_table_stmt}.
	 * @param ctx the parse tree
	 */
	void enterShow_table_stmt(SQLParser.Show_table_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#show_table_stmt}.
	 * @param ctx the parse tree
	 */
	void exitShow_table_stmt(SQLParser.Show_table_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#insert_stmt}.
	 * @param ctx the parse tree
	 */
	void enterInsert_stmt(SQLParser.Insert_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#insert_stmt}.
	 * @param ctx the parse tree
	 */
	void exitInsert_stmt(SQLParser.Insert_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#value_entry}.
	 * @param ctx the parse tree
	 */
	void enterValue_entry(SQLParser.Value_entryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#value_entry}.
	 * @param ctx the parse tree
	 */
	void exitValue_entry(SQLParser.Value_entryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#select_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSelect_stmt(SQLParser.Select_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#select_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSelect_stmt(SQLParser.Select_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#create_view_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_view_stmt(SQLParser.Create_view_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#create_view_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_view_stmt(SQLParser.Create_view_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#drop_view_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDrop_view_stmt(SQLParser.Drop_view_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#drop_view_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDrop_view_stmt(SQLParser.Drop_view_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#update_stmt}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_stmt(SQLParser.Update_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#update_stmt}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_stmt(SQLParser.Update_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#column_def}.
	 * @param ctx the parse tree
	 */
	void enterColumn_def(SQLParser.Column_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#column_def}.
	 * @param ctx the parse tree
	 */
	void exitColumn_def(SQLParser.Column_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#type_name}.
	 * @param ctx the parse tree
	 */
	void enterType_name(SQLParser.Type_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#type_name}.
	 * @param ctx the parse tree
	 */
	void exitType_name(SQLParser.Type_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#column_constraint}.
	 * @param ctx the parse tree
	 */
	void enterColumn_constraint(SQLParser.Column_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#column_constraint}.
	 * @param ctx the parse tree
	 */
	void exitColumn_constraint(SQLParser.Column_constraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LogicalExpression}
	 * labeled alternative in {@link SQLParser#multiple_condition}.
	 * @param ctx the parse tree
	 */
	void enterLogicalExpression(SQLParser.LogicalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicalExpression}
	 * labeled alternative in {@link SQLParser#multiple_condition}.
	 * @param ctx the parse tree
	 */
	void exitLogicalExpression(SQLParser.LogicalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CompareExpression}
	 * labeled alternative in {@link SQLParser#multiple_condition}.
	 * @param ctx the parse tree
	 */
	void enterCompareExpression(SQLParser.CompareExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CompareExpression}
	 * labeled alternative in {@link SQLParser#multiple_condition}.
	 * @param ctx the parse tree
	 */
	void exitCompareExpression(SQLParser.CompareExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ColumnVariable}
	 * labeled alternative in {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterColumnVariable(SQLParser.ColumnVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ColumnVariable}
	 * labeled alternative in {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitColumnVariable(SQLParser.ColumnVariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConstantVariable}
	 * labeled alternative in {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterConstantVariable(SQLParser.ConstantVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConstantVariable}
	 * labeled alternative in {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitConstantVariable(SQLParser.ConstantVariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BracketExpression}
	 * labeled alternative in {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBracketExpression(SQLParser.BracketExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BracketExpression}
	 * labeled alternative in {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBracketExpression(SQLParser.BracketExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#table_constraint}.
	 * @param ctx the parse tree
	 */
	void enterTable_constraint(SQLParser.Table_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#table_constraint}.
	 * @param ctx the parse tree
	 */
	void exitTable_constraint(SQLParser.Table_constraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ResultArbitraryColumn}
	 * labeled alternative in {@link SQLParser#result_column}.
	 * @param ctx the parse tree
	 */
	void enterResultArbitraryColumn(SQLParser.ResultArbitraryColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ResultArbitraryColumn}
	 * labeled alternative in {@link SQLParser#result_column}.
	 * @param ctx the parse tree
	 */
	void exitResultArbitraryColumn(SQLParser.ResultArbitraryColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ResultTableArbitraryColumn}
	 * labeled alternative in {@link SQLParser#result_column}.
	 * @param ctx the parse tree
	 */
	void enterResultTableArbitraryColumn(SQLParser.ResultTableArbitraryColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ResultTableArbitraryColumn}
	 * labeled alternative in {@link SQLParser#result_column}.
	 * @param ctx the parse tree
	 */
	void exitResultTableArbitraryColumn(SQLParser.ResultTableArbitraryColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ResultColumnFull}
	 * labeled alternative in {@link SQLParser#result_column}.
	 * @param ctx the parse tree
	 */
	void enterResultColumnFull(SQLParser.ResultColumnFullContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ResultColumnFull}
	 * labeled alternative in {@link SQLParser#result_column}.
	 * @param ctx the parse tree
	 */
	void exitResultColumnFull(SQLParser.ResultColumnFullContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#table_query}.
	 * @param ctx the parse tree
	 */
	void enterTable_query(SQLParser.Table_queryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#table_query}.
	 * @param ctx the parse tree
	 */
	void exitTable_query(SQLParser.Table_queryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#auth_level}.
	 * @param ctx the parse tree
	 */
	void enterAuth_level(SQLParser.Auth_levelContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#auth_level}.
	 * @param ctx the parse tree
	 */
	void exitAuth_level(SQLParser.Auth_levelContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#column_full_name}.
	 * @param ctx the parse tree
	 */
	void enterColumn_full_name(SQLParser.Column_full_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#column_full_name}.
	 * @param ctx the parse tree
	 */
	void exitColumn_full_name(SQLParser.Column_full_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#database_name}.
	 * @param ctx the parse tree
	 */
	void enterDatabase_name(SQLParser.Database_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#database_name}.
	 * @param ctx the parse tree
	 */
	void exitDatabase_name(SQLParser.Database_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#table_full_name}.
	 * @param ctx the parse tree
	 */
	void enterTable_full_name(SQLParser.Table_full_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#table_full_name}.
	 * @param ctx the parse tree
	 */
	void exitTable_full_name(SQLParser.Table_full_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#table_alias_name}.
	 * @param ctx the parse tree
	 */
	void enterTable_alias_name(SQLParser.Table_alias_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#table_alias_name}.
	 * @param ctx the parse tree
	 */
	void exitTable_alias_name(SQLParser.Table_alias_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#table_name}.
	 * @param ctx the parse tree
	 */
	void enterTable_name(SQLParser.Table_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#table_name}.
	 * @param ctx the parse tree
	 */
	void exitTable_name(SQLParser.Table_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#user_name}.
	 * @param ctx the parse tree
	 */
	void enterUser_name(SQLParser.User_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#user_name}.
	 * @param ctx the parse tree
	 */
	void exitUser_name(SQLParser.User_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#column_name}.
	 * @param ctx the parse tree
	 */
	void enterColumn_name(SQLParser.Column_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#column_name}.
	 * @param ctx the parse tree
	 */
	void exitColumn_name(SQLParser.Column_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#view_name}.
	 * @param ctx the parse tree
	 */
	void enterView_name(SQLParser.View_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#view_name}.
	 * @param ctx the parse tree
	 */
	void exitView_name(SQLParser.View_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#password}.
	 * @param ctx the parse tree
	 */
	void enterPassword(SQLParser.PasswordContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#password}.
	 * @param ctx the parse tree
	 */
	void exitPassword(SQLParser.PasswordContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StringConstant}
	 * labeled alternative in {@link SQLParser#literal_value}.
	 * @param ctx the parse tree
	 */
	void enterStringConstant(SQLParser.StringConstantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StringConstant}
	 * labeled alternative in {@link SQLParser#literal_value}.
	 * @param ctx the parse tree
	 */
	void exitStringConstant(SQLParser.StringConstantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DecimalConstant}
	 * labeled alternative in {@link SQLParser#literal_value}.
	 * @param ctx the parse tree
	 */
	void enterDecimalConstant(SQLParser.DecimalConstantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DecimalConstant}
	 * labeled alternative in {@link SQLParser#literal_value}.
	 * @param ctx the parse tree
	 */
	void exitDecimalConstant(SQLParser.DecimalConstantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RealConstant}
	 * labeled alternative in {@link SQLParser#literal_value}.
	 * @param ctx the parse tree
	 */
	void enterRealConstant(SQLParser.RealConstantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RealConstant}
	 * labeled alternative in {@link SQLParser#literal_value}.
	 * @param ctx the parse tree
	 */
	void exitRealConstant(SQLParser.RealConstantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TrueConstant}
	 * labeled alternative in {@link SQLParser#literal_value}.
	 * @param ctx the parse tree
	 */
	void enterTrueConstant(SQLParser.TrueConstantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TrueConstant}
	 * labeled alternative in {@link SQLParser#literal_value}.
	 * @param ctx the parse tree
	 */
	void exitTrueConstant(SQLParser.TrueConstantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FalseConstant}
	 * labeled alternative in {@link SQLParser#literal_value}.
	 * @param ctx the parse tree
	 */
	void enterFalseConstant(SQLParser.FalseConstantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FalseConstant}
	 * labeled alternative in {@link SQLParser#literal_value}.
	 * @param ctx the parse tree
	 */
	void exitFalseConstant(SQLParser.FalseConstantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndOperator}
	 * labeled alternative in {@link SQLParser#logicalOperator}.
	 * @param ctx the parse tree
	 */
	void enterAndOperator(SQLParser.AndOperatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndOperator}
	 * labeled alternative in {@link SQLParser#logicalOperator}.
	 * @param ctx the parse tree
	 */
	void exitAndOperator(SQLParser.AndOperatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrOperator}
	 * labeled alternative in {@link SQLParser#logicalOperator}.
	 * @param ctx the parse tree
	 */
	void enterOrOperator(SQLParser.OrOperatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrOperator}
	 * labeled alternative in {@link SQLParser#logicalOperator}.
	 * @param ctx the parse tree
	 */
	void exitOrOperator(SQLParser.OrOperatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EqualOperator}
	 * labeled alternative in {@link SQLParser#comparator}.
	 * @param ctx the parse tree
	 */
	void enterEqualOperator(SQLParser.EqualOperatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EqualOperator}
	 * labeled alternative in {@link SQLParser#comparator}.
	 * @param ctx the parse tree
	 */
	void exitEqualOperator(SQLParser.EqualOperatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GreateThanOperator}
	 * labeled alternative in {@link SQLParser#comparator}.
	 * @param ctx the parse tree
	 */
	void enterGreateThanOperator(SQLParser.GreateThanOperatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GreateThanOperator}
	 * labeled alternative in {@link SQLParser#comparator}.
	 * @param ctx the parse tree
	 */
	void exitGreateThanOperator(SQLParser.GreateThanOperatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LessThanOperator}
	 * labeled alternative in {@link SQLParser#comparator}.
	 * @param ctx the parse tree
	 */
	void enterLessThanOperator(SQLParser.LessThanOperatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LessThanOperator}
	 * labeled alternative in {@link SQLParser#comparator}.
	 * @param ctx the parse tree
	 */
	void exitLessThanOperator(SQLParser.LessThanOperatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LessEqualOperator}
	 * labeled alternative in {@link SQLParser#comparator}.
	 * @param ctx the parse tree
	 */
	void enterLessEqualOperator(SQLParser.LessEqualOperatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LessEqualOperator}
	 * labeled alternative in {@link SQLParser#comparator}.
	 * @param ctx the parse tree
	 */
	void exitLessEqualOperator(SQLParser.LessEqualOperatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GreatEqualOperator}
	 * labeled alternative in {@link SQLParser#comparator}.
	 * @param ctx the parse tree
	 */
	void enterGreatEqualOperator(SQLParser.GreatEqualOperatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GreatEqualOperator}
	 * labeled alternative in {@link SQLParser#comparator}.
	 * @param ctx the parse tree
	 */
	void exitGreatEqualOperator(SQLParser.GreatEqualOperatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotEqualOperator}
	 * labeled alternative in {@link SQLParser#comparator}.
	 * @param ctx the parse tree
	 */
	void enterNotEqualOperator(SQLParser.NotEqualOperatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotEqualOperator}
	 * labeled alternative in {@link SQLParser#comparator}.
	 * @param ctx the parse tree
	 */
	void exitNotEqualOperator(SQLParser.NotEqualOperatorContext ctx);
}