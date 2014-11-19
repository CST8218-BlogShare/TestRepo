package com.amzi.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amzi.dao.Blog;

public class GetBlogServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public GetBlogServlet() {
		 super();
	}
	
	//load the requested blog into the session and forward to blog.jsp
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		Blog b = null;
		int blogId = 0;
		String blogTitle = null;
		Boolean isBlogEdit = false;
		
		blogTitle = request.getParameter("blogTitle");
		isBlogEdit =  Boolean.parseBoolean(request.getParameter("isBlogEdit"));
		
		if(blogTitle == null){
			//handle error differently
			return;
		}
		
		b = (Blog) request.getSession().getAttribute("currentBlog");
		
		/*
		 * If the currentBlog attribute stored within the session has not been initialized or the blog to be loaded is different 
		 * from the currently stored blog, then a new blog is created and replaces the previous blog that was stored in the session.
		 */
		if(b == null || b.getBlogTitle().contentEquals(blogTitle) == false){
			
			b = new Blog();
			
			blogId = Blog.getBlogIdFromDatabaseByTitle(blogTitle);
			
			if(blogId > 0){
				b = Blog.getBlogFromDatabaseById(blogId);
				
				if(b != null){
					request.getSession().setAttribute("currentBlog", b); 
				}
				
			}else{
				
				if(blogId == -1){
					//db connection error
				}
				
				if(blogId == -2){
					//unable to find match
				}
				
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
