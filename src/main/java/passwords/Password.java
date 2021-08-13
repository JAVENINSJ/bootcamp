package passwords;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Random;

public class Password {

	protected static Connection conn;
	protected static final String user="sql11430725";
	protected static final String password="pSUEsjIpnb";
	
	public static void getLoginFile() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn=DriverManager.getConnection("jdbc:mysql://sql11.freemysqlhosting.net/?autoReconnect=true&serverTimezone=UTC&characterEncoding=utf8", user, password);
			conn.setAutoCommit(false);
			
		} catch (Exception e) {
			//System.out.println("There has been an error!");
			e.printStackTrace();
		}
	}

	public static boolean checkUser(String user, String password) {
		
		boolean isFound=false;
		try {			
			String sql = "SELECT * FROM "+"sql11430725"+".profiles where username like ?";
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
		boolean isFound=false;
		try {			
			String sql = "SELECT * FROM "+"sql11430725"+".profiles where username like ?";
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
			String sql1 = "SELECT * FROM "+"sql11430725"+".profiles where username like ?";
			PreparedStatement stmt1 = conn.prepareStatement(sql1);
			stmt1.setString(1, "%"+user+"%");		
			ResultSet rs = stmt1.executeQuery();
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
			String sql="INSERT INTO "+"sql11430725"+".profiles (username, password) VALUES(?, ?);";
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
/*
package passwords;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Password {

	public static JSONObject json = null;
	
	public static void getLoginFile() {
		try (FileReader reader = new FileReader("pass.json")) {
			JSONParser jsonParser = new JSONParser();
			json = (JSONObject) jsonParser.parse(reader);
		} catch (Exception e) {
			System.err.println("Pass.json file not found!");
			e.printStackTrace();
		}
	}

	public static boolean checkPassword(String user, String password) {
		String encryptedPassword = encrypt(password,(String) ((JSONObject) json.get(user)).get("salt"));
		encryptedPassword = encryptedPassword.substring(encryptedPassword.indexOf(':') + 1);
		
		if (encryptedPassword.equals(((JSONObject) json.get(user)).get("pass"))) {
			return true;
		}
		return false;
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
	public static void addPassword(String user, String password) {
		if (json == null) return;
		if (json.containsKey(user))	json.remove(user);
		Map<String, String> map = new HashMap<>();
		String salt = saltMine();
		String pass = encrypt(password, salt);
		map.put("salt", salt);
		map.put("pass", pass.substring(pass.indexOf(':') + 1));

		json.put(user, new JSONObject(map));

		writeToFile();
	}

	private static void writeToFile() {
		try (FileWriter fileWriter = new FileWriter("pass.json")) {

			fileWriter.write(json.toJSONString());
			fileWriter.flush();

		} catch (Exception e) {
			System.err.println("pass.json file not found!");
			e.printStackTrace();
		}
	}

	public static void removeAcc(String user) {
		if (json == null || json.get(user) == null)
			return;
		json.remove(user);
		writeToFile();
	}
}
*/
