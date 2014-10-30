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

	<jsp:include page="SearchBar.jsp"></jsp:include>

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
		<div class="FillScreenTextCentered" style= "width:95%; font-size:18px;">
			<%
				if(result.getResultCount() == 0){
			%>
					Unable to find any <%=result.getResultType() %> content relating to search term "<%= result.getSearchTerm() %>". 
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
								<td style="font-size:18px;">
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
								<td style="font-size:18px;">
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
								<td style="font-size:18px;">
								Title: <br>
								<%= rs.getString("postTitle") %>
								<td>
							</tr>
							<tr>
								<td style="font-size:18px;">
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
								<td style="font-size:18px;">
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