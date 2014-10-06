package com.amzi.servlets;  
  
import java.io.IOException;  

import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import javax.servlet.http.HttpSession;  
  

import com.amzi.dao.Blog;


public class BlogServlet extends HttpServlet{  
  
    private static final long serialVersionUID = 1L;  
    private Blog b = new Blog();
  
    public void doPost(HttpServletRequest request, HttpServletResponse response){ 
    	
    	int blogId;
    	PrintWriter out = null; 
    	//If a session has not been created, none will be created
    	
    	HttpSession userSession = request.getSession(false); 
    	
    	if(userSession == null){
    		/*Is this even possible????
    		  since the page object always contains a session object and we don't explicitly set it to null*/
    	}
    	
    	response.setContentType("text/html");  
    	
    	try{
    		out = response.getWriter();
    		return;
    	}catch(IOException ioE){
    		System.out.println("Print Writer could not be created from http request for this page, aborting Blog creation");
    		ioE.printStackTrace();
    	}
        
      	String blogTitle=request.getParameter("blogTitle");    
        String postTitle=request.getParameter("postTitle");    
        String postBody=request.getParameter("postBody");  
        int userId = Integer.parseInt((String) userSession.getAttribute("userId"));
        
        /*The function createBlog is called to take the contents entered into the
          form within blogCreate and insert the info into the database*/
        
        if(b.createBlog(blogTitle,postTitle, postBody, userId)){   
        	//getServletContext().setAttribute("errorCode", 0);
        	
        	userSession.setAttribute("blogTitle",blogTitle);
        	userSession.setAttribute("postTitle",postTitle);
        	userSession.setAttribute("postBody",postBody);
           
        	//userSession.setAttribute("CreationDate", BlogCreate.creationDate);
        }    
        else{    
            //getServletContext().setAttribute("errorCode", 1);
        	//getServletContext().setAttribute("errorMessage", BlogCreate.errorMessege);

            RequestDispatcher rd=request.getRequestDispatcher("BlogCreate.jsp");    
            
            //modify
            try {
				rd.include(request,response);
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    
        }    
        
        blogId = b.blogId;
        
        /*The function buildBlog is called in order to retrieve the author of blog and all
         *posts within the blog other than the first post created during blogCreation. */
        
        if(b.buildBlog(blogId, blogTitle, userId)){   
        	//getServletContext().setAttribute("errorCode", 0);
        	
        	userSession.setAttribute("blogAuthor", b.getAuthor());
            RequestDispatcher rd=request.getRequestDispatcher("Blog.jsp");    
            
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
            //getServletContext().setAttribute("errorCode", 1);
        	//getServletContext().setAttribute("errorMessage", BlogCreate.errorMessege);

            RequestDispatcher rd=request.getRequestDispatcher("BlogCreate.jsp");    
           
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
        
        //adding any additional posts to the page using the contents retrieved from the post table matching the current blogId.
        
        /*for(int i = 0; i < newBlog.getPostCount(); ++i ){
        	
        }*/
        
        if(out != null){
        	out.close();
        }
     
    }    
}   