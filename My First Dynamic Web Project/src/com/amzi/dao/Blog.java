package com.amzi.dao;  
  
 import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;  
import java.util.ArrayList;

  
public class Blog { 
	
	//private String errorMessage = null;
	
	private boolean newPost = false;
	private int blogId = -1;
	private int postCount = 0;
	private String author = null; 
	private String blogTitle = null;
	private String blogPostTitle = null;
	private String blogPostBody = null;
    private ArrayList<String> postTitleList = new ArrayList<String>();
	private ArrayList<String> postBodyList = new ArrayList<String>();
	
	private String errorMessage = null;
	
	public Blog(){
		
	}
	
	public Blog(String blogTitle, String blogPostTitle, String blogPostBody){
		Exception blogCreateError = new Exception();
		
		blogTitle = blogTitle.trim();
    	blogPostTitle = blogPostTitle.trim();
    	blogPostBody = blogPostBody.trim();
    	
    	try{
	    	if(blogTitle.equals("")){
	    		System.out.println("Blog Has no tittle, throwing java.lang.Exception.");
	    		//errorMessage = "Error with Post. No Post Title was not entered";
	    		throw blogCreateError;
	    	}
	    	
	    	if(blogPostTitle.equals("")){
	    		System.out.println("Post Has no tittle, throwing java.lang.Exception.");
	    		//errorMessage = "Error with Post. No Post Title was not entered";
	    		throw blogCreateError;
	    	}
	    	
	    	if(blogPostBody.equals("")){
	    		System.out.println("Post body does not contain any text, throwing java.lang.Exception.");
	    		//errorMessage = "Error with Post. No Post Title was not entered";
	    		throw blogCreateError;
	    	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	this.blogTitle = blogTitle;
    	this.blogPostTitle = blogPostTitle;
    	this.blogPostBody = blogPostBody;
	}
	
	public void setNewPost(boolean b){
		this.newPost=b;
	}
	
	public boolean getNewPost(){
		return newPost;
	}
	
	public int getBlogId(){
		return blogId;
	}
	
	public int getPostCount(){
		return postCount;
	}
	
	public String getErrorMessage(){
		return errorMessage;
	}
	
	public String getAuthor(){
		return author;
	}
	
	public String getBlogTitle(){
		return blogTitle;
	}
	
	public String getBlogPostTitle(){
		return blogPostTitle;
	}
	
	public String getBlogPostBody(){
		return blogPostBody;
	}
	
	public String getPostTitleAt(int i){
		return postTitleList.get(i);
	}
	
	public String getPostBodyAt(int i){
		return postBodyList.get(i);
	}
	
    public boolean insertBlogInDatabase(int userId) {          
	
        PreparedStatement pst = null; 
        ResultSet rs = null;
        DbConnection connectionManager = null;
        
        boolean status = true;  
        int postId;
        
        /*
         * The blog object used to call this function needs to call the appropriate constructor to have its
           blogTitle, blogPostTitle and blogPostBody parameters data members' before calling the insertBlogInDatabase function.
        */
        
        if(this.blogTitle == null){
        	return false;
        }
        
        if(this.blogPostTitle == null){
        	return false;
        }
        
        if(this.blogPostBody == null){
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
            
            //insert post title, blogid content, creation date into post table
            pst = connectionManager.getConnection().prepareStatement("insert into Post values(0, '"+blogId+"', '"+blogPostTitle+"','"+blogPostBody+"', curdate() )");  
            pst.executeUpdate(); 
            pst.close();
            
            //select postid from post table where blogid and title is the same
            
            pst = connectionManager.getConnection().prepareStatement("select postid from post where blogid = '"+blogId+"' and title = '"+blogPostTitle+"' ");  
            rs = pst.executeQuery(); 
            rs.first();
            postId = rs.getInt("postId");
            rs.close();
            pst.close();
            
  
            //insert postid and userid into user_post table
            
            pst = connectionManager.getConnection().prepareStatement("insert into user_post values('"+userId+"', '"+postId+"')");
            pst.executeUpdate();
            pst.close();
           
            //status = rs.next();
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
    
    public boolean buildBlog(int userId) {          
        
    	boolean status = true;  
        PreparedStatement pst = null; 
        ResultSet rs = null;
        DbConnection connectionManager = null;
        
        /* The value of i is used within the loop dedicated to filling the lists
         * that hold the contents of the posts within this specific blog */
        int i = 0;
        
        try {  
        	
        	//gaining access to the shared database connection.
        	connectionManager = DbConnection.getInstance();
  
        	/*
        	 * 
        	 * need to retrieve author name based on userid
        	 * 
        	 * when navigating from profile 
        	 * 
        	 * 		blog title will need to be retrieved by initializing the value with the value of the field clicked to open blog
        	 * 
        	 * when navigating from createBlog, 
        	 * 
        	 * 		title is set as session attribute after the blogs insertion to the db.
    		 *
    		 * select blogid based from blog using title
    		 *
    		 *  when opening from blogcreate blogId will already be stored in the user session as is passed as a parameter
    		 *  
    		 *  when opening from profile, this is not the case and the value -1 is passed.
    		 *
    		 *	"select blogid from blog where title = '"+blogTitle+"'";
    			
    		
    		select postid, title and content for all post from post table based on blogid
    		
    		
    		
    		
    		*/
        	
        	pst = connectionManager.getConnection().prepareStatement("select username from user where userid = '"+userId+"' ");
        	rs = pst.executeQuery();
        	rs.first();
        	this.author = rs.getString("username");
        	rs.close();
        	pst.close();
        	
        	if(this.blogId == -1){
        		
        		pst = connectionManager.getConnection().prepareStatement(" select blogid from blog where title = '"+blogTitle+"' "); 
            	rs = pst.executeQuery();
            	rs.first();
            	this.blogId = rs.getInt("blogid");
            	rs.close();
            	pst.close();
        		
        	}
        	
        	pst = connectionManager.getConnection().prepareStatement("select title, content from post where blogid = '"+blogId+"' ");
        	rs = pst.executeQuery();
        	
        	
        	while(rs.next()){
        		if(i < postCount){
        			++i;
        			continue;
        		}
        		postTitleList.add(rs.getString("title"));
        		postBodyList.add(rs.getString("content"));
        	}
        	
        	++postCount;
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

