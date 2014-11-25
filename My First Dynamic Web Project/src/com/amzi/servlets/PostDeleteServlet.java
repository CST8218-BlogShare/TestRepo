package com.amzi.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amzi.dao.Blog;
import com.amzi.dao.DbConnection;
import com.amzi.dao.Post;
import com.amzi.dao.PostEditPrivilege;

/**
 * Servlet implementation class PostDeleteServlet
 */
public class PostDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PostDeleteServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Blog b = null;
		Exception error = new Exception();
		int errorCode = 0;
		int postPos = -1;
		int postEditPrivilegeId = 0;
		
		b = (Blog) request.getSession().getAttribute("currentBlog");
		
		
		//if current blog cannot be retrieved, the session is no longer valid.
		if(b == null){
			System.exit(1);
		}
		
		try{
			
			DbConnection.getInstance().getConnection().setAutoCommit(false);
			
			postPos = Integer.parseInt(request.getParameter("postPos"));
			
			postEditPrivilegeId = PostEditPrivilege.getPostEditPrivilegeIdFromDatabaseByPostId(b.getPostAt(postPos).getPostId());
			
			if(postEditPrivilegeId <= 0){
				if(postEditPrivilegeId == -1){
					request.setAttribute("errorMessage", "Error retrieving post edit privilege by id, error connecting to database");
				}
				
				if(postEditPrivilegeId == -2){
					request.setAttribute("errorMessage", "Error retrieving post edit privilege by id, SQL error.");
				}
				
				throw error;
			}
			
			errorCode = PostEditPrivilege.deletePostEditPrivilegeFromDatabaseById(postEditPrivilegeId);
			
			if(errorCode < 0){
				
				if(errorCode == -1){
					request.setAttribute("errorMessage", "Error deleting postEditPrivilege, error connecting to database.");
				}
				
				if(errorCode == -2){
					request.setAttribute("errorMessage", "Error deleting postEditPrivilege, SQL error.");
				}
				
				throw error;
			}
			
			errorCode = Post.deletePostFromDatabaseById(b.getPostAt(postPos).getPostId());
			
			if(errorCode < 0){
				
				if(errorCode == -1){
					request.setAttribute("errorMessage", "Error deleting post, error connecting to database.");
				}
				
				if(errorCode == -2){
					request.setAttribute("errorMessage", "Error deleting post, SQL error.");
				}
				throw error;
			}
			
			errorCode = Post.addPostToPostDeleted(b.getPostAt(postPos).getPostId());
			
			if(errorCode < 0){
				if(errorCode == -1){
					request.setAttribute("errorMessage", "Error adding postid to postDeleted, error connecting to database.");
				}
				
				if(errorCode == -2){
					request.setAttribute("errorMessage", "Error adding postid to postDeleted, SQL error.");
				}
				
				if(errorCode == -3){
					request.setAttribute("errorMessage", "Error adding postid to postDelete, value of postId is invalid.");
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
			
			
		}catch(Exception e){
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
		
		RequestDispatcher rd=request.getRequestDispatcher("BlogEdit.jsp");
		 
		 try {
			rd.include(request,response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
