package com.vsked.sqlitemanager.domain;

import java.util.List;

public class SqliteDataType {
    public static final List<String> TYPES = List.of(
            "INTEGER", "TEXT", "REAL", "BLOB", "NUMERIC",
            "BOOLEAN", "DATE", "DATETIME", "INT", "VARCHAR(255)",
            "CHAR(1)", "FLOAT", "DOUBLE", "DECIMAL(10,2)"
    );
}
