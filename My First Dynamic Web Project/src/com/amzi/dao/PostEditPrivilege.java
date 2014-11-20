package com.amzi.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.amzi.dao.DbConnection;

public class PostEditPrivilege {

	private int postEditPrivilegeId = -1;
	
	public PostEditPrivilege() {
		
	}
	
	public int getPostEditPrivilegeId(){
		return postEditPrivilegeId;
	}
	
	public static int insertPostEditPrivilegeInDatabase(int postId, int userId){
		
		DbConnection connectionManager = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		int postEditPrivilegeId = 0;
		
		connectionManager = DbConnection.getInstance();
	     
	     if(connectionManager.getConnection() == null){
	        	System.out.println("Error with Blog Retrieval: Unable to establish connection with database.");
	        	return -1;
	       }
		
		if(postId == -1){
			System.out.println("The PostId value to be related to this postEditPrivilege object has not been initialized.");
			return -3;
		}
		
		if(userId == -1){
			System.out.println("The UserId value to be related to this postEditPrivilege object has not been initialized.");
			return -4;
		}
		
		
		try{
			
			 //insert into postEditPrivilege
	        pst = connectionManager.getConnection().prepareStatement("insert into postEditPrivilege values(0) ");
	        pst.executeUpdate();
	        pst.close();
	        
	       
	        //selecting value of postEditPrivilegeId column generated from previous statement.
	        pst = connectionManager.getConnection().prepareStatement("select last_insert_id() as PrivilegeId");
	        rs = pst.executeQuery();
	        
	        rs.first();
	        postEditPrivilegeId = rs.getInt("PrivilegeId");
	        
	        rs.close();
	        pst.close();
	        
	        //creating an entry in the post_postEditPrivilege table corresponding to this new post
	        pst = connectionManager.getConnection().prepareStatement("insert into post_postEditPrivilege values(?,?)");
	        pst.setInt(1, postId);
	        pst.setInt(2, postEditPrivilegeId);
	        pst.executeUpdate();
	        
	        pst.close();
	        
	       //creating an entry in the user_postEditPrivilege table corresponding to this new post
	        pst = connectionManager.getConnection().prepareStatement("insert into user_postEditPrivilege values(?,?)");
	        pst.setInt(1, userId);
	        pst.setInt(2, postEditPrivilegeId);
	        pst.executeUpdate();
	        
	        pst.close();
	        
		}catch(SQLException sqlE){
			//connectionManager.closeConnection();
        	sqlE.printStackTrace();
        	return -2;
        }finally { 
        	//connection obtained from DBConnection is closed at logout. 
           try {  
        	   if (rs != null) {  
        		   rs.close();
        	   }
        	   pst.close(); 
           } catch (SQLException e) {  
                e.printStackTrace();  
           }  
		}
		return postEditPrivilegeId;
	}
}
