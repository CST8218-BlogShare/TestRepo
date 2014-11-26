package com.amzi.servlets;  
  
import java.io.IOException;  
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import javax.servlet.http.HttpSession;  
  



import com.amzi.dao.Blog;
import com.amzi.dao.DbConnection;
import com.amzi.dao.Post;
import com.amzi.dao.PostEditPrivilege;
import com.amzi.dao.User;

public class BlogCreateServlet extends HttpServlet{  
  
	 private static final long serialVersionUID = 1L;
	 public static String errorMessege = null;
	 public static String errorMessegeFR = null;
	 
	 public BlogCreateServlet() {
		 super();
	 }
	 
	 protected void doPost(HttpServletRequest request, HttpServletResponse response){
		 
		 HttpSession userSession = null;
		 
		 Exception error = new Exception();
		 Boolean errorState = false;
		 
		 User u = null;
		 Blog b = null;
		 Post p = null;
		 int blogId = 0;
		 int postId = 0;
		 int postEditPrivilegeId = 0;
		 String blogTitle = "";
		 String postTitle = "";
		 String postBody = "";
		 Boolean blogIsPublic = false;
		 Boolean postIsPublic = false;
		 Boolean isEditMode = false;
		 
		 
		 //If a session has not been created, none will be created
		 userSession = request.getSession(false);
				
		try{
		
			u = (User) userSession.getAttribute("currentUser");

		}catch(NumberFormatException nfE){
			nfE.printStackTrace();
			System.exit(-1);
		}catch(IllegalStateException isE){
			isE.printStackTrace();
			System.exit(-1);
		}
		
		//if current user cannot be retrieved, the session is no longer valid.
		if(u == null){
			System.out.println("Current user could not be retrieved from current session");
			System.exit(-1);
		}
		
		try{
			
			blogTitle=request.getParameter("blogTitle").trim();
			
			if (blogTitle.length() == 0){
				request.setAttribute("errorMessage","errornoblogtitle");	
				throw error;
			}
			
			postTitle=request.getParameter("postTitle").trim();
			postBody=request.getParameter("postBody").trim();
			
			if (postTitle.length() == 0){
				request.setAttribute("errorMessage","errornopostitle");
				throw error;
			}
			
			if(postBody.length() == 0){
				
				request.setAttribute("errorMessage","errornopostcontent");	
				throw error;
			}
			
			//if the checkbox has not been activated, the parameter will not be initialized and the value null will be returned.
			if(request.getParameter("blogEditableCheckBox") != null){
				blogIsPublic = true;
			}
			
			/* 
			 * Setting up a database transaction where the blog, post and postEditPrivilege entries all need to be inserted into the database.
			 * If all three items cannot be inserted into the database, any completed insertions are reversed to this point.
			 */
			
			DbConnection.getInstance().getConnection().setAutoCommit(false);
			
			/*The function insertBlogInDatabase() is called to take the contents entered into the
			 form within blogCreate held within Blog Object b, and insert this info into the database
			 
			 This function also initializes the Blog's blogId data member with an integer value.
			 */
			blogId = Blog.insertBlogInDatabase(blogTitle, blogIsPublic, u.getUserId(), u.getUsername());
			
			if(blogId < 0){
				if(blogId == -1){
					request.setAttribute("errorMessage", "errorconnectfailed");
				}
				
				if(blogId == -2){
					request.setAttribute("errorMessage", "errorsql");
				}
				
				if(blogId == -3){
					request.setAttribute("errorMessage", "errorblogexists");
				}
				throw error;
			}
			
			b = Blog.getBlogFromDatabaseById(blogId);
			
			if(b == null){
				request.setAttribute("errorMessage", "errorsql");
				throw error;
			}
			
			postId = Post.insertPostInDatabase(postTitle, postBody, u.getUserId(), u.getUsername(), postIsPublic, b, isEditMode);
			
			if(postId < 0){
				
				if(postId == -1){
					request.setAttribute("errorMessage", "errorconnectfailed");
				}
				
				if(postId == -2){
					request.setAttribute("errorMessage", "errorsql");
				}
				
				throw error;
			}
			
			p = Post.getPostFromDatabaseById(postId);
			
			if(p == null){
				request.setAttribute("errorMessage", "error2");
				throw error;
			}
						
			postEditPrivilegeId = PostEditPrivilege.insertPostEditPrivilegeInDatabase(postId, u.getUserId());
			
			if(postEditPrivilegeId < 0){
				
				if(postEditPrivilegeId == -1){
					request.setAttribute("errorMessage", "errorconnectfailed");
				}else{
					request.setAttribute("errorMessage", "errorsql");
				}
				
				
				throw error;
			}		 
		
			//Now that it is proved that the insertion of blog, post and postEditPrivilege were successful, the changes are applied to the database. 
			DbConnection.getInstance().getConnection().commit();
			
			/*
			 * Ressetting the connection back to it's default behavior where every transaction is applied as soon as it is executed.
			 * This statement is placed here to avoid another try-catch block within the method. 
			 */
			
			DbConnection.getInstance().getConnection().setAutoCommit(true);
			
			/*
			 * Adding the newly created blog object to the ServletContext object, 
			 * allowing it and it's data members to be retrieved within Blog.jsp
			 */	 
			userSession.setAttribute("currentBlog", b);
				 	
			//Adding userBlogList back into the session is unneeded as userBlogList has the same reference id as the object stored in the userSession.
		}catch(Exception e){
			errorState = true;
			try {
				DbConnection.getInstance().getConnection().rollback();
				
				/*
				 * Ressetting the connection back to it's default behavior where every transaction is applied as soon as it is executed.
				 * This statement is placed here to avoid another try-catch block within the method. 
				 */
				
				DbConnection.getInstance().getConnection().setAutoCommit(true);
			} catch (SQLException sqlE) {
				// TODO Auto-generated catch block
				sqlE.printStackTrace();
			}
			e.printStackTrace();
		}
		
		if(errorState == false){
			RequestDispatcher rd=request.getRequestDispatcher("Blog.jsp");
				 
			try {
				rd.include(request,response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}else{
			 RequestDispatcher rd=request.getRequestDispatcher("BlogCreate.jsp");
			 
			 try {
				rd.include(request,response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			 
		}
	 }
} 