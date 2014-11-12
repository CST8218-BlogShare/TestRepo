package com.amzi.servlets;

import java.io.IOException;



import javax.servlet.RequestDispatcher;
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
		RequestDispatcher rd = null;
		Blog currentBlog = null;
		PostEdit currentPostEdit = null;
		int currentPostPos = -1;
		int currentUserId = -1;
		
		currentBlog = (Blog) request.getSession().getAttribute("currentBlog");
		currentPostPos = (int) request.getSession().getAttribute("currentPostPos");
		currentPostEdit = (PostEdit) request.getSession().getAttribute("currentPostEdit");
		currentUserId = (int) ((User) request.getSession().getAttribute("currentUser")).getUserId(); 
		
		//If the parameters passed to getPostAt are not initialized, editPostInDatabase will throw an error 
		
		if(currentBlog.getPostAt(currentPostPos).editPostInDatabase(currentBlog,currentPostPos,currentPostEdit,currentUserId)){
			rd = request.getRequestDispatcher("Blog.jsp");
		}else{
			rd = request.getRequestDispatcher("PostEditHistory.jsp");
		}
		
		try {
			rd.forward(request, response);
		} catch (ServletException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
