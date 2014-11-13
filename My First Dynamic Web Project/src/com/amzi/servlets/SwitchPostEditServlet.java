package com.amzi.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SwitchPostEditServlet
 */

public class SwitchPostEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SwitchPostEditServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		if(request.getParameter("postEditPos") != null){
			request.getSession().setAttribute("currentPostEditPos", Integer.parseInt(request.getParameter("postEditPos")));
		}
		
		try {
			request.getRequestDispatcher("PostEditHistory.jsp").forward(request, response); 
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
