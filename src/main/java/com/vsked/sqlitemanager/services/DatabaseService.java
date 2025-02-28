package com.vsked.sqlitemanager.services;

import com.vsked.sqlitemanager.domain.VConnection;
import com.vsked.sqlitemanager.domain.VDatabaseFile;

public class DatabaseService {

    private VDatabaseFile vDatabaseFile;
    private VConnection vConnection;

    public DatabaseService(VDatabaseFile vDatabaseFile) {
        this.vDatabaseFile = vDatabaseFile;
    }

    public VDatabaseFile getvDatabaseFile() {
        return vDatabaseFile;
    }

    public VConnection getvConnection() {
        return new VConnection(getvDatabaseFile());
    }
}
