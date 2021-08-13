package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class upgradeFunctions {
	protected Connection conn;
	protected static final String user="u14";
	protected static final String password="u14";


	
	public void updateParam(String username, String param, int i) {
		
		try {
			String sql="UPDATE userProfiles.profiles SET "+param+" = ? WHERE username = ?;";
			PreparedStatement stmt = conn.prepareStatement(sql);
		
			stmt.setInt(1, i);
			stmt.setString(2, username);
			stmt.executeUpdate();
			conn.commit();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public int getParam(String username, String param) {
		
		int i=-1;
		try {			
			String sql = "SELECT * FROM "+"userProfiles"+".profiles where username like ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%"+username+"%");
			
			ResultSet rs = stmt.executeQuery();
			rs.next();
			i =rs.getInt(param);
			return i;
		} catch (Exception e) {		
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return i;

	}
	
	public void accesstoDB() {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn=DriverManager.getConnection("jdbc:mysql://localhost/?autoReconnect=true&serverTimezone=UTC&characterEncoding=utf8", user, password);
			conn.setAutoCommit(false);
			System.out.println("Connections was successful!");
			
		} catch (Exception e) {
			System.out.println("There has been an error!");
			e.printStackTrace();
		}
	}
}
