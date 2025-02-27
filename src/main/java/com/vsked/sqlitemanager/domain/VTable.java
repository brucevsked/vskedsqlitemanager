package com.vsked.sqlitemanager.domain;

public class VTable {
    private VTableName VTableName;

    public VTable(VTableName VTableName) {
        this.VTableName = VTableName;
    }

    public VTableName getTableName() {
        return VTableName;
    }
}
