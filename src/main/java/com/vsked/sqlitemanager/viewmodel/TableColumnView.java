package com.vsked.sqlitemanager.viewmodel;

public class TableColumnView {
    private int cid;
    private String name;

    public TableColumnView(int cid, String name) {
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
