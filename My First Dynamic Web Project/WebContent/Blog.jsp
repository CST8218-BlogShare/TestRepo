<%@ page language="java" 
		 contentType="text/html; charset=ISO-8859-1"  
    	 pageEncoding="ISO-8859-1"
    	 import="com.amzi.dao.Blog, com.amzi.dao.User"
    %>  
<html>
<!-- The page used to display all Blogs created within BLOGSHARE
	
	Only a single blog is shown at a time,
	The content of the blog is retrieved from the Blog object created 
	from the functionality linked with CreateBlog.jsp or Profile.jsp.
	
-->

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> 
<head>
<link rel="stylesheet" href="Styles/LookAndFeel.css">
<title>BlogShare - Blog</title>
</head>
	<body>	
	<% 
		
		/* 
			Retrieving the user and blog objects accociated with the 
			currently logged in user and the blog to be displayed.
			
			Need to make null checks for these sessionState variables and throw appropriate exception.
		*/
		
		User u = (User) session.getAttribute("currentUser");
		Blog b = (Blog) getServletContext().getAttribute("currentBlog");
			
	 	/*
	 	  The function buildBlog is called in order to retrieve the author of blog and all
	 	  posts within the blog other than the first post created during blogCreation. 
	 	 
	 	*/
	 	
	 	b.buildBlog(u.getUserId());

	 %>
		
		<!-- navigation bar -->
		<div class="FillScreenTextCentered" style="background-color:lightgrey; height:auto; margin-bottom:2%;">
			<br>
			<table style="width:90%; margin-right:auto; margin-left:auto;">
				<tr style="height:50%;">
					<td><h3><a href="Home.jsp">BLOGSHARE</a></h3></td>
					<td rowspan="2" style="width:25%; font-size:24px;"> <input type=text name=navBarSearchTerm maxlength=100/></td>
					<td style="width:10%"> <input type=checkbox name=navBarBlogsCheck checked="checked"/>Blogs<p>  </td>
					<td style="width:10%"> <input type=checkbox name=navBarTitlesCheck checked="checked"/>Titles<p></td>
					<td style="width:10%"> <input type=checkbox name=navBarUsersCheck checked="checked"/>Users<p> </td>
					<td rowspan="2" style="width:25%">  <input type=button name=navBarSearch maxlength=100 value="Search"/></td>
				</tr>
				<tr style="height:50%;">
					<td><a href="Profile.jsp">Welcome <%= u.getUsername() %>!</a></td>
					<td style="width:10%"> <input type=checkbox name=navBarPostCheck checked="checked"/>Posts<p> </td>
					<td style="width:10%"> <input type=checkbox name=navBarContentCheck checked="checked"/>Content<p></td>
					<td style="width:10%"> <input type=checkbox name=navBarAuthorsCheck checked="checked"/>Authors<p> </td>
				</tr>
			</table>
			<br>
		</div>

		<!--table to hold pages content -->
		<table class="centered80W" style="margin-bottom:2%;">
			 	
			 	<!-- blog title -->
			 	<tr>
					<td>
						<p title="Blog Title"> <%= b.getBlogTitle() %> </p>
					</td>
					<td>
						<!-- space for edit logo -->
						<a href="BlogEdit.jsp"><img title="Read Only Element - User Cannot Edit" src="images/read.jpg" alt="Read Only"> </a>
					</td>
				</tr>
				
				<!--  link to profile of author -->
				
				<tr>
					<td>
						 <h3 title="Author of Blog"> <a href="Profile.jsp"> Written by <%= b.getAuthor() %>  </a> </h3> 
					</td>
				</tr>
				
				<!-- creating space  --> 
				<tr>
					<td>
						<br>
					</td>
				</tr>
				
				
				<!-- first post belonging to author of the blog, 
					
					This post is kept separate from the loop below that is dedicated to
					creating additional posts, since a different title is used on the content. 
					
					Could be changed once permissions are implemented since it would 
					be possible to dynamically retrieve the author of each post. Might happen, might not.
					 -->
				<tr>
						<td>
							<p title="Post Title - Owned By Author Of Blog "> <%= b.getBlogPostTitle() %> </p>
						</td>
						<td>
							<a href="BlogEdit.jsp"><img title="Read Only Element - User Cannot Edit" src="images/read.jpg" alt="Read Only"></a> 
						</td>
				</tr>
				
				<tr>
						<td style="background:white; text-align:left;">								
							<p title="Content Of Post - Owned By Author Of Blog" style="margin:5%"><%= b.getBlogPostBody() %></p> 
						</td>
				</tr>
				
				<!-- creating space -->
				<tr>
					<td>
						<br>
					</td>
				</tr> 
				
	
	
		<!-- Displaying additional posts, either created by the author or by another user -->
		<%
		
		if(b.getNewPost() == true){
			int i;
			//i is started at 1 in order to skip first post, which is already on page.
			for(i = 1 ; i < b.getPostCount(); ++i){
			
			//adding any additional posts to the page using the contents retrieved from the post table matching the current blogId.
		 	
			 %>
			 
			 <tr>
				<td>
					<p title="Title Of Post - Owned by Author Of Post"> <%= b.getPostTitleAt(i) %> </p>
				</td>
				
				<td>
					<a href="PostCreate.jsp"><%b.setEditMode(true, i); %><img title="Edit Enabled For Element - User Can Edit" src="images/edit.jpg" alt="Edit Enabled, click here"> </a>
				</td>
			</tr>
					
			<tr>
				<td style="background:white; text-align:left;">	
					<p title="Content Of Post - Owned by Author Of Post" style="margin:5%"><%= b.getPostBodyAt(i) %></p>
				</td>
			</tr>	
			 
			 
			 <!-- creating space -->
				<tr>
					<td>
						<br>
					</td>
				</tr> 
			 
			 <%
			 }
			 b.setNewPost(false);
		  }
			

		%>
		
		</table>
		
		<!--  Button to create a new post, when pressed the user is linked to PostCreate.jsp -->
		<form action="PostCreate.jsp">
			<table class="centered80W">
				<tr>
					<td style="width:80%;">
						<input type="submit" class=button value="Create New Post">
					</td>
					
					<td style="width:20%;">
					</td>
					
				</tr>
			</table>
		</form>
		
	<body>
</html>
