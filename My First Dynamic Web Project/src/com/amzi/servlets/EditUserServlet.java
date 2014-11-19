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

    	User userToEdit = (User) request.getSession().getAttribute("currentUser");
    	
    	if(userToEdit == null){
    		//if the attribute currentUser cannot be retrieved the session has become invalid
    		System.exit(-1);
    	}
          
        String name=request.getParameter("loginUsername");    
        String pass=request.getParameter("loginPassword");
        String newPass=request.getParameter("newPass");
        String newUsername = request.getParameter("newUsername");

        
        if(Login.validate(name, pass) != null){   
        	if(userToEdit.updateUserCredentialsInDatabase(newUsername, newPass)){ 
        		try {
					response.getWriter().print("SUCCESS");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
            }    
            else{    
                try {
					response.getWriter().print("SQL_ERROR");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}    
            }       
        }    
        else{   
            try {
				response.getWriter().print("WRONG_PASS");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    
        }    
		
	}

}
