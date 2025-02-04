package com.amzi.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.amzi.dao.DbConnection;

/**
 * Servlet implementation class LogoutServlet
 */
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public LogoutServlet() {
		 super();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		
		//closing the database connection.
		DbConnection.getInstance().closeConnection();
		
		//setting the session to it's default state. 
		request.getSession().invalidate();
		
		RequestDispatcher rd=request.getRequestDispatcher("Home.jsp");
		 
		 try {
			rd.forward(request,response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
