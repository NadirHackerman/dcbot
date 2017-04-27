package com.github.lbam.dcBot.Database.DAO;


import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.github.lbam.dcBot.Database.Factory.ConFactory;
import com.github.lbam.dcBot.Database.Models.Localization;

public class DaoPreferences {
	
	private static Connection con;
	
	public static boolean existeRegistro(String serverId){
		connect();
		try {
			PreparedStatement cmdp = con.prepareStatement("SELECT COUNT(*) as total FROM preferences WHERE guildId = ?");
			cmdp.setString(1, serverId);
			ResultSet rs = cmdp.executeQuery();
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
			PreparedStatement cmdp = con.prepareStatement("INSERT INTO preferences(guildId,lang) VALUES(?,?)");
			cmdp.setString(1, guild);
			cmdp.setString(2, lang);
			cmdp.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String getLang(String guild){
		connect();
		try {
			PreparedStatement cmdp = con.prepareStatement("SELECT * FROM preferences WHERE guildId = ?");
			cmdp.setString(1, guild);
			ResultSet rs = cmdp.executeQuery();
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
			PreparedStatement cmdp = con.prepareStatement("SELECT * FROM localization WHERE hash = ? AND lang = ?");
			cmdp.setString(1, hash);
			cmdp.setString(2, lang);
			ResultSet rs = cmdp.executeQuery();
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
			PreparedStatement cmdp = con.prepareStatement("SELECT * FROM localization WHERE hash = ? AND lang = ?");
			cmdp.setString(1, hash+"Text");
			cmdp.setString(2, lang);
			ResultSet rs = cmdp.executeQuery();
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
	
	public static void connect(){
		try {
			con = ConFactory.connection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void close(){
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
