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
            	errorMessageFR = "Il y a eu une erreur lors de la creation du Post. V�rifer que toute l'information est present.";
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
	
	public boolean setEditMode(boolean mode , int toEditP) {
		toEdit = toEditP;
		return isEditableMode = mode;
	}

	public boolean getEditMode() {
		return isEditableMode;
	}
	
	protected void setPostCount(int count){
		this.postCount = count;
	}
	
	public int getPostCount(){
		return postCount;
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
	
	public String getErrorMessage(){
		return errorMessage;
	}
	
	public String getBlogTitle(){
		return blogTitle;
	}
	
	protected void setBlogTitle(String title){
		this.blogTitle = title;
	}
	
	public Post getPostAt(int i){
		return postList.get(i);
	}
	
	protected void setAuthor(String author) {          
        this.author = author;
    }
	
	public String getAuthor(){
		return author;
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
		 
	 protected void addPost(Post p){
		 postList.add(p);
		 ++postCount;
	 }
	 
	 protected void removePost(int postAt){
		 postList.remove(postAt);
		 --postCount;
	 }
	 
	 public void buildBlogFromId(int id){
		 PreparedStatement pst = null; 
	     ResultSet rs = null;
	     DbConnection connectionManager = null;
	     int userId = -1;
	        
	     connectionManager = DbConnection.getInstance();
	        
	     this.blogId = id;
	        
	     try{
	    	 //retrieve userId based on blogId value within user_blog table
	         pst = connectionManager.getConnection().prepareStatement("select b.title as blogTitle, b.isPublic as isPublic, u.userId as userId from blog b, user_blog ub, user u where b.blogId = '"+blogId+"' AND b.blogId = ub.blogId AND u.userId = ub.userId");
	         rs = pst.executeQuery();
	         rs.first();
	         userId = rs.getInt("userid");
	         blogTitle = rs.getString("blogTitle");
	         isPublic = rs.getBoolean("isPublic");
	         rs.close();
	         pst.close();
	        	
	         //retrieve username from user table based on the retrieved value of userId and assign value to blog's author data member.
	         pst = connectionManager.getConnection().prepareStatement("select username from user where userId = '"+userId+"' ");
	         rs = pst.executeQuery();
	         rs.first();
	         this.author = rs.getString("username");
	         rs.close();
	         pst.close();
	        	
	         //get the blog's posts and their bodies from the database using the blogid
	         pst = connectionManager.getConnection().prepareStatement("select postId, title, content, isPublic from post where blogid = '"+blogId+"' ");
	         rs = pst.executeQuery();
	        	
	         while(rs.next()){
	        	 Post p = new Post();
	        	 p.setBlogId(blogId);
	        	 p.setPostId(rs.getInt("postId"));
	        	 p.setPostTitle(rs.getString("title"));
	        	 p.setPostBody(rs.getString("content"));
	        	 p.setIsPublic(rs.getBoolean("isPublic"));
	        	 //p.setCreationDateTime(rs.getString("creationDateTime"));
	        			
	        	 //selecting the author of the current post, by checking the contents of the user_post table to retireve, the userId associated with the post, and then selecting the username based on that userId. 
	        	 PreparedStatement authorPst = connectionManager.getConnection().prepareStatement("select username from user where userId = (select u.userId from user u, post p, user_post up where p.postId = '"+p.getPostId()+"' AND u.userId = up.userId AND p.postId = up.postId)");
	        	 ResultSet authorRs = authorPst.executeQuery();
	        	 authorRs.first();
	        	 p.setAuthor(authorRs.getString("username")); 
	        	 authorPst.close();
	        	 authorRs.close();
	        			
	        	addPost(p);
	        }
	        
	        rs.close();
	        pst.close();
	        
	        }catch(Exception e){
	        	 e.printStackTrace(); //may not be necessary
	             //status = false;
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
	 }
	 
	 //load the blog's id, author and posts from the database from the blogs unique title
	 public boolean buildBlogFromTitle(String blogTitle) {          
	        
	    	boolean status = true;  
	        PreparedStatement pst = null; 
	        ResultSet rs = null;
	        DbConnection connectionManager = null;
	        int userId = -1;
	        
	        //need to instantiate postId,blogId,title,content,creationDate,author
	        
	        	//gaining access to the shared database connection.
	        	connectionManager = DbConnection.getInstance();
	        try { 
	        	
	        	/*
	    	 	algorithm to retrieve author of blog, whether user is logged in or using searchBar as public user. 
	    	 	
	    	 	retrieve blogId from blog table based on the title value
	    		retrieve userId based on blogid value within the user_blog table 
	    		retrieve username from user table based on the retrieved value of userId
	    	*/
	        	this.blogTitle = blogTitle;
	        	
	        	
	        	//get the blog's id and isPubic value using the value of the title
	        	pst = connectionManager.getConnection().prepareStatement("select blogid, isPublic from blog where title = ?");
	        	pst.setString(1, blogTitle);
	            rs = pst.executeQuery();
	            rs.first();
	            this.blogId = rs.getInt("blogid");
	            this.isPublic = rs.getBoolean("isPublic");
	            rs.close();
	            pst.close();
	        	
	        	
	        	//retrieve userId based on blogId value within user_blog table
	        	pst = connectionManager.getConnection().prepareStatement("select u.userId as userId from blog b, user_blog ub, user u where b.blogId = '"+blogId+"' AND b.blogId = ub.blogId AND u.userId = ub.userId");
	        	rs = pst.executeQuery();
	        	rs.first();
	        	userId = rs.getInt("userid");
	        	rs.close();
	        	pst.close();
	        	
	        	//retrieve username from user table based on the retrieved value of userId and assign value to blog's author data member.
	        	pst = connectionManager.getConnection().prepareStatement("select username from user where userId = '"+userId+"' ");
	        	rs = pst.executeQuery();
	        	rs.first();
	        	this.author = rs.getString("username");
	        	rs.close();
	        	pst.close();
	        	
	        	//get the blog's posts and their bodies from the database using the blogid
	        	pst = connectionManager.getConnection().prepareStatement("select postId, title, content, isPublic from post where blogid = '"+blogId+"' ");
	        	rs = pst.executeQuery();
	        	
	        	while(rs.next()){
	        			Post p = new Post();
	        			p.setBlogId(blogId);
	        			p.setPostId(rs.getInt("postId"));
	        			p.setPostTitle(rs.getString("title"));
	        			p.setPostBody(rs.getString("content"));
	        			p.setIsPublic(rs.getBoolean("isPublic"));
	        			//p.setCreationDateTime(rs.getString("creationDateTime"));
	        			
	        			//selecting the author of the current post, by checking the contents of the user_post table to retireve, the userId associated with the post, and then selecting the username based on that userId. 
	        			PreparedStatement authorPst = connectionManager.getConnection().prepareStatement("select username from user where userId = (select u.userId from user u, post p, user_post up where p.postId = '"+p.getPostId()+"' AND u.userId = up.userId AND p.postId = up.postId)");
	        			ResultSet authorRs = authorPst.executeQuery();
	        			authorRs.first();
	        			p.setAuthor(authorRs.getString("username")); 
	        			authorPst.close();
	        			authorRs.close();
	        			
	        			addPost(p);
	        	}
	        	rs.close();
	        	pst.close();
	        	
	        } catch (SQLException sqlE) {  
	        	
	        	sqlE.printStackTrace();
	        	connectionManager.closeConnection();
	        	
	        	
	        	/*System.out.println("Blog field missing, throwing SQLException");
	        	sqlE.printStackTrace();
	        	setErrorMessage("Error with previous login attempt. Incorrect Username and Password.");*/
	        	
	        	status = false;
	        }catch(Exception e){
	        	 e.printStackTrace(); //may not be necessary
	             status = false;
	        } finally { 
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
	
    public static boolean insertBlogInDatabase(Blog b,int userId) {          
	
        PreparedStatement pst = null; 
        ResultSet rs = null;
        DbConnection connectionManager = null; 
        
        try {  
        	
        	//gaining access to the shared database connection.
        	connectionManager = DbConnection.getInstance();
  
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
            pst = connectionManager.getConnection().prepareStatement("insert into blog values(0, '"+b.getBlogTitle()+"', now(), '"+b.getIsPublicAsInt()+"')" );  
            pst.executeUpdate(); 
            //closing the connection to prepare for the next prepared statement.
            pst.close();
            
            //select blogid from blog table where title matches inserted value
            pst = connectionManager.getConnection().prepareStatement("select blogid from blog where title = '"+b.getBlogTitle()+"' ");
            rs = pst.executeQuery();
            rs.first();
            b.setBlogId(rs.getInt("blogId"));
            rs.close();
            pst.close();
             
            //insert blogid and userid into user_blog table
            pst = connectionManager.getConnection().prepareStatement("insert into user_blog values('"+userId+"', '"+b.getBlogId()+"')");
            pst.executeUpdate();
            pst.close();
            
        } catch (SQLException sqlE) {  
        	
        	
        	connectionManager.closeConnection();
        	System.out.println("Blog field missing, throwing SQLException");
        	sqlE.printStackTrace();
        	//errorMessage = "Error with previous login attempt. Incorrect Username and Password.";
        	
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
}  

