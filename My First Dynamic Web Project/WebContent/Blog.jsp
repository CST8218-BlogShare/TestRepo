<%@ page language="java" 
		 contentType="text/html; charset=ISO-8859-1"  
    	 pageEncoding="ISO-8859-1"
    	 import="com.amzi.dao.Blog, com.amzi.dao.User, com.amzi.dao.Post, java.util.ResourceBundle, java.io.IOException"
    %>  
<!DOCTYPE html>

<% 
	session.setAttribute("currentPage","Blog");
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
<jsp:include page="BootstrapInclude.html" />
<title>BlogShare - Blog</title>

<% 
		
		/* 
			Retrieving the user and blog objects accociated with the 
			currently logged in user and the blog to be displayed.
		*/
		
		Blog b = Blog.getBlogFromDatabaseById(((Blog) session.getAttribute("currentBlog")).getBlogId());
		User u = (User) session.getAttribute("currentUser");
		String postTitle = (String) session.getAttribute("postToView");		
		
		if(b == null){
			
		/* 	
			If the blog cannot be retrieved, this page cannot be displayed. 
			This should not happen within normal operation of the program.
			In response to this behaviour the current user is logged out.
		*/
			
			RequestDispatcher rd=request.getRequestDispatcher("/logoutServlet");
			 
			 try {
				rd.include(request,response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		
		//setting the initalized blog as a session attribute.
		session.setAttribute("currentBlog", b);
%>

<!--  If the user is navigating from the searchResults page causing the sessionVariable postToView to be set.
      The value is retrieved and is used to navigate to that specific post within the current blog being shown. -->
<% if(postTitle != null) { %>
		<script>
			function linkToPost(){
				var postTitle = '<%= postTitle %>';
				//navigating to the element of the page with id that corresponds with the value held in postTitle.
				window.location.hash=postTitle;
			}
			//this function will be called when the javascript window object has finished loading. 
			window.onLoad = linkToPost();
		</script>
<% 
       /* Clearing the postToView attribute in anticipation of the next time the Blog page will be accessed.*/
		session.setAttribute("postToView", null);
	}

%>

</head>
	<body>
	
	<jsp:include page="SearchBar.jsp"></jsp:include>

		<!--table to hold pages content -->
		<table class="fillScreenTextCentered80" style="margin-bottom:2%;">
			 	
			 	<!-- blog title -->
			 	<tr>
					<td>					
						<p title="<%=lang.getString("blogtitle")%>" style="font-size:36px"> <b> <%= b.getBlogTitle() %> </b> </p>
					</td>
					<td>
						<!-- space for edit logo 
						<a href="BlogEdit.jsp"><img title="<%=lang.getString("readonlyelement")%>" src="images/read.jpg" alt="<%=lang.getString("readonly")%>"> </a> -->
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
			
				Boolean editEnabled;
				Post p = b.getPostAt(i); 
				
				//if there is no user logged in, the post will never be editable.
				 if(u == null){
				 	editEnabled = false;
				 }else{
					editEnabled = Post.determinePostEditPrivilegeById(p.getPostId(), p.getAuthor(), p.getIsPublic() , u.getUserId(), u.getUsername());
				 }
		 %>
			 
			 <tr>
				<td>
					<!-- The postTitle is set as an id for each post in order to enable dynamic navigation to each,
					     in case that specific post is retrieved as a searchResult when the search bar is used. -->
					<p id="<%=p.getPostTitle()%>" title="<%=lang.getString("posttitle")%> <%= p.getAuthor() %>" style="font-size:20px; text-decoration:underline;"> <%= p.getPostTitle() %> </p>
				</td>
				
				<td>
					<% if(editEnabled == true){
						%>
						<a href="PostCreate.jsp?editEnabled=true&post=<%=i%>"><img title="<%=lang.getString("editenabled")%>" src="images/edit.jpg"  alt="<%=lang.getString("clicktoedit")%>"> </a>
					<% } %>
					
					<% if(editEnabled == false){ %>
						<img title="<%=lang.getString("editdisabled")%>" src="images/read.jpg"  alt="<%=lang.getString("clicktoview")%>">
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
		
		<% if(u != null){ %>
				
				<tr>
					<td>
						<input type="submit" class=button onClick="createPostClick()" value="<%=lang.getString("createnew")%>">
					</td>
				</tr>
				
		<% } %>
		
		</table>
		
		<!-- form triggered when the "Create New Post" button is clicked  -->
		<form name="createPostForm" action="PostCreate.jsp" method="get"></form>
		
		<script>
			function createPostClick(){
				var form = document.forms['createPostForm'];
				form.submit();
			}
		</script>
		
	<body>
</html>
