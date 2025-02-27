package com.vsked.sqlitemanager.domain;

public class VTableName {
    private String tableName;

    public VTableName(String tableName) {
        if(tableName==null){
            throw new IllegalArgumentException("table name not be null！");
        }
        //TODO check min and max table name length
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
