package com.amzi.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import com.amzi.dao.Blog;

public class Post {
	public static String creationDate = null;
	private String errorMessage = null;
	private int blogId = -1;
	private int postId = -1;
	private String author = null;
	private String postTitle = null;
	private String postBody = null;
	
	public Post() {
		
	}

	public Post(String postTitle, String postBody) {
		Exception postCreateError = new Exception();
		try {
			postTitle = postTitle.trim();
			postBody = postBody.trim();
			if (postTitle.equals("")) {
				System.out
						.println("Post has no tittle, throwing java.lang.Exception.");
				throw postCreateError;
			}
			if (postBody.equals("")) {
				System.out
						.println("Post has no body, throwing java.lang.Exception.");
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
	  
	        	blogId = b.getBlogId();
	        	
	        	if(blogId == -1){
	        		try {
						throw new Exception();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        	
	        	/*
	        	 *  
	        	 *  
	        	 *Algorithm used below
	        	 *  
	        	 	*insert post title, blogid content, creation date into post table
				    select postid from post table where blogid and title is the same
				    insert postid and user id into user_post
	        	 */
	        	//if(b.getEditMode() == false){
		        	
	        		//checking within current blog for a post with the same title as the one that is to be created.
	        		pst = connectionManager.getConnection().prepareStatement("select title from post where title = '"+postTitle+"' AND blogId = '"+blogId+"' ");
	        		rs = pst.executeQuery();
	        		
	        		//if the result set is empty, meaning that a match with the title of the post to be created is not found.
	        		if(rs.next() == false){
	        			//closing the connection to prepare for the next prepared statement.
	        			rs.close();
		        		pst.close();
		        		
		        		//insert post title, blogid content, creation date into post table
			            pst = connectionManager.getConnection().prepareStatement("insert into post values( 0, '"+blogId+"','"+postTitle+"','"+postBody+"', curdate() )");  
			            pst.executeUpdate();
			            pst.close();
			            
			            //select postid from post table where blogid and title is the same
			            pst = connectionManager.getConnection().prepareStatement("select postId from post where blogId = '"+blogId+"' AND title = '"+postTitle+"' ");
			            rs = pst.executeQuery();
			            rs.first();
			            this.postId = rs.getInt("postId");
			            rs.close();
			            pst.close();
			            
			            
			            //insert postid and user id into user_post
			            pst = connectionManager.getConnection().prepareStatement("insert into user_post values('"+userId+"', '"+postId+"') ");
			            pst.executeUpdate();
			            pst.close();
			           
			            b.addPost(postTitle,postBody);
			            b.setPostCount(b.getPostCount()+1);
	        		}
	        	//}
		            
		            
		            
	        	//Code below to be in the next iteration. Now that we are repopulating the fields for edit we need to update the table. First queary is being worked on
	        	/*else if( b.getEditMode() == true){
	        		//*insert post title, blogid content, creation date into post table
		            pst = connectionManager.getConnection().prepareStatement("update  post set title = '"+postTitle+"', content = '"+postBody+"' where blogId = '"+b.getBlogId()+"' )");  
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
	        	}*/
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

