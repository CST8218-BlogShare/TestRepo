package com.amzi.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amzi.dao.Blog;
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
				
		}catch(NumberFormatException nfE){
			nfE.printStackTrace();
			//what error is relevant here
			return;
		}catch(IllegalStateException isE){
			isE.printStackTrace();
			//what error is relevant here
			return;
		}
		
		try{
			postTitle = request.getParameter("postTitle").trim();
			postBody = request.getParameter("postBody").trim();
				
			//if the title/body are empty make the response PostCreate, with corresponding error
			if (postTitle.length() == 0 || postBody.length() == 0) {
					
				request.setAttribute("errorMessage", "alert.emptyfields");
				throw error;
				
			}
						
			//if the checkbox has not been activated, the parameter will not be initialized and the value null will be returned.
			if(request.getParameter("postEditableCheckBox") != null){
				postIsPublic = true;
			}
							
			
			//checking if the post is part of a blog that is publicly editable
		    if(b.getIsPublic() && b.getPostCount() != 0){
		    	postIsPublic = true;
		    }
			
		   
		     /* 
			  * Checking to see if the blog this post is a member or, or will be a member of has been deleted. 
			  * When isEditMode equals true, 
			  * The check for blogDeletion prevents any attempt to add a post to a non-existent blog.
			  */ 
			 
		    if(Blog.checkForDeletion(b.getBlogId()) == 1){
		    	request.setAttribute("errorMessage","alert.blogdeleted");
		    	throw error;
			}
			   
		    if(isEditMode == true){
			    	
		    /* 
			 * Checking to see if the the post that is about to be updated has been deleted.
			 * If the return value is null, the post cannot be found within the database.
			 */ 
		    	
		    	if(Post.checkForDeletion(b.getPostAt(b.getToEdit()).getPostId()) == 1){
		    		request.setAttribute("errorMessage", "alert.postdeleted");
		    		throw error;
		    	}
		    }
		    
		    postId = Post.insertPostInDatabase(postTitle, postBody, u.getUserId(), u.getUsername(), postIsPublic, b, isEditMode);
			
			
		    if(postId > 0){
				/* if the post is currently being edited, the post does not need to be added 
		    	   and insertPostEditPrivilegeInDatabase() is called by updatePostInDatabaseById() 
		    	   within insertPostInDatabase()
		    	*/
		    	if(!isEditMode){
					p = Post.getPostFromDatabaseById(postId); 
					b.addPost(p);
					
					postEditPrivilegeId = PostEditPrivilege.insertPostEditPrivilegeInDatabase(p.getPostId(), u.getUserId());
					
					if(postEditPrivilegeId < 0){
						if(postEditPrivilegeId == -1){
							request.setAttribute("errorMessage", "Error creating post while inserting PostEditPrivilege in database, error connecting to database");
						}
						
						if(postEditPrivilegeId == -2){
							request.setAttribute("errorMessage", "Error creating post while inserting PostEditPrivilege in database, error with SQL interaction with database.");
						}
						if(postEditPrivilegeId == -3){
							request.setAttribute("errorMessage", "Error creating post while inserting PostEditPrivilege in database, invalid postId value.");
						}
						if(postEditPrivilegeId == -4){
							request.setAttribute("errorMessage", "Error creating post while inserting PostEditPrivilege in database, invalid userId value.");
						}
						throw error;
					}
				}
			}else{
				if(postId == -1){
					request.setAttribute("errorMessage", "Error creating post while retreiving postId, error connecting to database");
				}
				
				if(postId == -2){
					request.setAttribute("errorMessage", "Error creating post while retrieving postId, error with SQL interaction with database.");
				}
				throw error;
			}
		}catch(Exception e){
			errorState = true;
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
