<%@ page language="java" 
	contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="com.amzi.dao.Blog, java.util.ResourceBundle"
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
	
	if(session.getAttribute("language") == null){
		session.setAttribute("language","EN");
	}
	
	ResourceBundle lang = ResourceBundle.getBundle("PostCreate_EN");
	
	if (session.getAttribute("language").toString().equals("FR")){
		lang = ResourceBundle.getBundle("PostCreate_FR");
	} 
	
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
					<%
						//if in post creation mode.
						if( isEditMode == false){
					%>
							<p style="font-size:20px; text-decoration:underline;"><%= lang.getString("entertitile") %></p>
					
							<input type=text name=postTitle maxlength=100 value=""/>	
					
							<p style="font-size:20px; text-decoration:underline;"><%= lang.getString("enterpost") %></p> 
							
							<textarea NAME="postBody" WRAP=soft COLS=80 ROWS=10></textarea>
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
			
			
			<!-- option to enable public editing of content  -->
			<% 	//if the post being edited is not the first post, display this option.
				if(toEdit != firstPostIndex) { %>
			
			<tr>
				<td class="FillScreenTextCentered">
					<p style="font-size:18px;">
					<%= lang.getString("content2") %>
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
					<input type=checkbox name="postEditableCheckBox"/><p style="font-size:18px;"><%= lang.getString("allowedit") %></p>
					<%
						//If in post editing mode
						}else if(isEditMode == true){
							//If the post being edited, is the first post of the blog. Don't show the option to make the post publicly editable.
							if(toEdit != firstPostIndex){
								//Determining if the value of isPublic has been set to true. Which means that the associated checkBox should be checked.
								if(b.getPostAt(toEdit).getIsPublic()){
					%>				
									<input type=checkbox name="postEditableCheckBox" checked="checked"/><p style="font-size:18px;"><%= lang.getString("allowedit") %></p>
					<%		
								}else{ 
					%>
									<input type=checkbox name="postEditableCheckBox"/><p style="font-size:18px;"><%= lang.getString("allowedit") %></p>
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
