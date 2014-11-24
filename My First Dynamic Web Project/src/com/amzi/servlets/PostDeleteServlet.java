package com.amzi.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amzi.dao.Blog;
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
			postPos = Integer.parseInt(request.getParameter("postPos"));
			
			postEditPrivilegeId = PostEditPrivilege.getPostEditPrivilegeIdFromDatabaseByPostId(b.getPostAt(postPos).getPostId());
			
			if(postEditPrivilegeId <= 0){
				if(postEditPrivilegeId == -1){
					request.setAttribute("errorMessage", "Error deleting post from database, error connecting to database");
				}
				
				if(postEditPrivilegeId == -2){
					request.setAttribute("errorMessage", "Error deleting post from database, error with SQL interaction with database.");
				}
				if(postEditPrivilegeId == -3){
					request.setAttribute("errorMessage", "Error deleting post from database, invalid postId value.");
				}
				if(postEditPrivilegeId == -4){
					request.setAttribute("errorMessage", "Error deleting post from database, invalid userId value.");
				}
				
				throw error;
			}
			
			errorCode = PostEditPrivilege.deletePostEditPrivilegeFromDatabaseById(postEditPrivilegeId);
			
			if(errorCode < 0){
				
				if(errorCode == -1){
					request.setAttribute("errorMessage", "Error deleting postEditPrivilege, error connecting to database.");
				}
				
				if(errorCode == -2){
					request.setAttribute("errorMessage", "Error deleting postEditPrivilege, SQL error while interacting with database.");
				}
				
				throw error;
			}
			
			errorCode = Post.deletePostFromDatabaseById(b.getPostAt(postPos).getPostId());
			
			if(errorCode < 0){
				
				if(errorCode == -1){
					request.setAttribute("errorMessage", "Error deleting post, error connecting to database.");
				}
				
				if(errorCode == -2){
					request.setAttribute("errorMessage", "Error deleting post, SQL error while interacting with database.");
				}
				throw error;
			}
			
			errorCode = Post.addPostToPostDeleted(b.getPostAt(postPos).getPostId());
			
			if(errorCode < 0){
				if(errorCode == -1){
					request.setAttribute("errorMessage", "Error adding postid to postDeleted, error connecting to database.");
				}
				
				if(errorCode == -2){
					request.setAttribute("errorMessage", "Error adding postid to postDeleted, SQL error while interacting with database.");
				}
				
				if(errorCode == -3){
					request.setAttribute("errorMessage", "Error adding postid to postDelete, value of postId is invalid.");
				}
				
				throw error;
			}
		
			b.removePost(postPos);
		}catch(Exception e){
			
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
