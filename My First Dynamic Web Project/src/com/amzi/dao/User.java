package com.amzi.dao;

//import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class User{

	private int userId = -1;
	private String username = "";
	private String password = "";
	private String dateRegistered = "";
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(int userId, String username, String password, String dateRegistered){
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.dateRegistered = dateRegistered;
	}
	
	public void setUserId(int id){
		this.userId=id;
	}
	
	public int getUserId(){
		return userId;
	}
	
	public void setUserName(String s){
		this.username=s;
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setPassword(String p){
		this.password = p;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setDateRegistered(String d){
		this.dateRegistered = d;
	}
	
	public String getDateRegistered(){
		return dateRegistered;
	}

	public boolean changePass(String newUsername, String newPass) {
		boolean status = false;
		
		PreparedStatement pst = null;  
	    DbConnection connectionManager = null;

	    Exception Error = new Exception();
	    
	     try {  
	       
	    	newPass = newPass.trim();
	        
	    	if (userId == -1) {
	        	System.out.println("Login class was not initialized before calling changePass().\n");
	        	//errorMessege = "Error with password edit attempt. Login.userId is null.";
	        	throw Error;
	        }
	    	
	    	if(newPass == ""){
	        	System.out.println("Password was not entered, throwing java.lang.Exception.\n");
	        	//errorMessege = "Error with password edit attempt. Password was not entered.";
	        	throw Error;
	        }
	        
	        if(newUsername == ""){
	        	System.out.println("Password was not entered, throwing java.lang.Exception.\n");
	        	//errorMessege = "Error with password edit attempt. Password was not entered.";
	        	throw Error;
	        }
	        
	         //gaining access to the shared database connection
	        connectionManager = DbConnection.getInstance();
	        
	        pst = connectionManager.getConnection().prepareStatement("update user set Username=?, Password=? where userID=?"); 
	        
	        pst.setString(1, newUsername);  
	        pst.setString(2, newPass); 
	        pst.setString(3, Integer.toString(userId));  

	        //returns the row count or 0 if nothing was updated 
	        if (pst.executeUpdate() == 1){
	        	this.username = newUsername;
	        	this.password = newPass;
	        	status = true;
	        } else {
	        	status = false;        
	        	System.out.println("Password change affected multiple rows of user table.\n");
	        	//errorMessege = "Error with password edit attempt. User table may have errors.";
	        	throw Error;
	        }
	        
	    } catch (SQLException sqlE) {  
	    	
	    	connectionManager.closeConnection();
	    	
	    	System.out.println("\n");
	    	sqlE.printStackTrace();
	    	//errorMessege = "SQL Error";
	    	
	    	status = false;
	    }catch(Exception e){
	    	 e.printStackTrace(); //may not be necessary
	         status = false;
	    }
	     finally { 
	    	//we now have to manage closing the connection a different way...at logout...
	        if (pst != null) {  
	            try {  
	                pst.close();  
	            } catch (SQLException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	    }  	
		
		return status;
	}
	
	//get a list of the titles of a particular user's blogs
	public ArrayList<String> getUserBlogs(int userId) {          
        
        PreparedStatement pst = null; 
        ResultSet rs = null;
        DbConnection connectionManager = null;
        ArrayList<String> userBlogs = null;
         
        try {  
        	
        	connectionManager = DbConnection.getInstance();
        	
        	pst = connectionManager.getConnection().prepareStatement("select b.title, b.blogid from blog b, user_blog ub, user u where b.blogid = ub.blogid and u.userid = ub.userid and u.userid =? order by b.creationDateTime desc");
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
        	connectionManager.closeConnection();
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
