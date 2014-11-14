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

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		User u = null;
		Blog b = null; 
		Post p = null;
		PostEditPrivilege pep = null;
		HttpSession userSession = null;
		String postTitle = "";
		String postBody = "";
		boolean postIsPublic = false;
		int toEdit = -1;
		boolean isEditMode  = false;
		
		
		// If a session has not been created, none will be created
		userSession = request.getSession(false);
		response.setContentType("text/html");
		
		u = (User) userSession.getAttribute("currentUser");
		b = (Blog) userSession.getAttribute("currentBlog");
		
		if( u != null && b != null){
		
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
				return;
			}catch(IllegalStateException isE){
				isE.printStackTrace();
				return;
			}
			
			postTitle = request.getParameter("postTitle").trim();
			postBody = request.getParameter("postBody").trim();
			
			//if the title/body are empty make the response PostCreate, with corresponding error
			if (postTitle.length() == 0 || postBody.length() == 0) {
				
				request.setAttribute("errorMessage", "alert.emptyfields");
				
				RequestDispatcher rd = request.getRequestDispatcher("PostCreate.jsp");
				try {
					rd.forward(request, response);
					return;
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
			}
					
			//if the checkbox has not been activated, the parameter will not be initialized and the value null will be returned.
			if(request.getParameter("postEditableCheckBox") != null){
				postIsPublic = true;
			}
			
			p = new Post(postTitle, postBody, u.getUsername(), postIsPublic);
			pep = new PostEditPrivilege();
			
			 if(Post.insertPostInDatabase(p, b,u.getUserId() ,isEditMode) && pep.insertPostEditPrivilegeInDatabase(p.getPostId(), u.getUserId())){
				
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
				RequestDispatcher rd = request.getRequestDispatcher("PostCreate.jsp");
				
				// modify
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
}
