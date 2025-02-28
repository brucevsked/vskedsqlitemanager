package com.vsked.sqlitemanager.services;

import com.vsked.sqlitemanager.domain.VTableColumn;
import com.vsked.sqlitemanager.domain.VTableName;
import com.vsked.sqlitemanager.domain.VConnection;
import com.vsked.sqlitemanager.domain.VTable;
import com.vsked.sqlitemanager.domain.VTableList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class TableService {
    private VConnection vConnection;

    public TableService(VConnection vConnection) {
        this.vConnection = vConnection;
    }

    public VConnection getvConnection() {
        return vConnection;
    }

    public VTableList getTables(){
        List<VTable> tableList=new LinkedList<>();

        try {
            Connection conn= getvConnection().getConnection();
            Statement st=conn.createStatement();
            String sql="SELECT name FROM sqlite_master WHERE type='table'";
            ResultSet rs=st.executeQuery(sql);

            String tableName="";

            while (rs.next()){
                tableName=rs.getString("name");
                tableList.add(new VTable(new VTableName(tableName)));
            }

            rs.close();
            st.close();
            conn.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new VTableList(tableList);
    }

    public List<VTableColumn> getColumns(VTableName tableName){
        List<VTableColumn> tableColumns=new LinkedList<>();
        try {

            Connection conn= getvConnection().getConnection();
            Statement st=conn.createStatement();
            String sql="select * from pragma_table_info('"+tableName.getTableName()+"')";
            ResultSet rs=st.executeQuery(sql);

            while (rs.next()){
                int cid=rs.getInt("cid");
                String columnName=rs.getString("name");
                String columnType=rs.getString("type");
                int isNotNull=rs.getInt("notnull");
                String defaultValue=rs.getString("dflt_value");
                int isPrimaryKey=rs.getInt("pk");
                //log.info(cid+"|"+columnName+"|"+columnType+"|"+isNotNull+"|"+defaultValue+"|"+isPrimaryKey);
                tableColumns.add(new VTableColumn(cid,columnName,columnType, isNotNull == 1,defaultValue,isPrimaryKey));
            }

            rs.close();
            st.close();
            conn.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return tableColumns;

    }
}
