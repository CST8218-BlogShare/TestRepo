package com.amzi.servlets;  
  
import java.io.IOException;  

import javax.servlet.RequestDispatcher;  
import javax.servlet.ServletException;  
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import javax.servlet.http.HttpSession;  
  




import com.amzi.dao.Blog;
import com.amzi.dao.Login;
import com.amzi.dao.Post;
import com.amzi.dao.PostEditPrivilege;
import com.amzi.dao.User;

@WebServlet("/BlogCreateServlet")
public class BlogCreateServlet extends HttpServlet{  
  
	 private static final long serialVersionUID = 1L;
	 public static String errorMessege = null;
	 public static String errorMessegeFR = null;
	 
	 protected void doPost(HttpServletRequest request, HttpServletResponse response){
		 
		 Blog b = null;
		 Post p = null;
		 PostEditPrivilege pep = null;
		 User u = null;
		 String blogTitle = "";
		 String postTitle = "";
		 String postBody = "";
		 Boolean blogIsPublic = false;
		 
		 
		 //If a session has not been created, none will be created
		 HttpSession userSession = request.getSession(false);
		
		 response.setContentType("text/html");
		
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
		
		blogTitle=request.getParameter("blogTitle");
		
		if (blogTitle.length() == 0){
			
	        request.setAttribute("errorMessage", "Error: You cannot leave the Blog Title empty.");
			RequestDispatcher rd=request.getRequestDispatcher("BlogCreate.jsp");
			try {
				rd.forward(request,response);
				return;
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		postTitle=request.getParameter("postTitle");
		postBody=request.getParameter("postBody");
		
		if (postTitle.length() == 0 || postBody.length() == 0){
			
			request.setAttribute("errorMessage", "Error: You cannot leave the Post Title or Body empty.");
			RequestDispatcher rd=request.getRequestDispatcher("BlogCreate.jsp");
			try {
				rd.forward(request,response);
				return;
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//if the checkbox has not been activated, the parameter will not be initialized and the value null will be returned.
		if(request.getParameter("blogEditableCheckBox") != null){
			blogIsPublic = true;
		}
		
		b = new Blog(blogTitle,u.getUsername(),blogIsPublic);
		//the first post is never publicly editable. 
		p = new Post(postTitle,postBody, u.getUsername(),false);
		pep = new PostEditPrivilege();
		
		/*The function insertBlogInDatabase() is called to take the contents entered into the
		 form within blogCreate held within Blog Object b, and insert this info into the database
		 
		 This function also initializes the Blog's blogId data member with an integer value.
		 */
		
		 if(b.insertBlogInDatabase(u.getUserId()) && p.insertPostInDatabase(u.getUserId(),b, false) && pep.insertPostEditPrivilegeInDatabase(p.getPostId(), u.getUserId()) ){
			 //getServletContext().setAttribute("errorCode", 0);
			 
			 
			 /*Adding the newly created blog object to the ServletContext object, 
			   allowing it and it's data members to be retrieved within Blog.jsp*/
			 
			 userSession.setAttribute("currentBlog", b);
			 	
			//Adding userBlogList back into the session is unneeded as userBlogList has the same reference id as the object stored in the userSession.
		 
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
			 request.setAttribute("errorMessage", "Error: Your blog title is not unique.");
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