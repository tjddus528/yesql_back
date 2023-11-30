package com.appletantam.yesql_back.sqlManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;


public class SqlConnector {


    public static Connection connection = null;
    public Statement stmt = null;
    public ResultSet rs = null;



    public SqlConnector(String mysqlUrl, String user, String password) throws SQLException{
        connection = DriverManager.getConnection(mysqlUrl, user, password);
        stmt = connection.createStatement();
    }

    public void createDB(String dbName) throws SQLException {
        stmt.executeUpdate("CREATE DATABASE "+dbName);
    }

    public void useDB(String dbName) throws SQLException{
        stmt.executeUpdate("USE "+dbName+";");
    }

    public boolean executeSql(String sql) throws SQLException {

        boolean haveReturn = stmt.execute(sql);
        if(haveReturn) {
            rs = stmt.executeQuery(sql);
        }
        return haveReturn;
    }

    public void closeConnection() throws SQLException{
        connection.close();
    }
}
