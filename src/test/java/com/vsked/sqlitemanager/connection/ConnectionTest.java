package com.vsked.sqlitemanager.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectionTest {

    private static final Logger log = LoggerFactory.getLogger(ConnectionTest.class);

    public String jdbcDriver="org.sqlite.JDBC";

    @Test
    public void connection(){

        try {
            Class.forName(jdbcDriver);
            //D:/tmp/sqlite/MySqliteDb1.db
            Connection conn= DriverManager.getConnection("jdbc:sqlite:myTestSqlite01.db");
            log.info("connection sqlite database success!");
            conn.close();
        } catch (Exception e) {
            log.error("connection test error",e);
        }
    }

    @Test
    public void getDatabase(){
        try {
            Class.forName(jdbcDriver);
            Connection conn= DriverManager.getConnection("jdbc:sqlite:myTestSqlite01.db");
            log.info("connection sqlite database success!");
            Statement st=conn.createStatement();
            String sql="PRAGMA database_list";
            ResultSet rs=st.executeQuery(sql);
            while (rs.next()){
                int seq=rs.getInt("seq");
                String name=rs.getString("name");
                String file=rs.getString("file");
                log.info(seq+"|"+name+"|"+file);
            }
            rs.close();
            st.close();
            conn.close();
        } catch (Exception e) {
            log.error("connection test error",e);
        }
    }

    @Test
    public void searchData(){
        try {
            Class.forName(jdbcDriver);
            Connection conn= DriverManager.getConnection("jdbc:sqlite:myTestSqlite01.db");
            log.info("connection sqlite database success!");
            Statement st=conn.createStatement();
            String sql="select ID,userName,age,password from users";
            ResultSet rs=st.executeQuery(sql);
            while (rs.next()){
                int id=rs.getInt("ID");
                String userName=rs.getString("userName");
                int age=rs.getInt("age");
                String password=rs.getString("password");
                log.info(id+"|"+userName+"|"+age+"|"+password);
            }
            rs.close();
            st.close();
            conn.close();
        } catch (Exception e) {
            log.error("connection test error",e);
        }
    }

    @Test
    public void getTableList(){
        try {
            Class.forName(jdbcDriver);
            Connection conn= DriverManager.getConnection("jdbc:sqlite:myTestSqlite01.db");
            log.info("connection sqlite database success!");
            Statement st=conn.createStatement();
            String sql="SELECT name FROM sqlite_master WHERE type='table'";
            ResultSet rs=st.executeQuery(sql);

            String tableName="";
            while (rs.next()){
                tableName=rs.getString("name");
                log.info(tableName);
            }

            rs.close();
            st.close();
            conn.close();
        } catch (Exception e) {
            log.error("connection test error",e);
        }
    }

    @Test
    public void getTableColumns(){
        try {
            Class.forName(jdbcDriver);
            Connection conn= DriverManager.getConnection("jdbc:sqlite:myTestSqlite01.db");
            log.info("connection sqlite database success!");
            Statement st=conn.createStatement();
            String sql="select * from pragma_table_info('users')";
            ResultSet rs=st.executeQuery(sql);

            while (rs.next()){
                int cid=rs.getInt("cid");
                String columnName=rs.getString("name");
                String columnType=rs.getString("type");
                int isNotNull=rs.getInt("notnull");
                String defaultValue=rs.getString("dflt_value");
                int isPrimaryKey=rs.getInt("pk");
                log.info(cid+"|"+columnName+"|"+columnType+"|"+isNotNull+"|"+defaultValue+"|"+isPrimaryKey);
            }

            rs.close();
            st.close();
            conn.close();
        } catch (Exception e) {
            log.error("connection test error",e);
        }
    }

    @Test
    public void getForeignKey(){
        //TODO get foreign key from sql
        //SELECT * FROM sqlite_master WHERE type='table' AND sql LIKE '%FOREIGN KEY%';
    }


}
