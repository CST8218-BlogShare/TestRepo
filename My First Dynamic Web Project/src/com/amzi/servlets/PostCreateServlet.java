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
		
		try{
		
			u = (User) userSession.getAttribute("currentUser");
			b = (Blog) userSession.getAttribute("currentBlog");
			
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
		
		postTitle = request.getParameter("postTitle");
		postBody = request.getParameter("postBody");
				
		//if the checkbox has not been activated, the parameter will not be initialized and the value null will be returned.
		if(request.getParameter("postEditableCheckBox") != null){
			postIsPublic = true;
		}
		
		p = new Post(postTitle, postBody, u.getUsername(), postIsPublic);
		pep = new PostEditPrivilege();
		
		 if(p.insertPostInDatabase(u.getUserId(), b, isEditMode) && pep.insertPostEditPrivilegeInDatabase(p.getPostId(), u.getUserId())){
			
			 RequestDispatcher rd=request.getRequestDispatcher("Blog.jsp");
			 
			try {
				rd.include(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
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
