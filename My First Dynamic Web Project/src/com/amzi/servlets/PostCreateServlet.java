package com.amzi.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amzi.dao.Blog;
import com.amzi.dao.DbConnection;
import com.amzi.dao.Post;
import com.amzi.dao.PostEditPrivilege;
import com.amzi.dao.User;
  
public class PostCreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PostCreateServlet() {
		 super();
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		Exception error = new Exception();
		Boolean errorState = false;
		String url = "";
		
		User u = null;
		Blog b = null; 
		Post p = null;
		String postTitle = "";
		String postBody = "";
		int postId = 0;
		int postEditPrivilegeId = 0;
		boolean postIsPublic = false;
		
		int toEdit = -1;
		boolean isEditMode  = false;
		

		u = (User) request.getSession().getAttribute("currentUser");
		b = (Blog) request.getSession().getAttribute("currentBlog");
		
		if(u == null || b == null){
			// If the current user and the current blog, cannot be retrieved from the session, the session is invalid 
			System.exit(-1);
		}
		
		try{
			isEditMode = Boolean.parseBoolean(request.getSession().getAttribute("editMode").toString());
		}catch(Exception e){
			isEditMode = false;
		}
				
		try{
			toEdit = Integer.parseInt(request.getSession().getAttribute("toEdit").toString());
		}catch(Exception e){
			toEdit = -1;
		}
		
		try{
			postTitle = request.getParameter("postTitle").trim();
			postBody = request.getParameter("postBody").trim();
				
			//if the title/body are empty make the response PostCreate, with corresponding error
			if(postTitle.length() == 0) {
				request.setAttribute("errorMessage", "errornotitle");
				throw error;
			}
			
			if(postBody.length() == 0){
				request.setAttribute("errorMessage", "errornocontent");
				throw error;
			}
			
			
			//checking if the post is part of a blog that is publicly editable
			////if the checkbox has not been activated, the parameter postEditableCheckBox will not be initialized and the value null will be returned.
		    if(b.getIsPublic() && b.getPostCount() != 0 || request.getParameter("postEditableCheckBox") != null ){ 
		    	postIsPublic = true;
		    }
						   
		     /* 
			  * Checking to see if the Blog this post is a member of or will be a member of, has been deleted. 
			  * When isEditMode equals true, 
			  *		The check for blogDeletion prevents any attempt to modify a post within a non-existent blog.
			  */ 
		    if(Blog.checkForDeletion(b.getBlogId()) == 1){
		    	request.setAttribute("errorMessage","errorblogdeleted");
		    	throw error;
			}
			   
		    if(isEditMode == true){
			    	
		    /* 
			 * Checking to see if the the post that is about to be updated has been deleted.
			 * If the return value is null, the post cannot be found within the database.
			 */ 
		    	
		    	if(Post.checkForDeletion(b.getPostAt(b.getToEdit()).getPostId()) == 1){
		    		request.setAttribute("errorMessage", "errorpostdeleted");
		    		throw error;
		    	}
		    }
		    
		    /* 
			 * Setting up a database transaction where the post and postEditPrivilege entries both need to be inserted into the database.
			 * If the two items cannot be inserted into the database, any completed insertions are reversed to this point.
			 */
			
			DbConnection.getInstance().getConnection().setAutoCommit(false);
		    
		    /* If the post is currently being edited (isEditMode == false), 
		     * The post is not added to a blog and updatePostInDatabaseById() is called within insertPostInDatabase().
		     */
		    postId = Post.insertPostInDatabase(postTitle, postBody, u.getUserId(), u.getUsername(), postIsPublic, b, isEditMode);
			
		    if(postId < 0){
		    	if(postId == -1){
					request.setAttribute("errorMessage", "errorconnectfailed");
				}
				
				if(postId == -2 || postId == -3){
					request.setAttribute("errorMessage", "errorsql");
				}
				
				if(postId == -4){
					request.setAttribute("errorMessage", "errorpostedit");
				}
				throw error;
		    }
			
			//If the post is not being updated and should be added to the current blog.
		    if(!isEditMode){
				p = Post.getPostFromDatabaseById(postId);
				
				if(p == null){
					request.setAttribute("errorMessage", "errorsql");
					throw error;
				}
					
				postEditPrivilegeId = PostEditPrivilege.insertPostEditPrivilegeInDatabase(postId, u.getUserId());
					
				if(postEditPrivilegeId < 0){
					if(postEditPrivilegeId == -1){
						request.setAttribute("errorMessage", "errorconnectfailed");
					}else{
						request.setAttribute("errorMessage", "errorsql");
					}
					throw error;
				}
			}
		    
		    //Now that it is proved that the insertion of blog, post and postEditPrivilege were successful, the changes are applied to the database. 
			DbConnection.getInstance().getConnection().commit();
			
			/*
			 * Ressetting the connection back to it's default behavior where every transaction is applied as soon as it is executed.
			 * This statement is placed here to avoid another try-catch block within the method. 
			 */
			
			DbConnection.getInstance().getConnection().setAutoCommit(true);
		    
		}catch(Exception e){
			errorState = true;
			try {
				if(DbConnection.getInstance().getConnection().getAutoCommit() == false){
					DbConnection.getInstance().getConnection().rollback();
				
					/*
				 	* Ressetting the connection back to it's default behavior where every transaction is applied as soon as it is executed.
				 	* This statement is placed here to avoid another try-catch block within the method. 
				 	*/
				
					DbConnection.getInstance().getConnection().setAutoCommit(true);
				}
			} catch (SQLException sqlE) {
				sqlE.printStackTrace();
			}
			e.printStackTrace();
		}
		
			
		if(errorState == false){
			url = "Blog.jsp";
		} 
		
		if(errorState == true){
			/*if(request.getSession().getAttribute("language").equals("EN")){
			}else if(request.getSession().getAttribute("language").equals("FR")){
			}*/
			if(isEditMode){
				url = "PostCreate.jsp?editEnabled=true&post="+toEdit;
			}else{
				url = "PostCreate.jsp";
			}
		}
		
		RequestDispatcher rd = request.getRequestDispatcher(url);

		try {
			rd.include(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
