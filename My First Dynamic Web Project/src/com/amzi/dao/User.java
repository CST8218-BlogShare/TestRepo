package com.amzi.dao;

//import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class User {

	private int userId = -1;
	private String username = null;
	private String password = null;
	private String dateRegistered = null;
	
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
	
public ArrayList<String[]> getUserBlogs(int userId) {          
        
        PreparedStatement pst = null; 
        ResultSet rs = null;
        DbConnection connectionManager = null;
        ArrayList<String[]> userBlogs = new ArrayList<String[]>();
         
        try {  
        	
        	connectionManager = DbConnection.getInstance();
        	
        	pst = connectionManager.getConnection().prepareStatement("select b.title, b.blogid from blog b, user_blog ub, user u where b.blogid = ub.blogid and u.userid = ub.userid and u.userid =?");
        	pst.setString(1, Integer.toString(userId));
        	rs = pst.executeQuery();
        	
        	if (rs.next()){

	        	rs.beforeFirst();
	        	
	        	while (rs.next()){	
	        		userBlogs.add(
	        				new String[]{rs.getString("blogid"), rs.getString("title")});
	        	}
        	}
        	rs.close();
        	pst.close();
        	
        } catch (SQLException sqlE) {  
        	
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
