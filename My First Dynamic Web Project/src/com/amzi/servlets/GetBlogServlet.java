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
		Exception error = new Exception();
		String url = "Blog.jsp";
		
		Blog b = null;
		int blogId = 0;
		String blogTitle = null;
		Boolean isBlogEdit = false;
		
		blogTitle = request.getParameter("blogTitle");
		isBlogEdit =  Boolean.parseBoolean(request.getParameter("isBlogEdit"));
		
		if(blogTitle == null){
			System.exit(-1);
		}
		
		b = (Blog) request.getSession().getAttribute("currentBlog");
		
		
		try{
		
			/*
			 * If the currentBlog attribute stored within the session has not been initialized or the blog to be loaded is different 
			 * from the currently stored blog, then a new blog is created and replaces the previous blog that was stored in the session.
			 */
			if(b == null || b.getBlogTitle().equals(blogTitle) == false){
				
				blogId = Blog.getBlogIdFromDatabaseByTitle(blogTitle);
					
				if(blogId < 0){
						
					if(blogId == -1){
						request.setAttribute("errorMessage", "getblogerror1");
					}
						
					if(blogId == -2){
						request.setAttribute("errorMessage", "getblogerror2");
					}
						
					throw error;	
				}
				
				/* 
				 * Since Blog.getBlogFromDatabaseById() is called on every load of Blog.jsp, a Blog object
				 * is initialized with only the blogId of the Blog corresponding to the retrieved title. 
				 */
				
				b = new Blog(blogId);
					
				request.getSession().setAttribute("currentBlog", b); 
			}	
			
			if(isBlogEdit){
				url = "BlogEdit.jsp";
			}
		
		}catch(Exception e){
			url = "Profile.jsp";
		}
			
		try {
			request.getRequestDispatcher(url).forward(request, response); 
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}//end of doPost
}
