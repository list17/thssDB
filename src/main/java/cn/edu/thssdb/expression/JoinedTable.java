package cn.edu.thssdb.expression;

import cn.edu.thssdb.schema.Database;
import cn.edu.thssdb.schema.Table;
import java.util.ArrayList;

public class JoinedTable {
    public class JoinOperator {
        public String joinedTableName;
        public Expression expression;
    }

    private String tableName;
    private ArrayList<JoinOperator> joinOps;
    private String alias;

    public JoinedTable(String tableName, ArrayList<JoinOperator> joinOps, String alias) {
        this.tableName = tableName;
        this.joinOps = joinOps;
        this.alias = alias;
    }

    public Table getTable(Database database) {
        Table baseTable = database.getTable(tableName);
        Table result = null;

        if (joinOps.isEmpty()) {
            if (alias != null) {
                result = baseTable.getAlias(alias);
            } else {
                result = baseTable;
            }
        } else {
            for (JoinOperator joinOp: joinOps) {
                Table joinedTable = database.getTable(joinOp.joinedTableName);

            }
        }
        return result;
    }

}
