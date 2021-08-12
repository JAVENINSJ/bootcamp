package passwords;

import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Password {

	protected static Connection conn;
	protected static final String user="u14";
	protected static final String password="u14";
	
	public static void getLoginFile() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn=DriverManager.getConnection("jdbc:mysql://localhost/?autoReconnect=true&serverTimezone=UTC&characterEncoding=utf8", user, password);
			conn.setAutoCommit(false);
			//System.out.println("Connections was successful!");
			
		} catch (Exception e) {
			System.out.println("There has been an error!");
			e.printStackTrace();
		}
	}

	public static boolean checkUser(String user, String password) {
		
		boolean isFound=false;
		try {			
			String sql = "SELECT * FROM "+"userProfiles"+".profiles where username like ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%"+user+"%");
			
			ResultSet rs = stmt.executeQuery();
			rs.next();
			
			if(rs!=null)
				isFound = true;
			return isFound;	
		} catch (Exception e) {		
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return isFound;
	}
	
	public static boolean checkPassword(String user, String password) {
		/*
		String encryptedPassword = encrypt(password,(String) ((JSONObject) json.get(user)).get("salt"));
		encryptedPassword = encryptedPassword.substring(encryptedPassword.indexOf(':') + 1);
		
		if (encryptedPassword.equals(((JSONObject) json.get(user)).get("pass"))) {
			return true;
		}
		return false;
		*/
		boolean isFound=false;
		try {			
			String sql = "SELECT * FROM "+"userProfiles"+".profiles where username like ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%"+user+"%");
			
			ResultSet rs = stmt.executeQuery();
			rs.next();
			String pswd_db=rs.getString("password");
			
			String encryptedPassword = encrypt(password,pswd_db.substring(0,pswd_db.indexOf(':')));
			encryptedPassword = encryptedPassword.substring(encryptedPassword.indexOf(':') + 1);
			
			if(encryptedPassword.equals(pswd_db.substring(pswd_db.indexOf(':')+1)))
				isFound = true;
			return isFound;	
		} catch (Exception e) {		
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return isFound;
	}
	
	
	private static String saltMine() {
		Random rand = new Random();
		int temp1 = rand.nextInt();
		return new String(Integer.toHexString(temp1));
	}

	private static String encrypt(String password, String salt) {
		Random rand = new Random(password.hashCode() + salt.hashCode());
		int temp1 = rand.nextInt();

		String hex = Integer.toHexString(temp1);

		return salt + ":" + hex;
	}

	@SuppressWarnings("unchecked")
	public static boolean addPassword(String user, String password) {		
		
		boolean created=false;
		int num=0;
		try {
			String sql1 = "SELECT * FROM "+"userProfiles"+".profiles where username like ?";
			PreparedStatement stmt1 = conn.prepareStatement(sql1);
			stmt1.setString(1, "%"+user+"%");		
			ResultSet rs = stmt1.executeQuery();
			//conn.commit();
			while(rs.next()) {
				num++;
			}				
		} catch (Exception e) {	
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		if(num==0) {
		try {
			String sql="INSERT INTO "+"userProfiles"+".profiles (username, password) VALUES(?, ?);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, user);
			String salt = saltMine();
			stmt.setString(2, encrypt(password,salt));
			int rows = stmt.executeUpdate();
			conn.commit();
			created=true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		}
		return created;
	}

}
