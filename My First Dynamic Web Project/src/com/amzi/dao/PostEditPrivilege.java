
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
	
	public static int getPostEditPrivilegeIdFromDatabaseByPostId(int postId){
		PreparedStatement pst = null;
		ResultSet rs = null;
		DbConnection connectionManager = null;
		int postEditPrivilegeId = -1;
		
		connectionManager = DbConnection.getInstance();
	    
		if(DbConnection.testConnection(connectionManager) == false){
			System.out.println("Error with retrieval of postEditPrivilegeId by postId: Unable to establish connection with database.");
			return -1;
		}
		  
	    try{
	    	pst = connectionManager.getConnection().prepareStatement("select pep.postEditPrivilegeId as postEditPrivilegeId"
	    													      + " from postEditPrivilege pep, post_posteditprivilege ppep, post p"
	    													      + " where pep.postEditPrivilegeId = ppep.postEditPrivilegeId"
	    													      + " and ppep.postId = p.postId"
	    													      + " and p.postid = ?"); 
	    	pst.setInt(1, postId);
	    	rs = pst.executeQuery();
	    	rs.first();
	    	
	    	postEditPrivilegeId = rs.getInt("postEditPrivilegeId");
	    	
	    	pst.close();
	    	rs.close();
	    	
	    }catch(SQLException sqlE){
	    	sqlE.printStackTrace();
	    	System.out.println("Error with retrieval of postEditPrivilegeId by postId: SQL error.");
	    	return -2;
	    }finally{
	    	//connection obtained from DBConnection is closed at logout. 
	    	try {
				rs.close();
				pst.close();
			} catch (SQLException sqlE) {
				sqlE.printStackTrace();
			}
	    }
		return postEditPrivilegeId;
	}
	
	public static int insertPostEditPrivilegeInDatabase(int postId, int userId){
		
		DbConnection connectionManager = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		int postEditPrivilegeId = 0;
		
		connectionManager = DbConnection.getInstance();
	    
		try {
     		if(connectionManager.getConnection().isValid(0) == false){
     			System.out.println("Error with insertion of postEditPrivilege into database: Unable to establish connection with database.");
     			connectionManager.closeConnection();
     			return -1;
     		}
	    } catch (SQLException sqlConE) {
     		sqlConE.printStackTrace();
	    }
		
		if(postId <= 0){
			System.out.println("Error with insertion of postEditPrivilege into database: The value of postId is invalid");
			return -3;
		}
		
		if(userId <= 0){
			System.out.println("Error with insertion of postEditPrivilege into database: The value of userId is invalid");
			return -4;
		}
		
		try{
			
			 //insert into postEditPrivilege
	        pst = connectionManager.getConnection().prepareStatement("insert into postEditPrivilege (PostEditPrivilegeId) values(0)");
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
	        pst = connectionManager.getConnection().prepareStatement("insert into post_postEditPrivilege (postId, postEditPrivilegeId) values(?,?)");
	        pst.setInt(1, postId);
	        pst.setInt(2, postEditPrivilegeId);
	        pst.executeUpdate();
	        
	        pst.close();
	        
	       //creating an entry in the user_postEditPrivilege table corresponding to this new post
	        pst = connectionManager.getConnection().prepareStatement("insert into user_postEditPrivilege (userId, postEditPrivilegeId) values(?,?)");
	        pst.setInt(1, userId);
	        pst.setInt(2, postEditPrivilegeId);
	        pst.executeUpdate();
	        
	        pst.close();
	        
		}catch(SQLException sqlE){
        	sqlE.printStackTrace();
        	System.out.println("Error with insertion of postEditPrivilege into database: SQL error");
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
	
	public static int deletePostEditPrivilegeFromDatabaseById(int postEditPrivilegeId){
		
		DbConnection connectionManager = null;
		PreparedStatement pst = null;
		
		connectionManager = DbConnection.getInstance();
	    
		try{
     		if(connectionManager.getConnection().isValid(0) == false){
     			System.out.println("Error with deletion of PostEditPrivilege by id: Unable to establish connection with database.");
     			connectionManager.closeConnection();
     			return -1;
     		}
	    }catch (SQLException sqlConE) {
     		sqlConE.printStackTrace();
	    }
		
		try{
			pst = connectionManager.getConnection().prepareStatement("delete from postEditPrivilege where postEditPrivilegeId = ?");
	    	pst.setInt(1, postEditPrivilegeId);
	    	pst.execute();
	    	pst.close();
	    }catch(SQLException sqlE){
	    	sqlE.printStackTrace();
	    	System.out.println("Error with deletion of PostEditPrivilege by id: SQL Error");
	    	return -2;
	    }finally{
	    	try {
				pst.close();
			} catch (SQLException sqlE) {
				sqlE.printStackTrace();
			}
	    } 		
		return 0;
	}
}
