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
			System.out.println("Error with Post Retrieval: Unable to retrieve Post contents based on PostId");
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
		
		if(connectionManager.getConnection() == null){
        	System.out.println("Error with Blog Retrieval: Unable to establish connection with database.");
        	return null;
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
		
		//get the blog's posts and their bodies from the database using the blogid
        try {
			pst = connectionManager.getConnection().prepareStatement("select postId, title, content, isPublic from post where blogid = ?");
			
			pst.setInt(1, blogId);
		        
		    rs = pst.executeQuery();
		       	
		    while(rs.next()){
		    	Post p = new Post();
		    	p.setBlogId(blogId);
		    	p.setPostId(rs.getInt("postId"));
		    	p.setPostTitle(rs.getString("title"));
		    	p.setPostBody(rs.getString("content"));
		    	p.setIsPublic(rs.getBoolean("isPublic"));
		    	//p.setCreationDateTime(rs.getString("creationDateTime"));
		       	p.setAuthor(getPostAuthorFromDatabaseById(p.getPostId()));
		    	
	    		posts.add(p);
		   }
			
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return posts;
	}
	
	
	public static int insertPostInDatabase(String postTitle, String postBody, int userId, String username, boolean postIsPublic, Blog b, boolean editMode) {          
		PreparedStatement pst = null; 
	    ResultSet rs = null;
	    DbConnection connectionManager = null;
	    Connection conn = null;
	    int postId = 0;
	    int postIsPublicAsInt = 0; // default value is false
	    
	    connectionManager = DbConnection.getInstance();
	    conn = connectionManager.getConnection();
	    
	    if(conn == null){
	    	System.out.println("Error with Post Insertion: Unable to establish connection with database.");
	        return -1;
	    }
	    
	    if(postIsPublic == true){
			postIsPublicAsInt = 1;
		}
	        	        
	    try{
	    	if(!editMode){
	    		/* Insert post title, blogid content, creation date into post table 
			     * The SQL function now(), retrieves the current dateTime value.
			     * the boolean isPublic needs to be converted to an int value, since the bool datatype is represented as TinyInt(1) by MySQL DBMS.
			    */
					     
		        pst = conn.prepareStatement("insert into post values( 0, ?, ?, ?, now(), ?)");  
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
				pst = conn.prepareStatement("insert into user_post values(?, ?) ");
				pst.setInt(1, userId);
				pst.setInt(2, postId);
				pst.executeUpdate();
				pst.close();
				
		    }else if( editMode ){
		        
		    	String titleBeforeEdit = "";
		        String contentBeforeEdit = "";
		        			
				postId = b.getPostAt(b.getToEdit()).getPostId();
					
				//Post.updatePostInDatabaseById(int postId, String postTitle, String postContent, boolean postIsPublic, int userId)
				
				/*In order to accurately track edits, the values of postTitle, postBody, and isPublic
				  need to captured before the update to the post within the post table is made*/
					        
				pst = conn.prepareStatement("select title, content from post where postid = ?");
				pst.setInt(1, postId);
				rs = pst.executeQuery();
				rs.first();
					        
				titleBeforeEdit = rs.getString("title");
				contentBeforeEdit = rs.getString("content");
					         
				rs.close();
				pst.close();
	
			    if(titleBeforeEdit == "" || contentBeforeEdit == ""){
				    return -3;
				}
					        
				if(PostEdit.insertPostEditInDatabase(userId,postId,titleBeforeEdit,contentBeforeEdit) == false){
					return -4;
				}
					        
				pst = conn.prepareStatement("UPDATE post SET title = ?, content = ?, isPublic = ? WHERE postID  = ?");  
				pst.setString(1, postTitle);
				pst.setString(2, postBody);
				pst.setBoolean(3, postIsPublic);
				pst.setInt(4, postId);
				pst.executeUpdate(); 
			    pst.close();
				            
				b.getPostAt(b.getToEdit()).postTitle = postTitle;
				b.getPostAt(b.getToEdit()).postBody = postBody;
			    b.getPostAt(b.getToEdit()).isPublic = postIsPublic;
		    }
	    } catch (SQLException sqlE) {  
	        //connectionManager.closeConnection();
	        //System.out.println("Post field missing, throwing SQLException");
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
	    return postId;
	}

	public static int updatePostInDatabaseById(int postId, String postTitle, String postContent, boolean postIsPublic, int userId){
		PreparedStatement pst = null;
		ResultSet rs = null;
		DbConnection connectionManager = null;
		String titleBeforeEdit = "";
	    String contentBeforeEdit = "";
	    
	    connectionManager = DbConnection.getInstance();
	    
	    if(connectionManager.getConnection() == null){
        	System.out.println("Error with Blog Retrieval: Unable to establish connection with database.");
        	return -1;
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

	    	if(titleBeforeEdit == "" || contentBeforeEdit == ""){
	    		return -3;
	    	}
			        
	    	if(PostEdit.insertPostEditInDatabase(userId,postId,titleBeforeEdit,contentBeforeEdit) == false){
	    		return -4;
	    	}
			        
	    	pst = connectionManager.getConnection().prepareStatement("UPDATE post SET title = ?, content = ?, isPublic = ? WHERE postID  = ?");  
	    	pst.setString(1, postTitle);
	    	pst.setString(2, postContent);
	    	pst.setBoolean(3, postIsPublic);
	    	pst.setInt(4, postId);
	    	pst.executeUpdate(); 
	    	pst.close();
	    
	    } catch (SQLException sqlE) {  
	    	//connectionManager.closeConnection();
	    	//System.out.println("Post field missing, throwing SQLException");
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
	    return 0;
	}
	
	public static int deletePostFromDatabaseById(int postId){
		
		 PreparedStatement pst = null; 
	     DbConnection connectionManager = null;
		
	     connectionManager = DbConnection.getInstance();
	     
	     if(connectionManager.getConnection() == null){		   
		    return -1;
		 }
	     	     	     
	     try{
	    	 pst = connectionManager.getConnection().prepareStatement("delete from post where postid = ?");
	    	 pst.setInt(1, postId);
	    	 if(pst.executeUpdate() != 1){
	    		 return -3;
	    	 }
	     }catch(SQLException sqlE){	
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
	
    public boolean reverseEditToPostInDatabase(Blog b, int postPosInBlog, PostEdit currentPostEdit, int currentUserId){
    	PreparedStatement pst = null;
		ResultSet rs = null;
		DbConnection connectionManager = null;
		int postEditId = -1;
		
		
	    connectionManager = DbConnection.getInstance();
    	
	    if(connectionManager.getConnection() == null){
	    	System.out.println("Error with Post Insertion: Unable to establish connection with database.");
	    	return false;
	    }
	    
		//postId, postTitle and postBody must be initialized must be initialized
		
    	try {
			pst = connectionManager.getConnection().prepareStatement("UPDATE post SET title = ?, content = ? WHERE postID  = ?");
			pst.setString(1, currentPostEdit.getTitleBeforeEdit());
			pst.setString(2, currentPostEdit.getContentBeforeEdit());
			pst.setInt(3, this.getPostId());
			pst.execute();
			
			pst.close();
			
			//PostEdit.insertPostEditInDatabase(currentUserId,postId,this.getPostTitle(),this.getPostBody()) == false
			
			 //Inserting the new postEdit row into the postEdit table.
	        pst = connectionManager.getConnection().prepareStatement("insert into postEdit values (0,?,now(),?,?)");
	        pst.setInt(1, this.getPostId());
	        pst.setString(2, this.getPostTitle());
	        pst.setString(3, this.getPostBody());
	        pst.execute();
	        
	        pst.close();
	        
	        //Retrieving the generated value for PostEdit id from the last insert into the postEdit table.
	        
	        pst = connectionManager.getConnection().prepareStatement("select last_insert_id() as postEditId");
	        rs = pst.executeQuery();
	        rs.first();
	        postEditId = rs.getInt("postEditId");
	        
	        rs.close();
	        pst.close();
	        
	        //Inserting a new row into the User_PostEdit table. 
	        
	        pst = connectionManager.getConnection().prepareStatement("insert into user_postedit values(?,?)");
	        pst.setInt(1,currentUserId);
	        pst.setInt(2, postEditId);
	        pst.execute();
	        
	        pst.close();
	        
	        b.getPostAt(postPosInBlog).setPostTitle(currentPostEdit.getTitleBeforeEdit());
	        b.getPostAt(postPosInBlog).setPostBody(currentPostEdit.getContentBeforeEdit());
	        				
		} catch (SQLException sqlE) {
			sqlE.printStackTrace();
			return false;
		}finally{
			try {
				pst.close();
				rs.close();
			} catch (SQLException sqlE) {
				sqlE.printStackTrace();
			}
			
		}
    	
    	return true;
    }
	
	public boolean determinePostEditPrivilege(User u){
		 Boolean editEnabled = true;
		 PreparedStatement pst = null;
		 ResultSet rs = null;
		
		 //if the post is not public and there is no user logged in, the post will never be editable.
		 if(u == null){
		 	editEnabled = false;
		 	return editEnabled;
		 }
		 
		 //if the post is public, it will always be editable.
		 if(isPublic == true){
		 	return editEnabled;
		 }
		 	

		/* 
		   If the current user is not the author of the post and the post is not publicly editable
		   An attempt is made to match current userId with a userId that is associated to the privilegeId of this post.
		 */
		if(!author.equals(u.getUsername())){
			
					
			DbConnection connectionManager = DbConnection.getInstance(); 					
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
					if(u.getUserId() == rs.getInt("userId")){
						editEnabled = true;
						break;
					}
				}
			}catch(SQLException sqlE){
				System.out.println("An exception was thrown while attempting to associate the current user with the edit privileges granted granted for this post.");
				sqlE.printStackTrace();
			}
		}
		return editEnabled;
	 }  
	
}

