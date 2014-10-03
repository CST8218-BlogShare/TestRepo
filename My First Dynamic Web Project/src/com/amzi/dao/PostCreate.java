package com.amzi.dao;  
  
import java.sql.PreparedStatement;  
import java.sql.SQLException;  
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
  
public class PostCreate {  
    
	public static String creationDate = null;
	public static String errorMessege = null;
	
	public static boolean post(String postTitle, String postBody) {          
        boolean status = true;  
        PreparedStatement pst = null; 
        ResultSet rs = null;
        DbConnection connectionManager = null;
        
        Exception postError = new Exception();
 
        try {  
           
        	postTitle = postTitle.trim();
        	postBody = postBody.trim();
        	if(postTitle.equals("")){
        		System.out.println("Post Has no tittle, throwing java.lang.Exception.");
        		errorMessege = "Error with Post. No Post Title was not entered";
        		throw postError;
        	}
        	
        	if(postBody.equals("")){
        		System.out.println("Post Has no tittle, throwing java.lang.Exception.");
        		errorMessege = "Error with Post. No Post Title was not entered";
        		throw postError;
        	}
        	
        	//gaining access to the shared database connection.
        	connectionManager = DbConnection.getInstance();
  
            pst = connectionManager.getConnection().prepareStatement("insert into Post values(0, 0, '"+postTitle+"','"+postBody+"', curdate() );");  
           
            pst.executeUpdate(); 
            
            //closing the connection to prepare for the next prepared statement.
            pst.close();
            /*
             * 
             * 
            pst = connectionManager.getConnection().prepareStatement("select * from user where username=? and password=?"); 
            
            pst.setString(1, postTitle);  
            pst.setString(2, postBody);  
  
            rs = pst.executeQuery(); 
            
            rs.first();
            */
            //if the query works,this should never be null. But do we wanna check just because??
            creationDate = rs.getString("CreationDate");
 
            
        // should totally catch this.. com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Duplicate entry 'Derek' for key 'User_Unique_Username'    
        } catch (SQLException sqlE) { 
        	
        	//should have a rollback here
        	
        	try{
        		connectionManager.getConnection().close();
        	}catch(SQLException sqlCloseE){
        		sqlCloseE.printStackTrace();
        	}
        	
        	System.out.println("Error inserting information of post in post table, throwing SQLException.");
        	sqlE.printStackTrace();
       	 	errorMessege = "Error completing post";
       	 	status = false;
       	 	
        } catch (Exception e) { 
        	 e.printStackTrace(); //may not be necessary
            status = false;
        } finally {  
            if (pst != null) {  
                try {  
                    pst.close();  
                } catch (SQLException sqlE) {  
                    sqlE.printStackTrace();  
                }  
            }  
            if (rs != null) {  
                try {  
                    rs.close();  
                } catch (SQLException sqlE) {  
                    sqlE.printStackTrace();  
                }  
            }  
        }  
        return status;  
    }  
}  