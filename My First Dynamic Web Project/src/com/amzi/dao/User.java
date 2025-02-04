package com.amzi.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class User{

	private int userId = -1;
	private String username = "";
	private String password = "";
	private String dateRegistered = "";
	
	public User() {
		
	}
		
	public User(int userId, String username, String password, String dateRegistered){
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.dateRegistered = dateRegistered;
	}
	
	protected void setUserId(int id){
		this.userId=id;
	}
	
	public int getUserId(){
		return userId;
	}
	
	protected void setUserName(String s){
		this.username=s;
	}
	
	public String getUsername(){
		return username;
	}
	
	protected void setPassword(String p){
		this.password = p;
	}
	
	public String getPassword(){
		return password;
	}
	
	protected void setDateRegistered(String d){
		this.dateRegistered = d;
	}
	
	public String getDateRegistered(){
		return dateRegistered;
	}

	public static User getUserFromDatabaseByCredentials(String username, String password){
		User u = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		DbConnection connectionManager = null;
	    
		connectionManager = DbConnection.getInstance();
		
		if(DbConnection.testConnection(connectionManager) == false){
			System.out.println("Error with User retrieval by credentials: Unable to establish connection with database.");
			return null;
		}
			
		try {
			pst = connectionManager.getConnection().prepareStatement("select * from user where username=? and password=?");
			pst.setString(1, username);  
		    pst.setString(2, password);  

		    rs = pst.executeQuery(); 
		    rs.first();
		    
		    u = new User(rs.getInt("userId"), rs.getString("username"), rs.getString("password"), rs.getString("DateRegistered"));
		    
		} catch (SQLException sqlE){
			System.out.println("Error with User retrieval by credentials: Unable to find matching user for given credentials.");
			sqlE.printStackTrace();
			return null;
		} finally{
			try {
				rs.close();
				pst.close();
			} catch (SQLException sqlE) {
				sqlE.printStackTrace();
			}
			
		}
		return u;
	}
	
	public static User getUserFromDatabaseById(int userId){
		 User u = null;
		 PreparedStatement pst = null; 
	     ResultSet rs = null;
	     DbConnection connectionManager = null;
	     
	     connectionManager = DbConnection.getInstance();
	     
	     if(DbConnection.testConnection(connectionManager) == false){
	    	 	System.out.println("Error with User retrieval by userId: Unable to establish connection with database.");
				return null;
		 }
	       
	     try {
	    	 pst = connectionManager.getConnection().prepareStatement("select username, dateRegistered from user where userid = ?");
	    	 pst.setInt(1, userId);
			 rs = pst.executeQuery();
			 rs.first();
		    
		     u = new User();
		     u.username = rs.getString("username");
		     u.dateRegistered = rs.getString("dateRegistered");
		     u.userId = userId;
		    
		     rs.close();
		     pst.close();
		    
	     } catch (SQLException sqlE) {
	    	 System.out.println("Error with User Retrieval by userid: Unable to retrieve User information based on UserId");
	    	 sqlE.printStackTrace();
			 return null;
	     }finally { 
	    	 try {  
	    		 rs.close();
	    		 pst.close();  
	         } catch (SQLException sqlE) {  
	        	 sqlE.printStackTrace();  
	         }  
	     }   
	     return u;
	}
	
	public static int getUserIdFromDatabaseByUsername(String username){
		int userId = 0;
		 PreparedStatement pst = null; 
	     ResultSet rs = null;
	     DbConnection connectionManager = null;
	     
	     connectionManager = DbConnection.getInstance();
	     
	     if(DbConnection.testConnection(connectionManager) == false){
	    	 	System.out.println("Error with User retrieval by userId: Unable to establish connection with database.");
				return -1;
		 }
	       
	     try {
	    	 pst = connectionManager.getConnection().prepareStatement("select userid from user where username = ?");
	    	 pst.setString(1, username);
			 rs = pst.executeQuery();
			 rs.first();

		     userId = rs.getInt("userId");
		    
		     rs.close();
		     pst.close();
		    
	     } catch (SQLException sqlE) {
	    	 System.out.println("Error with User Retrieval by userid: Unable to retrieve User information based on UserId");
	    	 sqlE.printStackTrace();
			 return -2;
	     }finally { 
	    	 try {  
	    		 rs.close();
	    		 pst.close();  
	         } catch (SQLException sqlE) {  
	        	 sqlE.printStackTrace();  
	         }  
	     }   
	     return userId;
	}
	
	//Called when the user completes registration
	public static int insertUserIntoDatabase(String username, String password){
		 PreparedStatement pst = null;
		 DbConnection connectionManager = null;
		 
	     connectionManager = DbConnection.getInstance();
	     
	     if(DbConnection.testConnection(connectionManager) == false){
	    	 	System.out.println("Error with insertion of user into database: Unable to establish connection with database.");
				return -1;
		 }
	     
		 try {
			pst = connectionManager.getConnection().prepareStatement("insert into User (userId, username, password, dateRegistered)  values(0, ?,?, curdate())");
			pst.setString(1,username);
	        pst.setString(2, password);
	        pst.executeUpdate(); 
	        pst.close();
		 }catch (MySQLIntegrityConstraintViolationException sqlCE) {
			System.out.println("Error with insertion of user into database: User with same name already exists.");
			sqlCE.printStackTrace();
			return -2;
		 }catch ( SQLException sqlE){
			System.out.println("Error with insertion of user into database: SQL error.");
			sqlE.printStackTrace();
			return -3;
		 }finally{
		 	try {
				pst.close();
			} catch (SQLException sqlE) {
				sqlE.printStackTrace();
			}
		 }
		 return 0;
	}
	
	public static int updateUserCredentialsInDatabase(String newUsername, String newPass, int userId) {
		PreparedStatement pst = null;  
	    DbConnection connectionManager = null;
	       	    	
	    //gaining access to the shared database connection
	    connectionManager = DbConnection.getInstance();
	    
	    if(DbConnection.testConnection(connectionManager) == false){
	    	System.out.println("Error with update of user credentials within database: Unable to establish connection with database.");
			return -1;
	    }
	    
	    try{  
	    	 
	        pst = connectionManager.getConnection().prepareStatement("update user set Username=?, Password=? where userID=?"); 
	        
	        pst.setString(1, newUsername);  
	        pst.setString(2, newPass); 
	        pst.setString(3, Integer.toString(userId));  

	        //returns the row count or 0 if nothing was updated 
	        if (pst.executeUpdate() > 1){
	        	//System.out.println("Password change affected multiple rows of user table.\n");
	        	//need to rollBack
	        }
	        
	    } catch (SQLException sqlE) {  
	    	System.out.println("Error with update of user credentials within database: SQL error.");
	    	sqlE.printStackTrace();
	    	return -2;
	    }
	     finally {   
	    	 try {  
	    		 pst.close();  
	         } catch (SQLException e) {  
	        	 e.printStackTrace();  
	         }  
	    }  
		return 0;
	}
	
	//get a list of the titles of a particular user's blogs
	public ArrayList<String> getUserBlogs(int userId) {          
        
        PreparedStatement pst = null; 
        ResultSet rs = null;
        DbConnection connectionManager = null;
        ArrayList<String> userBlogs = null;
         
        connectionManager = DbConnection.getInstance();
        
        if(DbConnection.testConnection(connectionManager) == false){
        	System.out.println("Error with retrieval of list of blogs by user id: Unable to establish connection with database.");
			return null;
	    }
        	
        try { 
        	
        	pst = connectionManager.getConnection().prepareStatement("select b.title, b.blogid from blog b, user_blog ub, user u"
        													       + " where b.blogid = ub.blogid "
        													       + "and u.userid = ub.userid "
        													       + "and u.userid =? "
        													       + "order by b.creationDateTime desc");
        	pst.setString(1, Integer.toString(userId));
        	rs = pst.executeQuery();
        	
        	if(rs.next()){

	        	rs.beforeFirst();
	        	userBlogs = new ArrayList<String>();
	        	
	        	while (rs.next()){	
	        		userBlogs.add(rs.getString("title"));
	        	}
	        	    	
        	}
        	rs.close();
        	pst.close();
        	
        } catch (SQLException sqlE){
        	System.out.println("Error with retrieval of list of blogs by user id: SQL error.");
        	sqlE.printStackTrace();
        	return null;	
        }finally { 
        	try {  
        		rs.close();
                pst.close();  
              } catch (SQLException e) {  
                e.printStackTrace();  
              }  
        }  
        return userBlogs;  
        
    }
}
