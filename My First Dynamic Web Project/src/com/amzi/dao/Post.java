package com.amzi.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ArrayList;

import com.amzi.dao.Blog;


public class Post {
	private int blogId = -1;
	private int postId = -1;
	private String author = null;
	private String postTitle = null;
	private String postBody = null;
	private boolean isPublic = false;
	//private String creationDateTime = null;
	
	public Post() {
		
	}
	
	public Post(String postTitle, String postBody, String username, boolean isPublic) {
		postTitle = postTitle.trim();
		postBody = postBody.trim();
		username = username.trim();
			
		if (postTitle.equals("")) {
			System.out.println("Post has no title.");
			return;
		}
			
		if (postBody.equals("")) {
			System.out.println("Post has no body.");
			return;
		}
			
		if (username.equals("")) {
			System.out.println("Username contains no characters.");
			return;
		}
		
		this.postTitle = postTitle;
		this.postBody = postBody;
		this.author = username;
		this.isPublic = isPublic;
	}

	public int getBlogId() {
		return blogId;
	}
	
	protected void setBlogId(int blogId) {
		this.blogId = blogId;;
	}
	
	public int getPostId() {
		return postId;
	}
	
	protected void setPostId(int postId) {
		this.postId = postId;
	}

	public String getAuthor() {
		return author;
	}
	
	protected void setAuthor(String author){
		this.author = author;
	}

	public String getPostTitle() {
		return postTitle;
	}
	
	protected void setPostTitle(String postTitle){
		this.postTitle = postTitle;
	}

	public String getPostBody() {
		return postBody;
	}
	
	protected void setPostBody(String postBody){
		this.postBody = postBody;
	}
	
	public boolean getIsPublic() {
		return isPublic;
	}
	
	protected void setIsPublic(boolean b){
		this.isPublic = b;
	}
	
	/*public String getCreationDateTime() {
		return creationDateTime;
	}
	
	protected void setCreationDateTime(String creationDateTime){
		this.creationDateTime = creationDateTime;
	}*/
	
	//author is not initialized in this method, as it is not a value in the post table.
	public static Post getPostFromDatabaseById(int postId){
		 Post p = null;
		 PreparedStatement pst = null; 
	     ResultSet rs = null;
	     DbConnection connectionManager = null;
	     
	     connectionManager = DbConnection.getInstance();
	     
	     try {
	    	 if(connectionManager.getConnection().isValid(0) == false){
	    		 System.out.println("Error with post retrieval by postId: Unable to establish connection with database.");
				 connectionManager.closeConnection();
				 return null;
	    	 }
	     } catch (SQLException sqlConE) {
			sqlConE.printStackTrace();
	     }
	     
	     try {
			pst = connectionManager.getConnection().prepareStatement("select blogid, title, content, isPublic from post where postid = ? ");
			pst.setInt(1, postId);
			rs = pst.executeQuery();
			rs.first();
			
			p = new Post();
			p.blogId = rs.getInt("blogId");
			p.postTitle = rs.getString("title");
			p.postBody = rs.getString("content");
			p.isPublic = rs.getBoolean("isPublic");
			p.setAuthor(Post.getPostAuthorFromDatabaseById(postId));
			p.postId = postId;

		} catch (SQLException sqlE) {
			System.out.println("Error with post retrieval by postId: Unable to retrieve post contents based on postId.");
			
			/*
        	 * Since the function getPostAuthorFromDatabaseById() is called within the try-catch block,
        	 * these error messages detect at what point the error was thrown.
        	 */
        	
        	//if the query within getPostListFromDatabasaByBlogId() failed to produce a result.
        	if(p.blogId == -1){
        		System.out.println("Error with post retrieval by postId: Unable to retrieve post contents based on postId.");
	    	}
	    	
        	//if the query within getPostAuthorFromDatabaseById() failed to produce a result.
	    	if(p.author == ""){
	    		System.out.println("Error with post retrieval by postId: Unable to retrieve author of post based on postId.");
	    	}
			
			sqlE.printStackTrace();
			return null;
		}finally{
			try {
				rs.close();
				pst.close();
			} catch (SQLException sqlE) {
				sqlE.printStackTrace();
			}
		}
	     return p;
	}
	
	public static String getPostAuthorFromDatabaseById(int postId){
		PreparedStatement pst = null;
		ResultSet rs = null;
		DbConnection connectionManager = null;
		String author = null;
		
		connectionManager = DbConnection.getInstance();
		
		 try {
	    	 if(connectionManager.getConnection().isValid(0) == false){
	    		 System.out.println("Error with post author retrieval by postId: Unable to establish connection with database.");
				 connectionManager.closeConnection();
				 return null;
	    	 }
	     } catch (SQLException sqlConE) {
			sqlConE.printStackTrace();
	     }
		
		/* 
		 * Selecting the author of the current post, by checking the contents of the user_post table 
		 * to retrieve the userId associated with the post, and then selecting the username based on that userId. 
		 */
		try{
	    	pst = connectionManager.getConnection().prepareStatement("select username from user where userId ="
	    													       + "(select u.userId from user u, post p, user_post up "
	    													       + " where p.postId = ? AND u.userId = up.userId AND p.postId = up.postId)");
	    	pst.setInt(1, postId);
	    	rs = pst.executeQuery();
			rs.first();
			author = (rs.getString("username")); 
			
		}catch(SQLException sqlE){
			System.out.println("Error with post author retrieval by postId: SQL error.");
			sqlE.printStackTrace();
			return null;
		}finally{
			try {
				rs.close();
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return author;
	}
	
	public static ArrayList<Post> getPostListFromDatabaseByBlogId(int blogId){
		PreparedStatement pst = null;
		ResultSet rs = null;
		DbConnection connectionManager = null;
		ArrayList<Post> posts = new ArrayList<Post>();
		
		connectionManager = DbConnection.getInstance();
		
		try {
	    	if(connectionManager.getConnection().isValid(0) == false){
	    		System.out.println("Error with post list retrieval by blogId: Unable to establish connection with database.");
				connectionManager.closeConnection();
				return null;
	    	}
	    } catch (SQLException sqlConE) {
	    	sqlConE.printStackTrace();
	    }
		
		//get the blog's posts and their bodies from the database using the blogid
        try {
			pst = connectionManager.getConnection().prepareStatement("select postId, title, content, isPublic from post where blogid = ?");
			
			pst.setInt(1, blogId);
		        
		    rs = pst.executeQuery();
		       	
		    while(rs.next()){
		    	Post p = new Post();
		    	
		    	p.postId =  rs.getInt("postId");
		    	p.postTitle = rs.getString("title");
		    	p.postBody = rs.getString("content");
		    	p.isPublic = rs.getBoolean("isPublic");
		       	p.author =  getPostAuthorFromDatabaseById(p.getPostId());
		       	p.blogId = blogId;
		    	
	    		posts.add(p);
		   }
			
        } catch (SQLException sqlE) {
        	System.out.println("Error with post list retrieval by blogId: SQL error.");
			
        	/*
        	 * Since the function getPostAuthorFromDatabaseById() is called within the try-catch block,
        	 * these error messages detect at what point the error was thrown.
        	 */
        	
        	//if the query within getPostListFromDatabasaByBlogId() failed to produce a result.
        	if(posts.get(posts.size() - 1).postId == -1){
        		System.out.println("Error with post list retrieval by blogId: Unable to retrieve post contents based on blogId.");
	    	}
	    	
        	//if the query within getPostAuthorFromDatabaseById() failed to produce a result.
	    	if(posts.get(posts.size() - 1).author == ""){
	    		System.out.println("Error with post list retrieval by blogId: Unable to retrieve author of post based on postId.");
	    	}
        	
        	sqlE.printStackTrace();
			return null;
		}
        return posts;
	}
		
	public static int insertPostInDatabase(String postTitle, String postBody, int userId, String username, boolean postIsPublic, Blog b, boolean editMode) {          
		PreparedStatement pst = null; 
	    ResultSet rs = null;
	    DbConnection connectionManager = null;
	    Connection conn = null;
	    int errorCode = 0;
	    int postId = 0;
	    int postIsPublicAsInt = 0; // default value is false
	    
	    connectionManager = DbConnection.getInstance();
	    conn = connectionManager.getConnection();
	    
	    try {
	    	if(connectionManager.getConnection().isValid(0) == false){
	    		System.out.println("Error with post insertion into database : Unable to establish connection with database.");
				connectionManager.closeConnection();
				return -1;
	    	}
	    } catch (SQLException sqlConE) {
	    	sqlConE.printStackTrace();
	    }
	     
	    if(postIsPublic == true){
			postIsPublicAsInt = 1;
		}
	        	        
	    	if(!editMode){
	    		/* Insert post title, blogid content, creation date into post table 
			     * The SQL function now(), retrieves the current dateTime value.
			     * the boolean isPublic needs to be converted to an int value, since the bool datatype is represented as TinyInt(1) by MySQL DBMS.
			    */
					
	    		try{
			        pst = conn.prepareStatement("insert into post (postid, blogid, title, content, creationDateTime, isPublic)"
			        						  + " values( 0, ?, ?, ?, now(), ?)");  
					pst.setInt(1, b.getBlogId());
					pst.setString(2, postTitle);
					pst.setString(3, postBody);
					pst.setInt(4,postIsPublicAsInt);
			        pst.executeUpdate();
					pst.close();
			            
					//selecting value of postId column generated from previous statement.
					pst = conn.prepareStatement("select last_insert_id() as PostId");
					rs = pst.executeQuery();
					rs.first();
					postId = rs.getInt("PostId");
					rs.close();
					pst.close();
						            
					//insert postid and user id into user_post
					pst = conn.prepareStatement("insert into user_post (userid,postid) values(?,?) ");
					pst.setInt(1, userId);
					pst.setInt(2, postId);
					pst.executeUpdate();
					pst.close();
				
	    		} catch (SQLException sqlE) {  
	    			System.out.println("Error with post insertion into database : SQL error.");
	    			sqlE.printStackTrace();
	    			return -2;
	    		}finally {
	    			//the connection the connectionManager object interacts with, is closed at logout. 
		            try {  
		            	
		            	if(rs != null){
		            		rs.close();
		            	}
		            	pst.close();  
		            	
		            } catch (SQLException e) {  
		                e.printStackTrace();  
		            }   
	    		}  
				
		    }else if( editMode ){
		            			
				postId = b.getPostAt(b.getToEdit()).getPostId();
					
				errorCode = Post.updatePostInDatabaseById(postId, postTitle, postBody, postIsPublic, userId);
				
				if(errorCode < 0){
					if(errorCode == -1){
						//error descriptions are unneeded as they are output from updatePostInDatabaseById();
						return -1;
					}
					
					if(errorCode == -2){
						return -2;
					}
					
					if(errorCode == -3){
						return -3;
					}
					
					if(errorCode == -4){
						return -4;
					}
					
				}
		    }
	    return postId;
	}

	public static int updatePostInDatabaseById(int postId, String postTitle, String postContent, boolean postIsPublic, int userId){
		PreparedStatement pst = null;
		ResultSet rs = null;
		DbConnection connectionManager = null;
		String titleBeforeEdit = "";
	    String contentBeforeEdit = "";
	    int errorCode = 0;
	    
	    connectionManager = DbConnection.getInstance();
	    
	    try {
	    	if(connectionManager.getConnection().isValid(0) == false){
	    		System.out.println("Error with post update: Unable to establish connection with database.");
				connectionManager.closeConnection();
				return -1;
	    	}
	    } catch (SQLException sqlConE) {
	    	sqlConE.printStackTrace();
	    }
	     
	    try{
	    	/*
	    	 * In order to accurately track edits, the values of postTitle, postBody, and isPublic
		  	 * need to captured before the update to the post within the post table is made
		  	 */
			        
	    	pst = connectionManager.getConnection().prepareStatement("select title, content from post where postid = ?");
	    	pst.setInt(1, postId);
	    	rs = pst.executeQuery();
	    	rs.first();
			        
	    	titleBeforeEdit = rs.getString("title");
	    	contentBeforeEdit = rs.getString("content");
			         
	    	rs.close();
	    	pst.close();
        
	    	errorCode = PostEdit.insertPostEditInDatabase(userId,postId,titleBeforeEdit,contentBeforeEdit);
	    	
	    	if(errorCode < 0){
	    		System.out.println("Error with post update: Unable to create post edit entry to track the update to post.");
	    		return -4;
	    	}
	    	    
	    	pst = connectionManager.getConnection().prepareStatement("UPDATE post SET title = ?, content = ?, isPublic = ? WHERE postID  = ?");  
	    	pst.setString(1, postTitle);
	    	pst.setString(2, postContent);
	    	pst.setBoolean(3, postIsPublic);
	    	pst.setInt(4, postId);
	    	
	    	if(pst.executeUpdate() == 0){
	    		System.out.println("Error with post update: The provided value for postId does not match any rows in post table.");
	    		return -3;
	    	}
	    	
	    	pst.close();
	    
	    } catch (SQLException sqlE){
	    	System.out.println("Error with post update: SQL error.");
	    	sqlE.printStackTrace();
	    	return -2;
	    }finally {
	    	//the connection the connectionManager object interacts with, is closed at logout. 
	    	try {  
	    		rs.close();
	    		pst.close();  
      	
	    	} catch (SQLException e) {  
	    		e.printStackTrace();  
	    	}   
	    } 
	    return 0;
	}
	
	public static int deletePostFromDatabaseById(int postId){
		
		 PreparedStatement pst = null; 
	     DbConnection connectionManager = null;
		
	     connectionManager = DbConnection.getInstance();
	     
	     try {
	    	 if(connectionManager.getConnection().isValid(0) == false){
	    		 System.out.println("Error with post deletion by postId: Unable to establish connection with database.");
	    		 connectionManager.closeConnection();
	    		 return -1;
		     }
		 } catch (SQLException sqlConE) {
		    	sqlConE.printStackTrace();
		 }
	      	     	     
	     try{
	    	 pst = connectionManager.getConnection().prepareStatement("delete from post where postid = ?");
	    	 pst.setInt(1, postId);
	    	 
	    	 if(pst.executeUpdate() == 0){
	    		 System.out.println("Error with post deletion by postId: The provided value for postId does not match any rows in post table.");
	    		 return -3;
	    	 }
	    	 
	     }catch(SQLException sqlE){	
	    	 System.out.println("Error with post deletion by postId: SQL error.");
	    	 sqlE.printStackTrace();
	    	 return -2;
	     }finally {
	    	 //the connection the connectionManager object interacts with, is closed at logout. 
	         try {  
	        	 pst.close();  
	         } catch (SQLException e) {  
	             e.printStackTrace(); 
	         }  
		 }
		return 0;
	}
	
	public static int addPostToPostDeleted(int postId){
		PreparedStatement ps = null; 
	    DbConnection connectionManager = null;
	    
	    if(postId <= 0){
	    	System.out.println("Error adding post to PostDeleted table: Provided value for postId is invalid.");
	    	return -3;
	    }
	    
	    connectionManager = DbConnection.getInstance();
	    
	    try {
	    	if(connectionManager.getConnection().isValid(0) == false){
	    		System.out.println("Error adding post to PostDeleted table: Unable to establish connection with database.");
	    		connectionManager.closeConnection();
	    		return -1;
		    }
		} catch (SQLException sqlConE) {
			sqlConE.printStackTrace();
		}
	     
	    try{
	    	ps = connectionManager.getConnection().prepareStatement("insert into postdeleted(postDeletedId, postId) values(0,?)");
		    ps.setInt(1,postId);
		    ps.execute();
	    }catch(SQLException slqE){
		    System.out.println("Error adding post to PostDeleted table: SQL error");
		    return -2;
	    }finally{
		    try {
		    	ps.close();
		    } catch (SQLException sqlE) {
		    	sqlE.printStackTrace();
		    }
	    }
	    return 0;
	}
		
	public static int checkForDeletion(int postId){
		PreparedStatement ps = null; 
	    ResultSet rs = null;
	    DbConnection connectionManager = null;
		
	    if(postId <= 0){
	    	System.out.println("Error with post deletion check: Provided value for postId is invalid.");
	    	return -3;
	    }
	    
	    connectionManager = DbConnection.getInstance();
	    
	    try {
	    	if(connectionManager.getConnection().isValid(0) == false){
	    		System.out.println("Error with post deletion check: Unable to establish connection with database.");
	    		connectionManager.closeConnection();
	    		return -1;
		    }
		} catch (SQLException sqlConE) {
			sqlConE.printStackTrace();
		}
	    
	    try{
		   ps = connectionManager.getConnection().prepareStatement("select postId from postDeleted where postId = ?");
		   ps.setInt(1, postId);
		   
		   rs = ps.executeQuery();
		   rs.last();
		   
		   //if the result set contains any rows, this post has been deleted and has been added to the postDeleted table.
		   if(rs.getRow() > 0){
			   return 1;
		   }
		   
	   }catch(SQLException sqlE){
		   System.out.println("Error with post deletion check: SQL error");
		   return -2;
	   }finally{
		   try {
			   rs.close();
			   ps.close();
		   } catch (SQLException e) {
			   e.printStackTrace();
		   }
	   }	    
		return 0;
	}
	
	public static boolean determinePostEditPrivilegeById(int postId, String author, boolean isPublic, int userId, String username){
		 Boolean editEnabled = true;
		 PreparedStatement pst = null;
		 ResultSet rs = null;
		
		 //if the post is public, it will always be editable.
		 if(isPublic == true){
		 	return editEnabled;
		 }
		 	
		/* 
		   If the current user is not the author of the post and the post is not publicly editable
		   An attempt is made to match current userId with a userId that is associated to the privilegeId of this post.
		 */
		if(!author.equals(username)){
			
					
			DbConnection connectionManager = DbConnection.getInstance(); 
	    	
			try {
		    	if(connectionManager.getConnection().isValid(0) == false){
		    		System.out.println("Error with determining PostEditPrivilege by postId: Unable to establish connection with database.");
		    		connectionManager.closeConnection();
		    		return false;
			    }
			} catch (SQLException sqlConE) {
				sqlConE.printStackTrace();
			}
			
			editEnabled = false;
				
			try{
				pst = connectionManager.getConnection().prepareStatement("select u.userid as userId from user u, post p, user_post up, posteditprivilege pep, user_posteditprivilege upep, post_posteditprivilege ppep" +
																		 " where u.userid = upep.userid AND " +
																		 " upep.postEditPrivilegeId = pep.postEditPrivilegeId AND " +
																	     " pep.postEditPrivilegeId = ppep.postEditPrivilegeId AND " +
																		 " p.postId = ppep.postid AND " +
																		 " u.userid = up.userid AND " +
																		 " P.postid = up.postid AND " +
																		 " p.postid = ? "); 
				pst.setInt(1, postId);
				rs = pst.executeQuery();
				while(rs.next()){
					//if the user has a corresponding entry for the post within postEditPrivilege
					if(userId == rs.getInt("userId")){
						editEnabled = true;
						break;
					}
				}
			}catch(SQLException sqlE){
				System.out.println("Error with determining PostEditPrivilege by postId: SQL error.");
				sqlE.printStackTrace();
			}finally{
				try {
					rs.close();
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return editEnabled;
	 }  
	
	public int reverseEditToPostInDatabase(int postPosInBlog, PostEdit currentPostEdit, int currentUserId){
    	PreparedStatement pst = null;
		ResultSet rs = null;
		DbConnection connectionManager = null;
		int errorCode = 0;
		
	    connectionManager = DbConnection.getInstance();
    	
	    try {
	    	if(connectionManager.getConnection().isValid(0) == false){
	    		System.out.println("Error with post edit reversal: Unable to establish connection with database.");
	    		connectionManager.closeConnection();
	    		return -1;
		    }
		} catch (SQLException sqlConE) {
			sqlConE.printStackTrace();
		}
	     
		//postId, postTitle and postBody must be initialized must be initialized
		
    	try {
			
    		connectionManager.getConnection().setAutoCommit(false);
    		
    		pst = connectionManager.getConnection().prepareStatement("UPDATE post SET title = ?, content = ? WHERE postID  = ?");
			pst.setString(1, currentPostEdit.getTitleBeforeEdit());
			pst.setString(2, currentPostEdit.getContentBeforeEdit());
			pst.setInt(3, this.getPostId());
			pst.execute();
			
			pst.close();
			
			errorCode = PostEdit.insertPostEditInDatabase(currentUserId,postId,this.getPostTitle(),this.getPostBody());	
	        				
		} catch (SQLException sqlE) {
			sqlE.printStackTrace();
			errorCode = -2;
		}finally{
			try {
				if(rs != null){
					rs.close();
				}
				pst.close();
			} catch (SQLException sqlE) {
				sqlE.printStackTrace();
			}			
		}
    	
    	try {
    		if(errorCode < 0){
    			connectionManager.getConnection().rollback();
        		connectionManager.getConnection().setAutoCommit(true);
    			
        		if(errorCode == -1){
        			System.out.println("Error inserting post edit into database: Unable to establish connection with database.");
        		}
        		
        		if(errorCode == -2){
        			System.out.println("Error inserting post edit into database: SQL error.");
        		}
    		}else{
    			connectionManager.getConnection().commit();
    			connectionManager.getConnection().setAutoCommit(true);
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return errorCode;
	}
}

