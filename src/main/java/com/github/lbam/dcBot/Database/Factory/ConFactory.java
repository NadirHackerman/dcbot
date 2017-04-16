package com.github.lbam.dcBot.Database.Factory;
import java.sql.Connection;  
import java.sql.DriverManager;  

public class ConFactory {
	public static Connection getConnection(String url, String username, String password) throws Exception {
		Class.forName("com.mysql.jdbc.Driver"); 
		return DriverManager.getConnection(url, username, password);
	}
}
