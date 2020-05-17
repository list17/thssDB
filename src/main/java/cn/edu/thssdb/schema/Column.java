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

        public boolean equals(FullName other) {
            // 两者都存在前缀时, 先比较前缀.
            // 若其中一个不存在前缀或是两者前缀相同, 则直接比较后缀名.
            if (prefix != null && other.prefix != null && !prefix.equals(other.prefix)) {
                return false;
            } else if (name.equals(other.name)) {
                return true;
            } else {
                return false;
            }
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

    public Column getCopiedColumn(boolean withPrefix) {
        Column copiedColumn = new Column(this.fullName.name,
                this.type,
                this.primary,
                this.notNull,
                this.maxLength);
        if (withPrefix) {
            copiedColumn.setPrefix(this.fullName.prefix);
        }
        return copiedColumn;
    }

    public String getName() { return this.fullName.toString(); }

    public FullName getColumnFullName() { return this.fullName; }

    public String toString() {
        return this.getName() + ',' + type + ',' + primary + ',' + notNull + ',' + maxLength;
    }
}
