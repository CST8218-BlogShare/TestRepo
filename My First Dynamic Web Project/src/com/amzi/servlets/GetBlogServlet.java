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
		
		/*
		 * If the currentBlog attribute stored within the session has not been initialized or the blog to be loaded is different 
		 * from the currently stored blog, then a new blog is created and replaces the previous blog that was stored in the session.
		 */
		if(b == null || b.getBlogTitle().contentEquals(blogTitle) == false){
			
			b = new Blog();
			
			try{
				blogId = Blog.getBlogIdFromDatabaseByTitle(blogTitle);
				
				if(blogId < 0){
					
					if(blogId == -1){
						request.setAttribute("errorMessage", "Error building blog from link in profile, error connecting to database.");
					}
					
					if(blogId == -2){
						request.setAttribute("errorMessage", "Error building blog from link in profile, SQL error while interacting with database");
					}
					
					throw error;	
				}
				
				b = Blog.getBlogFromDatabaseById(blogId);
					
				if(b == null){
					request.setAttribute("errorMessage", "Error creating blog for display on webpage, unable to retrieve blog from database.");
					throw error;
				}
				
				request.getSession().setAttribute("currentBlog", b); 
				
				//If the edit button has been clicked on the profile page instead of the title of the blog. 
				if(isBlogEdit == true){
					url = "BlogEdit.jsp";
				}
				
			}catch(Exception e){
				url = "Profile.jsp";
			}	
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
