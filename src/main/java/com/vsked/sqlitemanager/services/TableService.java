package com.vsked.sqlitemanager.services;

import com.vsked.sqlitemanager.domain.VTableName;
import com.vsked.sqlitemanager.domain.VConnection;
import com.vsked.sqlitemanager.domain.VTable;
import com.vsked.sqlitemanager.domain.VTableList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class TableService {

    public VTableList getTables(VConnection connection){
        List<VTable> tableList=new LinkedList<>();

        try {

            Connection conn= connection.getConnection();
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
}
