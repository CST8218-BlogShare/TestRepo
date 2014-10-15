package com.amzi.dao;  
  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;  
import java.util.ArrayList;

public class Blog { 
	
	//private String errorMessage = null;
	
	private int blogId = -1;
	private int postCount = 0;
	private String author = null; 
	private String blogTitle = null;
    private ArrayList<String> postTitleList = new ArrayList<String>();
	private ArrayList<String> postBodyList = new ArrayList<String>();
	
	private boolean isEditableMode = false;
	private int toEdit = 0;
	
	private String errorMessage = null;
	
	public Blog(){
		
	}
	
	public Blog(String blogTitle){
		Exception blogCreateError = new Exception();
		
		blogTitle = blogTitle.trim();
    	//blogPostTitle = blogPostTitle.trim();
    	//blogPostBody = blogPostBody.trim();
    	
    	try{
	    	if(blogTitle.equals("")){
	    		System.out.println("Blog Has no tittle, throwing java.lang.Exception.");
	    		//errorMessage = "Error with Post. No Post Title was not entered";
	    		throw blogCreateError;
	    	}
	    	
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	this.blogTitle = blogTitle;
	}
	
	public boolean setEditMode(boolean mode , int toEditP) {
		toEdit = toEditP;
		return isEditableMode = mode;
	}

	public boolean getEditMode() {
		return isEditableMode;
	}
	
	public void setPostCount(int count){
		this.postCount = count;
	}
	
	public int getPostCount(){
		return postCount;
	}
	
	public int getBlogId(){
		return blogId;
	}
	
	public int getToEdit() {
		return toEdit;
	}
	
	public String getErrorMessage(){
		return errorMessage;
	}
	
	public String getBlogTitle(){
		return blogTitle;
	}
	
	public String getPostTitleAt(int i){
		return postTitleList.get(i);
	}
	
	public String getPostBodyAt(int i){
		return postBodyList.get(i);
	}
	
	public boolean setAuthor(int userId) {          
        
    	boolean status = true;  
        PreparedStatement pst = null; 
        ResultSet rs = null;
        DbConnection connectionManager = null;
        
        try {  
        	
        	//gaining access to the shared database connection.
        	connectionManager = DbConnection.getInstance();
        	
        	pst = connectionManager.getConnection().prepareStatement("select username from user where userid = '"+userId+"' ");
        	rs = pst.executeQuery();
        	rs.first();
        	this.author = rs.getString("username");
        	rs.close();
        	pst.close();
        	      	
        } catch (SQLException sqlE) {  
        	
        	connectionManager.closeConnection();
        	sqlE.printStackTrace();
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
	
	public String getAuthor(){
		return author;
	}
	
	 public void addPost(String postTitle, String postBody){
	    	postTitleList.add(postTitle);
	    	postBodyList.add(postBody);

	    }
	
    public boolean insertBlogInDatabase(int userId) {          
	
        PreparedStatement pst = null; 
        ResultSet rs = null;
        DbConnection connectionManager = null;
        
        boolean status = true;  
        
        /*
         * The blog object used to call this function needs to call the appropriate constructor to have
           have its blogTitle initialized before calling the insertBlogInDatabase function.
        */
        
        if(this.blogTitle == null){
        	return false;
        }
        
       /* if(userId == -1 ){
        	return false;
        }*/
        
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
			  
			  
			  taken out atm.....
			  insert post title, blogid, content, creation date into post table
			  select postid from post table where blogid and title are matched
			  insert postid and user id into user_post
			  
        	 */
        	
        	//insert blog title and creation date into blog table
            pst = connectionManager.getConnection().prepareStatement("insert into blog values( 0, '"+blogTitle+"', curdate() )");  
            pst.executeUpdate(); 
            //closing the connection to prepare for the next prepared statement.
            pst.close();
            
            //select blogid from blog table where title matches inserted value
            pst = connectionManager.getConnection().prepareStatement("select blogid from blog where title = '"+blogTitle+"' ");
            rs = pst.executeQuery();
            rs.first();
            this.blogId = rs.getInt("blogId");
            rs.close();
            pst.close();
            
            
            //insert blogid and userid into user_blog table
            pst = connectionManager.getConnection().prepareStatement("insert into user_blog values('"+userId+"', '"+blogId+"')");
            pst.executeUpdate();
            pst.close();
         
        } catch (SQLException sqlE) {  
        	
        	
        	connectionManager.closeConnection();
        	System.out.println("Blog field missing, throwing SQLException");
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
     
    //load the blog's id, author and posts from the database from the blogs unique title
    public boolean buildBlogFromTitle(String blogTitle) {          
        
    	boolean status = true;  
        PreparedStatement pst = null; 
        ResultSet rs = null;
        DbConnection connectionManager = null;
        
        	//gaining access to the shared database connection.
        	connectionManager = DbConnection.getInstance();
        try { 	
        	if(this.blogId == -1){
        		
        		//get the blog's id with the title
        		pst = connectionManager.getConnection().prepareStatement(" select blogid from blog where title = ?");
        		pst.setString(1, blogTitle);
            	rs = pst.executeQuery();
            	rs.first();
            	this.blogId = rs.getInt("blogid");
            	this.blogTitle = blogTitle;
            	rs.close();
            	pst.close();
        		
        	}
        	
        	//get the blog's posts and their bodies from the database using the blogid
        	pst = connectionManager.getConnection().prepareStatement("select title, content from post where blogid = '"+blogId+"' ");
        	rs = pst.executeQuery();
        	rs.last();
        	postCount = rs.getRow();
        	rs.beforeFirst();
        	
    
        	while(rs.next()){
        			postTitleList.add(rs.getString("title")); 
        			postBodyList.add(rs.getString("content"));
        	}
        	rs.close();
        	pst.close();
        	
        } catch (SQLException sqlE) {  
        	
        	
        	connectionManager.closeConnection();
        	
        	
        	/*System.out.println("Blog field missing, throwing SQLException");
        	sqlE.printStackTrace();
        	setErrorMessage("Error with previous login attempt. Incorrect Username and Password.");*/
        	
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

