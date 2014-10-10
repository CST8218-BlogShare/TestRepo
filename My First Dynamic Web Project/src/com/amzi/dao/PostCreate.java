package com.amzi.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;




import org.apache.catalina.connector.Request;

import com.amzi.dao.Blog;
import com.sun.xml.internal.ws.client.RequestContext;

public class PostCreate {
	public static String creationDate = null;
	private String errorMessage = null;
	private int blogId = -1;
	private int postId = -1;
	private int postCount = 0;
	private String author = null;
	private String postTitle = null;
	private String postBody = null;
	private boolean isEditableMode = false;
	private int toEdit = 0;
	
	private Blog b = null;

	public PostCreate() {
		
	}

	public PostCreate(String postTitle, String postBody) {
		Exception postCreateError = new Exception();
		try {
			postTitle = postTitle.trim();
			postBody = postBody.trim();
			if (postTitle.equals("")) {
				System.out
						.println("Post Has no tittle, throwing java.lang.Exception.");
				throw postCreateError;
			}
			if (postBody.equals("")) {
				System.out
						.println("Post Has no body, throwing java.lang.Exception.");
				throw postCreateError;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.postTitle = postTitle;
		this.postBody = postBody;
	}

	public int getBlogId() {
		return blogId;
	}
	
	public int getPostId() {
		return postId;
	}

	public int getPostCount() {
		return postCount;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getAuthor() {
		return author;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public String getPostBody() {
		return postBody;
	}
	
	 public boolean insertPostInDatabase(int userId, Blog b) {          
		 
	        PreparedStatement pst = null; 
	        ResultSet rs = null;
	        DbConnection connectionManager = null;
	        
	        boolean status = true;  
	       
	        /*
	         * The post object used to call this function needs to call the appropriate constructor to have its
	           postTitle and postBody data members initialized before calling the insertPostInDatabase function.
	        */
	        
	        if(this.postTitle == null){
	        	try {
					throw new Exception();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        
	        if(this.postBody == null){
	        	try {
					throw new Exception();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        
	        try {  
	        	
	        	
	        	//gaining access to the shared database connection.
	        	connectionManager = DbConnection.getInstance();
	  
	        	/*
	        	 *  
	        	 *  
	        	 *Algorithm used below
	        	 *  
	        	 	*insert post title, blogid content, creation date into post table
				    select postid from post table where blogid and title is the same
				    insert postid and user id into user_post
	        	 */
	        	
	        	//*insert post title, blogid content, creation date into post table
	            pst = connectionManager.getConnection().prepareStatement("insert into post values( 0, '"+b.getBlogId()+"','"+postTitle+"','"+postBody+"', curdate() )");  
	            pst.executeUpdate(); 
	            //closing the connection to prepare for the next prepared statement.
	            pst.close();
	            
	            //select postid from post table where blogid and title is the same
	            pst = connectionManager.getConnection().prepareStatement("select postId from post where blogId = '"+b.getBlogId()+"' AND title = '"+postTitle+"' ");
	            rs = pst.executeQuery();
	            rs.first();
	            this.postId = rs.getInt("postId");
	            rs.close();
	            pst.close();
	            
	            
	            //insert postid and user id into user_post
	            pst = connectionManager.getConnection().prepareStatement("insert into user_post values('"+userId+"', '"+postId+"') ");
	            pst.executeUpdate();
	            pst.close();
	            
	            b.setNewPost(true);
	            //status = rs.next();
	        } catch (SQLException sqlE) {  
	        	
	        	
	        	connectionManager.closeConnection();
	        	System.out.println("Post field missing, throwing SQLException");
	        	sqlE.printStackTrace();
	        	//errorMessage = "Error with previous login attempt. Incorrect Username and Password.";
	        	
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

