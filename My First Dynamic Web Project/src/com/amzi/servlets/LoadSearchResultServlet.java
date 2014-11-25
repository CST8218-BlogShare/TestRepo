package com.amzi.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amzi.dao.Post;
import com.amzi.dao.Blog;
import com.amzi.dao.User;
import com.amzi.dao.SearchResult;

/**
 * Servlet implementation class LoadSearchResultServlet
 */

public class LoadSearchResultServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LoadSearchResultServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response){ 
		Exception error = new Exception();
		SearchResult currentSearch = null;
		Post p = null;
		Blog b = null;
		User u = null;
		String url = "";
				
		currentSearch =  (SearchResult) request.getSession().getAttribute("currentSearchResult");
		//if currentSearch cannot be retrieved from the session, the session is invalid.
		if(currentSearch == null){
			System.exit(-1);
		}
		
		try{
			switch(currentSearch.getResultType()){
				case "Blog":
					b = Blog.getBlogFromDatabaseById(Integer.parseInt(request.getParameter("blogId")));
					
					if(b == null){
						request.setAttribute("errorMessage", "Error loading blog contents from search result, unable to retrieve blog from database by id.");
						throw error;
					}
					
					request.getSession().setAttribute("currentBlog", b);
					url = "Blog.jsp";
					break;
				case "Post": 
					p = Post.getPostFromDatabaseById(Integer.parseInt(request.getParameter("postId")));
					
					if(p == null){
						request.setAttribute("errorMessage", "Error loading post contents from search result, unable to retrieve post from database by id.");
						throw error;
					}
					
					b = Blog.getBlogFromDatabaseById(p.getBlogId());
					
					if(b == null){
						request.setAttribute("errorMessage", "Error loading blog contents from search result, unable to retrieve blog from database by id.");
						throw error;
					}
					
					request.getSession().setAttribute("currentBlog", b);
					request.getSession().setAttribute("postToView",p.getPostTitle());
					url = "Blog.jsp";
			
					break;
				case "User":
					u = User.getUserFromDatabaseById(Integer.parseInt(request.getParameter("userId")));
					
					if(u == null){
						request.setAttribute("errorMessage", "Error loading user contents from search result, unable to retrieve user from database id.");
						throw error;
					}
					
					request.getSession().setAttribute("currentProfile", u);
					url = "Profile.jsp";
			}
		}catch(Exception e){
			url = "SearchResults.jsp";
		}
		
		try {
			request.getRequestDispatcher(url).forward(request, response); 
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
