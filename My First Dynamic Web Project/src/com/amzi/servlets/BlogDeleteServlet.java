package com.amzi.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amzi.dao.Blog;
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
		ArrayList<Post> postsInBlog = null;
		String blogTitle = "";
		int errorCode = 0;
		int blogId = 0;
		int postEditPrivilegeId = 0;
		
		
		blogTitle = request.getParameter("blogTitle");
		blogTitle = blogTitle.trim();
		
		try{
			if(blogTitle.equals("")){
				request.setAttribute("errorMessage", "Error: Blog title is empty.");
				throw error;
			}
			
			blogId = Blog.getBlogIdFromDatabaseByTitle(blogTitle);
			
			if(blogId <= 0){
				request.setAttribute("errorMessage", "Error: Unable to retrieve blogId using blogTitle.");
				throw error;
			}
			
			postsInBlog = Post.getPostListFromDatabaseByBlogId(blogId);
			
			if(postsInBlog == null){
				request.setAttribute("errorMessage", "Error: Unable to retrieve post list using blogId.");
				throw error;
			}
			
			for(Post post : postsInBlog){
				
				postEditPrivilegeId = PostEditPrivilege.getPostEditPrivilegeIdFromDatabaseByPostId(post.getPostId());
				
				if(postEditPrivilegeId <= 0){
					throw error;
				}
				
				errorCode = PostEditPrivilege.deletePostEditPrivilegeFromDatabaseById(postEditPrivilegeId);
				
				if(errorCode < 0){
					throw error;
				}
				
			}
			
			//if the method is unsuccessful the value false will be returned.
			if(Blog.deleteBlogFromDatabase(blogTitle) == false){
				request.setAttribute("errorMessage", "Error: Unable to delete blog from database.");
				throw error;
			}
		}catch(Exception e){
			System.out.println("Error deleting post");
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
