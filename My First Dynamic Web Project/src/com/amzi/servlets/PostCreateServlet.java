package com.amzi.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.amzi.dao.Blog;
import com.amzi.dao.Post;
  
public class PostCreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		Post p = null;
		PrintWriter out = null;
		Blog b = null; 
		int userId = -1;
		
		// If a session has not been created, none will be created
		HttpSession userSession = request.getSession(false);
		response.setContentType("text/html");
		
		
		try {
			out = response.getWriter();
		} catch (IOException ioE) {
			ioE.printStackTrace();
			return;
		}
		String postTitle = request.getParameter("postTitle");
		String postBody = request.getParameter("postBody");
		p = new Post(postTitle, postBody);
		
		try{
		
			userId = (int) userSession.getAttribute("userId");
			b = (Blog) userSession.getAttribute("currentBlog");
		
		}catch(NumberFormatException nfE){
			nfE.printStackTrace();
			return;
		}
		
		 if(p.insertPostInDatabase(userId, b)){
			
			 RequestDispatcher rd=request.getRequestDispatcher("Blog.jsp");
			 
			try {
				rd.include(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			RequestDispatcher rd = request.getRequestDispatcher("PostCreate.jsp");
			
			// modify
			try {
				rd.include(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (out != null) {
			out.close();
		}
	}
}
