package com.winning.sx.common.dao.mysql;

import com.winning.sx.common.util.common;

import java.beans.Statement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Created by smq on 16/7/23.
 */




public class mysqlDao {
    private Connection conn = null;
    PreparedStatement statement = null;

    // connect to MySQL
    void connSQL() throws IOException {

        String server = common.GetConfigValue("mysql.server");
        String port = common.GetConfigValue("mysql.port");
        String database = common.GetConfigValue("mysql.database");//"jdbc:mysql://localhost:3306/demo?characterEncoding=UTF-8";
        String username = common.GetConfigValue("mysql.user");//"root";
        String password = common.GetConfigValue("mysql.password");//""; // 加载驱动程序以连接数据库

        String url = "jdbc:mysql://"+server+":"+port+"/"+database+"?characterEncoding=UTF-8";


        try {
            Class.forName("com.mysql.jdbc.Driver" );
            conn = DriverManager.getConnection( url,username, password );
        }
        //捕获加载驱动程序异常
        catch ( ClassNotFoundException cnfex ) {
            System.err.println(
                    "装载 JDBC/ODBC 驱动程序失败。" );
            cnfex.printStackTrace();
        }
        //捕获连接数据库异常
        catch ( SQLException sqlex ) {
            System.err.println( "无法连接数据库" );
            sqlex.printStackTrace();
        }
    }

    // disconnect to MySQL
    void deconnSQL() {
        try {
            if (conn != null)
                conn.close();
        } catch (Exception e) {
            System.out.println("关闭数据库问题 ：");
            e.printStackTrace();
        }
    }

    // execute selection language
    ResultSet selectSQL(String sql) {
        ResultSet rs = null;
        try {
            statement = conn.prepareStatement(sql);
            rs = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    //测试用例,动态传入参数
    public void runSql() throws SQLException {
        String insert = "insert into ju_users(ju_userID,TaobaoID,ju_userName,ju_userPWD) values(?,?,?,?)";
        String update = "update ju_users set ju_userPWD =123 where ju_userName= 'mm'";
        String delete = "delete from ju_users where ju_userName= 'mm'";

        PreparedStatement psql;
        ResultSet res;

        //预处理添加数据，其中有两个参数--“？”
        psql = conn.prepareStatement(insert);

        psql.setString(1, "888");              //设置参数1  ju_userID
        psql.setInt(2, 9999);      //设置参数2，  TaobaoID
        psql.setString(3, "TestName");      //设置参数3 ju_userName
        psql.setString(4, "123456");      //设置参数4  ju_userPWD

        psql.executeUpdate();           //执行Insert

        //查询修改数据后student表中的数据
        psql = conn.prepareStatement("select * from ju_users");
        res = psql.executeQuery();          //执行预处理sql语句
        System.out.println("执行增加、修改、删除后的数据");
        String ju_userID=null,ju_userName=null,ju_userPWD=null;
        while(res.next()){
            ju_userID = res.getString("ju_userID");
            ju_userName = res.getString("ju_userName");
            //ju_userName = new String(ju_userName.getBytes("ISO-8859-1"),"gb2312");
            System.out.println(ju_userID + "\t" + ju_userName);
        }

        //预处理更新（修改）数据
        psql = conn.prepareStatement("update ju_users set ju_userPWD =? where ju_userName= ?");
        psql.setString(1,"23456");       //设置参数1，ju_userPWD
        psql.setString(2,"TestName");    //设置参数2，ju_userName
        psql.executeUpdate();

        //预处理删除数据
        psql = conn.prepareStatement("delete from ju_users where ju_userName= ?");
        psql.setString(1, "TestName");
        psql.executeUpdate();

        //查询修改数据后student表中的数据
        psql = conn.prepareStatement("select * from ju_users");
        res = psql.executeQuery();          //执行预处理sql语句
        System.out.println("执行增加、修改、删除后的数据");

//        String ju_userID=null,ju_userName=null,ju_userPWD=null;
        while(res.next()){
            ju_userID = res.getString("ju_userID");
            ju_userName = res.getString("ju_userName");
            //ju_userName = new String(ju_userName.getBytes("ISO-8859-1"),"gb2312");
            System.out.println(ju_userID + "\t" + ju_userName);
        }
        res.close();
        psql.close();

    }
    // execute insertion language
    boolean insertSQL(String sql) {
        try {
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("插入数据库时出错：");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("插入时出错：");
            e.printStackTrace();
        }
        return false;
    }
    //execute delete language
    boolean deleteSQL(String sql) {
        try {
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("插入数据库时出错：");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("插入时出错：");
            e.printStackTrace();
        }
        return false;
    }
    //execute update language
    boolean updateSQL(String sql) {
        try {
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("插入数据库时出错：");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("插入时出错：");
            e.printStackTrace();
        }
        return false;
    }
    // show data in ju_users
    void layoutStyle2(ResultSet rs) {
        System.out.println("-----------------");
        System.out.println("执行结果如下所示:");
        System.out.println("-----------------");
        System.out.println(" 用户ID" + "/t/t" + "淘宝ID" + "/t/t" + "用户名"+ "/t/t" + "密码");
        System.out.println("-----------------");
        try {
            while (rs.next()) {
                System.out.println(rs.getInt("ju_userID") + "/t/t"
                        + rs.getString("taobaoID") + "/t/t"
                        + rs.getString("ju_userName")
                        + "/t/t"+ rs.getString("ju_userPWD"));
            }
        } catch (SQLException e) {
            System.out.println("显示时数据库出错。");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("显示出错。");
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {

        mysqlDao h = new mysqlDao();
        try{
            h.connSQL();
        }
        catch (Exception er){
            er.printStackTrace();
        }
        String s = "select * from ju_users";

        String insert = "insert into ju_users(ju_userID,TaobaoID,ju_userName,ju_userPWD) values("+8329+","+34243+",'mm','789')";
        String update = "update ju_users set ju_userPWD =123 where ju_userName= 'mm'";
        String delete = "delete from ju_users where ju_userName= 'mm'";

        if (h.insertSQL(insert) == true) {
            System.out.println("insert successfully");
            ResultSet resultSet = h.selectSQL(s);
            h.layoutStyle2(resultSet);
        }
        if (h.updateSQL(update) == true) {
            System.out.println("update successfully");
            ResultSet resultSet = h.selectSQL(s);
            h.layoutStyle2(resultSet);
        }
        if (h.insertSQL(delete) == true) {
            System.out.println("delete successfully");
            ResultSet resultSet = h.selectSQL(s);
            h.layoutStyle2(resultSet);
        }

        h.deconnSQL();
    }
}