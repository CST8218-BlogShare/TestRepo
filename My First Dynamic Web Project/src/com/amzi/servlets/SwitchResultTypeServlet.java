package com.amzi.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amzi.dao.SearchResult;

/**
 * Servlet implementation class switchResultTypeServlet
 */
public class SwitchResultTypeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	public SwitchResultTypeServlet() {
        super();
    }
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		
		String type = request.getParameter("resultType");
		SearchResult result = (SearchResult) request.getSession().getAttribute("currentSearchResult");
		
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
