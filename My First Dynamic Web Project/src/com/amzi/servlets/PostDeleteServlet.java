package com.amzi.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amzi.dao.Blog;

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
		int postPos = -1;
		
		b = (Blog) request.getSession().getAttribute("currentBlog");
		
		//if current blog cannot be retrieved, the session is no longer valid.
		if(b == null){
			System.exit(1);
		}
		
		try{
			postPos = Integer.parseInt(request.getParameter("postPos"));
			if(b.getPostAt(postPos).removePostFromDatabase(b, postPos) == false){
				//create error message about database error
			}
		}catch(NumberFormatException nfe){//if postPostion cannot be initialized 
			nfe.printStackTrace();
			//create error message about being unable to read value correctly.
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
