package com.vsked.sqlitemanager.domain;

public class VTableColumnNotNull {
    private boolean isNotNull;

    public VTableColumnNotNull(boolean isNotNull) {
        this.isNotNull = isNotNull;
    }

    public boolean isNotNull() {
        return isNotNull;
    }
}
