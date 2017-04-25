package com.github.lbam.dcBot.Database.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.lbam.dcBot.Database.Factory.ConFactory;
import com.github.lbam.dcBot.Database.Models.Localization;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class DaoPreferences {
	
	private static final String url = System.getenv("DBSERVER"), username = System.getenv("DBUSER"), password = System.getenv("DBPASS");
	
	public static boolean existeRegistro(String serverId){
		Statement cmd = connect();
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
			close(cmd);
		}
	}
	
	public static void createPreferences(String guild, String lang){
		Statement cmd = connect();
		try {
			cmd.executeUpdate("INSERT INTO preferences(guildId,lang)"
					+ "VALUES("+"'"+guild+"'"+","+"'"+lang+"'"+")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String getLang(String guild){
		Statement cmd = connect();
		try {
			ResultSet rs = cmd.executeQuery("SELECT * FROM preferences p WHERE p.guildId = '"+guild+"'");
			rs.next();
			return rs.getString("lang");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			close(cmd);
		}
	}
	
	public static Localization getLocal(String hash, String lang){
		Statement cmd = connect();
		try {
			ResultSet rs = cmd.executeQuery("SELECT * FROM localization l WHERE l.hash = '"+hash+"Text'"+" AND l.lang = '"+lang+"'");
			rs.next();
			Localization locale = new Localization(rs.getString("lang"),rs.getString("hash"),rs.getString("text"));
			return locale;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			close(cmd);
		}
	}
	
	public static Localization getTitle(String hash, String lang){
		Statement cmd = connect();
		try {
			ResultSet rs = cmd.executeQuery("SELECT * FROM localization l WHERE l.hash = '"+hash+"Title'"+" AND l.lang = '"+lang+"'");
			rs.next();
			Localization locale = new Localization(rs.getString("lang"),rs.getString("hash"),rs.getString("text"));
			return locale;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			close(cmd);
		}
	}
	
	public static void insertChampionsHint(){
		Statement cmd = connect();
		try {
			cmd.executeUpdate("INSERT INTO localization(lang , hash, text) SELECT 'br', c.name, c.hint FROM champions c");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Statement connect(){
		try {
			Connection con = (Connection) ConFactory.getConnection(url, username, password);
			Statement cmd = (Statement) con.createStatement();
			return cmd;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	static public void close(Statement cmd){
		try {
			cmd.getConnection().close();
			cmd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
