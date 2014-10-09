package com.amzi.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.amzi.dao.Login;

@WebServlet("/EditUserServlet")
public class EditUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	HttpSession userSession = request.getSession(false);    	
    	if(userSession == null){

    	}    
          
        String name=request.getParameter("loginUsername");    
        String pass=request.getParameter("loginPassword");
        String newPass=request.getParameter("newPass");
        String newUsername = request.getParameter("newUsername");
        
        Login userToEdit = new Login();
        
        if(userToEdit.validate(name, pass)){   
        	if(userToEdit.changePass(newUsername, newPass)){   
            	
                RequestDispatcher rd=request.getRequestDispatcher("/LoadProfileServlet");    
                rd.forward(request,response);    
            }    
            else{    
                getServletContext().setAttribute("errorCode", 1);
            	getServletContext().setAttribute("errorMessage", "Error updating the password.");

                //RequestDispatcher rd=request.getRequestDispatcher("ProfileEdit.jsp");    
                //rd.include(request,response);    
            	RequestDispatcher rd=request.getRequestDispatcher("/LoadProfileServlet");    
                rd.forward(request,response);    
            }       
        }    
        else{    
            getServletContext().setAttribute("errorCode", 1);
        	getServletContext().setAttribute("errorMessage", "You entered an incorrect Password.");

            //RequestDispatcher rd=request.getRequestDispatcher("ProfileEdit.jsp");    
            //rd.include(request,response);    
        	RequestDispatcher rd=request.getRequestDispatcher("/LoadProfileServlet");    
            rd.forward(request,response);    
        }    
		
	}

}
