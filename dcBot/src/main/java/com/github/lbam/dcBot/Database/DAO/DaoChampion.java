package com.github.lbam.dcBot.Database.DAO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class DaoChampion {
	
	private String url, username, password;
	private Properties prop;
	
	private Connection con;
	private Statement cmd;
	
	public DaoChampion() {
		prop = new Properties();
    	try {
			prop.load(new FileInputStream("config.properties"));
			url = prop.getProperty("database");
			username = prop.getProperty("dbuser");
			password = prop.getProperty("dbpassword");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
