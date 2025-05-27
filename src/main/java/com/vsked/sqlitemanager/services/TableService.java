package com.vsked.sqlitemanager.services;

import com.vsked.sqlitemanager.domain.VPage;
import com.vsked.sqlitemanager.domain.VPageTotalElementsNumber;
import com.vsked.sqlitemanager.domain.VTableColumn;
import com.vsked.sqlitemanager.domain.VTableColumnId;
import com.vsked.sqlitemanager.domain.VTableColumnName;
import com.vsked.sqlitemanager.domain.VTableColumnNotNull;
import com.vsked.sqlitemanager.domain.VTableColumnPk;
import com.vsked.sqlitemanager.domain.VTableDfltValue;
import com.vsked.sqlitemanager.domain.VTableRecordCount;
import com.vsked.sqlitemanager.domain.VTableTableColumnDataType;
import com.vsked.sqlitemanager.viewmodel.TableColumnView;
import com.vsked.sqlitemanager.domain.VTableName;
import com.vsked.sqlitemanager.domain.VTable;
import com.vsked.sqlitemanager.domain.VTableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TableService {

    private static final Logger log = LoggerFactory.getLogger(TableService.class);

    private DatabaseService databaseService;

    public TableService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public DatabaseService getDatabaseService() {
        return databaseService;
    }

    public VTableList getTables() {
        List<VTable> tableList = new LinkedList<>();

        try {
            Connection conn = getDatabaseService().getvConnection().getConnection();
            Statement st = conn.createStatement();
            String sql = "SELECT name FROM sqlite_master WHERE type='table'";
            ResultSet rs = st.executeQuery(sql);

            String tableName = "";

            while (rs.next()) {
                tableName = rs.getString("name");
                if (log.isInfoEnabled()) {
                    log.info(tableName);
                }
                tableList.add(new VTable(new VTableName(tableName)));
            }

            rs.close();
            st.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new VTableList(tableList);
    }

    public List<VTableColumn> getColumns(VTableName tableName) {
        List<VTableColumn> tableColumns = new LinkedList<>();
        try {

            Connection conn = getDatabaseService().getvConnection().getConnection();
            Statement st = conn.createStatement();
            String sql = "select * from pragma_table_info('" + tableName.getTableName() + "')";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int cid = rs.getInt("cid");
                String columnName = rs.getString("name");
                String columnDataType = rs.getString("type");
                int isNotNull = rs.getInt("notnull");
                String defaultValue = rs.getString("dflt_value");
                int pk = rs.getInt("pk");
                if (log.isInfoEnabled()) {
                    log.info(cid + "|" + columnName + "|" + columnDataType + "|" + isNotNull + "|" + defaultValue + "|" + pk);
                }

                tableColumns.add(new VTableColumn(new VTableColumnId(cid), new VTableColumnName(columnName), new VTableTableColumnDataType(columnDataType), new VTableColumnNotNull(isNotNull == 1), new VTableDfltValue(defaultValue), new VTableColumnPk(pk)));
            }

            rs.close();
            st.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return tableColumns;

    }

    public VTableRecordCount getTableRecordCount(VTableName tableName) {
        int recordCount = 0;
        try {

            Connection conn = getDatabaseService().getvConnection().getConnection();
            log.info("connection sqlite database success!");
            Statement st = conn.createStatement();
            String sql = "select count(1) ct from " + tableName.getTableName();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                recordCount = rs.getInt(1);
                if (log.isInfoEnabled()) {
                    log.info(recordCount + "|");
                }
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            log.error("connection test error", e);
        }
        return new VTableRecordCount(recordCount);
    }

    public int getTablePageCount(VTableName tableName, VPage page) {
        VTableRecordCount count = getTableRecordCount(tableName);
        BigDecimal b1 = new BigDecimal(count.getRecordCount());
        BigDecimal b2 = new BigDecimal(page.getPageSize().getPageSize());
        BigDecimal b3 = b1.divide(b2).setScale(0, RoundingMode.CEILING);
        return b3.intValue();
    }

    public List<TableColumnView> getTableViewColumns(List<VTableColumn> tableColumnList) {
        List<TableColumnView> tableColumnViews = new LinkedList<>();
        for (VTableColumn tableColumn : tableColumnList) {
            tableColumnViews.add(new TableColumnView(tableColumn.getId().getCid(), tableColumn.getName().getName()));
        }
        return tableColumnViews;
    }

    public VPage getData(VTableName tableName, VPage vpage) {
        List<Map> tableDataList = new ArrayList<>();
        try {
            VTableRecordCount tableRecordCount = getTableRecordCount(tableName);
            VPageTotalElementsNumber pageTotalElementsNumber = new VPageTotalElementsNumber(tableRecordCount.getRecordCount());
            vpage.setTotalElementsNumber(pageTotalElementsNumber);
            Connection conn = getDatabaseService().getvConnection().getConnection();
            Statement st = conn.createStatement();
            String sql = "select * from " + tableName.getTableName();
            sql = getPageSql(sql, vpage);

            ResultSet rs = st.executeQuery(sql);

            ResultSetMetaData metaData = rs.getMetaData();
            int maxFiledSize = metaData.getColumnCount();

            while (rs.next()) {
                Map data = new HashMap<>();
                for (int i = 1; i <= maxFiledSize; i++) {
                    data.put(metaData.getColumnName(i), rs.getString(i));
                }
                tableDataList.add(data);
            }
            vpage.setData(tableDataList);
            rs.close();
            st.close();
        } catch (Exception e) {
            log.error("get table data error", e);
        }
        return vpage;
    }

    public String getPageSql(String sql, VPage page) {
        String pageSql = "";
        if (page.getCurrentPageIndex().getPageIndex() == 0) {
            pageSql = sql + " limit " + page.getPageSize().getPageSize();
            return pageSql;
        }

        pageSql = sql + " limit " + page.getPageSize().getPageSize() + " OFFSET " + page.getCurrentPageIndex().getPageIndex() * page.getPageSize().getPageSize();
        return pageSql;
    }

    public void addColumn(VTableName tableName, String columnName, String dataType, boolean isNotNull) throws SQLException {
        // 构建 SQL 语句
        StringBuilder sql = new StringBuilder("ALTER TABLE ")
                .append(tableName.getTableName()) // 表名
                .append(" ADD COLUMN ")
                .append(columnName) // 字段名称
                .append(" ")
                .append(dataType); // 数据类型

        // 如果字段需要非空约束，添加 NOT NULL
        if (isNotNull) {
            sql.append(" NOT NULL");
        }

        // 执行 SQL 语句
        try (Connection connection = databaseService.getvConnection().getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute(sql.toString());
        } catch (SQLException e) {
            throw new SQLException("Failed to add column: " + columnName, e);
        }
    }


    // TableService 类中的 executeQuery 方法
    public List<Map<String, String>> executeQuery(String sql) throws SQLException {
        List<Map<String, String>> resultList = new ArrayList<>();
        Connection conn = databaseService.getvConnection().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            Map<String, String> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                String value = rs.getString(i);
                row.put(columnName, value);
            }
            resultList.add(row);
        }

        rs.close();
        stmt.close();
        return resultList;
    }

    public int executeUpdate(String sql) throws SQLException {
        Connection conn = databaseService.getvConnection().getConnection();
        Statement stmt = conn.createStatement();
        int rowsAffected = stmt.executeUpdate(sql);
        stmt.close();
        return rowsAffected;
    }

    public void createTable(VTableName tableName) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName.getTableName() + " (id INTEGER PRIMARY KEY AUTOINCREMENT)";
        try (Statement stmt = databaseService.getvConnection().getConnection().createStatement()) {
            stmt.execute(sql);
        }

    }

    public void renameTable(VTableName oldName, VTableName newName) throws SQLException {
        String sql = "ALTER TABLE " + oldName.getTableName() + " RENAME TO " + newName.getTableName();
        try (Statement stmt = databaseService.getvConnection().getConnection().createStatement()) {
            stmt.execute(sql);
        }
    }

    public void deleteTable(VTableName tableName) throws SQLException {
        String sql = "DROP TABLE IF EXISTS " + tableName.getTableName();
        try (Statement stmt = databaseService.getvConnection().getConnection().createStatement()) {
            stmt.execute(sql);
        }
    }

}
