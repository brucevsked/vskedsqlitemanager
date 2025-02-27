package com.vsked.sqlitemanager.domain;

import java.util.LinkedList;
import java.util.List;

public class VTableList {

    private List<VTable> tables=new LinkedList<>();

    public VTableList(List<VTable> tables) {
        this.tables = tables;
    }

    public List<VTable> getTables() {
        return tables;
    }
}
