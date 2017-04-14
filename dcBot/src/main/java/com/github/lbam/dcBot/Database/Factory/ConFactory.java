package com.github.lbam.dcBot.Database.Factory;
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.SQLException; 

public class ConFactory {
	public static Connection getConnection(String url, String username, String password) {
		try {
			return DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			//Não foi possível se conectar ao banco de dados.
		}
		return null;
	}
}
