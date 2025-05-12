package com.vsked.sqlitemanager.domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VTableTableColumnDataType {
    private final StringProperty dataType;

    public VTableTableColumnDataType(String dataType) {
        if (dataType == null) {
            throw new IllegalArgumentException("table column data type cannot be null");
        }
        this.dataType = new SimpleStringProperty(dataType);
    }

    public String getDataType() {
        return dataType.get();
    }

    public StringProperty getDataTypeProperty() {
        return dataType;
    }

    public void setDataType(String value) {
        dataType.set(value);
    }
}
