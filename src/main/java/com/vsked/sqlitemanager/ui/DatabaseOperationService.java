package com.vsked.sqlitemanager.ui;

import com.vsked.sqlitemanager.domain.VTableColumn;
import com.vsked.sqlitemanager.domain.VTableName;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;

public class DatabaseOperationService {

    private ApplicationMainUI applicationMainUI;

    public DatabaseOperationService(ApplicationMainUI applicationMainUI) {
        this.applicationMainUI = applicationMainUI;
    }

    public void executeUpdate(String sql) throws SQLException {
        try (Statement stmt = applicationMainUI.getDatabaseService().getvConnection().getConnection().createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    public void saveTableChanges(VTableName tableName, List<VTableColumn> updatedColumns) throws SQLException {
        String tempTableName = tableName.getTableName() + "_temp";
        Connection connection = null;
        Statement stmt = null;

        try {
            connection = applicationMainUI.getDatabaseService().getvConnection().getConnection();
            stmt = connection.createStatement();

            StringBuilder createTempTableSql = new StringBuilder("CREATE TABLE " + tempTableName + " (");
            for (VTableColumn column : updatedColumns) {
                createTempTableSql.append(column.getName().getName())
                        .append(" ")
                        .append(column.getDataType().getDataType());

                if (column.getNotNull().isNotNull()) {
                    createTempTableSql.append(" NOT NULL");
                }

                if (column.getPk().getPrimaryKey() == 1) {
                    createTempTableSql.append(" PRIMARY KEY");
                }

                createTempTableSql.append(", ");
            }

            createTempTableSql.setLength(createTempTableSql.length() - 2);
            createTempTableSql.append(")");

            stmt.execute(createTempTableSql.toString());

            StringBuilder copyDataSql = new StringBuilder("INSERT INTO " + tempTableName + " SELECT ");
            for (VTableColumn column : updatedColumns) {
                copyDataSql.append(column.getName().getName()).append(", ");
            }

            copyDataSql.setLength(copyDataSql.length() - 2);
            copyDataSql.append(" FROM ").append(tableName.getTableName());
            stmt.execute(copyDataSql.toString());

            String dropTableSql = "DROP TABLE " + tableName.getTableName();
            stmt.execute(dropTableSql);

            String renameTableSql = "ALTER TABLE " + tempTableName + " RENAME TO " + tableName.getTableName();
            stmt.execute(renameTableSql);
        } catch (SQLException e) {
            throw new SQLException("Failed to save table changes: " + e.getMessage(), e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }
}
