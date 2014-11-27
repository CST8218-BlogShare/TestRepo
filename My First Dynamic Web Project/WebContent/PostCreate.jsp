<%@ page language="java" 
	contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="com.amzi.dao.Blog, com.amzi.dao.User, java.util.ResourceBundle, java.io.IOException"
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
	
	Blog b = Blog.getBlogFromDatabaseById(((Blog) session.getAttribute("currentBlog")).getBlogId());
	User u = (User) session.getAttribute("currentUser");
	
	if(b == null || u == null){
	/* 	
		If the blog or current user cannot be retrieved, this page cannot be displayed. 
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
	<form name="postCreateForm" action="postCreateServlet" method="post">
		<table  class="FillScreenTextCentered">
			<!-- blog title -->
			<tr style="margin-bottom: 5%;">
				<td> <p style="font-size:48px;"> <b> <%= b.getBlogTitle() %> </b></p></td>
			</tr>
		
			<!-- creating space -->
			<tr>
				<td>
					<br>
					<br>
				</td>
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
				<td>
					<br>
				</td>
			</tr>
		
			<tr>
				<td style="background:white; text-align:left;">	
					<p title="<%= lang.getString("contentpopup") %>" style="margin:5%; color:black;"><%= b.getPostAt(firstPostIndex).getPostBody() %></p>
				</td>
			</tr>
		
			<!-- creating space -->
			<tr>
				<td>
					<br>
					<br>
				</td>
			</tr>
				
			<!-- post to be added -->
			<tr>
				<td>
					<%  
						if( request.getAttribute("errorMessage") != null){ 
					%>
							<div class="container">
								<div class="alert alert-danger alert-dismissible" role="alert">
									<button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
									<%= lang.getString(request.getAttribute("errorMessage").toString()) %>
								</div>
							</div>
					<%	
						}
					%>
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
							//if editing the first post of the blog, the title cannot be modified.
							if(toEdit == firstPostIndex) { 
					%>
							<p style="font-size:20px; text-decoration:underline;"><%= lang.getString("posttitle") %></p>
							<p style="font-size:28px"> <b> <%= lang.getString("explanationofblog") %></b> </p>
							<input type=hidden name=postTitle value="<%=b.getPostAt(toEdit).getPostTitle() %>"/>
					<% 
							}else{ 
					%>
								<p style="font-size:20px; text-decoration:underline;"><%= lang.getString("posttitle") %></p>
								<input type=text name=postTitle maxlength=100 value="<%=b.getPostAt(toEdit).getPostTitle() %>"/>
					<% 
							} 
					%>
						<br>
						<br>
						<p style="font-size:20px; text-decoration:underline;"><%= lang.getString("postcontent") %></p> 
						<textarea NAME="postBody" WRAP=soft COLS=100 ROWS=10><%=b.getPostAt(toEdit).getPostBody() %></textarea>
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
							/*	
								If the user is logged in, the user is the author of the post and this is not the first post. 
								Show the option to make the post publicly editable. 
							*/
							if(u != null && b.getPostAt(toEdit).getAuthor().equals(u.getUsername()) && toEdit != firstPostIndex){
					%>
								<tr>
									<td class="FillScreenTextCentered">
										<p style="font-size:18px;">
											<%= lang.getString("content2") %>
											<br>
										</p>
									</td>
								</tr>
						
					
					<%			
								if(b.getIsPublic()){ 
					%>
									<tr>
										<td class="FillScreenTextCentered">
											<p style="font-size:18px;">
												<%=lang.getString("content3") %>
											</p>
										
										</td>
									</tr>
							
									<tr>
										<td>
											<input type="checkbox" checked="checked" disabled="disabled"/><p style="font-size:18px;"><%= lang.getString("allowedit") %></p>
										</td>
									</tr>
					<%			
								}else{
					
									/*
									Determining if the value of isPublic for the post to be edited has been set to true. 
									If true, the associated checkBox should be checked.
									*/
									if(b.getPostAt(toEdit).getIsPublic()){
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
					<% 				}//end of if(b.getPostAt(toEdit).getIsPublic())	
								}//end of if(b.getIsPublic())
					%>			
							
					<% 		}//end of if(u != null && b.getPostAt(toEdit).getAuthor() == u.getUsername() && toEdit != firstPostIndex)
						}else{
					%>		
							<% 
								//If the entire blog is publicly editable, this post is always editable. 
								if(b.getIsPublic()) { 
							%>
									<tr>
										<td class="FillScreenTextCentered">
											<p style="font-size:18px;">
												<%= lang.getString("content2") %>
												<br>
												<%=lang.getString("content3") %>
											</p>
										
										</td>
									</tr>
								
									<tr>
										<td>
											<input type="checkbox" checked="checked" disabled="disabled"/><p style="font-size:18px;"><%= lang.getString("allowedit") %></p>
										</td>
									</tr>
									
										
							<% 
								//If the post is not publicly editable, the user has the choice of whether to make the post editable or not.
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
								}//end of if(b.getIsPublic())
							%>	
					<% 
						}//end of if(isEditMode == true)
					%>					 	
						  
			
			<!-- creating space -->
			<tr>
				<td><br></td>
			</tr>
			
			<tr>
				<td> 
					<input type="submit" class=button style="margin-bottom:1%;" value="<%= lang.getString("save") %>">
				</td>
			</tr>
			
			<tr>
				<td>
					<input type="button" class="button" onClick="cancelOnClick()" value="<%= lang.getString("cancel") %>"/>
				</td>
			</tr>
				
		</table>
	</form>
	
	<form name="cancelPostCreateForm" action="Blog.jsp" method="get"></form>
	
	<script>
		function cancelOnClick(){
			var form = document.forms['cancelPostCreateForm'];
			form.submit();
		}
	</script>
</body>
</html>
