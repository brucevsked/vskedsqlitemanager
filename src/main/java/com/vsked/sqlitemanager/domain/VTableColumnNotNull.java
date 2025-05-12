package com.vsked.sqlitemanager.domain;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.BooleanProperty;

public class VTableColumnNotNull {
    private final BooleanProperty isNotNull;

    public VTableColumnNotNull(boolean isNotNull) {
        this.isNotNull = new SimpleBooleanProperty(isNotNull);
    }

    public boolean isNotNull() {
        return isNotNull.get();
    }

    public BooleanProperty isNotNullProperty() {
        return isNotNull;
    }

    public void setNotNull(boolean value) {
        this.isNotNull.set(value);
    }
}