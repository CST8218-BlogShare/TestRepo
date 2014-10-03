package com.amzi.servlets;  
  
import java.io.IOException;  
import javax.servlet.RequestDispatcher;  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import javax.servlet.http.HttpSession;  
  

import com.amzi.dao.Login;  
  
public class LoginServlet extends HttpServlet{  
  
    private static final long serialVersionUID = 1L;  
  
    public void doPost(HttpServletRequest request, HttpServletResponse response)    
            throws ServletException, IOException {   //need handle other exceptions.
    	
    	
    	//If a session has not been created, none will be created
    	HttpSession userSession = request.getSession(false); 
    	
    	if(userSession == null){
    		/*Is this even possible????
    		  since the page object always contains a session object and we don't explictly set it to null*/
    	}
    	
    	
    	
    	response.setContentType("text/html");    
        //PrintWriter out = response.getWriter();    
          
        String n=request.getParameter("loginUsername");    
        String p=request.getParameter("loginUserpass");   
            
        if(Login.validate(n, p)){   
        	getServletContext().setAttribute("errorCode", 0);
        	userSession.setAttribute("username",n);
            userSession.setAttribute("dateRegistered", Login.dateRegistered);
            
            RequestDispatcher rd=request.getRequestDispatcher("Profile.jsp");    
            rd.forward(request,response);    
        }    
        else{    
            getServletContext().setAttribute("errorCode", 1);
        	getServletContext().setAttribute("errorMessage", Login.errorMessege);

            RequestDispatcher rd=request.getRequestDispatcher("Home.jsp");    
            rd.include(request,response);    
        }    
     
    }    
}   