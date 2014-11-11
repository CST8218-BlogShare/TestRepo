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
					The title and post shown below are the first post of this Blog.<br>
					<br>
					The content shown contains a general explanation of the Blog,<br>
					and it is displayed to help create the content of your post. <br>
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
					<%
						//if in post creation mode.
						if( isEditMode == false){
					%>
							<p style="font-size:20px; text-decoration:underline;">Enter New Post Title</p>
					
							<input type=text name=postTitle maxlength=100 value=""/>	
					
							<p style="font-size:20px; text-decoration:underline;">Enter New Post Content</p> 
							
							<textarea NAME="postBody" WRAP=soft COLS=80 ROWS=10></textarea>
					<%
						//If in post editing mode
						}else if(isEditMode == true){
					%>
							<% //if editing the first post of the blog, the title cannot be modified.
								if(toEdit == firstPostIndex) { %>
									<p style="font-size:20px; text-decoration:underline;">Post Title</p>
									<p style="font-size:28px"> <b> Explanation of Blog </b> </p>
									<input type=hidden name=postTitle value="<%=b.getPostAt(toEdit).getPostTitle() %>"/>
							<% }else{ %>
									<p style="font-size:20px; text-decoration:underline;">Post Title</p>
									<input type=text name=postTitle maxlength=100 value="<%=b.getPostAt(toEdit).getPostTitle() %>"/>
							<% } %>
						
							<p style="font-size:20px; text-decoration:underline;">Post Content</p> 
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
			<% 	//if the post being edited is not the first post, display this option.
				if(toEdit != firstPostIndex) { %>
			
			<tr>
				<td class="FillScreenTextCentered">
					<p style="font-size:18px;">
						This post may be made editable by any BlogShare visitor.<br>
						By selecting the option below.<br>
						<br>
					</p>
				</td>
			</tr>
			
			<% } %>	
					
			<tr>
				<td>
					<%	//if in post creation mode
						if(isEditMode == false){
					%>
					<input type=checkbox name="postEditableCheckBox"/><p style="font-size:18px;">Allow Public Editing</p>
					<%
						//If in post editing mode
						}else if(isEditMode == true){
							//If the post being edited, is the first post of the blog. Don't show the option to make the post publicly editable.
							if(toEdit != firstPostIndex){
								//Determining if the value of isPublic has been set to true. Which means that the associated checkBox should be checked.
								if(b.getPostAt(toEdit).getIsPublic()){
					%>				
									<input type=checkbox name="postEditableCheckBox" checked="checked"/><p style="font-size:18px;">Allow Public Editing</p>
					<%		
								}else{ 
					%>
									<input type=checkbox name="postEditableCheckBox"/><p style="font-size:18px;">Allow Public Editing</p>
					<% 			}
							}		
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
