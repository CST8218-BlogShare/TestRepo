package com.amzi.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amzi.dao.Blog;
import com.amzi.dao.DbConnection;
import com.amzi.dao.Post;
import com.amzi.dao.PostEditPrivilege;

/**
 * Servlet implementation class BlogDeleteServlet
 */

public class BlogDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BlogDeleteServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		Exception error = new Exception();
		DbConnection connectionManager = null;
		ArrayList<Post> postsInBlog = null;
		String blogTitle = "";
		int errorCode = 0;
		int blogId = 0;
		int postEditPrivilegeId = 0;
		
		//if this is not true, this servlet will never succeed and the request is invalid.
		if(request.getParameter("blogTitle") == null){
			System.exit(-1);
		}
		
		blogTitle = request.getParameter("blogTitle");
		
		connectionManager = DbConnection.getInstance();
		
		try{
			
			if(DbConnection.testConnection(connectionManager) == false){
				request.setAttribute("errorMessage", "blogdelete.errorconnectfailed");
				throw error;
			}
			
			/* 
			 * Setting up a database transaction where the blog and postEditPrivilege entries all need to be deleted from the database.
			 * If both items cannot be deleted, any completed deletions are reversed to this point.
			 */
			
			connectionManager.getConnection().setAutoCommit(false);
			
			blogId = Blog.getBlogIdFromDatabaseByTitle(blogTitle);
			
			if(blogId < 0){
				if(blogId == -1){
					request.setAttribute("errorMessage", "blogdelete.errorconnectfailed");
				}
				
				if(blogId == -2){
					request.setAttribute("errorMessage", "blogdelete.errorsql");
				}
				throw error;
			}
			
			postsInBlog = Post.getPostListFromDatabaseByBlogId(blogId);
			
			if(postsInBlog == null){
				request.setAttribute("errorMessage", "blogdelete.errorsql");
				throw error;
			}
			
			for(Post post : postsInBlog){
				
				postEditPrivilegeId = PostEditPrivilege.getPostEditPrivilegeIdFromDatabaseByPostId(post.getPostId());
				
				if(postEditPrivilegeId < 0){
					
					if(postEditPrivilegeId == -1){
						request.setAttribute("errorMessage", "blogdelete.errorconnectfailed");
					}
					
					if(postEditPrivilegeId == -2){
						request.setAttribute("errorMessage", "blogdelete.errorsql");
					}
					
					throw error;
				}
				
				errorCode = PostEditPrivilege.deletePostEditPrivilegeFromDatabaseById(postEditPrivilegeId);
				
				if(errorCode < 0){
					
					if(errorCode == -1){
						request.setAttribute("errorMessage", "blogdelete.errorconnectfailed");
					}
					
					if(errorCode == -2){
						request.setAttribute("errorMessage", "blogdelete.errorsql");
					}
					
					throw error;
				}
				
			}
			
			//if the method is unsuccessful the value false will be returned.
			errorCode = Blog.deleteBlogFromDatabaseById(blogId);
			
			if(errorCode < 0){
				
				if(errorCode == -1){
					request.setAttribute("errorMessage", "blogdelete.errorconnectfailed");
				}else{
					request.setAttribute("errorMessage", "blogdelete.errorsql");
				}
				
				throw error;
			}
			
			errorCode = Blog.addBlogToBlogDeleted(blogId);
			
			if(errorCode < 0){
				
				if(errorCode == -1){
					request.setAttribute("errorMessage", "blogdelete.errorconnectfailed");
				}else{
					request.setAttribute("errorMessage", "blogdelete.errorsql");
				}
				throw error;
			}
			
			if(DbConnection.testConnection(connectionManager) == false){
				request.setAttribute("errorMessage", "blogdelete.errorconnectfailed");
				throw error;
			}
			
			//Now that it is proven that the deletion of blog and postEditPrivilege were successful, the changes are applied to the database.
			connectionManager.getConnection().commit();
			
			/*
			 * Resetting the connection back to it's default behavior where every transaction is applied as soon as it is executed.
			 * This statement is placed here to avoid another try-catch block within the method. 
			 */
			
			connectionManager.getConnection().setAutoCommit(true);
			
		}catch(Exception e){
			System.out.println("Error deleting post, for more details see stack trace.");
			
			if(DbConnection.testConnection(connectionManager) == true){
				try {
					connectionManager.getConnection().rollback();
					connectionManager.getConnection().setAutoCommit(true);
				} catch (SQLException sqlE) {
					// TODO Auto-generated catch block
					sqlE.printStackTrace();
				}
			}
			e.printStackTrace();
		}
				
		try {
			request.getRequestDispatcher("Profile.jsp").forward(request, response); 
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
