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
  
public class LoginServlet extends HttpServlet{  
  
    private static final long serialVersionUID = 1L;  
  
    public void doPost(HttpServletRequest request, HttpServletResponse response)    
            throws ServletException, IOException {    
  
        response.setContentType("text/html");    
        PrintWriter out = response.getWriter();    
          
        String n=request.getParameter("loginUsername");    
        String p=request.getParameter("loginUserpass");   
          
        /*HttpSession session = request.getSession(false);  
        if(session!=null) {
        	//if session is null need to throw an error. 
        }*/
  
        if(Login.validate(n, p)){   
        	HttpSession session = request.getSession(false);  
              if(session!=null) {
            	  session.setAttribute("name",n);
                  session.setAttribute("dateRegistered", Login.dateRegistered);
              }
              
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
            out.print("<p style=\"color:red\">But they really like appearing here. <br> Sorry username or password error on login.</p>");    
            RequestDispatcher rd=request.getRequestDispatcher("home.jsp");    
            rd.include(request,response);    
        }    
  
        out.close();    
    }    
}   