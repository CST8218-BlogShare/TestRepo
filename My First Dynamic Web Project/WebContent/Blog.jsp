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
	<% 
		Blog b = (Blog) getServletContext().getAttribute("currentBlog");
	
		int userId = Integer.parseInt(session.getAttribute("userId").toString());
	
	 	/*The function buildBlog is called in order to retrieve the author of blog and all
	 	*posts within the blog other than the first post created during blogCreation. */
	 	b.buildBlog(userId);
	 
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
					<td>Welcome <%= session.getAttribute("username") %>!</td>
					<td style="width:10%"> <input type=checkbox name=navBarPostCheck checked="checked"/>Posts<p> </td>
					<td style="width:10%"> <input type=checkbox name=navBarContentCheck checked="checked"/>Content<p></td>
					<td style="width:10%"> <input type=checkbox name=navBarAuthorsCheck checked="checked"/>Authors<p> </td>
				</tr>
			</table>
			<br>
		</div>

		<!--table to hold pages content -->
		<table style="width:80%;  margin-bottom:2%; margin-left:10%; margin-right:10%;">
			 	
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
				
				
				<!-- first post belonging to author of the blog -->
				<tr>
						<td>
							<p title="Post Title - Owned By Author Of Blog "> <%= b.getBlogPostTitle() %> </p>
						</td>
						<td>
							<a href="BlogEdit.jsp"><img title="Read Only Element - User Cannot Edit" src="images/read.jpg" alt="Read Only"></a> 
						</td>
				</tr>
				
				<!-- creating space -->
				<tr>
					<td>
						<br>
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
				
	
		<%
		
		if(b.getPostsShown() == false){
		//adding any additional posts to the page using the contents retrieved from the post table matching the current blogId.
		  for(int i = 1; i < b.getPostCount(); ++i ){
		 	
			 %>
			 
			 <tr>
				<td>
					<p title="Title Of Post - Owned by Author Of Post"> <%= b.getPostTitleAt(i) %> </p>
				</td>
				
				<td>
					<a href="PostEdit.jsp"><img title="Edit Enabled For Element - User Can Edit" src="images/edit.jpg" alt="Edit Enabled, click here"> </a>
				</td>
			</tr>
					
			<!-- creating space  -->
			<tr>
				<td>
					<br>
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
			b.setPostsShown(true);
		}

		%>
		
		</table>
		
		<form action="PostCreate.jsp">
			<table style="width:80%; margin-left: 10%; margin-right: 10%;">
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
