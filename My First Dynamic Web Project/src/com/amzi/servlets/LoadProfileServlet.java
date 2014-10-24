package com.amzi.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.amzi.dao.User;

/* considering taking this Servlet out all together and just rebuilding the userBlogList everytime profile is accessed.
   since
   1. The list is not stored in the session. 
   2. Dont have to worry about calling this servlet when the profile is accessed from the navigation bar. 
   3. Gets rid of the need to add to the list when a new blog is made as it is always rebuilt based on the contents of the database.
*/

@WebServlet("/LoadProfileServlet")
public class LoadProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession userSession = request.getSession(false);
		User u = (User) userSession.getAttribute("currentUser");

		if (u == null) {
			try {
				throw new Exception();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
				
		ArrayList<String> userBlogList = u.getUserBlogs(u.getUserId());
		
		userSession.setAttribute("userBlogList", userBlogList);
		
		try {
			request.getRequestDispatcher("Profile.jsp").forward(request, response); 
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}    
		
	}

}
