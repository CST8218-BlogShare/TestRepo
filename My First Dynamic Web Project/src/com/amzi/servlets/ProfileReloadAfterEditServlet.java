package com.amzi.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amzi.dao.User;

/**
 * Servlet implementation class ReloadProfileAfterEdit
 */
@WebServlet("/ProfileReloadAfterEditServlet")
public class ProfileReloadAfterEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ProfileReloadAfterEditServlet() {
        super();
    }

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = null;
		int userId = -1;
		
		//will always be initialized since it is a hidden attribute of this form.
		userId = Integer.parseInt(request.getParameter("userId"));
		
		//does not initialize password, as it is not displayed anywhere within the website, and remains accessible in the database.
		u = User.getUserFromDatabaseById(userId);
		
		if(u == null){
			request.setAttribute("errorMessage","profilereload.error");
		}else{
			request.getSession().setAttribute("currentUser", u);
		}
		try {
			request.getRequestDispatcher("Profile.jsp").forward(request, response); 
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}
