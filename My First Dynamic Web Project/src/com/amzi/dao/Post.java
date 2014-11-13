package com.amzi.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;

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
	
	/*public Post(int postId, int blogId, String postTitle, String postBody, String username, boolean isPublic) {
		
		postTitle = postTitle.trim();
		postBody = postBody.trim();
		username = username.trim();
		
		if(postId == -1){
			System.out.println("PostId value has not been initialized.");
			return;
		}
		
		if(blogId == -1){
			System.out.println("BlogId value has not been initialized.");
			return;
		}
		
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
		
		this.postId = postId;
		this.blogId = blogId;
		this.postTitle = postTitle;
		this.postBody = postBody;
		this.author = username;
		this.isPublic = isPublic;
	}*/
	
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
	
	public int getIsPublicAsInt() {
		if(isPublic == true){
			return 1;
		}
		
		return 0;
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
	public void buildPostFromId(int postId){
		 PreparedStatement pst = null; 
	     ResultSet rs = null;
	     DbConnection connectionManager = null;
	     
	     this.postId = postId;
	     connectionManager = DbConnection.getInstance();
	     
	     try {
			pst = connectionManager.getConnection().prepareStatement("select blogid, title, content, isPublic from post where postid = "+this.postId+" ");
			rs = pst.executeQuery();
			rs.first();
			this.blogId = rs.getInt("blogId");
			this.postTitle = rs.getString("title");
			this.postBody = rs.getString("content");
			this.isPublic = rs.getBoolean("isPublic");
		} catch (SQLException sqlE) {
			sqlE.printStackTrace();
		}
	}
		
	public boolean insertPostInDatabase(int userId, Blog b, boolean editMode) {          
		 
	        PreparedStatement pst = null; 
	        ResultSet rs = null;
	        DbConnection connectionManager = null;
	        Connection conn = null;
	       
	        /*
	         * The post object used to call this function needs to call the appropriate constructor to have its
	           postTitle and postBody data members initialized before calling the insertPostInDatabase function.
	        */
	        
	        if(this.postTitle == null){
	        	System.out.println("The title of the post object in use, has not been initialized");
	        	return false;
	        }
	        
	        if(this.postBody == null){
	        	System.out.println("The body of the post object in use, has not been initialized");
				return false;
	        }
	        
	        if(userId == -1){
				System.out.println("The userId value that is to be related to this post, has not been initialized");
				return false;
	        }
	        
	        if(b == null){
				System.out.println("The blog object that will be related to this post, has not been initialized");
				return false;
	        }
	        
	        try {  
	        	
	        	
	        	//gaining access to the shared database connection.
	        	connectionManager = DbConnection.getInstance();
	        	conn = connectionManager.getConnection();
	        	/*
	        	 * If the blog object b has been successfully initialized, 
	        	 * the blogId object is guaranteed to be set to a value other than -1.
	        	 */
	        	
	        	this.blogId = b.getBlogId();
	        	
	        	
	        	/* 
	        	 *Algorithm used below
	        	 *  
	        	 	Removed! 
	        	 		select title from post table based on a match to the title and blogid of the post to be inserted.
	        	 			if a match is found, do not insert this post into the database.  
	        	 		
	        	 		
	        	 	insert post title, blogid content, creation date into post table
				    select postid from post table where blogid and title is the same
				    insert postid and user id into user_post
	        	 */
	        	
	        	/*
	        	  
		        	//checking within current blog for a post with the same title as the one that is to be created.
	        		pst = conn.prepareStatement("select title from post where title = '"+postTitle+"' AND blogId = '"+blogId+"' ");
	        		rs = pst.executeQuery();
	        		
	        		//If there is an entry within the resultSet, then a post with an identical title already exists within the Blog.
	        		if(rs.next() == true){
	        			System.out.println("This post cannot be created.");
	        			System.out.println("A post with the title " + postTitle  + " already exists within " + b.getBlogTitle());
	        			//No need to close the ResultSet or PreparedStatement objects as close() is called within the finally clause.
	        			return false;
	        		}
	        		
	        		//closing the ResultSet and PreparedStatment objects, in order to prepare for the next database query.
	        		rs.close();
	        		pst.close();
	        		
	        	*/
	        		
	        		//checking if the post is part of a blog that is publicly editable.
	        		if(b.getIsPublic() && b.getPostCount() != 0){
	        			this.setIsPublic(true);
	        		}
	        		
	        		if(!editMode){
		        		/* Insert post title, blogid content, creation date into post table 
		        		 * The SQL function now(), retrieves the current dateTime value.
		        		 * the boolean isPublic needs to be converted to an int value, since the bool datatype is represented as TinyInt(1) by MySQL DBMS.*/
				        pst = conn.prepareStatement("insert into post values( 0, '"+blogId+"','"+postTitle+"','"+postBody+"', now(), '"+getIsPublicAsInt()+"' )");  
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
				        pst = conn.prepareStatement("insert into user_post values('"+userId+"', '"+postId+"') ");
				        pst.executeUpdate();
				        pst.close();
				        		        	
				        //b.addPost(postTitle, postBody);
				        b.addPost(this); 
	        		}else if( editMode ){
	        			String titleBeforeEdit = "";
	        			String contentBeforeEdit = "";
	        			
				        this.postId = b.getPostAt(b.getToEdit()).getPostId();
				        
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
				        	return false;
				        }
				        
				        if(PostEdit.insertPostEditIntoDatabase(userId,postId,titleBeforeEdit,contentBeforeEdit) == false){
				        	return false;
				        }
				        
				        pst = conn.prepareStatement("UPDATE post SET title = ?, content = ?, isPublic = ? WHERE postID  = ?");  
			            pst.setString(1, postTitle);
			            pst.setString(2, postBody);
			            pst.setBoolean(3, isPublic);
			            pst.setInt(4, postId);
		        		pst.executeUpdate(); 
			            pst.close();
			            
			            b.getPostAt(b.getToEdit()).postTitle = postTitle;
			            b.getPostAt(b.getToEdit()).postBody = postBody;
			            b.getPostAt(b.getToEdit()).isPublic = isPublic;
	        		}
	        } catch (SQLException sqlE) {  
	        	
	        	connectionManager.closeConnection();
	        	//System.out.println("Post field missing, throwing SQLException");
	        	sqlE.printStackTrace();
	        	return false;
	        }
	         finally {
	        	//the connection the connectionManager object interacts with, is closed at logout. 
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
	        return true;  
	    }

    public boolean editPostInDatabase(Blog b, int postPosInBlog, PostEdit currentPostEdit, int currentUserId){
    	PreparedStatement pst = null;
		ResultSet rs = null;
		DbConnection connectionManager = null;
		int postEditId = -1;
		
		connectionManager = DbConnection.getInstance();
    	
		//postId, postTitle and postBody must be initialized must be initialized
		
    	try {
			pst = connectionManager.getConnection().prepareStatement("UPDATE post SET title = ?, content = ? WHERE postID  = ?");
			pst.setString(1, currentPostEdit.getTitleBeforeEdit());
			pst.setString(2, currentPostEdit.getContentBeforeEdit());
			pst.setInt(3, this.getPostId());
			pst.execute();
			
			pst.close();
			
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
	
	public boolean removePostFromDatabase(Blog b, int postPos){
		
		 PreparedStatement pst = null; 
	     ResultSet rs = null;
	     DbConnection connectionManager = null;
	     int postId = -1; 
		
	     connectionManager = DbConnection.getInstance();
	     
	     postId = b.getPostAt(postPos).getPostId();
	     
	     if(postId == -1){
	    	 return false;
	     }
	     
	     try{
	    	 pst = connectionManager.getConnection().prepareStatement("delete from post where postid = ?");
	    	 pst.setInt(1, postId);
	    	 if(pst.executeUpdate() == 1){
	    		 b.removePost(postPos);
	    	 }
	     }catch(SQLException sqlE){	
	    	 sqlE.printStackTrace();
	    	 return false;
	     }finally {
	        	//the connection the connectionManager object interacts with, is closed at logout. 
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
		return true;
	}
	
	public boolean determinePostEditPrivilege(User u){
		 Boolean editEnabled = true;
		 PreparedStatement pst = null;
		 ResultSet rs = null;
			
		 //if the post is public, it will always be editable.
		 if(isPublic == true){
		 	return editEnabled;
		 }
		 	
		 //if the post is not public and there is no user logged in, the post will never be editable.
		 if(u == null){
		 	editEnabled = false;
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
																		 " p.postid = '"+postId+"'"); 
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

