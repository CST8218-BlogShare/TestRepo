<%@ page language="java" 
	contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="com.amzi.dao.Blog"
	%>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<head>
<link rel="stylesheet" href="Styles/LookAndFeel.css">
<title>BlogShare - PostCreate</title>
</head>
<!--table to hold pages content -->

<%
	Blog b = (Blog) getServletContext().getAttribute("currentBlog");


%>

<form name="postForm" action="postCreateServlet" method="post">
	<table  class="centered80W">

		<!-- blog title -->
		<tr style="margin-bottom: 5%;">
			<td><label> <%= b.getBlogTitle() %></label></td>
		</tr>

		<!-- creating space -->
		<tr>
			<td><br></td>
		</tr>

		<!-- first post -->
		<tr>
			<td><label> <%= b.getBlogPostTitle() %> </label></td>
		</tr>

		<!-- creating space -->
		<tr>
			<td><br></td>
		</tr>


		<tr>
			<td><textarea NAME="postBodyUn" READONLY="readonly" WRAP=soft
					COLS=80 ROWS=10><%= b.getBlogPostBody() %></textarea> <br>
				<br></td>
		</tr>

		<!-- creating space -->
		<tr>
			<td><br></td>
		</tr>
		
		<!-- post to be added -->
		<tr>
			<td>The Title and Post shown above is your previous post to help
				you continue writing your blog below

				<p>New Post Title Below</p>
				<input type=text name=postTitle maxlength=100 />
				
				<p>New Post Content Below</p> 
				<textarea NAME="postBody" WRAP=soft
					COLS=80 ROWS=10>
				</textarea>
			</td>
		</tr>
		
		<!-- creating space -->
		<tr>
			<td><br></td>
		</tr>
		
	<tr>
		<td>
			<input type="submit" class=button value="Save">
		</td>
	</tr>
	</table>
</form>

<form action="Profile.jsp">
	<input class="centered80W" type="submit" width="wrap_content" value="Cancel" style="font-size:18px;">
</form>
<body>
</html>
