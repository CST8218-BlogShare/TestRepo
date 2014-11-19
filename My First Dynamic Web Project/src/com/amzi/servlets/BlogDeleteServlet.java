package com.amzi.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amzi.dao.Blog;

/**
 * Servlet implementation class BlogDeleteServlet
 */

public class BlogDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BlogDeleteServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		String blogTitle = "";
		    
		blogTitle = request.getParameter("blogTitle");
		blogTitle = blogTitle.trim();
		
		if(blogTitle.equals("")){
			request.setAttribute("errorMessage", "Error: Blog title is empty.");
		}else{
		
			//if the method is unsuccessful the value false will be returned.
			//when the function executes successfully the value will not be set.
			if(Blog.deleteBlogFromDatabase(blogTitle) == false){
				request.setAttribute("errorMessage", "Error: Unable to delete blog from database.");
			}
		}
				 
		try {
			request.getRequestDispatcher("Profile.jsp").forward(request, response); 
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
