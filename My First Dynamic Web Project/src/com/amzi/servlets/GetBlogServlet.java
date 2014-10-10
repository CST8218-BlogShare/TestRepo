package com.amzi.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amzi.dao.Blog;

@WebServlet("/GetBlogServlet")
public class GetBlogServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Blog b = new Blog();
		String blogTitle = request.getParameter("blogTitle");
		
		if (b.buildBlogFromTitle(blogTitle)){
			
			getServletContext().setAttribute("currentBlog", b);
			try {
				request.getRequestDispatcher("Blog.jsp").forward(request, response); 
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}    
			
		} else {
			
		}
		
		
	}

}
