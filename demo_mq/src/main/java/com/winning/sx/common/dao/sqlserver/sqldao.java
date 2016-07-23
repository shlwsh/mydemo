package com.winning.sx.common.dao.sqlserver;

/**
 * Created by smq on 16/6/28.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;

/**
 * @author Bob
 *
 */
public class sqldao {


    /**
     * @param args
     */
    public   void sqlRun() {
        // TODO Auto-generated method stub
        // Create a variable for the connection string.
        String connectionUrl = "jdbc:sqlserver://192.168.160.122:1433;databaseName=WinningMDM;integratedSecurity=false;user=sa;password=;";

        // Declare the JDBC objects.
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Establish the connection.
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(connectionUrl);

            // Create and execute an SQL statement that returns some data.
            String SQL = "select top 10 cdecode,cdename from DE_DataElement";
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL);

            // Iterate through the data in the result set and display it.
            while (rs.next()) {

                System.out.println("【SUCC】获取 SQL-SERVER 的连接已成功。[code]="+rs.getString(1)+";name:"+
                        rs.getString(2));
            }
        }

        // Handle any errors that may have occurred.
        catch (Exception e) {
            System.out.println("【ERROR】打开 SQL-SERVER 的连接失败。");
            e.printStackTrace();
        }

        finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (Exception e) {
                }
            if (stmt != null)
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            if (con != null)
                try {
                    con.close();
                } catch (Exception e) {
                }
        }
    }
}