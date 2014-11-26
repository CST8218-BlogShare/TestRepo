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
        Exception error = new Exception(); 
		String name=request.getParameter("loginUsername");    
        String pass=request.getParameter("loginPassword");
        String newPass=request.getParameter("newPass");
        String newUsername = request.getParameter("newUsername");
        int errorCode = 0;
        int userId = -1;

        userId = ((User) (request.getSession().getAttribute("currentUser"))).getUserId();
        
        if(userId == -1){
        	System.exit(-1);
        }
        
        try{
        
	        //checking for invalid credentials
	        
        	 newUsername = newUsername.trim();
	            
 	        if(newUsername == ""){
 	        	try {
 					response.getWriter().print("BLANK_NAME");
 				} catch (IOException e) {
 					e.printStackTrace();
 				}  
 	        	throw error;
 	        }
        	
	        newPass = newPass.trim();
	        
	        if(newPass == ""){
	        	try {
					response.getWriter().print("BLANK_PASS");
				} catch (IOException e) {
					e.printStackTrace();
				}
	        	throw error;
	        }
	         
	        if(Login.validate(name, pass) != null){   
	        
	        	errorCode = User.updateUserCredentialsInDatabase(newUsername, newPass, userId);
	        	
	        	if(errorCode < 0){
		        	if(errorCode == -1){
		        		try {
		        			response.getWriter().print("CONNECT_ERROR");
		        		} catch (IOException e) {
		        			e.printStackTrace();
		        		} 
		        		throw error;
		        	}
		        	
		        	if(errorCode == -2){
		        		 try {
		 					response.getWriter().print("SQL_ERROR");
		 				} catch (IOException e) {
		 					e.printStackTrace();
		 				}
		        		throw error;
		        	}
	        	}
	        	
	        		try {
						response.getWriter().print("SUCCESS");
					} catch (IOException e) {
						e.printStackTrace();
					}   
	        		
	        }else{   
	            try {
					response.getWriter().print("WRONG_PASS");
				} catch (IOException e) {
					e.printStackTrace();
				}   
	        }    
        }catch(Exception e){
        	System.out.println("An error occured while editing the credentials of the current user");
        	e.printStackTrace();
        }
        
	}

}
