package com.amzi.servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.amzi.dao.Blog;
/**
 * Servlet implementation class BlogChangeTitleServlet
 */
public class BlogChangeTitleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	public BlogChangeTitleServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		int errorCode = 0;
		int blogId = -1;
		String newBlogTitle = "";
		
		
		blogId = ((Blog) request.getSession().getAttribute("currentBlog")).getBlogId();
		
		//if current blog cannot be retrieved, the session is no longer valid.
		if(blogId == -1){
			System.exit(1);
		}
		
		newBlogTitle = request.getParameter("newBlogTitle");
		newBlogTitle = newBlogTitle.trim();
         
	    if(newBlogTitle.length() != 0){
	    	errorCode = Blog.updateTitleInDatabase(newBlogTitle, blogId);
			
	    	if(errorCode < 0){
	    		if(errorCode == -1){
	    			
	    			request.setAttribute("errorMessage", "alert.error1");
	    		}
	    		
	    		if(errorCode == -2){
	    			request.setAttribute("errorMessage", "alert.error2");
	    		}
	    	}
	    	
	    }else{
	    	request.setAttribute("errorMessage", "alert.emptyfields");
	    }
		
		RequestDispatcher rd=request.getRequestDispatcher("BlogEdit.jsp");
		 
		 try {
			rd.include(request,response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
