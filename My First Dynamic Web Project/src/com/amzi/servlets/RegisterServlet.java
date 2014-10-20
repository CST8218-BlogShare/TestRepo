package com.amzi.servlets;

import java.io.IOException;  

import javax.servlet.RequestDispatcher;  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import javax.servlet.http.HttpSession;

import com.amzi.dao.Register; 
import com.amzi.dao.User;

public class RegisterServlet extends HttpServlet {

	 private static final long serialVersionUID = 1L;  
	  
	    public void doPost(HttpServletRequest request, HttpServletResponse response){ //need to handle other exceptions.
	    	User u = null;
	    	String name = "";
	    	String pass = "";
	    	String pass2 = "";
	    	    	
	        //does this need to be done??
	    	response.setContentType("text/html");    
	          
	        name=request.getParameter("registerUsername");    
	        pass=request.getParameter("registerUserPass");
	        pass2=request.getParameter("registerReenterPass");
	        
	        HttpSession userSession = request.getSession();  
	        
	        if(userSession == null){
	    		/*Is this even possible????
	    		  since the page object always contains a session object and we don't explictly set it to null*/
	    	}
	           
	        u = Register.validate(name, pass, pass2);
	        
	        if(u != null){  
	        	getServletContext().setAttribute("errorCode", 0);
	        		        	
	        	userSession.setAttribute("currentUser", u);
	        	
	        	//added for use in postCreate.jsp, since only the single value from user is needed there
	        	//userSession.setAttribute("userId", u.getUserId());
	        	
	        	//added for use in home.jsp, since only the single value from user is needed there
	        	userSession.setAttribute("username",name);
	           
	            RequestDispatcher rd=request.getRequestDispatcher("/LoadProfileServlet");    
	            
	            try {
					rd.forward(request,response);
				} catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}    
	        }    
	        else{
	        	getServletContext().setAttribute("errorCode", 1);
	        	getServletContext().setAttribute("errorMessage", Register.errorMessege);
	        	
	        	RequestDispatcher rd=request.getRequestDispatcher("Home.jsp");    
	            
	        	try {
					rd.include(request,response);
				} catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}    
	        }    
	    }
}