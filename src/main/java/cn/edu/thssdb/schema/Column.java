package cn.edu.thssdb.schema;

import cn.edu.thssdb.type.ColumnType;
import java.io.Serializable;

public class Column implements Comparable<Column>, Serializable {

    private ColumnType type;
    private boolean primary;
    private boolean notNull;
    private int maxLength;

    public class FullName {
        public String prefix;
        public String name;

        public FullName(String name) {
            this.name = name;
        }

        public String toString() {
            if (this.prefix != null) {
                return this.prefix + "." + this.name;
            } else {
                return this.name;
            }
        }
    }

    private FullName fullName;

    public Column(String name, ColumnType type, boolean primary, boolean notNull, int maxLength) {
        this.fullName = new FullName(name);
        this.type = type;
        this.primary = primary;
        this.notNull = notNull;
        this.maxLength = maxLength;
    }

    public void setPrefix(String prefix) {
        this.fullName.prefix = prefix;
    }


    @Override
    public int compareTo(Column e) {
        return this.getName().compareTo(e.getName());
    }

    public boolean isPrimary() {
        return this.primary;
    }

    public boolean isNotNull() {
        return this.notNull;
    }

    public ColumnType getType() {
        return this.type;
    }

    public int getMaxLength() { return this.maxLength; }

    public Column getCopiedColumn() {
        Column copiedColumn = new Column(this.fullName.name,
                this.type,
                this.primary,
                this.notNull,
                this.maxLength);
        copiedColumn.setPrefix(this.fullName.prefix);
        return copiedColumn;
    }

    public String getName() { return this.fullName.toString(); }

    public FullName getColumnFullName() { return this.fullName; }

    public String toString() {
        return this.getName() + ',' + type + ',' + primary + ',' + notNull + ',' + maxLength;
    }
}
