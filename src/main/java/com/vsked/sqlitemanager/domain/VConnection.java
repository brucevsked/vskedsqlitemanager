package com.vsked.sqlitemanager.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class VConnection {

    private final Connection connection;
    private final VDatabaseFile currentDatabaseFile;

    public VConnection(VDatabaseFile databaseFile) throws IllegalArgumentException {
        if (databaseFile == null) {
            throw new IllegalArgumentException("Database file cannot be null.");
        }

        if (databaseFile.getDatabaseFile() == null) {
            throw new IllegalArgumentException("Underlying File object in databaseFile is null.");
        }

        String databaseFilePath = databaseFile.getDatabaseFile().getAbsolutePath();

        if (!isValidDatabasePath(databaseFilePath)) {
            throw new IllegalArgumentException("Invalid database file path: must end with .db and not be empty.");
        }

        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFilePath);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to establish database connection: " + e.getMessage(), e);
        }

        this.currentDatabaseFile = databaseFile;
    }

    /**
     * Validates the database file path.
     */
    private boolean isValidDatabasePath(String path) {
        if (path == null || path.trim().isEmpty()) {
            return false;
        }
        String lowCasePath = path.toLowerCase();
        return lowCasePath.endsWith(".db");
    }

    public VDatabaseFile getCurrentDatabaseFile() {
        return currentDatabaseFile;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // Reconnect using the same database file
            return new VConnection(this.currentDatabaseFile).getConnection();
        }
        return connection;
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
