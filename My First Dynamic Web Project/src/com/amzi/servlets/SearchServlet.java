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
import com.amzi.dao.SearchResult;
import com.amzi.dao.User;

/**
 * Servlet implementation class SearchServlet
 */
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public SearchServlet() {
		 super();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		
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
						String username = null;
						
						ps = connectionManager.getConnection().prepareStatement("select blogId from blog where isPublic = 1"
																			  + " AND title like '%"+searchTerm+"%'");
						
						rs = ps.executeQuery();
						
						searchResultsBlog = SearchResult.parseSearchResults(searchResultsBlog,"blogId",rs);
						
						if(searchResultsBlog == null){
							//error
						}
						
						rs.close();
						ps.close();
						
						username = ((User) request.getSession().getAttribute("currentUser")).getUsername();
						
						if(username == null){
							//error
						}
						
						//since privileges may not be given to edit a blog, only select from the set of blogs where the currentUser is the author of the blog.
						//further checks are unneeded as editing privileges may not be granted for a a blog. 
						ps =  connectionManager.getConnection().prepareStatement("select b.blogID from blog b, user u, user_blog ub " +
																				" where b.title like '%"+searchTerm+"%' " +
																				" AND b.blogId = ub.blogId" +
																				" AND u.userId = ub.userId" +
																				" AND u.username = ? AND" +
																				" b.postId NOT IN" + 
																				" (select blogId from blog where isPublic = 1)");
						ps.setString(1,username);
						
						rs = ps.executeQuery();
						
						searchResultsBlog = SearchResult.parseSearchResults(searchResultsBlog,"blogId",rs);
						
						if(searchResultsBlog == null){
							//error
						}
						
						rs.close();
						ps.close();
						
					}else{
						ps =  connectionManager.getConnection().prepareStatement("select blogID from blog where title like '%"+searchTerm+"%'");
					
						rs = ps.executeQuery();
						
						searchResultsBlog = SearchResult.parseSearchResults(searchResultsBlog,"blogId",rs);
						
						if(searchResultsBlog == null){
							//error;
						}
						
						ps.close();
						rs.close();
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
				String searchTermTitleBody = "";

				if(searchTitle){
					searchTermTitleBody = searchTermTitleBody.concat("title like '%"+searchTerm+"%'");
				}
				
				if(searchBody){					
					if(!searchTermTitleBody.contentEquals("")){
						searchTermTitleBody = searchTermTitleBody.concat(" OR ");
					}
					searchTermTitleBody = searchTermTitleBody.concat("content like '%"+searchTerm+"%'");
				}
				
				try{
					
					//determining the query to use when searching for the searchTerm.
					if(searchEditable == true){
						int userId = -1;
						
						ps = connectionManager.getConnection().prepareStatement("select postId from post where isPublic = 1"
																		      + " AND ("+searchTermTitleBody+")");
						
						rs = ps.executeQuery();
						
						searchResultsPost = SearchResult.parseSearchResults(searchResultsPost,"postId",rs);
						
						if(searchResultsPost == null){
							//error
						}
						
						ps.close();
						rs.close();
						
						userId = ((User) request.getSession().getAttribute("currentUser")).getUserId();
						
						if(userId == -1){
							//error
						}
						
						//find the posts where the user has been granted editing privileges and the title and or body of the post matches the search term.  
						ps = connectionManager.getConnection().prepareStatement("select p.postId from post p, user u, posteditprivilege pep, user_posteditprivilege upep, post_posteditprivilege ppep" +
																				" where u.userid = upep.userid AND" +
																				" upep.postEditPrivilegeId = pep.postEditPrivilegeId AND" + 
																				" pep.postEditPrivilegeId = ppep.postEditPrivilegeId AND" +
																				" p.postid = ppep.postid AND" +
																				" u.userId = ? AND" + 
																				" ("+searchTermTitleBody+") AND"
																			  + " p.postId NOT IN" + 
																				" (select postId from post where isPublic = 1)");
						ps.setInt(1,userId);
						rs = ps.executeQuery();
						
						searchResultsPost = SearchResult.parseSearchResults(searchResultsPost,"postId",rs);
						
						if(searchResultsPost == null){
							//error
						}
						
						ps.close();
						rs.close();
						
					}else{
						//the editable option has not been enabled. 
						String queryString = "select postId from post where " + searchTermTitleBody; 
						ps = connectionManager.getConnection().prepareStatement(queryString);
						
						rs = ps.executeQuery();
						
						searchResultsPost = SearchResult.parseSearchResults(searchResultsPost,"postId",rs);
						
						if(searchResultsPost == null){
							//error
						}
						
						ps.close();
						rs.close();
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
					
					searchResultsUser = SearchResult.parseSearchResults(searchResultsUser,"userId",rs);
					
					if(searchResultsUser == null){
						//error
					}
					
					ps.close();
					rs.close();
					
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
