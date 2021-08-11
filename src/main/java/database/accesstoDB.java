package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class accesstoDB {
	protected Connection conn;
	protected static final String user="u14";
	protected static final String password="u14";


	public accesstoDB() {
		
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
	
	public void createUser(String username, String password) {
		
		int num=0;
		try {
			String sql1 = "SELECT * FROM "+"userProfiles"+".profiles where username like ?";
			PreparedStatement stmt1 = conn.prepareStatement(sql1);
			stmt1.setString(1, "%"+username+"%");		
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
			stmt.setString(1, username);
			stmt.setString(2, password);
			int rows = stmt.executeUpdate();
			conn.commit();
			if(rows>0)
				System.out.println("Profile created");
		}catch(Exception e) {
			e.printStackTrace();
		}
		}
		else System.out.println("Username is taken");
	}
	
	public boolean findProfile(String username, String password) {
		boolean isFound=false;
		try {			
			String sql = "SELECT * FROM "+"userProfiles"+".profiles where username like ? and password like ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%"+username+"%");
			stmt.setString(2, "%"+password+"%");
			
			ResultSet rs = stmt.executeQuery();
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
	
	public static void main(String[] argv) {
		accesstoDB u = new accesstoDB();
		u.createUser("n", "b");
		u.createUser("n", "b");
		u.createUser("b", "b");
		u.createUser("anna", "bumbiere");
		boolean found=u.findProfile("anna", "bumbiere");
		if(found)
			System.out.println("User found!");
		else
			System.out.println("User NOT found!");
	}
		/*-
		 * TODO #1 When new TeacherManager is created, create connection to the database server:
		 * - url = "jdbc:mysql://localhost/?autoReconnect=true&serverTimezone=UTC&characterEncoding=utf8"
		 * - user = AllTests.user
		 * - pass = AllTests.password
		 * Notes:
		 * 1. Use database name imported from jtm.testsuite.AllTests.database
		 * 2. Do not pass database name into url, because some statements in tests need to be executed
		 * server-wise, not just database-wise.
		 * 3. Set AutoCommit to false and use conn.commit() where necessary in other methods
		 */

	/**
	 * Returns a Teacher instance represented by the specified ID.
	 * 
	 * @param id the ID of teacher
	 * @return a Teacher object
	 */
	/*
	public Teacher findTeacher(int id) {	
		try {
			String sql = "SELECT * FROM "+database+".Teacher where id=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setLong(1, id);
			
			ResultSet rs = stmt.executeQuery();
			//conn.commit();
			
			rs.next();
			return new Teacher(rs.getInt(1),rs.getString(2),rs.getString(3));
				
		} catch (Exception e) {		
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		// TODO #2 Write an sql statement that searches teacher by ID.
		// If teacher is not found return Teacher object with zero or null in
		// its fields!
		// Hint: Because default database is not set in connection,
		// use full notation for table "databaseXX.Teacher"
		return new Teacher();
	}

	/*
	 * Returns a list of Teacher object that contain the specified first name and
	 * last name. This will return an empty List of no match is found.
	 * 
	 * @param firstName the first name of teacher.
	 * @param lastName  the last name of teacher.
	 * @return a list of Teacher object.
	
	public List<Teacher> findTeacher(String firstName, String lastName) {
		List<Teacher> teachers = new ArrayList<>();
		try {
			
			String sql = "SELECT * FROM "+database+".Teacher where firstname like ? and lastname like ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%"+firstName+"%");
			stmt.setString(2, "%"+lastName+"%");
			
			ResultSet rs = stmt.executeQuery();
			//conn.commit();
			while(rs.next()) {
				teachers.add(new Teacher(rs.getInt(1),rs.getString(2),rs.getString(3)));
			}
			return teachers;
				
		} catch (Exception e) {		
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		// TODO #2 Write an sql statement that searches teacher by ID.
		// If teacher is not found return Teacher object with zero or null in
		// its fields!
		// Hint: Because default database is not set in connection,
		// use full notation for table "databaseXX.Teacher"
		return new ArrayList<>();

	}

	/**
	 * Insert an new teacher (first name and last name) into the repository.
	 * 
	 * @param firstName the first name of teacher
	 * @param lastName  the last name of teacher
	 * @return true if success, else false.
	 

	public boolean insertTeacher(String firstName, String lastName) {
		boolean status = false;
		try {
			String sql="INSERT INTO "+database+".Teacher (firstname, lastname) VALUES(?, ?);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, firstName);
			stmt.setString(2, lastName);
			int rows = stmt.executeUpdate();
			conn.commit();
			if(rows>0)
				status=true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Insert teacher object into database
	 * 
	 * @param teacher
	 * @return true on success, false on error (e.g. non-unique id)
	 
	public boolean insertTeacher(Teacher teacher) {
		boolean status = false;
		try {
			String sql="INSERT INTO "+database+".Teacher VALUES(?, ?, ?);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, teacher.getId());
			stmt.setString(2, teacher.getFirstName());
			stmt.setString(3, teacher.getLastName());
			int rows = stmt.executeUpdate();
			conn.commit();
			if(rows>0)
				status=true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Updates an existing Teacher in the repository with the values represented by
	 * the Teacher object.
	 * 
	 * @param teacher a Teacher object, which contain information for updating.
	 * @return true if row was updated.
	 
	public boolean updateTeacher(Teacher teacher) {
		boolean status = false;
		try {
			String sql="UPDATE "+database+".Teacher SET firstname = ?, lastname = ? WHERE id = ?;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(3, teacher.getId());
			stmt.setString(1, teacher.getFirstName());
			stmt.setString(2, teacher.getLastName());
			int rows = stmt.executeUpdate();
			conn.commit();
			if(rows>0)
				status=true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Delete an existing Teacher in the repository with the values represented by
	 * the ID.
	 * 
	 * @param id the ID of teacher.
	 * @return true if row was deleted.
	 
	public boolean deleteTeacher(int id) {
		boolean status = false;
		try {
			String sql="DELETE FROM "+database+".Teacher WHERE id = ?;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			//stmt.setString(1, teacher.getFirstName());
			//stmt.setString(2, teacher.getLastName());
			int rows = stmt.executeUpdate();
			conn.commit();
			if(rows>0)
				status=true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	public void closeConnecion() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		*/
	}
