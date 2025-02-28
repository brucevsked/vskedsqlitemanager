package com.vsked.sqlitemanager.domain;

public class VTableColumn {
    private int cid;
    private String name;
    private String dataType;
    private boolean isNotNull;
    private String dflt_value;
    private int pk;

    public VTableColumn(int cid, String name, String dataType, boolean isNotNull, String dflt_value, int pk) {
        this.cid = cid;
        this.name = name;
        this.dataType = dataType;
        this.isNotNull = isNotNull;
        this.dflt_value = dflt_value;
        this.pk = pk;
    }

    public int getCid() {
        return cid;
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    public boolean isNotNull() {
        return isNotNull;
    }

    public String getDflt_value() {
        return dflt_value;
    }

    public int getPk() {
        return pk;
    }
}
