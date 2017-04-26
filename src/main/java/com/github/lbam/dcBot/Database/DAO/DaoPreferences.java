package com.github.lbam.dcBot.Database.DAO;


import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.lbam.dcBot.Database.Factory.ConFactory;
import com.github.lbam.dcBot.Database.Models.Localization;

public class DaoPreferences {
	
	private static Statement cmd;
	private static Connection con;
	
	public static boolean existeRegistro(String serverId){
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
	
	public static void createPreferences(String guild, String lang){
		connect();
		try {
			cmd.executeUpdate("INSERT INTO preferences(guildId,lang)"
					+ "VALUES("+"'"+guild+"'"+","+"'"+lang+"'"+")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String getLang(String guild){
		connect();
		try {
			ResultSet rs = cmd.executeQuery("SELECT * FROM preferences p WHERE p.guildId = '"+guild+"'");
			rs.next();
			return rs.getString("lang");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			close();
		}
	}
	
	public static Localization getLocal(String hash, String lang){
		connect();
		try {
			ResultSet rs = cmd.executeQuery("SELECT * FROM localization l WHERE l.hash = '"+hash+"Text'"+" AND l.lang = '"+lang+"'");
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
	
	public static Localization getTitle(String hash, String lang){
		connect();
		try {
			ResultSet rs = cmd.executeQuery("SELECT * FROM localization l WHERE l.hash = '"+hash+"Title'"+" AND l.lang = '"+lang+"'");
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
	
	public static void connect(){
		try {
			con = ConFactory.connection();
			cmd = con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void close(){
		try {
			con.close();
			cmd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
