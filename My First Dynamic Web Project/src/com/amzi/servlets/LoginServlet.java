package com.amzi.servlets;  
  
import java.io.IOException;  
import javax.servlet.RequestDispatcher;  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import javax.servlet.http.HttpSession;  
  
import com.amzi.dao.Login; 
import com.amzi.dao.User;
  
public class LoginServlet extends HttpServlet{  
  
    private static final long serialVersionUID = 1L;  
  
    public void doPost(HttpServletRequest request, HttpServletResponse response){   //need handle other exceptions.
    	User u = null;
    	
    	//If a session has not been created, none will be created
    	HttpSession userSession = request.getSession(false); 
    	
    	if(userSession == null){
    		/*Is this even possible????
    		  since the page object always contains a session object and we don't explictly set it to null*/
    	}
    	
    	response.setContentType("text/html");       
          
        String name=request.getParameter("loginUsername");    
        String pass=request.getParameter("loginUserpass");   
            
        u = Login.validate(name, pass);
        
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
        	getServletContext().setAttribute("errorMessage", Login.errorMessege);

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