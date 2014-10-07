package com.amzi.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.amzi.dao.Blog;



@WebServlet("/LoadProfileServlet")
public class LoadProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession userSession = request.getSession(false);
		Blog blog = new Blog();

		if (userSession == null) {
		}
				
		ArrayList<String[]> userBlogList = blog.getUserBlogs(userSession.getAttribute("userId").toString());
		
		request.setAttribute("userBlogList", userBlogList);
		
		try {
			request.getRequestDispatcher("Profile.jsp").forward(request, response); 
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}    
		
	}

}
