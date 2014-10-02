package com.amzi.servlets;

import java.io.IOException;  
import java.io.PrintWriter;  
  


import javax.servlet.RequestDispatcher;  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import com.amzi.dao.Register; 

public class RegisterServlet extends HttpServlet {

	 private static final long serialVersionUID = 1L;  
	  
	    public void doPost(HttpServletRequest request, HttpServletResponse response)    
	            throws ServletException, IOException {    
	  
	        response.setContentType("text/html");    
	        PrintWriter out = response.getWriter();    
	          
	        String n=request.getParameter("registerUsername");    
	        String p=request.getParameter("registerUserPass");
	        String p2=request.getParameter("registerReenterPass");
	        
	        HttpSession session = request.getSession();  
	            
	        if(Register.validate(n, p, p2)){  
	        	this.getServletContext().setAttribute("errorCode", 0);
	        	session.setAttribute("username",n);
	            session.setAttribute("dateRegistered", Register.dateRegistered);
	           
	            
	            /*
	               * 
	               * 
	               * could use this approach to catch error in event of null.
	               * try{
	               * 
	               * 	if(session == null){
	               * 		throw exception;
	               * 	}
	               * 
	               *  	HttpSession session = request.getSession(false);  
	               *  	session.setAttribute("name",n);
	               *  	session.setAttribute("dateRegistered", Login.dateRegistered);
	               * }catch(Exception e){
	               * 	 System.out.println(e);  
	               *    //load error page once fully implemented
	               * }
	               * 
	               */
	        	
	        	
	        	RequestDispatcher rd=request.getRequestDispatcher("Profile.jsp");    
	            rd.forward(request,response);    
	        }    
	        else{
	        	this.getServletContext().setAttribute("errorCode", 1);
	        	session.setAttribute("errorMessage",Register.errorMessage);
	        	
	        	RequestDispatcher rd=request.getRequestDispatcher("Home.jsp");    
	            rd.include(request,response);    
	        }    
	  
	        out.close();    
	    }
}