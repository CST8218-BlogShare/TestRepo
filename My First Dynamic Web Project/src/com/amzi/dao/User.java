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
		
		try {
			pst = connectionManager.getConnection().prepareStatement("select * from user where username=? and password=?");
			pst.setString(1, username);  
		    pst.setString(2, password);  

		    rs = pst.executeQuery(); 
		    rs.first();
		    
		    u = new User(rs.getInt("userId"), rs.getString("username"), rs.getString("password"), rs.getString("DateRegistered"));
		    
		} catch (SQLException sqlE) {
			sqlE.printStackTrace();
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
	    	 System.out.println("Error with User Retrieval: Unable to retrieve User information based on UserId");
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
	
	//Called when the user completes registration
	public static int insertUserIntoDatabase(String username, String password){
		 PreparedStatement pst = null;
		 DbConnection connectionManager = null;
		 
	     connectionManager = DbConnection.getInstance();
	     
	     if(connectionManager.getConnection() == null){
	    	 return -1;
	     }
		
		 try {
			pst = connectionManager.getConnection().prepareStatement("insert into User values(0, ?,?, curdate() );");
			pst.setString(1,username);
	        pst.setString(2, password);
	        pst.executeUpdate(); 
	        pst.close();
		} catch (MySQLIntegrityConstraintViolationException sqlCE) {
			sqlCE.printStackTrace();
			return -2;
		} catch ( SQLException sqlE){
			sqlE.printStackTrace();
			return -3;
		}
		 finally{
			try {
				pst.close();
			} catch (SQLException sqlE) {
				sqlE.printStackTrace();
			}
		}
		 return 0;
	}
	
	public static boolean updateUserCredentialsInDatabase(String newUsername, String newPass, int userId) {
		PreparedStatement pst = null;  
	    DbConnection connectionManager = null;
	    
	     try {  
	       
	    	newPass = newPass.trim();
	        newUsername = newUsername.trim();
	    	
	    	if(newPass == ""){
	        	System.out.println("Password was not entered, throwing java.lang.Exception.\n");
	        	//errorMessege = "Error with password edit attempt. Password was not entered.";
	        	return false;
	        }
	        
	        if(newUsername == ""){
	        	System.out.println("Password was not entered, throwing java.lang.Exception.\n");
	        	//errorMessege = "Error with password edit attempt. Password was not entered.";
	        	return false;
	        }
	        
	        //gaining access to the shared database connection
	        connectionManager = DbConnection.getInstance();
	        
	        if(connectionManager.getConnection() == null){
	        	System.out.println("");
	        }
	        
	        pst = connectionManager.getConnection().prepareStatement("update user set Username=?, Password=? where userID=?"); 
	        
	        pst.setString(1, newUsername);  
	        pst.setString(2, newPass); 
	        pst.setString(3, Integer.toString(userId));  

	        //returns the row count or 0 if nothing was updated 
	        if (pst.executeUpdate() > 1){
	        	System.out.println("Password change affected multiple rows of user table.\n");
	        	//need to rollBack
	        	return false;
	        }
	        
	    } catch (SQLException sqlE) {  
	    	System.out.println("\n");
	    	sqlE.printStackTrace();
	    	return false;
	    }
	     finally {   
	    	 try {  
	    		 pst.close();  
	         } catch (SQLException e) {  
	        	 e.printStackTrace();  
	         }  
	    }  
		return true;
	}
	
	//get a list of the titles of a particular user's blogs
	public ArrayList<String> getUserBlogs(int userId) {          
        
        PreparedStatement pst = null; 
        ResultSet rs = null;
        DbConnection connectionManager = null;
        ArrayList<String> userBlogs = null;
         
        try {  
        	
        	connectionManager = DbConnection.getInstance();
        	
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
        	
        } catch (SQLException sqlE) {  
        	
        	sqlE.printStackTrace();
        	//connectionManager.closeConnection();
        	userBlogs = null;
        	
        }catch(Exception e){
        	
        	 e.printStackTrace();
        	 userBlogs = null;
             
        }
         finally { 

            if (pst != null) {  
                try {  
                    pst.close();  
                } catch (SQLException e) {  
                    e.printStackTrace();  
                }  
            }  
            if (rs != null) {  
                try {  
                    rs.close();  
                } catch (SQLException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        
        return userBlogs;  
        
    }
}
