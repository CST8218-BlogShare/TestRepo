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
	Blog b = (Blog) session.getAttribute("currentBlog");

	try{
		boolean isEditMode = b.getEditMode();
		int toEdit = b.getToEdit();
	}catch(Exception e){
		
	}

%>
<body>
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
				<td><label> <%= b.getPostAt(firstPostIndex).getPostTitle() %> </label></td>
			</tr>
	
			<!-- creating space -->
			<tr>
				<td><br></td>
			</tr>
	
	
			<tr>
				<td style="background:white; text-align:left;">	
						<p title="Content Of Post - Owned by Author Of Post" style="margin:5%"><%= b.getPostAt(firstPostIndex).getPostBody() %></p>
				</td>
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
					<%
						if( b.getEditMode() == false){
					%>
							<input type=text name=postTitle maxlength=100/>	
					
							<p>New Post Content Below</p> 
							
							<textarea NAME="postBody" WRAP=soft COLS=80 ROWS=10></textarea>
					<%
						}else if(b.getEditMode() == true){
					%>
							<input type=text name=postTitle maxlength=100 value="<%=b.getPostAt(b.getToEdit()).getPostTitle() %>"/>
					
							<p>New Post Content Below</p> 
							<textarea NAME="postBody" WRAP=soft COLS=80 ROWS=10>
								<%=b.getPostAt(b.getToEdit()).getPostBody() %>
							</textarea>
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
					<font size=4>
						This post may be made editable by any BlogShare visitor.<br>
						By selecting the option below.<br>
					</font>
				</td>
			</tr>
					
			<tr>
				<td>
					<input type=checkbox name="postEditableCheckBox"/><font>Allow Public Editing</font><p>
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
</body>
</html>
