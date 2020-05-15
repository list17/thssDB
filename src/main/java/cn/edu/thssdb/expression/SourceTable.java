package cn.edu.thssdb.expression;

import cn.edu.thssdb.schema.Column;
import cn.edu.thssdb.schema.Database;
import cn.edu.thssdb.schema.Table;
import java.util.ArrayList;

public class SourceTable {
    public class JoinOperator {
        public String joinedTableName;
        public Expression expression;
    }

    private String tableName;
    private ArrayList<JoinOperator> joinOps;
    private String alias;


    public SourceTable(String tableName, ArrayList<JoinOperator> joinOps, String alias) {
        this.tableName = tableName;
        this.joinOps = joinOps;
        this.alias = alias;
    }

    public void setAttribute(Database database) {
        Table baseTable = database.getTable(tableName);

        if (joinOps.isEmpty()) {

        } else {
            for (JoinOperator joinOp: joinOps) {
                Table toJoinTable = database.getTable(joinOp.joinedTableName);

                // 获取join后的表的列属性, 带前缀(便于辨识列), 然后建立临时表.
                ArrayList<Column> joinedColumn = new ArrayList<>();
                joinedColumn.addAll(baseTable.getCopiedColumns(true));
                joinedColumn.addAll(toJoinTable.getCopiedColumns(true));
                Table joinedTable = new Table("",
                        baseTable.getTableName() + " JOIN " + toJoinTable.getTableName(),
                        joinedColumn);

            }
        }
    }

}
