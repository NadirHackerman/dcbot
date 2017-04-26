package com.github.lbam.dcBot.Database.Factory;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
 
public class ConFactory {
 
    private static ConFactory connect = new ConFactory();
    public static final String username = System.getenv("DBUSER");
    public static final String password = System.getenv("DBPASS");
    public static final String url = System.getenv("DBSERVER");
 
 
    private ConFactory(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static Connection createConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(username, password, url);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return con;
    }
 
    public static Connection connection(){
        return connect.createConnection();
    }
}