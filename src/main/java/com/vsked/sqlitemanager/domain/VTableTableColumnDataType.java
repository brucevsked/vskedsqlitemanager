package com.vsked.sqlitemanager.domain;

public class VTableTableColumnDataType {
    private String dataType;

    public VTableTableColumnDataType(String dataType) {
        if(dataType==null){
            throw new IllegalArgumentException("table column data type not be null");
        }
        this.dataType = dataType;
    }

    public String getDataType() {
        return dataType;
    }
}
