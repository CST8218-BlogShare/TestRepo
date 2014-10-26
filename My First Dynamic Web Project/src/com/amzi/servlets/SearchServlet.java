package com.amzi.servlets;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amzi.dao.DbConnection;
import com.amzi.dao.User;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
  
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }*/

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		
		DbConnection connectionManager = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String searchTerm = "";
		boolean searchBlogs = false;
		boolean searchPosts = false;
		boolean searchTitle = false;
		boolean searchBody = false;
		boolean searchEditable = false;
		boolean searchUsers = false;
		
		ArrayList<Integer> searchResultBlogs = null;
		ArrayList<Integer> searchResultPosts = null;
		ArrayList<Integer> searchResultUsers = null;
		
		searchTerm = request.getParameter("navBarSearchTerm");
		searchTerm = searchTerm.trim();
		
		if(searchTerm != ""){
			connectionManager = DbConnection.getInstance();
			
			if(request.getParameter("navBarBlogsCheck") != null){
				searchBlogs = true;
			}
			
			if(request.getParameter("navBarPostsCheck") != null){
				searchPosts = true;
			}
			
			if(request.getParameter("navBarTitleCheck") != null){
				searchTitle = true;
			}
			
			if(request.getParameter("navBarBodyCheck") != null){
				searchBody = true;
			}
			
			if(request.getParameter("navBarEditableCheck") != null){
				searchEditable = true;
			}
			
			if(request.getParameter("navBarUsersCheck") != null){
				searchUsers = true;
			}
			
			if(searchBlogs == true){
				searchResultBlogs = new ArrayList<Integer>();
				try {
					
					if(searchEditable == true){
						User u = (User) request.getSession().getAttribute("currentUser");
						
						//if the user is logged in
						if(u == null){
							//only search for blogs that are publicly editable. 
							ps =  connectionManager.getConnection().prepareStatement("select blogID from blog where title like '%"+searchTerm+"%' AND isPublic = 1");
						}else{
							//since privileges may not be given to edit a blog, only select from the set of blogs where the currentUser is the author of the blog
							//further checks are unneeded as editing privileges may not be granted for a a blog. 
							ps =  connectionManager.getConnection().prepareStatement("select blogID from blog where title like '%"+searchTerm+"%' AND username = '"+u.getUsername()+"'");
						}
					}else{
						ps =  connectionManager.getConnection().prepareStatement("select blogID from blog where title like '%"+searchTerm+"%'");
					}
					
					rs = ps.executeQuery();
					
					while(rs.next()){
						searchResultBlogs.add(rs.getInt("blogID"));
					}
					
					
				} catch (SQLException sqlE) {
					// TODO Auto-generated catch block
					sqlE.printStackTrace();
				}finally{
					if (ps != null) {  
						try {  
							ps.close();  
			            } catch (SQLException sqlE) {  
			            	sqlE.printStackTrace();  
			            }  
			        }  
			           
					if (rs != null) {  
						try {  
							rs.close();  
			            } catch (SQLException sqlE) {  
			            	sqlE.printStackTrace();  
			            }  
			       }  
				}
			}
			
			if(searchPosts == true){
				//String searchTermColumns = "";
				String searchTermTitleBody = "";
				searchResultPosts = new ArrayList<Integer>();
				
				if(searchTitle){
					//searchTermColumns = searchTermColumns.concat("title");
					searchTermTitleBody = searchTermTitleBody.concat("title like '%"+searchTerm+"%'");
				}
				
				if(searchBody){
					/*if(!searchTermColumns.contentEquals("")){
						searchTermColumns = searchTermColumns.concat(", ");
					}
					searchTermColumns = searchTermColumns.concat("content");*/
					
					if(!searchTermTitleBody.contentEquals("")){
						searchTermTitleBody = searchTermTitleBody.concat(" OR ");
					}
					searchTermTitleBody = searchTermTitleBody.concat("content like '%"+searchTerm+"%'");
				}
				
				try{
					
					//determining the query to use when searching for thr searchTerm.
					
					if(searchEditable == true){
						User u = (User) request.getSession().getAttribute("currentUser");
						
						//the query being used will depend on if the user is a public or registered user. 
						if(u == null){
							String queryString = "select postId from post where " + searchTermTitleBody + " AND isPublic = 1"; 
							ps = connectionManager.getConnection().prepareStatement(queryString);
						}else{
							//find the posts where the user has been granted editing privileges and the title and or body of the post matches the search term.  
							ps = connectionManager.getConnection().prepareStatement("select p.postId from post p, user u, user_post up, posteditprivilege pep, user_posteditprivilege upep, post_posteditprivilege ppep" +
																					" where u.userid = upep.userid AND" +
																					" upep.postEditPrivilegeId = pep.postEditPrivilegeId AND" + 
																					" pep.postEditPrivilegeId = ppep.postEditPrivilegeId AND" +
																					" p.postid = ppep.postid AND" +
																					" u.userid = up.userid AND" +
																					" p.postid = up.postid AND" +
																					" "+searchTermTitleBody+" AND" + 
																					" u.userId = "+u.getUserId()+" ");
						}	
					}else{
						//the editable option has not been enabled. 
						String queryString = "select postId from post where " + searchTermTitleBody; 
						ps = connectionManager.getConnection().prepareStatement(queryString);
					}
					
					rs = ps.executeQuery();
					
					while(rs.next()){
						searchResultPosts.add(rs.getInt("postId"));
					}
					
				}catch(SQLException sqlE){
					sqlE.printStackTrace();
				}finally{
					if (ps != null) {  
						try {  
							ps.close();  
			            } catch (SQLException sqlE) {  
			            	sqlE.printStackTrace();  
			            }  
			        }  
			           
					if (rs != null) {  
						try {  
							rs.close();  
			            } catch (SQLException sqlE) {  
			            	sqlE.printStackTrace();  
			            }  
			       }  
				}
			}
			
			if(searchUsers == true){
				searchResultUsers = new ArrayList<Integer>();
				
				try{
					
					ps = connectionManager.getConnection().prepareStatement("select userId from user where username like '%"+searchTerm+"%'"); 
					rs = ps.executeQuery();
					
					while(rs.next()){
						searchResultUsers.add(rs.getInt("userId"));
					}
					
				}catch(SQLException sqlE){
					sqlE.printStackTrace();
				}finally{
					if (ps != null) {  
						try {  
							ps.close();  
			            } catch (SQLException sqlE) {  
			            	sqlE.printStackTrace();  
			            }  
			        }  
			           
					if (rs != null) {  
						try {  
							rs.close();  
			            } catch (SQLException sqlE) {  
			            	sqlE.printStackTrace();  
			            }  
			       }  
				}
				
			}
		
			RequestDispatcher rd=request.getRequestDispatcher("SearchResults.jsp");
			 
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
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}*/
	

}
