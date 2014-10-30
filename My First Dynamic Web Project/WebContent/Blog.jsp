<%@ page language="java" 
		 contentType="text/html; charset=ISO-8859-1"  
    	 pageEncoding="ISO-8859-1"
    	 import="com.amzi.dao.Blog, com.amzi.dao.User, com.amzi.dao.Post, java.util.ResourceBundle"
    %>  
<!DOCTYPE html>

<% 
	session.setAttribute("currentpage","Blog");
	ResourceBundle lang = ResourceBundle.getBundle("Blog_EN");
	
	//if the session language is FR switch to french, otherwise remains english as set above
	if (session.getAttribute("language").toString().equals("FR")){
		lang = ResourceBundle.getBundle("Blog_FR");
	} 
	
	//if the user clicked change language, set to appropriate language
	if (request.getParameter("language") != null){	
		if (request.getParameter("language").equals("FR")){
			lang = ResourceBundle.getBundle("Blog_FR");
			session.setAttribute("language","FR");
		} else {
			lang = ResourceBundle.getBundle("Blog_EN");
			session.setAttribute("language","EN");
		}
	}		
	%>
	
<html>
<!--  The page used to display all Blogs created within BLOGSHARE
	
	Only a single blog is shown at a time,
	The content of the blog is retrieved from the Blog object created 
	from the functionality linked with CreateBlog.jsp or Profile.jsp.
	
-->
<head>
<meta charset="UTF-8">
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
	
	<jsp:include page="SearchBar.jsp"></jsp:include>

		<!--table to hold pages content -->
		<table class="centered80W" style="margin-bottom:2%;">
			 	
			 	<!-- blog title -->
			 	<tr>
					<td>					
						<p title="<%=lang.getString("blogtitle")%>" style="font-size:28px"> <b> <%= b.getBlogTitle() %> </b> </p>
					</td>
					<td>
						<!-- space for edit logo -->
						<a href="BlogEdit.jsp"><img title="<%=lang.getString("readonlyelement")%>" src="images/read.jpg" alt="<%=lang.getString("readonly")%>"> </a>
					</td>
				</tr>
				
				<!--  link to profile of author -->
				
				<tr>
					<td>
						<a href="Profile.jsp" title="<%=lang.getString("blogauthor")%>" style="color:lightblue; font-size:18px;"> <%=lang.getString("writtenby")%> <%= b.getAuthor() %>  </a>
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
				Boolean editEnabled = p.determinePostEditPrivilege(u);
				
				
				/*
				//If the post is not public and the current user is not the author of the post.
				if( !p.getIsPublic() && !p.getAuthor().equals(u.getUsername())){
					
					//An attempt is made to match current userId with a userId that is associated to the privilegeId of this post. 
					PreparedStatement pst = null;
					ResultSet rs = null;
					DbConnection connectionManager = DbConnection.getInstance(); 					
					editEnabled = false;
					
					try{
						pst = connectionManager.getConnection().prepareStatement("select u.userid as userId from user u, post p, user_post up, posteditprivilege pep, user_posteditprivilege upep, post_posteditprivilege ppep" +
																				 " where u.userid = upep.userid AND " +
																				 " upep.postEditPrivilegeId = pep.postEditPrivilegeId AND " +
																			     " pep.postEditPrivilegeId = ppep.postEditPrivilegeId AND " +
																				 " p.postId = ppep.postid AND " +
																				 " u.userid = up.userid AND " +
																				 " P.postid = up.postid AND " +
																				 " p.postid = '"+p.getPostId()+"'"); 
						
						while(rs.next()){
							//if the user does not have a corresponding entry for the post within postEditPrivilege
							if(u.getUserId() == rs.getInt("userId")){
								editEnabled = true;
								break;
							}
						}
					}catch(SQLException sqlE){
						System.out.println("An exception was thrown while attempting to associate the current user with the edit privileges granted granted for this post.");
						sqlE.printStackTrace();
					}
				}
				*/
				
		 %>
			 
			 <tr>
				<td>
					<p title="<%=lang.getString("posttitle")%> <%= p.getAuthor() %>" style="font-size:20px; text-decoration:underline;"> <%= p.getPostTitle() %> </p>
				</td>
				
				<td>
					<% if(editEnabled == true){
						%>
					<a href="PostCreate.jsp?editEnabled=true&post=<%=i%>"><img title="<%=lang.getString("editenabled")%>" src="images/edit.jpg"  alt="<%=lang.getString("clicktoedit")%>"> </a>
					<% } %>
					
					<% if(editEnabled == false){ %>
					<a href="PostCreate.jsp?editEnabled=false"><img title="<%=lang.getString("editdisabled")%>" src="images/read.jpg"  alt="<%=lang.getString("clicktoview")%>"> </a>
					<% } %>	
				</td>
			</tr>
					
			<tr>
				<td style="background:white; text-align:left;">	
					 <p title="<%=lang.getString("postcontent")%> <%= p.getAuthor() %>" style="margin:5%; color:black;"><%= p.getPostBody() %></p>
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
						<input type="submit" class=button value="<%=lang.getString("createnew")%>">
					</td>
					
					<td style="width:20%;">
					</td>
					
				</tr>
			</table>
		</form>
		
	<body>
</html>
