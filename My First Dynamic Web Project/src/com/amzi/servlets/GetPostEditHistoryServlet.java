package com.amzi.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.amzi.dao.Blog;
import com.amzi.dao.PostEdit;
import com.amzi.dao.User;

/**
 * Servlet implementation class GetPostEditHistoryServlet
 */
public class GetPostEditHistoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String errorMessege = null;
	public static String errorMessegeFR = null;
	 
    public GetPostEditHistoryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		Blog b = null;
		String url = "";
		int postPos = -1;
		HttpSession userSession = null;
		
		b = (Blog) request.getSession().getAttribute("currentBlog");
		postPos = Integer.parseInt(request.getParameter("postPos"));
		
		// If a session has not been created, none will be created
		userSession = request.getSession(false);
	
		/*to track postEdit history, we want to initialze a list with a post, 
		 * but because we do not want to bog down the system, with keeping all postEdits in memory,
		 * this list will only be initialized when needed and is set to null after the page is navigated away from???
		 */
		
		if(postPos != -1 && b != null){
			
			ArrayList<PostEdit> postEdits = PostEdit.getResultsFromDatabase(b.getPostAt(postPos).getPostId());
			request.getSession().setAttribute("currentPostEditList", postEdits);
			request.getSession().setAttribute("currentPostPos", postPos);
			request.getSession().setAttribute("currentPost", b.getPostAt(postPos));
			request.getSession().setAttribute("currentPostEditPos", 0);
			
			
			if(postEdits.size() == 0){
				if(userSession.getAttribute("language").equals("EN"))
					request.setAttribute("errorMessage", "Error: No modifications found");
				else if(userSession.getAttribute("language").equals("FR")){
					request.setAttribute("errorMessage", "Erreur: Aucune modifications trouvé");	
				}
				url = "BlogEdit.jsp";
				
			}else{
				url = "PostEditHistory.jsp";
			}
			
			try {
				request.getRequestDispatcher(url).forward(request, response); 
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}
	}

}
