package com.amzi.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.amzi.dao.Blog;

public class GetBlogServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	//load the requested blog into the session and forward to blog.jsp
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		HttpSession userSession = request.getSession(false);
		Blog b = null;
		String blogTitle = null;
		Boolean isBlogEdit = false;
		
		if(userSession == null){
			System.out.println("Session state could not be retrieved, closing BlogShare");
			//exiting in error state
			System.exit(1);
		}
		
		blogTitle = request.getParameter("blogTitle");
		isBlogEdit =  Boolean.parseBoolean(request.getParameter("isBlogEdit"));
		
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
				System.out.print("Error building blog with name: " + blogTitle + ".");
				
				//User stays on profile page. 
				try {
					request.getRequestDispatcher("Profile.jsp").forward(request, response); 
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
				
			}
		}
		
		if(isBlogEdit == false){
			
			try {
				request.getRequestDispatcher("Blog.jsp").forward(request, response); 
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}else{
			
			try {
				request.getRequestDispatcher("BlogEdit.jsp").forward(request, response); 
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}  
			
		}
	}//end of doPost
		
}
