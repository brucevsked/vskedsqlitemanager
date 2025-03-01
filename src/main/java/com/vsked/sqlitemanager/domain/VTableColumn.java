package com.vsked.sqlitemanager.domain;

public class VTableColumn {

    private VTableColumnId id;
    private VTableColumnName name;
    private VTableTableColumnDataType dataType;
    private VTableColumnNotNull notNull;
    private VTableDfltValue dfltValue;
    private VTableColumnPk pk;

    public VTableColumn(VTableColumnId id, VTableColumnName name, VTableTableColumnDataType dataType, VTableColumnNotNull notNull, VTableDfltValue dfltValue, VTableColumnPk pk) {
        this.id = id;
        this.name = name;
        this.dataType = dataType;
        this.notNull = notNull;
        this.dfltValue = dfltValue;
        this.pk = pk;
    }

    public VTableColumnId getId() {
        return id;
    }

    public VTableColumnName getName() {
        return name;
    }

    public VTableTableColumnDataType getDataType() {
        return dataType;
    }

    public VTableColumnNotNull getNotNull() {
        return notNull;
    }

    public VTableDfltValue getDfltValue() {
        return dfltValue;
    }

    public VTableColumnPk getPk() {
        return pk;
    }
}
