package com.vsked.sqlitemanager.viewmodel;

public class TableColumn {
    private int cid;
    private String name;

    public TableColumn(int cid, String name) {
        this.cid = cid;
        this.name = name;
    }

    public int getCid() {
        return cid;
    }

    public String getName() {
        return name;
    }

}
