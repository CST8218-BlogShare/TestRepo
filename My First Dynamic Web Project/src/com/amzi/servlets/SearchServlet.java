package com.amzi.servlets;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amzi.dao.DbConnection;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
  
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }*/

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		//booleans to check out Blogs, Posts, Titles, Content, Users, Authors
		
		DbConnection connectionManager = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String searchTerm = "";
		boolean searchBlogs = false;
		boolean searchPosts = false;
		boolean searchTitle = false;
		boolean searchBody = false;
		boolean searchUsers = false;
		boolean searchAuthors = false;
		
		if(request.getParameter("navBarBlogsCheck") != null){
			searchBlogs = true;
		}
		
		if(request.getParameter("navBarPostsCheck") != null){
			searchPosts = true;
		}
		
		if(request.getParameter("navBarTitleCheck") != null){
			searchTitle = true;
		}
		
		if(request.getParameter("navBarBodyCheck") != null){
			searchBody = true;
		}
		
		if(request.getParameter("navBarUsersCheck") != null){
			searchUsers = true;
		}
		
		if(request.getParameter("navBarAuthorsCheck") != null){
			searchAuthors = true;
		}
		
		searchTerm = request.getParameter("navBarSearchTerm");
		searchTerm = searchTerm.trim();
		
		if(searchTerm != ""){
			String searchTitleBody = "";
			connectionManager = DbConnection.getInstance();
			
			
			//left off here
			if(searchBlogs == true){
				try {
					ps =  connectionManager.getConnection().prepareStatement("select title from blogs where title like %'"+searchTerm+"'%");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(searchPosts == true){
				if(searchTitle){
					searchTitleBody = searchTitleBody.concat("title");
				}
				if(searchBody){
					if(!searchTitleBody.contentEquals("")){
						searchTitleBody = searchTitleBody.concat(", ");
					}
					searchTitleBody = searchTitleBody.concat("body");
				}
			}
			
			
		}
		
		
		
		
		
		
		RequestDispatcher rd=request.getRequestDispatcher("SearchResults.jsp");
		 
			try {
				rd.include(request,response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}*/
	

}
