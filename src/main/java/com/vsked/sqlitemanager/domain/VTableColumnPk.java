package com.vsked.sqlitemanager.domain;

public class VTableColumnPk {
    private int primaryKey;

    public VTableColumnPk(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }
}
