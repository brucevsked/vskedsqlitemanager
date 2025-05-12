package com.vsked.sqlitemanager.domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class VTableColumnName {
    private final SimpleStringProperty name;

    public VTableColumnName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("table column name cannot be null");
        }
        this.name = new SimpleStringProperty(name);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty getNameProperty() {
        return name;
    }
}
