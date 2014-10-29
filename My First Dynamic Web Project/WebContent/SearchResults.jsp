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

	<script>
		function BlogClicked(elementClicked){
			var postCheckBox = document.getElementById("navBarPostsCheck");
			var titleCheckBox = document.getElementById("navBarTitleCheck");
			var bodyCheckBox = document.getElementById("navBarBodyCheck");
			var editCheckBox = document.getElementById("navBarEditableCheck");
			
			//if post is not activated, else do nothing.
			if(postCheckBox.checked == false){
				
				//if blog is being activated
				if(elementClicked.checked == true){
					//enable and check title checkbox
					titleCheckBox.checked=true;
					titleCheckBox.disabled=false;
					
					//uncheck and disable body checkbox
					bodyCheckBox.checked=false;
					bodyCheckBox.disabled=true;
					
					//enabling editable checkbox
					editCheckBox.disabled = false;
				
				//if blog is being deactivated
				}else{
					//uncheck and disable title Checkbox
					titleCheckBox.checked = false;
					titleCheckBox.disabled = true;
					
					//uncheck and disable editable checkbox
					editCheckBox.checked = false;
					editCheckBox.disabled = true;
				}
			}
		}
	
		//title and content only available if post is clicked.
		function PostClicked(elementClicked){	
			var blogCheckBox = document.getElementById("navBarBlogsCheck");
			var titleCheckBox = document.getElementById("navBarTitleCheck");
			var bodyCheckBox = document.getElementById("navBarBodyCheck");
			var editCheckBox = document.getElementById("navBarEditableCheck");
			
			//if activating post
			if(elementClicked.checked == true){

				//check and enable title checkbox
				titleCheckBox.checked = true;
				titleCheckBox.disabled = false;
				
				//check and enable body checkbox
				bodyCheckBox.checked = true;
				bodyCheckBox.disabled = false;
				
				//enable editable checkbox
				editCheckBox.disabled = false;

			//if deactivating post
			}else{
				//uncheck body checkbox but leave enabled
				bodyCheckBox.checked = false;
				bodyCheckBox.disabled = true;
				
				//if blog isn't currently enabled
				if(blogCheckBox.checked == false){
					
					//uncheck and disable title checkbox
					titleCheckBox.checked = false;
					titleCheckBox.disabled = true;
					
					//uncheck and disable editable checkbox
					editCheckBox.checked = false;
					editCheckBox.disabled = true;
						
				}
			}
		}
	</script>

		<!-- Navigation and Search Bar -->
		<header class="FillScreenTextCentered" style="background-color:lightgrey; height:auto; margin-bottom:2%;">
			<br>
			<form name="searchForm" action="searchServlet" method="post" >
				<table style="width:90%; margin-right:auto; margin-left:auto;">
					<tr style="height:50%;">
						<td><h3>BLOGSHARE</h3></td>
						<td rowspan="2" style="width:25%; font-size:24px;"> <input type=text name=navBarSearchTerm maxlength=100/></td>
						<td style="width:10%"> <input type=checkbox id="navBarBlogsCheck" name="navBarBlogsCheck" checked="checked" OnClick="BlogClicked(this)"/>Blogs<p>  </td>
						<td style="width:10%"> <input type=checkbox id="navBarTitleCheck" name="navBarTitleCheck" checked="checked"/>Titles<p></td>
						<td style="width:10%"> <input type=checkbox id="navBarEditableCheck" name="navBarEditableCheck" />Editable<p> </td>
						<td rowspan="2" style="width:25%">  <input type=submit name=navBarSearch value="Search"/></td>
					</tr> 
					<tr style="height:50%;">
						<% if(session.getAttribute("currentUser") == null){ %>
							<td> Welcome!</td>
						<% }else{ %>
							<td> <a href="Profile.jsp">Welcome <%= ((User)session.getAttribute("currentUser")).getUsername() %>!</a></td>
						<% } %>
						<td style="width:10%"> <input type=checkbox id="navBarPostsCheck" name="navBarPostsCheck" OnClick="PostClicked(this)" checked="checked"/>Posts<p> </td>
						<td style="width:10%"> <input type=checkbox id="navBarBodyCheck" name="navBarBodyCheck" checked="checked"/>Body<p></td>
						<td style="width:10%"> <input type=checkbox id="navBarUsersCheck" name="navBarUsersCheck"/>Users<p> </td>
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