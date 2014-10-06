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
  
    public void doPost(HttpServletRequest request, HttpServletResponse response){
    	
    	Login l = new Login();
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
            
        if(l.validate(n, p)){   
        	getServletContext().setAttribute("errorCode", 0);
        	userSession.setAttribute("username",n);
        	userSession.setAttribute("userId", l.getUserId());
            userSession.setAttribute("dateRegistered", l.getDateRegistered());
            
            RequestDispatcher rd=request.getRequestDispatcher("Profile.jsp");    
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
        	getServletContext().setAttribute("errorMessage", l.getErrorMessage());

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