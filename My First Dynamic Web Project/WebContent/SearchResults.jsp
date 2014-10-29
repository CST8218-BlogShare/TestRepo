<%@ page language="java" 
		import="com.amzi.dao.User, com.amzi.dao.SearchResult, com.amzi.dao.DbConnection, java.sql.PreparedStatement, java.sql.ResultSet, java.sql.SQLException" %>
<!DOCTYPE html>
<html>
<head>
<meta  charset="UTF-8">
<link rel="stylesheet" href="Styles/LookAndFeel.css">
<title>BlogShare - Search Results</title>
</head>
<body>

	
	<%
		SearchResult result = (SearchResult) session.getAttribute("currentSearchResult");
	%>

<!-- Navigation and Search Bar -->
	<!-- Navigation and Search Bar -->
		<header class="FillScreenTextCentered" style="background-color:lightgrey; height:auto; margin-bottom:2%;">
			<br>
			<form name="searchForm" action="searchServlet" method="post" >
				<table style="width:90%; margin-right:auto; margin-left:auto;">
					<tr style="height:50%;">
						<td><h3><a href="Home.jsp">BLOGSHARE</a></h3></td>
						<td rowspan="2" style="width:25%; font-size:24px;"> <input type=text name=navBarSearchTerm maxlength=100/></td>
						<td style="width:10%"> <input type=checkbox name=navBarBlogsCheck checked="checked"/>Blogs<p>  </td>
						<td style="width:10%"> <input type=checkbox name=navBarTitleCheck checked="checked"/>Titles<p></td>
						<td style="width:10%"> <input type=checkbox name=navBarEditableCheck />Editable<p> </td>
						<td rowspan="2" style="width:25%">  <input type=submit name=navBarSearch value="Search"/></td>
					</tr>
					<tr style="height:50%;">
						<% if(session.getAttribute("currentUser") == null){ %>
							<td> Welcome!</td>
						<% }else{ %>
							<td> <a href="Profile.jsp">Welcome <%= ((User)session.getAttribute("currentUser")).getUsername() %>!</a></td>
						<% } %>
						<td style="width:10%"> <input type=checkbox name=navBarPostsCheck checked="checked"/>Posts<p> </td>
						<td style="width:10%"> <input type=checkbox name=navBarBodyCheck checked="checked"/>Content<p></td>
						<td style="width:10%"> <input type=checkbox name=navBarUsersCheck />Users<p> </td>
					</tr>
				</table>
			</form>
			<br>
		</header>
	
	
	<form name="resultTypeFrom" action="switchResultTypeServlet" method="post">	
		<section class="FillScreenTextCentered" style="background-color:DodgerBlue;">
			<table  style="width:100%; border-spacing:1em; text-align:center; font-size:28px; ">
				<tr>
					<td class="searchSelectButton" <% if(result.getResultType().contentEquals("Blog")) { %> style="background-color:white;" <% } %>> <input type=submit name=resultType value="Blog" /></td>
					<td class="searchSelectButton" <% if(result.getResultType().contentEquals("Post")) { %> style="background-color:white;" <% } %>> <input type=submit name=resultType value="Post" /></td>
					<td class="searchSelectButton" <% if(result.getResultType().contentEquals("User")) { %> style="background-color:white;" <% } %>> <input type=submit name=resultType value="User"/></td>
				</tr>
			</table>
		</section>
	</form>

	<section class="FillScreenTextCentered" style="background-color:AliceBlue; height:auto; padding-top:0.5%; padding-bottom:0.5%;">
		<div class="FillScreenTextCentered" style= "width:95%;">
			<%
				if(result.getResultCount() == 0){
			%>
					<p>Unable to find any <%=result.getResultType() %> content relating to search term "<%= result.getSearchTerm() %>".</p> 
			<% 
				}else{
			%>		
					<%= result.getResultCount() %> <%= result.getResultType() %>(s) found relating to search term "<%= result.getSearchTerm() %>".
			<% 
				}
			%>
		</div>
		
		<% 
		
			for(int i = 0; i < result.getResultCount(); ++i){ 
			
				PreparedStatement ps = null;
				ResultSet rs = null;
				DbConnection connectionManager = DbConnection.getInstance();
				String t = result.getResultType();
		%>
		
		<article style= "width:95%; margin-left:auto; margin-right:auto; border-style:dashed; border-width:thin; border-color:lightgrey;">
			<table style="width:100%;">
			<% 
				try{
					switch(t){
						case "Blog": 
							ps = connectionManager.getConnection().prepareStatement("select title from blog where blogid = "+result.getResultIdAt(i)+" ");
							rs = ps.executeQuery();
							rs.first();
			%>
							<tr>
								<td>
								Blog Title: <br>
								<%= rs.getString("title") %>
								</td>
							</tr>
			<% 				break;
						case "Post":
							ps = connectionManager.getConnection().prepareStatement("select b.title as blogTitle, p.title as postTitle, p.content as postContent" +
						   														    " from blog b, post p" +
																				    " where b.blogid = p.blogid AND" +
																				    " p.postId =  "+result.getResultIdAt(i)+" ");
							rs = ps.executeQuery();
							rs.first();
			 %>
		
							<tr>
								<td>
								Blog Title: <br>
								<%= rs.getString("blogTitle") %>
								</td>
							</tr>
								
							<!-- making space -->
							<tr>
								<td>
									<br>
								</td>
							</tr>
								
							<tr>
								<td>
								Title: <br>
								<%= rs.getString("postTitle") %>
								<td>
							</tr>
							<tr>
								<td>
								Content: <br>
								<%= rs.getString("postContent") %>
								</td>
							</tr>
			<% 				break;
						case "User": 
							ps = connectionManager.getConnection().prepareStatement("select username from user where userid = "+result.getResultIdAt(i)+" ");
							rs = ps.executeQuery();
							rs.first();
			%>
							<tr>
								<td>
								Username: <br>
								<%= rs.getString("username") %>
								</td>
							</tr>	
					
							
			<% 		
					} 
			%>
			</table>
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