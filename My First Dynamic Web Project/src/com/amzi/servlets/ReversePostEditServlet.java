package com.amzi.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amzi.dao.User;
import com.amzi.dao.PostEdit;
import com.amzi.dao.Blog;

/**
 * Servlet implementation class ReversePostEditServlet
 */
public class ReversePostEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ReversePostEditServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		Blog currentBlog = null;
		PostEdit currentPostEdit = null;
		String url = "";
		int currentPostPos = -1;
		int currentUserId = -1;
		int errorCode = 0;
		
		currentBlog = (Blog) request.getSession().getAttribute("currentBlog");
		currentPostPos = (int) request.getSession().getAttribute("currentPostPos");
		currentPostEdit = (PostEdit) request.getSession().getAttribute("currentPostEdit");
		currentUserId = (int) ((User) request.getSession().getAttribute("currentUser")).getUserId(); 
		
		//If the parameters passed to getPostAt are not initialized, editPostInDatabase will throw an error 
		
		errorCode = currentBlog.getPostAt(currentPostPos).reverseEditToPostInDatabase(currentPostPos,currentPostEdit,currentUserId);
		
		if(errorCode < 0){
			if(errorCode == -1){
				request.setAttribute("errorMessage", "reversepostediterror1.");
			}
				
			if(errorCode == -2){
				request.setAttribute("errorMessage", "reversepostediterror2");
			}
			url = "PostEditHistory.jsp";
		}else{
			 url = "Blog.jsp"; 
		}
		 
		try {
			request.getRequestDispatcher(url).forward(request, response);
		} catch (ServletException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
