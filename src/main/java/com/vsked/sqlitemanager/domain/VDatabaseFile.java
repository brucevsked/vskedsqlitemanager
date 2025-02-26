package com.vsked.sqlitemanager.domain;

import java.io.File;

public class VDatabaseFile {
    private File databaseFile;

    public VDatabaseFile(File databaseFile) {
        if(databaseFile==null){
            throw new IllegalArgumentException("database file name can not be null");
        }
        this.databaseFile = databaseFile;
    }
}
