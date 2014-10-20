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
	
	public boolean insertPostEditPrivilegeInDatabase(int postId, int userId){
		boolean status = true;
		DbConnection connectionManager = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		if(postId == -1){
			System.out.println("The PostId value to be related to this postEditPrivilege object has not been initialized.");
			return false;
		}
		
		if(userId == -1){
			System.out.println("The UserId value to be related to this postEditPrivilege object has not been initialized.");
			return false;
		}
		
		
		try{
			
			connectionManager = DbConnection.getInstance();
			
			 //insert into postEditPrivilege
	        pst = connectionManager.getConnection().prepareStatement("insert into postEditPrivilege values(0) ");
	        pst.executeUpdate();
	        pst.close();
	        
	       
	        //selecting generated value for postEditPrivilegeId column from previous statement.
	        pst = connectionManager.getConnection().prepareStatement("select last_insert_id() as PrivilegeId");
	        rs = pst.executeQuery();
	        rs.first();
	        postEditPrivilegeId = rs.getInt("PrivilegeId");
	        rs.close();
	        pst.close();
	        
	        //creating an entry in the post_postEditPrivilege table corresponding to this new post
	        pst = connectionManager.getConnection().prepareStatement("insert into post_postEditPrivilege values('"+postId+"','"+postEditPrivilegeId+"') ");
	        pst.executeUpdate();
	        pst.close();
	        
	       //creating an entry in the user_postEditPrivilege table corresponding to this new post
	        pst = connectionManager.getConnection().prepareStatement("insert into user_postEditPrivilege values('"+userId+"','"+postEditPrivilegeId+"') ");
	        pst.executeUpdate();
	        pst.close();
	        
		}catch(SQLException sqlE){
			connectionManager.closeConnection();
        	sqlE.printStackTrace();
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
            if (rs != null) {  
                try {  
                    rs.close();  
                } catch (SQLException e) {  
                    e.printStackTrace();  
                }  
            }  
        
		}
		return status;
	}

}
