package com.vsked.sqlitemanager.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class VConnection {

    private final Connection connection;

    public VConnection(Connection connection) throws SQLException {
        if(connection==null){
            throw new SQLException("connection is null");
        }

        if(connection.isClosed()){
            throw new SQLException("connection has closed");
        }
        this.connection = connection;
    }

    public VConnection(String databaseFilePath) throws SQLException {
        if(databaseFilePath==null){
            throw new SQLException("connection database file name is null");
        }

        String lowCaseFileName=databaseFilePath.toLowerCase();

        if(!lowCaseFileName.endsWith(".db")){
            throw new SQLException("connection database file name is not end with db");
        }

        if(databaseFilePath.length()<=3){
            throw new SQLException("connection database file name can't empty");
        }

        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:"+databaseFilePath);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
