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
	final int firstPostIndex = 0;
	boolean isEditMode;
	int toEdit = -1;
	
	Blog b = (Blog) session.getAttribute("currentBlog");
	
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
				<td> <p style="font-size:32px;"> <b> <%= b.getBlogTitle() %> </b></p></td>
			</tr>
	
			<!-- creating space -->
			<tr>
				<td><br><br></td>
			</tr>
			
			<tr>
				<td>
					<p style="font-size:17px;">
					The Title and Post shown below are from the first post of this blog.<br>
					They should contain a general explanation of the blog, and are shown to help you create your post. <br>
					</p>
					<br>
				</td>
			</tr>
	
			<!-- first post -->
			<tr>
				<td><p style="font-size:20px; text-decoration:underline;"> <%= b.getPostAt(firstPostIndex).getPostTitle() %> </p></td>
			</tr>
	
			<!-- creating space -->
			<tr>
				<td><br></td>
			</tr>
	
	
			<tr>
				<td style="background:white; text-align:left;">	
						<p title="Content Of Post - Owned by Author Of Post" style="margin:5%; color:black;"><%= b.getPostAt(firstPostIndex).getPostBody() %></p>
				</td>
			</tr>
	
			<!-- creating space -->
			<tr>
				<td><br><br></td>
			</tr>
			
			<!-- post to be added -->
			<tr>
				<td>
					<p style="font-size:20px; text-decoration:underline;">New Post Title</p>
					<%
						if( isEditMode == false){
					%>
							<input type=text name=postTitle maxlength=100/>	
					
							<p style="font-size:20px; text-decoration:underline;">New Post Content</p> 
							
							<textarea NAME="postBody" WRAP=soft COLS=80 ROWS=10></textarea>
					<%
						}else if(isEditMode == true){
					%>
							<input type=text name=postTitle maxlength=100 value="<%=b.getPostAt(toEdit).getPostTitle() %>"/>
					
							<p style="font-size:20px; text-decoration:underline;">Current Post Content</p> 
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
			
			
			<!-- option to enable public editing of content  -->
			
			<tr>
				<td class="FillScreenTextCentered">
					<p style="font-size:18px;">
						This post may be made editable by any BlogShare visitor.<br>
						By selecting the option below.<br>
						<br>
					</p>
				</td>
			</tr>
					
			<tr>
				<td>
					<%
						if(isEditMode == false){
					%>
					<input type=checkbox name="postEditableCheckBox"/><p style="font-size:18px;">Allow Public Editing</p>
					<%
						}else if(isEditMode == true){
							if(b.getPostAt(toEdit).getIsPublic()){
					%>
								<input type=checkbox name="postEditableCheckBox" checked="checked"/><p style="font-size:18px;">Allow Public Editing</p>
					<%		
							}else{ 
					%>
								<input type=checkbox name="postEditableCheckBox"/><p style="font-size:18px;">Allow Public Editing</p>
						<% } %>
					<%
						}
					%>
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
	
	<form action="Blog.jsp">
		<input class="centered80W" type="submit" width="wrap_content" value="Cancel" style="font-size:18px;">
	</form>
</body>
</html>
