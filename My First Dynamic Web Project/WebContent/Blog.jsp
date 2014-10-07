<%@ page language="java" 
		 contentType="text/html; charset=ISO-8859-1"  
    	 pageEncoding="ISO-8859-1"
    	 import="com.amzi.dao.Blog"
    %>  
<html>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> 
<head>
<link rel="stylesheet" href="Styles/LookAndFeel.css">
<title>BlogShare - Blog</title>
</head>
	<body>
	
	<% Blog b = (Blog) getServletContext().getAttribute("currentBlog"); %>
		
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
					<td>Welcome <%= session.getAttribute("username") %>!</td>
					<td style="width:10%"> <input type=checkbox name=navBarPostCheck checked="checked"/>Posts<p> </td>
					<td style="width:10%"> <input type=checkbox name=navBarContentCheck checked="checked"/>Content<p></td>
					<td style="width:10%"> <input type=checkbox name=navBarAuthorsCheck checked="checked"/>Authors<p> </td>
				</tr>
			</table>
			<br>
		</div>

		<!--table to hold pages content -->
		<table style="width:80%;  margin-left:10%; margin-right:10%;">
			 	
			 	<!-- blog title -->
			 	<tr style="margin-bottom:5%;">
					<td>
						<p> <%= b.getBlogTitle() %> </p>
					</td>
					<td>
						<!-- space for edit logo -->
						<a href="BlogEdit.jsp"><img src="images/read.jpg" alt="Edit Enabled, click here"> </a>
					</td>
				</tr>
				
				<!--  link to profile of author -->
				
				<tr>
					<td>
						 <h3><a href="Profile.jsp"> Written by <%= b.getAuthor() %> </a> </h3>
					</td>
				</tr>
				
				<!-- creating space -->
				<tr>
					<td>
						<br>
					</td>
				</tr>
				
				
				<!-- first post -->
				<tr>
						<td>
							<p><%= b.getBlogPostTitle() %> </p>
						</td>
						<td>
							<a href="BlogEdit.jsp"><img src="images/read.jpg" alt="Edit Enabled, click here"></a> 
						</td>
				</tr>
				
				<!-- creating space -->
				<tr>
					<td>
						<br>
					</td>
				</tr>
				
				
				<tr>
						<td>
							<textarea NAME="blogPostContent" READONLY="readonly" WRAP=soft COLS=80 ROWS=10>
								
								<%= b.getBlogPostBody() %>
							
							</textarea>
							<br>
							
							<br>
						</td>
				</tr>
				
				<!-- creating space -->
				<tr>
					<td>
						<br>
					</td>
				</tr>
				
				
			
		</table>
		
		<table style="width:80%;  margin-left:10%; margin-right:10%;">
		
		<%
		
		int userId = Integer.parseInt((String)session.getAttribute("userId"));
		
		 /*The function buildBlog is called in order to retrieve the author of blog and all
		 *posts within the blog other than the first post created during blogCreation. */
		 b.buildBlog(userId);
		 
		//adding any additional posts to the page using the contents retrieved from the post table matching the current blogId.
		 for(int i = 0; i < b.getPostCount(); ++i ){
		 	
			 %>
			 
			 <tr>
				<td>
					<p> <%= b.getPostTitleAt(i) %> </p>
				</td>
				
				<td>
					<a href="PostEdit.jsp"><img src="images/edit.jpg" alt="Edit Enabled, click here"> </a>
				</td>
			</tr>
					
			<!-- creating space -->
			<tr>
				<td>
					<br>
				</td>
			</tr>
				
					
			<tr>
				<td>
					<textarea NAME="post1Content" WRAP=soft  READONLY="readonly" COLS=80 ROWS=10>
						<%= b.getPostBodyAt(i) %>
					</textarea>
				</td>
			</tr>	
			 
			 <%
		 }

		%>
		
		</table>
		
	<body>
</html>
