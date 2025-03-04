package com.vsked.sqlitemanager.services;

import com.vsked.sqlitemanager.domain.VTableColumn;
import com.vsked.sqlitemanager.domain.VTableColumnId;
import com.vsked.sqlitemanager.domain.VTableColumnName;
import com.vsked.sqlitemanager.domain.VTableColumnNotNull;
import com.vsked.sqlitemanager.domain.VTableColumnPk;
import com.vsked.sqlitemanager.domain.VTableDfltValue;
import com.vsked.sqlitemanager.domain.VTableTableColumnDataType;
import com.vsked.sqlitemanager.viewmodel.TableColumnView;
import com.vsked.sqlitemanager.domain.VTableName;
import com.vsked.sqlitemanager.domain.VConnection;
import com.vsked.sqlitemanager.domain.VTable;
import com.vsked.sqlitemanager.domain.VTableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TableService {

    private static final Logger log = LoggerFactory.getLogger(TableService.class);

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
                if(log.isInfoEnabled()){
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
                String columnDataType=rs.getString("type");
                int isNotNull=rs.getInt("notnull");
                String defaultValue=rs.getString("dflt_value");
                int pk=rs.getInt("pk");
                if(log.isInfoEnabled()){
                    log.info(cid+"|"+columnName+"|"+columnDataType+"|"+isNotNull+"|"+defaultValue+"|"+pk);
                }

                tableColumns.add(new VTableColumn(new VTableColumnId(cid),new VTableColumnName(columnName),new VTableTableColumnDataType(columnDataType), new VTableColumnNotNull(isNotNull == 1),new VTableDfltValue(defaultValue),new VTableColumnPk(pk)));
            }

            rs.close();
            st.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return tableColumns;

    }

    public List<TableColumnView> getTableViewColumns(List<VTableColumn> tableColumnList){
        List<TableColumnView> tableColumnViews =new LinkedList<>();
        for(VTableColumn tableColumn:tableColumnList){
            tableColumnViews.add(new TableColumnView(tableColumn.getId().getCid(),tableColumn.getName().getName()));
        }
        return tableColumnViews;
    }

    public List<Map> getData(VTableName tableName){
        List<Map> tableDataList=new ArrayList<>();
        try {
            Connection conn= getvConnection().getConnection();
            Statement st=conn.createStatement();
            String sql="select * from "+tableName.getTableName();
            ResultSet rs=st.executeQuery(sql);

            ResultSetMetaData metaData=rs.getMetaData();
            int maxFiledSize = metaData.getColumnCount();

            while (rs.next()){
                Map data=new HashMap<>();
                for(int i=1;i<=maxFiledSize;i++){
                    data.put(metaData.getColumnName(i),rs.getString(i));
                }
                tableDataList.add(data);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            log.error("get table data error",e);
        }
        return tableDataList;
    }
}
