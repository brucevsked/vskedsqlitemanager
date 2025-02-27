package com.vsked.sqlitemanager.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class VConnection {

    private final Connection connection;

    public VConnection(VDatabaseFile databaseFile) {
        if(databaseFile==null){
            throw new IllegalArgumentException("connection database file is null code1");
        }

        if(databaseFile.getDatabaseFile()==null){
            throw new IllegalArgumentException("connection database file is null code2");
        }

        String databaseFilePath=databaseFile.getDatabaseFile().getAbsolutePath();

        String lowCaseFileName=databaseFilePath.toLowerCase();

        if(!lowCaseFileName.endsWith(".db")){
            throw new IllegalArgumentException("connection database file name is not end with db");
        }

        if(databaseFilePath.length()<=3){
            throw new IllegalArgumentException("connection database file name can't empty");
        }

        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:"+databaseFilePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
