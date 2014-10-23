package com.amzi.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.amzi.dao.Blog;

@WebServlet("/GetBlogServlet")
public class GetBlogServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	//load the requested blog into the session and forward to blog.jsp
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		Blog b = null;
		String blogTitle = null;
		HttpSession userSession = request.getSession(false);
		
		if(userSession == null){
			//do something
			return;
		}
		
		blogTitle = request.getParameter("blogTitle");
		
		if(blogTitle == null){
			return;
		}
		
		b = (Blog) userSession.getAttribute("currentBlog");
		
		/*
		 * If the currentBlog attribute stored within the session has not been initialized or the blog to be loaded is different 
		 * from the currently stored blog, then a new blog is created and replaces the previous blog that was stored in the session.
		 */
		if(b == null || b.getBlogTitle().contentEquals(blogTitle) == false){
			
			b = new Blog();
			
			if (b.buildBlogFromTitle(blogTitle)){
				userSession.setAttribute("currentBlog", b);  
			}else{
				//throw some error
				return;
			}
		}
		
		try {
			request.getRequestDispatcher("Blog.jsp").forward(request, response); 
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
}
