package com.github.lbam.dcBot.Database.DAO;

import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.github.lbam.dcBot.Database.Factory.ConFactory;
import com.github.lbam.dcBot.Database.Models.Localization;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class DaoPreferences {
	
	private String url, username, password;
	private Properties prop;
	
	private Statement cmd;
	private Connection con;
	
	public DaoPreferences(){
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
	
	public boolean existeRegistro(String serverId){
		connect();
		try {
			ResultSet rs = cmd.executeQuery("SELECT COUNT(*) as total FROM preferences WHERE guildId = " + serverId);
			rs.next();
			if(rs.getInt("total") > 0){
				return true;
			}else{
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			close();
		}
	}
	
	public void createPreferences(String guild, String lang){
		connect();
		try {
			cmd.executeUpdate("INSERT INTO preferences(guildId,lang)"
					+ "VALUES("+"'"+guild+"'"+","+"'"+lang+"'"+")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Localization getText(String hash, String lang){
		connect();
		try {
			ResultSet rs = cmd.executeQuery("SELECT * FROM localization l WHERE l.hash = '"+hash+"'"+" AND l.lang = '"+lang+"'");
			rs.next();
			Localization locale = new Localization(rs.getString("lang"),rs.getString("hash"),rs.getString("text"));
			return locale;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			close();
		}
	}
	
	public void insertChampionsHint(){
		connect();
		try {
			cmd.executeUpdate("INSERT INTO localization(lang , hash, text) SELECT 'br', c.name, c.hint FROM champions c");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void connect(){
		try {
			con = (Connection) ConFactory.getConnection(url, username, password);
			cmd = (Statement) con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close(){
		try {
			con.close();
			cmd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
