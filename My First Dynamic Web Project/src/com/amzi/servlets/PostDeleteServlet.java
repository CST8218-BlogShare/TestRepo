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
		Exception error = new Exception();
		DbConnection connectionManager = null;
		Blog b = null;
		int errorCode = 0;
		int postPos = -1;
		int postEditPrivilegeId = 0;
		
		b = (Blog) request.getSession().getAttribute("currentBlog");
		
		
		//if current blog cannot be retrieved, the session is no longer valid.
		if(b == null){
			System.exit(1);
		}
		
		connectionManager = DbConnection.getInstance();
		
		try{
			
			if(DbConnection.testConnection(connectionManager) == false){
				request.setAttribute("errorMessage", "postdelete.errorconnectfailed");
				throw error;
			}
			
			connectionManager.getConnection().setAutoCommit(false);
			
			postPos = Integer.parseInt(request.getParameter("postPos"));
			
			postEditPrivilegeId = PostEditPrivilege.getPostEditPrivilegeIdFromDatabaseByPostId(b.getPostAt(postPos).getPostId());
			
			if(postEditPrivilegeId <= 0){
				if(postEditPrivilegeId == -1){
					request.setAttribute("errorMessage", "postdelete.errorconnectfailed");
				}
				
				if(postEditPrivilegeId == -2){
					request.setAttribute("errorMessage", "postdelete.errorsql");
				}
				
				throw error;
			}
			
			errorCode = PostEditPrivilege.deletePostEditPrivilegeFromDatabaseById(postEditPrivilegeId);
			
			if(errorCode < 0){
				
				if(errorCode == -1){
					request.setAttribute("errorMessage", "postdelete.errorconnectfailed");
				}
				
				if(errorCode == -2){
					request.setAttribute("errorMessage", "postdelete.errorsql");
				}
				
				throw error;
			}
			
			errorCode = Post.deletePostFromDatabaseById(b.getPostAt(postPos).getPostId());
			
			if(errorCode < 0){
				
				if(errorCode == -1){
					request.setAttribute("errorMessage", "postdelete.errorconnectfailed");
				}else{
					request.setAttribute("errorMessage", "postdelete.errorsql");
				}
				throw error;
			}
			
			errorCode = Post.addPostToPostDeleted(b.getPostAt(postPos).getPostId());
			
			if(errorCode < 0){
				if(errorCode == -1){
					request.setAttribute("errorMessage", "postdelete.errorconnectfailed");
				}else{
					request.setAttribute("errorMessage", "postdelete.errorsql");
				}
				
				throw error;
			}
			
			if(DbConnection.testConnection(connectionManager) == false){
				request.setAttribute("errorMessage", "postdelete.errorconnectfailed");
				throw error;
			}
			
			//Now that it is proved that the insertion of post and postEditPrivilege were successful, the changes are applied to the database. 
			connectionManager.getConnection().commit();
			
			/*
			 * Ressetting the connection back to it's default behavior where every transaction is applied as soon as it is executed.
			 * This statement is placed here to avoid another try-catch block within the method. 
			 */
			
			connectionManager.getConnection().setAutoCommit(true);
			
			
		}catch(Exception e){
			if(DbConnection.testConnection(connectionManager) == true){
				try {
					DbConnection.getInstance().getConnection().rollback();
					
					/*
					 * Resetting the connection back to it's default behavior where every transaction is applied as soon as it is executed.
					 * This statement is placed here to avoid another try-catch block within the method. 
					 */
					
					DbConnection.getInstance().getConnection().setAutoCommit(true);
				} catch (SQLException sqlE) {
					// TODO Auto-generated catch block
					sqlE.printStackTrace();
				}
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
