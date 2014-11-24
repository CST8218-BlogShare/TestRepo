package com.amzi.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amzi.dao.User;
import com.amzi.dao.Login;

public class EditUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public EditUserServlet() {
		 super();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {          
        String name=request.getParameter("loginUsername");    
        String pass=request.getParameter("loginPassword");
        String newPass=request.getParameter("newPass");
        String newUsername = request.getParameter("newUsername");

        int userId = ((User) (request.getSession().getAttribute("currentUser"))).getUserId();
        
        if(Login.validate(name, pass) != null){   
        	if(User.updateUserCredentialsInDatabase(newUsername, newPass, userId)){ 
        		try {
					response.getWriter().print("SUCCESS");
				} catch (IOException e) {
					e.printStackTrace();
				}   
            }    
            else{    
                try {
					response.getWriter().print("SQL_ERROR");
				} catch (IOException e) {
					e.printStackTrace();
				}    
            }       
        }    
        else{   
            try {
				response.getWriter().print("WRONG_PASS");
			} catch (IOException e) {
				e.printStackTrace();
			}    
        }    
		
	}

}
