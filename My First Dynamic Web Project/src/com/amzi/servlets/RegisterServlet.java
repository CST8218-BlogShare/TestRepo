package com.amzi.servlets;

import java.io.IOException;  
import java.io.PrintWriter;  

import javax.servlet.RequestDispatcher;  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import javax.servlet.http.HttpSession;

import com.amzi.dao.Login;
import com.amzi.dao.Register; 
import com.amzi.dao.User;

public class RegisterServlet extends HttpServlet {

	 private static final long serialVersionUID = 1L;  
	  
	    public void doPost(HttpServletRequest request, HttpServletResponse response)    
	            throws ServletException, IOException { //need to handle other exceptions.
	  
	        response.setContentType("text/html");    
	        PrintWriter out = response.getWriter();    
	          
	        String name=request.getParameter("registerUsername");    
	        String pass=request.getParameter("registerUserPass");
	        String pass2=request.getParameter("registerReenterPass");
	        
	        HttpSession userSession = request.getSession();  
	        
	        if(userSession == null){
	    		/*Is this even possible????
	    		  since the page object always contains a session object and we don't explictly set it to null*/
	    	}
	            
	        if(Register.validate(name, pass, pass2)){  
	        	getServletContext().setAttribute("errorCode", 0);
	        	
	        	
	        	User u = new User(Register.userId,name,pass,Login.dateRegistered);
	        	
	        	userSession.setAttribute("currentUser", u);
	        	/*userSession.setAttribute("userId", Register.userId);
	        	userSession.setAttribute("username",n);
	            userSession.setAttribute("dateRegistered", Register.dateRegistered);*/
	           
	            RequestDispatcher rd=request.getRequestDispatcher("/LoadProfileServlet");    
	            rd.forward(request,response);    
	        }    
	        else{
	        	getServletContext().setAttribute("errorCode", 1);
	        	getServletContext().setAttribute("errorMessage", Register.errorMessege);
	        	
	        	RequestDispatcher rd=request.getRequestDispatcher("Home.jsp");    
	            rd.include(request,response);    
	        }    
	  
	        out.close();    
	    }
}