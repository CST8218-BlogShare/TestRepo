package com.amzi.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ReversePostEditServlet
 */
@WebServlet("/ReversePostEditServlet")
public class ReversePostEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ReversePostEditServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* need to retrieve the current post?? */
	}

}
