package com.amzi.dao;  
  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;  
import java.util.ArrayList;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class Blog { 
	
	private int blogId = -1;
	private int postCount = 0;
	private String author = ""; 
	private String blogTitle = "";
	private ArrayList<Post> postList = new ArrayList<Post>();
	private boolean isPublic = false;
	
	private boolean isEditableMode = false;
	private int toEdit = 0;
	
	public static String errorMessage = null;
	public static String errorMessageFR = null;
	public Blog(){
		
	}
		
	public Blog(String blogTitle, String username, Boolean isPublic){
		Exception blogCreateError = new Exception();
		
		blogTitle = blogTitle.trim();
		username = username.trim();
    	
    	try{
	    	if(blogTitle.equals("")){
	    		System.out.println("Blog does not have a title, throwing blogCreateError.");
	    		errorMessage = "Error with Blog. Missing information";
            	errorMessageFR = "Il y a eu une erreur lors de la creation du Blog. Vérifer que toute l'information est present.";
	    		throw blogCreateError;
	    	}
	   
	    	if(username.equals("")){
	    		System.out.println("Username contains no characters, throwing blogCreateError.");
	    		errorMessage = "Problem with User";
            	errorMessageFR = "Il y a eu une erreur lors de la creation du Blog.";
	    		throw blogCreateError;
	    	}
	    	
	    }catch(Exception e){
    		e.printStackTrace();
    		return;
    	}
    	
    	this.blogTitle = blogTitle;
    	this.author = username;
    	this.isPublic = isPublic;
	}
	
	public Blog(int blogId){
		
		if(blogId > 0){
			this.blogId = blogId;
		}else{
			return;
		}
	}
	
	public boolean getEditMode() {
		return isEditableMode;
	}
	
	public boolean setEditMode(boolean mode , int toEditP) {
		toEdit = toEditP;
		return isEditableMode = mode;
	}
	
	public int getPostCount(){
		return postCount;
	}
	
	protected void setPostCount(int count){
		this.postCount = count;
	}
	
	public int getBlogId(){
		return blogId;
	}
	
	protected void setBlogId(int id){
		this.blogId = id;
	}
	
	public int getToEdit() {
		return toEdit;
	}
	
	protected void setToEdit(int toEdit){
		this.toEdit = toEdit;
	}
	
	
	/*
	protected void setErrorMessage(String message){
		this.errorMessage = message;
	}
	
	
	public String getErrorMessage(){
		return errorMessage;
	}
	*/
	
	public String getBlogTitle(){
		return blogTitle;
	}
	
	protected void setBlogTitle(String title){
		this.blogTitle = title;
	}
	
	public Post getPostAt(int i){
		return postList.get(i);
	}
	
	protected void setPostAt(int i, Post p){
		this.postList.add(i, p);
	}
	
	public String getAuthor(){
		return author;
	}
	
	protected void setAuthor(String author) {          
        this.author = author;
    }
	
	public boolean getIsPublic(){
		return isPublic;
	}
	
	protected void setIsPublic(boolean isPublic){
		this.isPublic = isPublic;
	}
	
	 
	protected void setPostList(ArrayList<Post> posts){
		 this.postList = posts; 
		 this.postCount = posts.size();
	}
	 
	//called at every load of Blog.jsp and BlogEdit.jsp.
	public static Blog getBlogFromDatabaseById(int blogId){
		 Blog b = null;
		 PreparedStatement pst = null; 
	     ResultSet rs = null;
	     DbConnection connectionManager = null;
	        
	     connectionManager = DbConnection.getInstance();
	     
	     try {
			if(connectionManager.getConnection().isValid(0) == false){
			    System.out.println("Error with Blog retrieval by blogId: Unable to establish connection with database.");
			    connectionManager.closeConnection();
			    return null;
			 }
		 } catch (SQLException sqlConE) {
			sqlConE.printStackTrace();
		 }
	        
	     /*
 	 		algorithm to retrieve author of blog, whether user is logged in or using searchBar as public user. 
 	 	
 	 	    Retrieve a row from the blog table as well as an specific user id
 	 	    from the user table based on the blogid value within the user_blog table. 
 		    Retrieve the username value from a row in the user table based on the retrieved value of userId.
	      */
	        
	     try{
	    	 
	         pst = connectionManager.getConnection().prepareStatement("select title, isPublic from blog where blogid = ?");
	         pst.setInt(1, blogId);
	         rs = pst.executeQuery();
	         rs.first();
	         
	         b = new Blog();
	         b.blogTitle = rs.getString("title");
	         b.isPublic = rs.getBoolean("isPublic");
	         b.author = Blog.getBlogAuthorFromDatabaseById(blogId);
	         b.blogId = blogId;
	         
	         rs.close();
	         pst.close();
	
	     }catch(SQLException sqlE){    	 
	    	  
	    	 System.out.println("Error with Blog retrieval by blogId: SQL error");
	    	 
	    	 /*
	          * Since the function getBlogAuthorFromDatabaseById() is called within the try-catch block,
	          * these error messages detect at what point the error was thrown.
	          */
	    	 
	    	//if the query within getBlogFromDatabaseById() failed to produce a result.
	    	 if(b.blogTitle == ""){
	    		 System.out.println("Error with Blog retrieval by blogId: Unable to retrieve Blog contents based on BlogId.");
	    	 }
	    	 
	    	//if the query within getBlogAuthorFromDatabaseById() failed to produce a result.
	    	 if(b.author == ""){
	    		 System.out.println("Error with Blog retrieval by blogId: Unable to retrieve author of Blog based on BlogId.");
	    	 }
	    	  
	    	 sqlE.printStackTrace();
	    	 return null;
	      }finally { 
	    	  try {  
	    		  rs.close();
	              pst.close();  
	           } catch (SQLException e) {  
	        	   e.printStackTrace();  
	           }  
	     }  	
	     
	     if(Post.getPostListFromDatabaseByBlogId(blogId) == null){
	    	 return null;
	     }
	     
	     b.setPostList(Post.getPostListFromDatabaseByBlogId(blogId));
	     return b;
	 }
	 
	public static String getBlogAuthorFromDatabaseById(int blogId){
		 PreparedStatement pst = null;
		 ResultSet rs = null;
		 DbConnection connectionManager = null;
		 String author = null;
		 
		 //gaining access to the shared database connection.
	     connectionManager = DbConnection.getInstance();
	      
	     try {
	     	if(connectionManager.getConnection().isValid(0) == false){
	     		System.out.println("Error with Blog author retrieval by blogId: Unable to establish connection with database.");
	     		connectionManager.closeConnection();
	     		return null;
	     	}
	     } catch (SQLException sqlConE) {
	     	sqlConE.printStackTrace();
	     }
	     
	     
         try{
	         pst = connectionManager.getConnection().prepareStatement("select username from user where userId = "
	        		 												+ "(select u.userId from user u, blog b, user_blog ub"
	                		 										+ " where b.blogId = ? AND b.blogId = ub.blogId AND u.userId = ub.userId)");
	         pst.setInt(1, blogId);
	         rs = pst.executeQuery();
	         rs.first();
	         
	         author = rs.getString("username");
	         
         }catch(SQLException sqlE){
        	 System.out.println("Error with Blog author retrieval by blogId: Unable to find match for BlogId in database.");
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
	 
	//load the blog's id, author and posts from the database from the blogs unique title
	public static int getBlogIdFromDatabaseByTitle(String blogTitle) {          
	        
		PreparedStatement pst = null; 
	    ResultSet rs = null;
	    DbConnection connectionManager = null;
	    int blogId = 0;
	        	        
	    //gaining access to the shared database connection.
	    connectionManager = DbConnection.getInstance();
	    
	    try {
     		if(connectionManager.getConnection().isValid(0) == false){
     			System.out.println("Error with BlogId retrieval by Blog title: Unable to establish connection with database.");
     			connectionManager.closeConnection();
     			return -1;
     		}
	    } catch (SQLException sqlConE) {
     		sqlConE.printStackTrace();
	    }
	    	        
	    try { 
	           	
	        //get the blog's id and isPubic value using the value of the title
	        pst = connectionManager.getConnection().prepareStatement("select blogid from blog where title = ?");
	        pst.setString(1, blogTitle);
	           
	        rs = pst.executeQuery();
	        rs.first();
	            
	        blogId = rs.getInt("blogid");
	           
	    } catch (SQLException sqlE) {  
	    	System.out.println("Error with BlogId retrieval by Blog title: Unable to find match for Blog title named: " + blogTitle + ".");
	    	sqlE.printStackTrace();
	        return -2;
	    } finally { 
	        try {  
	        	rs.close();
	        	pst.close();  
	         } catch (SQLException e) {  
	            e.printStackTrace();  
	         }    
	    }
	    return blogId;
	 }
	
    public static int insertBlogInDatabase(String blogTitle, boolean blogIsPublic, int userId, String username) {          
	
        PreparedStatement pst = null; 
        ResultSet rs = null;
        DbConnection connectionManager = null;
        int blogId = 0;
        int blogIsPublicAsInt = 0;//default value is false
        
        //gaining access to the shared database connection.
        connectionManager = DbConnection.getInstance();
        	
        try {
     		if(connectionManager.getConnection().isValid(0) == false){
     			System.out.println("Error with insertion of Blog into database: Unable to establish connection with database.");
     			connectionManager.closeConnection();
     			return -1;
     		}
     	} catch (SQLException sqlConE) {
     		sqlConE.printStackTrace();
     	}
        	    
        if(blogIsPublic == true){
    		blogIsPublicAsInt = 1;
    	}
        	
        /*
         *  
         *  
         *Algorithm used below
         *  
         *insert blog title and creation date into blog table
		  select blogid from blog table where title matches blogTitle
		  insert blogid and userid into user_blog table			  
         */
        try { 	
        	//insert blog title and creation date into blog table -- The SQL function now(), retrieves the current dateTime value. 
        	//the boolean isPublic needs to be converted to an int value, since the bool datatype is represented as TinyInt(1) by MySQL DBMS.
            pst = connectionManager.getConnection().prepareStatement("insert into blog (blogid, title, creationDateTime, isPublic) values (0, ?, now(), ?)");  
            pst.setString(1,blogTitle);
            pst.setInt(2, blogIsPublicAsInt);
            pst.executeUpdate(); 
            
            //closing the connection to prepare for the next prepared statement.
            pst.close();
             
            //Retrieving the generated value for blogId from the last insert into the blog table.
	        pst = connectionManager.getConnection().prepareStatement("select last_insert_id() as blogId");
	        rs = pst.executeQuery();
	        rs.first();
	        blogId = rs.getInt("blogId");
                        
            rs.close();
            pst.close();
             
            //insert blogid and userid into user_blog table
            pst = connectionManager.getConnection().prepareStatement("insert into user_blog (userid, blogid) values(?, ?)");
            pst.setInt(1,userId);
            pst.setInt(2, blogId);
            pst.executeUpdate();
            pst.close();
        }catch (MySQLIntegrityConstraintViolationException sqlIntConViolE){
        	System.out.println( "Error with insertion of Blog into database: The entered value for blog title is not unique.");
        	sqlIntConViolE.printStackTrace();
        	return -3;
        } catch (SQLException sqlE) {  
        	System.out.println("Error with insertion of Blog into database: SQL error.");
        	sqlE.printStackTrace();
        	return -2;
        }finally{ 
        //the connection the connectionManager object interacts with, is closed at logout. 
           try {  
        	  //if the blog title enter is not unique and an error is thrown the result set in never initialized.
        	  if(rs != null){
        		  rs.close();
        	  }
               pst.close();  
           } catch (SQLException e) {  
        	   e.printStackTrace();  
           }  
        }    
        return blogId;
    } 
    
    /*Fulfills the update portion of crud functionality, since the title is the only editable portion of a blog*/
    public static int updateTitleInDatabase(String newTitle, int blogId){
    	PreparedStatement pst = null; 
    	DbConnection connectionManager = null;
          
        connectionManager = DbConnection.getInstance();
	    
        try {
     		if(connectionManager.getConnection().isValid(0) == false){
     			System.out.println("Error with updating Blog title: Unable to establish connection with database.");
     			connectionManager.closeConnection();
     			return -1;
     		}
	    } catch (SQLException sqlConE) {
     		sqlConE.printStackTrace();
	    }
        
        try {
			pst = connectionManager.getConnection().prepareStatement("UPDATE Blog set title = ? where blogid = ?");
			pst.setString(1, newTitle);
			pst.setInt(2, blogId);
			//if one row was affected by the update, change the title of this blog to the new value..
			if(pst.executeUpdate() != 1){
				//rollback
			}				
       } catch (SQLException sqlE) {
    	   System.out.println("Error with updating Blog title: SQL error.");
    	   sqlE.printStackTrace();
    	   return -2;
       }finally{
			//the connection the connectionManager object interacts with, is closed at logout. 
	        try {  
	        	pst.close();  
	        } catch (SQLException sqlE) {  
	            sqlE.printStackTrace();  
	        }  
	        
       }
    	return 0;
   }

    public static int deleteBlogFromDatabaseById(int blogId){
    	PreparedStatement pst = null;
    	DbConnection connectionManager = null;
    	
    	connectionManager = DbConnection.getInstance();
    	
    	try {
      		if(connectionManager.getConnection().isValid(0) == false){
      			System.out.println("Error with Blog deletion by BlogId: Unable to establish connection with database.");
      			connectionManager.closeConnection();
      			return -1;
      		}
 	    } catch (SQLException sqlConE) {
      		sqlConE.printStackTrace();
 	    }
    	
    	try {
			pst = connectionManager.getConnection().prepareStatement("delete from blog where blogId = ?");
			pst.setInt(1, blogId);
			pst.executeUpdate();
		} catch (SQLException sqlE) {
			System.out.println("Error with Blog deletion by BlogId: SQL error.");
			sqlE.printStackTrace();
			return -2;
		}finally{
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return 0;
    }
    
    public static int addBlogToBlogDeleted(int blogId){
		PreparedStatement ps = null; 
	    DbConnection connectionManager = null;
	    
	    if(blogId <= 0){
	    	System.out.println("Error adding Blog to BlogDeleted table: Provided value for blogId is invalid.");
	    	return -3;
	    }
	    
	    connectionManager = DbConnection.getInstance();
	    
	    try {
      		if(connectionManager.getConnection().isValid(0) == false){
      			System.out.println("Error adding Blog to BlogDeleted table: Unable to establish connection with database.");
      			connectionManager.closeConnection();
      			return -1;
      		}
 	    } catch (SQLException sqlConE) {
      		sqlConE.printStackTrace();
 	    }
	    
	    try{
		   ps = connectionManager.getConnection().prepareStatement("insert into blogdeleted(blogDeletedId, blogId) values(0,?)");
		   ps.setInt(1,blogId);
		   ps.execute();
	    }catch(SQLException slqE){
		   System.out.println("Error adding Blog to BlogDeleted table: SQL Error");
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
    
    public static int checkForDeletion(int blogId){
    	PreparedStatement ps = null; 
	    ResultSet rs = null;
	    DbConnection connectionManager = null;
		
	    if(blogId <= 0){
	    	System.out.println("Error with Blog deletion check: Provided value for blogId is invalid.");
	    	return -3;
	    }
	    
	    connectionManager = DbConnection.getInstance();
	    
	    try {
      		if(connectionManager.getConnection().isValid(0) == false){
      			System.out.println("Error with blog deletion check: Unable to establish connection with database.");
      			connectionManager.closeConnection();
      			return -1;
      		}
 	    } catch (SQLException sqlConE) {
      		sqlConE.printStackTrace();
 	    }
	    
	    
	    
	    try{
	   
		   ps = connectionManager.getConnection().prepareStatement("select blogId from blogDeleted where blogId = ?");
		   ps.setInt(1, blogId);
		   
		   rs = ps.executeQuery();
		   
		   rs.last();
		   
		  //if the result set contains any rows, this blog has been deleted and has been added to the postDeleted table.
		   if(rs.getRow() > 0){
			   return 1;
		   }
	    
	    }catch(SQLException sqlE){
		   System.out.println("Error with Blog deletion check: SQL Error");
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

}

