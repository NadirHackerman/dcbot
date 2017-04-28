package com.github.lbam.dcBot.Database.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.lbam.dcBot.Database.Factory.ConFactory;

public class DaoPlayer {
	private static Connection con;
	
	public static int getProgress(String id) {
		connect();
		ResultSet rs = null;
		PreparedStatement cmd = null;
		try {
			cmd = con.prepareStatement("SELECT COUNT(*) AS total FROM progresso "
					+ "WHERE idPlayer = ? AND p.status = 1");
			cmd.setString(1, id);
			rs = cmd.executeQuery();
			rs.next();
			return rs.getInt("total");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(rs, cmd);
		}
		return 0;
	}
	
	public static int getTries(String id) {
		connect();
		ResultSet rs = null;
		PreparedStatement cmd = null;
		try {
			cmd = con.prepareStatement("SELECT COUNT(*) AS total "
					+ "FROM progresso "
					+ "WHERE idPlayer = ?");
			rs = cmd.executeQuery();
			rs.next();
			return rs.getInt("total");
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}finally {
			close(rs, cmd);
		}
	}
	
	public static void connect(){
		try {
			con = ConFactory.connection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void close(ResultSet rs, PreparedStatement cmd){
		try {
			con.close();
			rs.close();
			cmd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
