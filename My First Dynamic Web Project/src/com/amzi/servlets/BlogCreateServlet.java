package com.amzi.servlets;  
  
import java.io.IOException;  
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import javax.servlet.http.HttpSession;  
  

import com.amzi.dao.Blog;
import com.amzi.dao.Post;
import com.amzi.dao.PostEditPrivilege;
import com.amzi.dao.User;
  
public class BlogCreateServlet extends HttpServlet{  
  
	 private static final long serialVersionUID = 1L;
	 
	 public void doPost(HttpServletRequest request, HttpServletResponse response){
		 
		 Blog b = null;
		 Post p = null;
		 PostEditPrivilege pep = null;
		 User u = null;
		 
		 //If a session has not been created, none will be created
		 HttpSession userSession = request.getSession(false);
		
		 response.setContentType("text/html");
		
		String blogTitle=request.getParameter("blogTitle");
		String postTitle=request.getParameter("postTitle");
		String postBody=request.getParameter("postBody");

		
		b = new Blog(blogTitle);
		p = new Post(postTitle,postBody);
		pep = new PostEditPrivilege();
		
		try{
		
			u = (User) userSession.getAttribute("currentUser");

		}catch(NumberFormatException nfE){
			nfE.printStackTrace();
			//return;
		}catch(IllegalStateException isE){
			isE.printStackTrace();
			//return;
		}
		
		if(u == null){
			System.out.println("Current user could not be retrieved from current session");
			return;
		}
		
		/*The function insertBlogInDatabase() is called to take the contents entered into the
		 form within blogCreate held within Blog Object b, and insert this info into the database
		 
		 This function also initializes the Blog's blogId data member with an integer value.
		 */
		
		 if(b.insertBlogInDatabase(u.getUserId(), u.getUsername()) && p.insertPostInDatabase(u.getUserId(),u.getUsername(),b) && pep.insertPostEditPrivilegeInDatabase(p.getPostId(), u.getUserId()) ){
			 //getServletContext().setAttribute("errorCode", 0);
			 
			 
			 /*Adding the newly created blog object to the ServletContext object, 
			   allowing it and it's data members to be retrieved within Blog.jsp*/
			 
			 userSession.setAttribute("currentBlog", b);
			 
			 //userSession.setAttribute("blogId", b.getBlogId());//may not need to re-add
			 
			 //Get the current users bloglist from Context
			 //Add the new blog to the list and load it back into the context
			 ArrayList<String> userBlogList = (ArrayList<String>) userSession.getAttribute("userBlogList");
			
			 if(userBlogList != null){
				 userBlogList.add(0,b.getBlogTitle());
				 //Adding userBlogList back into the session is unneeded as userBlogList has the same reference id as the object stored in the userSession.
			 }
		 
			 RequestDispatcher rd=request.getRequestDispatcher("Blog.jsp");
			 
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
		 else{
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
	 }
} 