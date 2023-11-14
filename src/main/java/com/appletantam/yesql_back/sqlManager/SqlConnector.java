package com.appletantam.yesql_back.sqlManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;


public class SqlConnector {


    public static Connection connection = null;
    public Statement stmt = null;
    public ResultSet rs = null;



    public SqlConnector(String mysqlUrl, String user, String password) {

        try {
            connection = DriverManager.getConnection(mysqlUrl, user, password);
            stmt = connection.createStatement();

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

    }

    public void useDB(String dbName) {
        try {
            stmt.executeUpdate("USE "+dbName+";");
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public boolean executeSql(String sql) throws SQLException {

        boolean haveReturn = stmt.execute(sql);
        if(haveReturn) {
            rs = stmt.executeQuery(sql);
        }
        return haveReturn;
    }

    public void closeConnection() {
        try { connection.close(); }
        catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
}
