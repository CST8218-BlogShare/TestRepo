<%@ page language="java" 
	contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="com.amzi.dao.Blog, com.amzi.dao.User, java.util.ResourceBundle"
	%>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<head>
<jsp:include page="BootstrapInclude.html" />
<title>BlogShare - PostCreate</title>
</head>
<!--table to hold pages content -->

<%
	final int firstPostIndex = 0;
	boolean isEditMode;
	int toEdit = -1;
	
	
	
	if(session.getAttribute("language") == null){
		session.setAttribute("language","EN");
	}
	
	ResourceBundle lang = ResourceBundle.getBundle("PostCreate_EN");
	
	if (session.getAttribute("language").toString().equals("FR")){
		lang = ResourceBundle.getBundle("PostCreate_FR");
	} 
	
	Blog b = (Blog) session.getAttribute("currentBlog");
	User u = (User) session.getAttribute("currentUser");
	
	if(b == null){
		//System.out.println("The session is invalid");
		System.exit(1);
	}
	
	try{
		isEditMode = Boolean.parseBoolean((request.getParameter("editEnabled")));
		//will always throw an error if not in editMode as a post will not be retrievable
		toEdit = Integer.parseInt(request.getParameter("post"));
	}catch(Exception e){
		isEditMode = false;
		toEdit = -1;
	}
	session.setAttribute("editMode", isEditMode);
	session.setAttribute("toEdit", toEdit);
	b.setEditMode(isEditMode, toEdit);
	
%>
<body>

	<form name="postForm" action="postCreateServlet" method="post">
		<table  class="centered80W">
	
			<!-- blog title -->
			<tr style="margin-bottom: 5%;">
				<td> <p style="font-size:48px;"> <b> <%= b.getBlogTitle() %> </b></p></td>
			</tr>
	
			<!-- creating space -->
			<tr>
				<td><br><br></td>
			</tr>
			
			<tr>
				<td>
					<p style="font-size:17px;">
						<%= lang.getString("content1") %>
					</p>
					<br>
				</td>
			</tr>
	
			<!-- first post -->
			<tr>
				<td><p style="font-size:20px; text-decoration:underline;"> <%= lang.getString("explanationofblog") %> </p></td>
			</tr>
	
			<!-- creating space -->
			<tr>
				<td><br></td>
			</tr>
	
	
			<tr>
				<td style="background:white; text-align:left;">	
						<p title="<%= lang.getString("contentpopup") %>" style="margin:5%; color:black;"><%= b.getPostAt(firstPostIndex).getPostBody() %></p>
				</td>
			</tr>
	
			<!-- creating space -->
			<tr>
				<td><br><br></td>
			</tr>
			
			<!-- post to be added -->
			<tr>
				<td>
					<%  if( request.getAttribute("errorMessage") != null)
						{ %>
						<div class="container">
							<div class="alert alert-danger alert-dismissible" role="alert">
								<button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
								<%= lang.getString(request.getAttribute("errorMessage").toString()) %>
							</div>
						</div>
					<%	}	%>
					<%
						//if in post creation mode.
						if( isEditMode == false){
					%>
							<p style="font-size:20px; text-decoration:underline;"><%= lang.getString("entertitile") %></p>
					
							<input type=text name=postTitle maxlength=100 value=""/>	
					
							<br>
							<br>
							
							<p style="font-size:20px; text-decoration:underline;"><%= lang.getString("enterpost") %></p> 
							
							<textarea  NAME="postBody" WRAP=soft COLS=100 ROWS=10></textarea>
					<%
						//If in post editing mode
						}else if(isEditMode == true){
					%>
							<% //if editing the first post of the blog, the title cannot be modified.
								if(toEdit == firstPostIndex) { %>
									<p style="font-size:20px; text-decoration:underline;"><%= lang.getString("posttitle") %></p>
									<p style="font-size:28px"> <b> <%= lang.getString("explanationofblog") %></b> </p>
									<input type=hidden name=postTitle value="<%=b.getPostAt(toEdit).getPostTitle() %>"/>
							<% }else{ %>
									<p style="font-size:20px; text-decoration:underline;"><%= lang.getString("posttitle") %></p>
									<input type=text name=postTitle maxlength=100 value="<%=b.getPostAt(toEdit).getPostTitle() %>"/>
							<% } %>
						
							<p style="font-size:20px; text-decoration:underline;"><%= lang.getString("postcontent") %></p> 
							<textarea NAME="postBody" WRAP=soft COLS=80 ROWS=10><%=b.getPostAt(toEdit).getPostBody() %></textarea>
					<%
						}
					%>
				</td>
			</tr>
			
			
			<!-- creating space -->
			<tr>
				<td><br></td>
			</tr>
					
			
					<%	if(isEditMode == true){ 
							if(u != null && b.getPostAt(toEdit).getAuthor() == u.getUsername() && toEdit != firstPostIndex){
					%>
						 		<!--  Only show the isPublic option if the current user is the author of the post. 
							  	If the post being edited, is the first post of the blog. Don't show the option to make the post publicly editable. -->
								<tr>
									<td class="FillScreenTextCentered">
										<p style="font-size:18px;">
											<%= lang.getString("content2") %>
											<br>
										</p>
									</td>
								</tr>
						
					<!--  Determining if the value of isPublic has been set to true. Which means that the associated checkBox should be checked.-->
					<%			if(b.getPostAt(toEdit).getIsPublic()){
					%>				
									<tr>
										<td>
											<input type=checkbox name="postEditableCheckBox" checked="checked"/><p style="font-size:18px;"><%= lang.getString("allowedit") %></p>
										</td>
									<tr>		
					<%				
								}else{ 
					%>				
									<tr>
										<td>
											<input type=checkbox name="postEditableCheckBox"/><p style="font-size:18px;"><%= lang.getString("allowedit") %></p>
										</td>
									</tr>
					<% 			}		
							}
						}else{
					%>		
							<tr>
								<td class="FillScreenTextCentered">
									<p style="font-size:18px;">
										<%= lang.getString("content2") %>
										<br>
									</p>
								</td>
							</tr>
					
							<tr>
								<td>
									<input type=checkbox name="postEditableCheckBox"/><p style="font-size:18px;"><%= lang.getString("allowedit") %></p>
								</td>
							</tr>
					<% 
						}
					%>					 	
						  
			
			<!-- creating space -->
			<tr>
				<td><br></td>
			</tr>
			
			<tr>
				<td> 
					<input type="submit" class=button value="<%= lang.getString("save") %>">
				</td>
			</tr>
		</table>
	</form>
	
	<form action="Blog.jsp">
		<input class="centered80W" type="submit" width="wrap_content" value="<%= lang.getString("cancel") %>" style="font-size:18px;">
	</form>
</body>
</html>
