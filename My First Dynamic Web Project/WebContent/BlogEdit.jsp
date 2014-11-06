<%@ page language="java" contentType="text/html;" import="com.amzi.dao.Blog, com.amzi.dao.Post"%>
<!DOCTYPE html>
<html>
<meta charset="UTF-8">
<head>
<link rel="stylesheet" href="Styles/LookAndFeel.css">
<title>BlogShare - BlogEdit</title>
</head>

<%

 Blog b = (Blog) session.getAttribute("currentBlogToEdit");

%>

<div class="FullScreenCentered">
	<h2><%= b.getBlogTitle() %></h2>
</div>

<form name="changeBlogTitleForm" action="changeBlogTitleServlet" method="post">
	<table>
		<tr>
			<td>
				Title:
			</td>
			
			<td>
				<input type="text" value= <%=b.getBlogTitle()%> />	
			</td>
			
			<td>
				<input type="submit" value="Edit Title"/>
			</td>
		</tr>
	</table>
</form>
	
<form name="deletePostForm" action="deletePostServlet" method="post">
	<table>
		<tr>
			<td>
				<h3>Posts</h3>
			</td>
		</tr>
		<% for(int i = 0; i < b.getPostCount(); ++i) { %>
			
			<tr>
				<td>
					<%= b.getPostAt(i).getPostTitle() %>
				</td>
				<td>
					<input type="submit" value="Delete Post"/>
				</td>
				<td>
					Edit History
				</td>
			</tr>
			
		<% } %>
	</table>
</form>

		<!--table to hold pages content 
		<table style="width:80%;  margin-left:10%; marin-right:10%;">
			 	
			 	<!-- blog title -->
			 	<tr style="margin-bottom:5%;">
					<td>
						<p> Blog Title </p>
						<p><input type=text name=blogTitle maxlength=100/></p>
					</td>
				</tr>
				
				<!-- creating space -->
				<tr>
					<td>
						<br>
					</td>
				</tr>
				
				
				
				<!-- first post 
				<tr>
						<td>
							<p>Post Title </p>
							<input type=text name=blogTitle  maxlength=100/>	
						</td>
				</tr>
				
				<!-- creating space 
				<tr>
					<td>
						<br>
					</td>
				</tr>
				
				
				<tr>
						<td>
							<p>Post Body</p>
							<textarea NAME="postBody" WRAP=soft COLS=80 ROWS=10></textarea>
							<br>
							<br>
						</td>
				</tr>
				
				<!-- creating space 
				<tr>
					<td>
						<br>
					</td>
				</tr>
				
		
				<!-- additional posts 
			 
<tr>
						<td>
							<form action="Blog.jsp">
	   							<input type="submit" value="Save">
							</form>
						<td>
						</td>
					</tr>
					
					<!-- creating space 
				<tr>
					<td>
						<form action="Profile.jsp">
   							<input type="submit" width="wrap_content" value="Cancel">
						</form>
					</td>
				</tr>
				-->
			
		</table>
	<body>
</html>
