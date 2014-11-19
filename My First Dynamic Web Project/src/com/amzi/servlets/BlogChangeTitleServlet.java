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
		Blog b = null;
		String newBlogTitle = "";
		
		b = (Blog) request.getSession().getAttribute("currentBlog");
		
		//if current blog cannot be retrieved, the session is no longer valid.
		if(b == null){
			System.exit(1);
		}//??
		
		newBlogTitle = request.getParameter("newBlogTitle");
		
		newBlogTitle = newBlogTitle.trim();
         
	    if(newBlogTitle.length() != 0){
	    	if(b.updateTitleInDatabase(b.getBlogId(),newBlogTitle) == false){
				//request.setAttribute("errorMessage", alert.errorUpdatingTitle);
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
