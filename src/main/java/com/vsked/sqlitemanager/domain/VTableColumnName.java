package com.vsked.sqlitemanager.domain;

public class VTableColumnName {
    private String name;

    public VTableColumnName(String name) {
        if(name==null){
            throw new IllegalArgumentException("table column not be null");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
