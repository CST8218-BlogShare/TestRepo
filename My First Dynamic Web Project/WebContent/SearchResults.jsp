<%@ page language="java" 
		import="com.amzi.dao.User, com.amzi.dao.SearchResult, java.util.ResourceBundle, com.amzi.dao.DbConnection, java.sql.PreparedStatement, java.sql.ResultSet, java.sql.SQLException" %>
<!DOCTYPE html>
<html>
<head>
<meta  charset="UTF-8">
<jsp:include page="BootstrapInclude.html" />

<title>BlogShare - Search Results</title>
</head>
<body>

<%
	SearchResult result = (SearchResult) session.getAttribute("currentSearchResult");


	session.setAttribute("currentPage","SearchResults");
	ResourceBundle lang = ResourceBundle.getBundle("SearchResults_EN");

	//if the session language is FR switch to french, otherwise remains english as set above
	if (session.getAttribute("language").toString().equals("FR")){
		lang = ResourceBundle.getBundle("SearchResults_FR");
	} 

	//if the user clicked change language, set to appropriate language
	if (request.getParameter("language") != null){	
		if (request.getParameter("language").equals("FR")){
			lang = ResourceBundle.getBundle("SearchResults_FR");
			session.setAttribute("language","FR");
		} else {
			lang = ResourceBundle.getBundle("SearchResults_EN");
			session.setAttribute("language","EN");
		}
	}		

%>

	<jsp:include page="SearchBar.jsp"></jsp:include>
	
	<%  
		if( request.getAttribute("errorMessage") != null)
		{ %>
		<div class="container">
			<div class="alert alert-danger alert-dismissible" role="alert">
				<button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<%= lang.getString(request.getAttribute("errorMessage").toString()) %>
			</div>
		</div>
	<%	}	%>
	
	<form name="resultTypeFrom" action="switchResultTypeServlet" method="post">	
		<section class="FillScreenTextCentered" style="background-color:DodgerBlue;">
			<table  style="width:100%; border-spacing:1em; text-align:center; font-size:28px; ">
				<tr>
					<td class="searchSelectButton" <% if(result.getResultType().equals("Blog")) { %> style="background-color:white;" <% } %>> <input type=submit name=resultType value="<%= lang.getString("blog") %>" /></td>
					<td class="searchSelectButton" <% if(result.getResultType().equals("Post")) { %> style="background-color:white;" <% } %>> <input type=submit name=resultType value="<%= lang.getString("post") %>" /></td>
					<td class="searchSelectButton" <% if(result.getResultType().equals("User")) { %> style="background-color:white;" <% } %>> <input type=submit name=resultType value="<%= lang.getString("user") %>"/></td>
				</tr>
			</table>
		</section>
	</form>

	<section class="FillScreenTextCentered" style="background-color:LightGrey; height:auto; padding-top:0.5%; padding-bottom:0.5%;">
		<br>
		<div class="FillScreenTextCentered" style= "width:95%; font-size:18px;">
		<b>
			<%
				if(result.getResultCount() == 0){
			%>
					<%=lang.getString("unabletofind") %> <%=result.getResultType() %> <%=lang.getString("unabletofind2") %> "<%= result.getSearchTerm() %>".
			<% 
				}else{
			%>		
					<%= result.getResultCount() %> <%= result.getResultType() %> <%=lang.getString("foundrelating") %> "<%= result.getSearchTerm() %>".
			<% 
				}
			%>
		</b>
		<br>
		<br>
		</div>
		
		<% 
		
			for(int i = 0; i < result.getResultCount(); ++i){ 
			
				PreparedStatement ps = null;
				ResultSet rs = null;
				DbConnection connectionManager = DbConnection.getInstance();
				int blogId = -1;
				String blogTitle="";
				String blogAuthor="";
				String postTitle="";
				String postBody="";
				
		%>
		
		<!--  out.println( " class=\"blog-link list-group-item\" blogTitle=\"" + blogTitle + "\">"+ blogTitle +"</li>"); -->		
															
		<article class="whiteDashedBorder" style="width:95%; margin-left:auto; margin-right:auto; background:DodgerBlue">
			<form name="loadSearchResultForm" action="loadSearchResultServlet" method="post">
				<table style="width:100%; margin-right:auto; margin-left:auto; text-align:center; color:white" >
				<% 
					try{
						switch(result.getResultType()){
							case "Blog": 
								ps = connectionManager.getConnection().prepareStatement("select title from blog where blogid = "+result.getResultIdAt(i)+" ");
								rs = ps.executeQuery();
								rs.first();
								blogTitle = rs.getString("title");
								
								ps.close();
								rs.close();
								
								ps = connectionManager.getConnection().prepareStatement( "select username from user u, blog b, user_blog ub" +
																						 " where" + 
								      												     " b.blogid = "+result.getResultIdAt(i)+" AND" +
																				   		 " b.blogid = ub.blogid AND" +
							            											     " u.userId = ub.userId");
								rs = ps.executeQuery();
								rs.first();
								blogAuthor = rs.getString("username");
	
								ps.close();
								rs.close();
				%>
				
								<tr>
									<td style="font-size:18px; width:50%;">
									<div style="float:right; padding-right:2%">
										<div style="text-decoration:underline"><%=lang.getString("blogtitle") %></div> 
										<%= blogTitle %>
									</div>
									</td>
									
									<td style="font-size:18px; width:50%;">
									<div style="float:left; padding-left:2%">
										<div style="text-decoration:underline"><%=lang.getString("author") %></div> 
										<%= blogAuthor %> 
									</div>
								</tr>
								
								<!-- making space -->
								<tr>
									<td>
										<br>
									</td>
								</tr>
								
								<tr>
									<td colspan="2">
										<input type="hidden" name="blogId" value="<%= result.getResultIdAt(i) %>">
										<input  style="color:black;" type="submit" name="loadSearchResult" value="<%=lang.getString("loadblog") %>"/>
									</td>
								</tr>
				<% 				break;
							case "Post":
								
								ps = connectionManager.getConnection().prepareStatement("select b.blogid as blogId, b.title as blogTitle, p.title as postTitle, p.content as postBody" +
							   														    " from blog b, post p" +
																					    " where b.blogid = p.blogid AND" +
																					    " p.postId =  "+result.getResultIdAt(i)+" ");
								rs = ps.executeQuery();
								rs.first();
								blogTitle = rs.getString("blogTitle");
								blogId = rs.getInt("blogId");
								postTitle = rs.getString("postTitle");
								postBody = rs.getString("postBody");
								
								ps.close();
								rs.close();
								
								ps = connectionManager.getConnection().prepareStatement( "select username from user u, blog b, user_blog ub" +
										 												 " where" + 
																				         " b.blogid = "+blogId+" AND" +
																						 " b.blogid = ub.blogid AND" +
																			             " u.userId = ub.userId");
								rs = ps.executeQuery();
								rs.first();
								blogAuthor = rs.getString("username");
								
								ps.close();
								rs.close();
				 %>
			
								<tr>
									<td style="font-size:18px; width:50%;">
										<div style="float:right; padding-right:2%">
											<div style="text-decoration:underline"><%=lang.getString("blogtitle") %></div> 
											<%= blogTitle %>
										</div>
									</td>
									
									<td style="font-size:18px; width:50%;">
									<div style="float:left; padding-left:2%">
										<div style="text-decoration:underline"><%=lang.getString("author") %></div> 
										<%= blogAuthor %> 
									</div>
								</tr>
									
								<!-- making space -->
								<tr>
									<td>
										<br>
									</td>
								</tr>
									
								<tr>
									<td colspan="2" style="font-size:18px;">
										<div style="text-decoration:underline"> <%=lang.getString("title") %> </div> 
										<%= postTitle %>
									</td>
								</tr>
								
								<!-- making space -->
								<tr>
									<td>
										<br>
									</td>
								</tr>
								
								<tr>
									<td colspan="2" style="font-size:18px;">
										<div style="text-decoration:underline"> <%=lang.getString("content") %> </div> 
										<%= postBody %>
									</td>
								</tr>
								
								<!-- making space -->
								<tr>
									<td>
										<br>
									</td>
								</tr>
								
								<tr>
									<td colspan="2">
										<input type="hidden" name="postId" value="<%= result.getResultIdAt(i) %>">
										<input style="color:black;" type="submit" name="loadSearchResult" value="<%=lang.getString("loadpost") %>"/>
									</td>
								</tr>
								
				<% 				break;
							case "User": 
								ps = connectionManager.getConnection().prepareStatement("select username from user where userid = "+result.getResultIdAt(i)+" ");
								rs = ps.executeQuery();
								rs.first();
				%>
								<tr>
									<td style="font-size:18px;">
									<%=lang.getString("username") %>: <br>
									<%= rs.getString("username") %>
									</td>
								</tr>	
								
								<!-- making space -->
								<tr>
									<td>
										<br>
									</td>
								</tr>
								
								<tr>
									<td colspan="2">
										<input type="hidden" name="userId" value="<%= result.getResultIdAt(i) %>">
										<input style="color:black;" type="submit" name="loadSearchResult" value="<%=lang.getString("loadprofile") %>"/>
									</td>
								</tr>		
				<% 		
						} 
				%>
				</table>
			</form>
		</article>

		<%	
			}catch(SQLException sqlE){
				System.out.println("An exception was thrown while attempting to retrieve the contents of the searchResult.");
				sqlE.printStackTrace();
			}
		} //end of for loop
		
		%>
	</section>
</body>
</html>