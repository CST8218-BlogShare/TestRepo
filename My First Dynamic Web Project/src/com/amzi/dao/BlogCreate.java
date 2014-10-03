package com.amzi.dao;  
  
 import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;  

  
public class BlogCreate { 
	
	public static String creationDate = null;
	public static String errorMessege = null;
	
    public static boolean postBlog(String blogTitle, String postTitle, String postBody) {          
        boolean status = true;  
        PreparedStatement pst = null; 
        ResultSet rs = null;
        DbConnection connectionManager = null;
        
        Exception postError = new Exception();
 
        try {  
        	blogTitle = postTitle.trim();
        	postTitle = postTitle.trim();
        	postBody = postBody.trim();
        	
        	if(blogTitle.equals("")){
        		System.out.println("Blog Has no tittle, throwing java.lang.Exception.");
        		errorMessege = "Error with Post. No Post Title was not entered";
        		throw postError;
        	}
        	
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
  
            pst = connectionManager.getConnection().prepareStatement("insert into blog values( 0, '"+blogTitle+"', curdate() );");  
           
            pst.executeUpdate(); 
            
            pst.close();
            
            pst = connectionManager.getConnection().prepareStatement("insert into Post values(0, 0, '"+postTitle+"','"+postBody+"', curdate() );");  
            
            pst.executeUpdate(); 
            
            pst.close();
            //closing the connection to prepare for the next prepared statement.
            pst.close();
            //if the query works,this should never be null. But do we wanna check just because..yeaah
            creationDate = rs.getString("CreationDate");
            
            //status = rs.next();
        } catch (SQLException sqlE) {  
        	
        	try{
        		connectionManager.getConnection().close();
        	}catch(SQLException sqlCloseE){
        		sqlCloseE.printStackTrace();
        	}
        	
        	System.out.println("Blog field missing, throwing SQLException");
        	sqlE.printStackTrace();
        	errorMessege = "Error with previous login attempt. Incorrect Username and Password.";
        	
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