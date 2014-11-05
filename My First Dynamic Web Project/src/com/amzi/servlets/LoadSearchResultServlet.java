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
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadSearchResultServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response){ 
		Post p = null;
		Blog b = null;
		User u = null;
		SearchResult currentSearch = null;
		String url = "";
				
		currentSearch =  (SearchResult) request.getSession().getAttribute("currentSearchResult");
		if(currentSearch == null){
			//error
		}
		
		switch(currentSearch.getResultType()){
			case "Blog":
				b = new Blog();
				b.buildBlogFromId(Integer.parseInt(request.getParameter("blogId")));
				request.getSession().setAttribute("currentBlog", b);
				url = "Blog.jsp";
				break;
			case "Post":
				b = new Blog();
				p = new Post();
				//p.buildPostFromId does not initialize the author paramater of the post object. 
				p.buildPostFromId(Integer.parseInt(request.getParameter("postId")));
				b.buildBlogFromId(p.getBlogId());
				request.getSession().setAttribute("currentBlog", b);
				request.getSession().setAttribute("postToView",p.getPostTitle());
				url = "Blog.jsp";
		
				break;
			case "User":
				u = new User();
				u.buildUserFromId(Integer.parseInt(request.getParameter("userId")));
				request.getSession().setAttribute("currentProfile", u);
				url = "Profile.jsp";
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
