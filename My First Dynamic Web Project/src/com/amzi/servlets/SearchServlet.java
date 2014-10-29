package com.amzi.servlets;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amzi.dao.DbConnection;
import com.amzi.dao.User;
import com.amzi.dao.SearchResult;

/**
 * Servlet implementation class SearchServlet
 */
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		
		User u = null;
		SearchResult searchResult = null;
	
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
		
		ArrayList<Integer> searchResultsBlog = null;
		ArrayList<Integer> searchResultsPost = null;
		ArrayList<Integer> searchResultsUser = null;
		
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
				try {
					
					if(searchEditable == true){
						u = (User) request.getSession().getAttribute("currentUser");
						
						//if the user is not logged in
						if(u == null){
							//only search for blogs that are publicly editable. 
							ps =  connectionManager.getConnection().prepareStatement("select blogID from blog where title like '%"+searchTerm+"%' AND isPublic = 1");
						//if the user is logged in
						}else{
							//since privileges may not be given to edit a blog, only select from the set of blogs where the currentUser is the author of the blog.
							//further checks are unneeded as editing privileges may not be granted for a a blog. 
							ps =  connectionManager.getConnection().prepareStatement("select b.blogID from blog b, user u, user_blog ub " +
																					 " where b.title like '%"+searchTerm+"%' " +
																					 " AND b.blogId = ub.blogId" +
																					 " AND u.userId = ub.userId" +
																					 " AND u.username = '"+u.getUsername()+"' ");
						}
					}else{
						ps =  connectionManager.getConnection().prepareStatement("select blogID from blog where title like '%"+searchTerm+"%'");
					}
					
					rs = ps.executeQuery();
					
					//determining if the list that will holds the corresponding searchResults, needs to initialized by checking if the resultSet contains any rows of data.
					rs.last();
					if(rs.getRow() > 0 ){
						searchResultsBlog = new ArrayList<Integer>();
					}
					rs.beforeFirst();
					
					while(rs.next()){
						searchResultsBlog.add(rs.getInt("blogID"));
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
						 u = (User) request.getSession().getAttribute("currentUser");
						
						//the query being used will depend on if the user is a public or registered user. 
						if(u == null){
							String queryString = "select postId from post where isPublic = 1 AND " + searchTermTitleBody; 
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
					
					//determining if the list that will holds the corresponding searchResults, needs to initialized by checking if the resultSet contains any rows of data.
					rs.last();
					if(rs.getRow() > 0 ){
						searchResultsPost = new ArrayList<Integer>();
					}
					rs.beforeFirst();
					
					while(rs.next()){
						searchResultsPost.add(rs.getInt("postId"));
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
				try{
					
					ps = connectionManager.getConnection().prepareStatement("select userId from user where username like '%"+searchTerm+"%'"); 
					rs = ps.executeQuery();
					
					//determining if the list that will holds the corresponding searchResults, needs to initialized by checking if the resultSet contains any rows of data.
					rs.last();
					if(rs.getRow() > 0 ){
						searchResultsUser = new ArrayList<Integer>();
					}
					rs.beforeFirst();
					
					while(rs.next()){
						searchResultsUser.add(rs.getInt("userId"));
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
		}
		
		//need to figure out way to load same page that called request.
		searchResult = new SearchResult(searchTerm,searchResultsBlog,searchResultsPost,searchResultsUser);
	    request.getSession().setAttribute("currentSearchResult", searchResult);
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
