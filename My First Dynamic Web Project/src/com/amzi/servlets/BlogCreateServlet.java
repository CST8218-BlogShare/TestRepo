package com.amzi.servlets;  
  
import java.io.IOException;  

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
	 public static String errorMessege = null;
	 public static String errorMessegeFR = null;
	 
	 public BlogCreateServlet() {
		 super();
	 }
	 
	 protected void doPost(HttpServletRequest request, HttpServletResponse response){
		 
		 HttpSession userSession = null;
		 
		 User u = null;
		 Blog b = null;
		 Post p = null;
		 PostEditPrivilege pep = null;
		 int blogId = 0;
		 int postId = 0;
		 String blogTitle = "";
		 String postTitle = "";
		 String postBody = "";
		 Boolean blogIsPublic = false;
		 Boolean postIsPublic = false;
		 Boolean isEditMode = false;
		 
		 //If a session has not been created, none will be created
		 userSession = request.getSession(false);
		
		 response.setContentType("text/html");
		
		try{
		
			u = (User) userSession.getAttribute("currentUser");

		}catch(NumberFormatException nfE){
			nfE.printStackTrace();
			System.exit(-1);
		}catch(IllegalStateException isE){
			isE.printStackTrace();
			System.exit(-1);
		}
		
		if(u == null){
			System.out.println("Current user could not be retrieved from current session");
			System.exit(-1);
		}
		
		blogTitle=request.getParameter("blogTitle");
		
		if (blogTitle.length() == 0){
			if(userSession.getAttribute("language").equals("EN"))
				request.setAttribute("errorMessage", "Error: You cannot leave the Blog Title empty.");
			else if(userSession.getAttribute("language").equals("FR")){
				request.setAttribute("errorMessage", "Erreur: Il manque un titre au Blog.");	
			}
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
		
		postTitle=request.getParameter("postTitle").trim();
		postBody=request.getParameter("postBody").trim();
		
		if (postTitle.length() == 0 || postBody.length() == 0){
			
			if(userSession.getAttribute("language").equals("EN"))
				request.setAttribute("errorMessage", "Error: You cannot leave the Post Title or Body empty.");
			else if(userSession.getAttribute("language").equals("FR")){
				request.setAttribute("errorMessage", "Erreur: Il vous manque le Titre ou le contenu du Post.");	
			}
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
		
		pep = new PostEditPrivilege();
		
		/*The function insertBlogInDatabase() is called to take the contents entered into the
		 form within blogCreate held within Blog Object b, and insert this info into the database
		 
		 This function also initializes the Blog's blogId data member with an integer value.
		 */
		
		blogId = Blog.insertBlogInDatabase(blogTitle, blogIsPublic, u.getUserId(), u.getUsername());
		
		if(blogId > 0){
			b = Blog.getBlogFromDatabaseById(blogId);
		}
		
		postId = Post.insertPostInDatabase(postTitle, postBody, u.getUserId(), u.getUsername(), postIsPublic, b, isEditMode);
		
		if(postId > 0){
			p = Post.getPostFromDatabaseById(postId);
		}
		
		/*
		 //checking if the post is part of a blog that is publicly editable....could move
	    if(b.getIsPublic() && b.getPostCount() != 0){
	    	p.setIsPublic(true);
	    }
	    
	    	b.addPost(p); 
	    
	    *
	    */
		
		 if(PostEditPrivilege.insertPostEditPrivilegeInDatabase(pep,p.getPostId(), u.getUserId()) ){
			 //getServletContext().setAttribute("errorCode", 0);
			 
			 
			 /*Adding the newly created blog object to the ServletContext object, 
			   allowing it and it's data members to be retrieved within Blog.jsp*/
			 
			 userSession.setAttribute("currentBlog", b);
			 	
			//Adding userBlogList back into the session is unneeded as userBlogList has the same reference id as the object stored in the userSession.
		 
			 RequestDispatcher rd=request.getRequestDispatcher("Blog.jsp");
			 
			try {
				rd.include(request,response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		 }
		 else{
			 request.setAttribute("errorMessage", "Error: Your blog title is not unique.");
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