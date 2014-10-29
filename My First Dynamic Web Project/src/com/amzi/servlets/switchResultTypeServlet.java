package com.amzi.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.amzi.dao.SearchResult;

/**
 * Servlet implementation class switchResultTypeServlet
 */
public class switchResultTypeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		
		String type = request.getParameter("resultType");
		SearchResult result = (SearchResult) request.getSession(false).getAttribute("currentSearchResult");
		
		if(type.contentEquals("Blog")){
			result.setResults(result.getResultsBlog(), type);
		}
		
		if(type.contentEquals("Post")){
			result.setResults(result.getResultsPost(), type);
		}
		
		if(type.contentEquals("User")){
			result.setResults(result.getResultsUser(), type);
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

}
