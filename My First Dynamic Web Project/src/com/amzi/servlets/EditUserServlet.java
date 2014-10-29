package com.amzi.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.amzi.dao.User;
import com.amzi.dao.Login;

@WebServlet("/EditUserServlet")
public class EditUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	HttpSession userSession = request.getSession(false);    	
    	User userToEdit = (User) userSession.getAttribute("currentUser");
    	response.setContentType("text/plain");
    	response.setCharacterEncoding("UTF-8");
    	/*if(userSession == null){

    	}*/    
          
        String name=request.getParameter("loginUsername");    
        String pass=request.getParameter("loginPassword");
        String newPass=request.getParameter("newPass");
        String newUsername = request.getParameter("newUsername");

        
        if(Login.validate(name, pass) != null){   
        	if(userToEdit.changePass(newUsername, newPass)){ 
        		response.getWriter().print("SUCCESS");   
            }    
            else{    
                response.getWriter().print("SQL_ERROR");    
            }       
        }    
        else{   
            response.getWriter().print("WRONG_PASS");    
        }    
		
	}

}
