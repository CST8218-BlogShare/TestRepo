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
		
		newBlogTitle = request.getParameter("newBlogTitle");
		newBlogTitle = newBlogTitle.trim();
         
	    if(newBlogTitle.length() != 0){
	    	errorCode = Blog.updateTitleInDatabase(newBlogTitle, blogId);
			
	    	if(errorCode < 0){
	    		if(errorCode == -1){
	    			request.setAttribute("errorMessage", "errorconnectfailed");
	    		}else{
	    			request.setAttribute("errorMessage", "errorsql");
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
