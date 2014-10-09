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
import com.amzi.dao.PostCreate;
  
public class PostCreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		PostCreate p = null;
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
		p = new PostCreate(postTitle, postBody);
		
		try{
		
			userId = (int) userSession.getAttribute("userId");
			b = (Blog) getServletContext().getAttribute("currentBlog");
		
		}catch(NumberFormatException nfE){
			nfE.printStackTrace();
			return;
		}
		/*The function insertBlogInDatabase() is called to take the contents entered into the
		 form within blogCreate held within Blog Object b, and insert this info into the database
		 
		 This function also initializes the Blog's blogId data member with an integer value.
		 and sets the newPost data member within Blog to true. Setting the boolean to 
		 true allows the newPost to be added to the pages content within Blog.jsp
		 */
		 if(p.insertPostInDatabase(userId, b)){
			 //getServletContext().setAttribute("errorCode", 0);
			 
			 //getServletContext().setAttribute("currentPost", p);
			 
			 //userSession.setAttribute("CreationDate", BlogCreate.creationDate);
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
			// getServletContext().setAttribute("errorCode", 1);
			// getServletContext().setAttribute("errorMessage",
			// BlogCreate.errorMessege);
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
