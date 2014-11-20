package com.amzi.dao;  
  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;  
import java.util.ArrayList;

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
	    		errorMessage = "Error with Post. Missing information";
            	errorMessageFR = "Il y a eu une erreur lors de la creation du Post. Vérifer que toute l'information est present.";
	    		throw blogCreateError;
	    	}
	   
	    	if(username.equals("")){
	    		System.out.println("Username contains no characters, throwing blogCreateError.");
	    		errorMessage = "Problem with User";
            	errorMessageFR = "Il y a eu une erreur lors de la creation du Post.";
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
	
	//I don't like that these are public...at all.
	
	 public void addPost(Post p){
		 postList.add(p);
		 ++postCount;
	 }
	 
	 public void removePost(int postAt){
		 postList.remove(postAt);
		 --postCount;
	 }
	 
	 protected void setPostList(ArrayList<Post> posts){
		 this.postList = posts; 
		 this.postCount = posts.size();
	 }
	 
	 public static Blog getBlogFromDatabaseById(int blogId){
		 Blog b = null;
		 PreparedStatement pst = null; 
	     ResultSet rs = null;
	     DbConnection connectionManager = null;
	        
	     connectionManager = DbConnection.getInstance();
	     
	     if(connectionManager.getConnection() == null){
	        	System.out.println("Error with Blog Retrieval: Unable to establish connection with database.");
	        	return null;
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
	    	  if(b == null){
	    		  System.out.println("Error with Blog Retrieval: Unable to retrieve value of UserId based on BlogId");
	    	  }
	    	  
	    	  if(b.blogTitle == ""){
	    		  System.out.println("Error with Blog Retrieval: Unable to retrieve Blog contents based on BlogId");
	    	  }
	    	  
	    	  if(b.author == ""){
	    		  System.out.println("Error with Blog Retrieval: Unable to retrieve author of blog based on UserId");
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
	        
	     if(connectionManager.getConnection() == null){
	        //System.out.println("Error with BlogId Retrieval: Unable to establish connection with database.");
	        return null;
	     }
	     
	  /*
	     //retrieve userId based on blogId value within user_blog table
         pst = connectionManager.getConnection().prepareStatement("select u.userId as userId from blog b, user_blog ub, user u"
        		 												+ " where b.blogId = ? AND b.blogId = ub.blogId AND u.userId = ub.userId");

         pst.setInt(1,blogId);
         rs = pst.executeQuery();
         rs.first();
         
         userId = rs.getInt("userid");
         
         rs.close();
         pst.close();
         
      */
         try{
	         pst = connectionManager.getConnection().prepareStatement("select username from user where userId = "
	        		 												+ "(select u.userId from user u, blog b, user_blog ub"
	                		 										+ " where b.blogId = ? AND b.blogId = ub.blogId AND u.userId = ub.userId)");
	         pst.setInt(1, blogId);
	         rs = pst.executeQuery();
	         rs.first();
	         
	         author = rs.getString("username");
         }catch(SQLException sqlE){
        	 sqlE.printStackTrace();
        	 return null;
         }finally{
        	 try {
				rs.close();
				pst.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
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
	        
	        if(connectionManager.getConnection() == null){
	        	System.out.println("Error with BlogId Retrieval: Unable to establish connection with database.");
	        	return -1;
	        }
	        
	        try { 
	           	
	        	//get the blog's id and isPubic value using the value of the title
	        	pst = connectionManager.getConnection().prepareStatement("select blogid from blog where title = ?");
	        	pst.setString(1, blogTitle);
	           
	        	rs = pst.executeQuery();
	            rs.first();
	            
	            blogId = rs.getInt("blogid");
	           
	        } catch (SQLException sqlE) {  
	        	blogId = -2;
	        	sqlE.printStackTrace();
	        	System.out.println("Error with BlogId Retrieval: Unable to find match for Blog title named: " + blogTitle + ".");
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
        
        try {  
        	
        	//gaining access to the shared database connection.
        	connectionManager = DbConnection.getInstance();
  
        	if(connectionManager.getConnection() == null){
	        	System.out.println("Error with Blog Insertion: Unable to establish connection with database.");
	        	return -1;
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
        	
        	//insert blog title and creation date into blog table -- The SQL function now(), retrieves the current dateTime value. 
        	//the boolean isPublic needs to be converted to an int value, since the bool datatype is represented as TinyInt(1) by MySQL DBMS.
            pst = connectionManager.getConnection().prepareStatement("insert into blog values(0, ?, now(), ?)");  
            pst.setString(1,blogTitle);
            pst.setInt(2, blogIsPublicAsInt);
            pst.executeUpdate(); 
            
            //closing the connection to prepare for the next prepared statement.
            pst.close();
            
            /*
            //select blogid from blog table where title matches inserted value
            pst = connectionManager.getConnection().prepareStatement("select blogid from blog where title = ?");
            pst.setString(1, b.getBlogTitle());*/
            
            //Retrieving the generated value for blogId from the last insert into the blog table.
	        pst = connectionManager.getConnection().prepareStatement("select last_insert_id() as blogId");
	        rs = pst.executeQuery();
	        rs.first();
	        blogId = rs.getInt("blogId");
                        
            rs.close();
            pst.close();
             
            //insert blogid and userid into user_blog table
            pst = connectionManager.getConnection().prepareStatement("insert into user_blog values(?, ?)");
            pst.setInt(1,userId);
            pst.setInt(2, blogId);
            pst.executeUpdate();
            pst.close();
            
        } catch (SQLException sqlE) {  
        	//connectionManager.closeConnection();
        	System.out.println("Blog field missing, throwing SQLException");
        	sqlE.printStackTrace();
        	//errorMessage = "Error with previous login attempt. Incorrect Username and Password.";
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
    
    /*Fulfills the update portion of crud functionality, since the title is the only editable portion of a blog */
    public boolean updateTitleInDatabase(int blogId, String newTitle){
    	PreparedStatement pst = null; 
    	DbConnection connectionManager = null;
          
        connectionManager = DbConnection.getInstance();
        
       try {
			pst = connectionManager.getConnection().prepareStatement("UPDATE Blog set title = ? where blogid = ?");
			pst.setString(1, newTitle);
			pst.setInt(2, blogId);
			//if one row was affected by the update, change the title of this blog to the new value..
			if(pst.executeUpdate() == 1){
				setBlogTitle(newTitle);
			}
			
			
			
       } catch (SQLException sqlE) {
    	   sqlE.printStackTrace();
    	   return false;
       }finally{
			//the connection the connectionManager object interacts with, is closed at logout. 
	        if (pst != null) {  
	            try {  
	                pst.close();  
	            } catch (SQLException sqlE) {  
	                sqlE.printStackTrace();  
	            }  
	        } 
       }
    	return true;
   }

    public static boolean deleteBlogFromDatabase(String blogTitle){
    	PreparedStatement pst = null;
    	DbConnection connectionManager = null;
    	
    	connectionManager = DbConnection.getInstance();
    	
    	try {
			
			pst = connectionManager.getConnection().prepareStatement("delete from blog where title = ?");
			pst.setString(1, blogTitle);
			pst.executeUpdate();
	    	
		} catch (SQLException sqlE) {
			sqlE.printStackTrace();
			return false;
		}finally{
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return true;
    }

}

