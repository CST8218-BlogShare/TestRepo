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
import com.amzi.dao.PostCreate;
  
public class BlogCreateServlet extends HttpServlet{  
  
	 private static final long serialVersionUID = 1L;
	 
	 public void doPost(HttpServletRequest request, HttpServletResponse response){
		 
		 Blog b = null;
		 PostCreate p = null;
		 PrintWriter out = null;
		 int userId;
		 
		 //If a session has not been created, none will be created
		 HttpSession userSession = request.getSession(false);
		
		 response.setContentType("text/html");
		 try{
			 out = response.getWriter();
		 }catch(IOException ioE){
			 ioE.printStackTrace();
			 return;
		 }
		
		String blogTitle=request.getParameter("blogTitle");
		String postTitle=request.getParameter("postTitle");
		String postBody=request.getParameter("postBody");

		
		b = new Blog(blogTitle);
		p = new PostCreate(postTitle,postBody);
		
		try{
		
			userId = (int) userSession.getAttribute("userId");

		}catch(NumberFormatException nfE){
			nfE.printStackTrace();
			return;
		}
		/*The function insertBlogInDatabase() is called to take the contents entered into the
		 form within blogCreate held within Blog Object b, and insert this info into the database
		 
		 This function also initializes the Blog's blogId data member with an integer value.
		 */
		
		 if(b.insertBlogInDatabase(userId) && p.insertPostInDatabase(userId, b)){
			 //getServletContext().setAttribute("errorCode", 0);
			 
			 
			 /*Adding the newly created blog object to the ServletContext object, 
			   allowing it and it's data members to be retrieved within Blog.jsp*/
			 
			 userSession.setAttribute("currentBlog", b);
			 userSession.setAttribute("blogId", b.getBlogId());//may not need to re-add
			 
			 //Get the current users bloglist from Context
			 //Add the new blog to the list and load it back into the context
			 ArrayList<String> userBlogList = (ArrayList<String>) userSession.getAttribute("userBlogList");
			
			 if(userBlogList != null){
				 userBlogList.add(b.getBlogTitle());
				 //userSession.setAttribute("userBlogList", userBlogList);
				 	//Adding userSession back into the session is uneeded as userBlogList has the same reference id as the object stored in the userSession.
			 }
		 
			 
			
			 //userSession.setAttribute("CreationDate", BlogCreate.creationDate);
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
		
		 if(out != null){
		 	out.close();
		 }
	 }
} 