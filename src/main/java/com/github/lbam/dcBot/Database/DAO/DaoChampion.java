package com.github.lbam.dcBot.Database.DAO;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.github.lbam.dcBot.Database.Factory.ConFactory;
import com.github.lbam.dcBot.Database.Models.Champion;

public class DaoChampion {
	
	private Connection con;
	
	public Champion getRandomChampion(String id) {
		connect();
		ResultSet rs = null;
		PreparedStatement cmd = null;
		try {
			cmd =  con.prepareStatement("SELECT * "
							+ "FROM champions "
							+ "WHERE id NOT IN"
							+ "(SELECT idChampion "
							+ "FROM progresso "
							+ "WHERE status = 1 "
							+ "AND idPlayer = ?) ORDER BY RAND() LIMIT 1");
			cmd.setString(1, id);
			
			rs = cmd.executeQuery();
			rs.next();
			Champion c = new Champion(
					rs.getInt("id"), rs.getString("name"), 
					rs.getString("representation"));
			
			return c;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally {
			close(rs, cmd);
		}
	}
	
	public int getMaxChampion() {
		connect();
		ResultSet rs = null;
		PreparedStatement cmd = null;
		try {
			cmd = con.prepareStatement("SELECT "
					+ "MAX(id) AS total "
					+ "FROM champions");
			rs = cmd.executeQuery();
			rs.next();
			return rs.getInt("total")+1;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}finally {
			close(rs, cmd);
		}
	}
	
	public void registerIncorrectGuess(String idPlayer, int idChampion, int usedHint) {
		connect();
		PreparedStatement cmd = null;
		try {
			cmd = con.prepareStatement("INSERT INTO progresso(idPlayer, idChampion, status, hint) "
					+ "VALUES(?,?,?,?)");
			cmd.setString(1, idPlayer);
			cmd.setInt(2, idChampion);
			cmd.setInt(3, 0);
			cmd.setInt(4, usedHint);
			cmd.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(null, cmd);
		}
	}
	
	public void registerCorrectAnswer(String idPlayer, int idChampion, int usedHint) {
		connect();
		PreparedStatement cmd = null;
		try {
			cmd = con.prepareStatement("INSERT INTO progresso(idPlayer, idChampion, status, hint) "
					+ "VALUES(?,?,?,?)");
			cmd.setString(1, idPlayer);
			cmd.setInt(2, idChampion);
			cmd.setInt(3, 1);
			cmd.setInt(4, usedHint);
			cmd.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(null, cmd);
		}
	}
	
	public int getUsedHints(String playerId) {
		connect();
		ResultSet rs = null;
		PreparedStatement cmd = null;
		try {
			cmd = con.prepareStatement("SELECT COUNT(DISTINCT p.idChampion) as total "
					+ "FROM progresso p "
					+ "WHERE p.idPlayer = ? AND p.hint = 1");
			cmd.setString(1, playerId);
			rs = cmd.executeQuery();
			rs.next();
			return rs.getInt("total");
		}catch(SQLException e) {
			e.printStackTrace();
			return 0;
		}finally {
			close(rs, cmd);
		}
	}
	
	public Champion getChampionById(int id){
		connect();
		ResultSet rs = null;
		PreparedStatement cmd = null;
		try {
			cmd = con.prepareStatement("SELECT * FROM champions WHERE id = ?");
			cmd.setInt(1, id);
			rs = cmd.executeQuery();
			rs.next();
			return new Champion(rs.getInt("id"), rs.getString("name"), 
					rs.getString("representation"));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		finally{
			close(rs, cmd);
		}
		
	}
	
	public void connect() {
		try {
			con = ConFactory.connection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close(ResultSet rs, PreparedStatement cmd) {
		try {
			con.close();
			rs.close();
			cmd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
