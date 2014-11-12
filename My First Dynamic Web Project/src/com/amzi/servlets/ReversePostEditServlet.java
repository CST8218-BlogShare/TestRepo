package com.amzi.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ReversePostEditServlet
 */
public class ReversePostEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ReversePostEditServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getRequestDispatcher("BlogEdit.jsp").forward(request, response); 
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}

}
