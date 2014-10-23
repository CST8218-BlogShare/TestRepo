<%@ page language="java" 
		 contentType="text/html; charset=ISO-8859-1"  
    	 pageEncoding="ISO-8859-1"
    	 import="com.amzi.dao.Blog, com.amzi.dao.User, com.amzi.dao.Post, com.amzi.dao.DbConnection, java.sql.PreparedStatement, java.sql.ResultSet, java.sql.SQLException "
    %>  
<html>
<!--  The page used to display all Blogs created within BLOGSHARE
	
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
		Blog b = (Blog) session.getAttribute("currentBlog");
		User u = (User) session.getAttribute("currentUser");
					
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
					<td><a href="Profile.jsp">Welcome <%= session.getAttribute("username") %>!</a></td>
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
				
	
	
		<!-- Displaying posts, either created by the author or by another user -->
		<%
			
			//adding any additional posts to the page using the contents retrieved from the post table matching the current blogId.
	 	
			for(int i = 0 ; i < b.getPostCount(); ++i){
			
				Post p = b.getPostAt(i);
				Boolean editEnabled = true;
				
				//If the post is not public and the current user is not the author of the post.
				if( !p.getIsPublic() && !p.getAuthor().equals(u.getUsername())){
					
					
					//An attempt is made to match current userId with the userId that is associated to the privilegeId of this post. 
					PreparedStatement pst = null;
					ResultSet rs = null;
					DbConnection connectionManager = DbConnection.getInstance(); 					
					
					try{
						pst = connectionManager.getConnection().prepareStatement("select u.userid as userId from user u, post p, posteditprivilege pep, user_posteditprivilege u_pep, post_posteditprivilege p_pep"
																				+ " where u.userid = u_pep.userid"
																				+ " and u_pep.postEditPrivilegeId = pep.postEditPrivilegeId"
																				+ " and pep.postEditPrivilegeId = p_pep.postEditPrivilegeId"
																				+ " and p_pep.postid = p.postid"
																				+ " and u.userid = '"+u.getUserId()+"' ");
						rs = pst.executeQuery();
						rs.first();
						
						//if the user does not have a corresponding entry for the post within postEditPrivilege
						if(u.getUserId() != rs.getInt("userId")){
							editEnabled = false;	
						}
					}catch(SQLException sqlE){
						System.out.println("An exception was thrown while attempting to associate the current user with the edit privileges granted granted for this post.");
						sqlE.printStackTrace();
					}
				}
				
		 %>
			 
			 
			 
			 <tr>
				<td>
					<p title="Title Of Post - Created by <%= p.getAuthor() %> "> <%= p.getPostTitle() %> </p>
				</td>
				
				<td>
					<% if(editEnabled == true){ %>
					<a href="PostCreate.jsp"><img title="Edit Enabled For Element - User Can Edit" src="images/edit.jpg"  alt="Click here to edit."> </a>
					<% } %>
					
					<% if(editEnabled == false){ %>
					<a href="PostCreate.jsp"><img title="Edit Disabled For Element - User Can View" src="images/read.jpg"  alt="Click here to view."> </a>
					<% } %>	
				</td>
			</tr>
					
			<tr>
				<td style="background:white; text-align:left;">	
					<p title="Content Of Post - Created by <%= p.getAuthor() %>" style="margin:5%"><%= p.getPostBody() %></p>
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
