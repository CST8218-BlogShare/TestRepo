package com.amzi.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		HttpSession userSession = null;
		
		Exception error = new Exception();
		Boolean errorState = false;
		
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
		
		
		// If a session has not been created, none will be created
		userSession = request.getSession(false);
		
		u = (User) userSession.getAttribute("currentUser");
		b = (Blog) userSession.getAttribute("currentBlog");
		
		if(u == null || b == null){
			// If the current user and the current blog, cannot be retrieved from the session, the session is invalid 
			System.exit(-1);
		}
		
		try{
			try{
				isEditMode = Boolean.parseBoolean((userSession.getAttribute("editMode").toString()));
			}catch(Exception e){
				isEditMode = false;
			}
				
			try{
				toEdit = Integer.parseInt(userSession.getAttribute("toEdit").toString());
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
					
				RequestDispatcher rd = request.getRequestDispatcher("PostCreate.jsp?editEnabled=true&post="+toEdit);
					
				try {
					rd.forward(request, response);
					return;
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//throw error;
			}
						
			//if the checkbox has not been activated, the parameter will not be initialized and the value null will be returned.
			if(request.getParameter("postEditableCheckBox") != null){
				postIsPublic = true;
			}//????
							
			
			//checking if the post is part of a blog that is publicly editable
		    if(b.getIsPublic() && b.getPostCount() != 0){
		    	postIsPublic = true;
		    }
			
		    postId = Post.insertPostInDatabase(postTitle, postBody, u.getUserId(), u.getUsername(), postIsPublic, b, isEditMode);
			
			if(postId > 0){
				p = Post.getPostFromDatabaseById(postId); 
				b.addPost(p);
			}else{
				if(postId == -1){
					
				}
				
				throw error;
			}
			
			postEditPrivilegeId = PostEditPrivilege.insertPostEditPrivilegeInDatabase(p.getPostId(), u.getUserId());
			
			if(postEditPrivilegeId < 0){
				throw error;
			}
		}catch(Exception e){
			errorState = true;
			e.printStackTrace();
		}
		
			
		if(errorState == false){
			RequestDispatcher rd=request.getRequestDispatcher("Blog.jsp");
				 
			try {
				rd.include(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			if(userSession.getAttribute("language").equals("EN"))
				request.setAttribute("errorMessage", "Error: Unable to create Post. Make sure all fields have been modified.");
			else if(userSession.getAttribute("language").equals("FR")){
				request.setAttribute("errorMessage", "Erreur: Impossible de créer le Post. Assurez-vous d'avoir modifier tout les champs.");	
			}
			
			RequestDispatcher rd = request.getRequestDispatcher("PostCreate.jsp?editEnabled=true&post="+toEdit);

			try {
				rd.include(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
