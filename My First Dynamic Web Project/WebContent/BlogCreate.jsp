<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<head>
		<jsp:include page="BootstrapInclude.html" />
		<title>BlogShare - BlogCreate</title>
	</head>
	
	<body>
	
		<%  
		if( request.getAttribute("errorMessage") != null)
		{ %>
		<div class="container">
			<div class="alert alert-danger alert-dismissible" role="alert">
				<button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<%= request.getAttribute("errorMessage") %>
			</div>
		</div>
	<%	}	%>
		 <!--table to hold pages content -->
		<form name="blogCreateForm" action="BlogCreateServlet" method="post">
			<table class="centered80W">
		
				<!--  description for user about what to do on the page -->
				<tr>
					<td>
						<div class="centered80W" style="margin-bottom:3%;">
							<p style="font-size:32px;">
							   	<b>Create Your Blog</b> <br>
							</p> 
						</div>
					</td>
				</tr>
				
				<!-- blog title -->
				<tr style="margin-bottom: 5%;">
					<td>
						<p style="font-size:20px; text-decoration:underline"> 
						Blog Title
						</p>
						<input type=text name=blogTitle maxlength=100 />
					</td>
				</tr>
		
				<!-- creating space -->
				<tr>
					<td><br></td>
				</tr>
				
				<tr>
					<td>
						<div class="centered80W" style="margin-bottom:2%;">
							<p style="font-size:18px">
								Please use the first post to describe the purpose of your Blog.<br>
							</p>
						</div>
					<td>
				<tr>
				
		
				<!-- first post -->
				<tr>
					<td>
						<p style="font-size:20px; text-decoration:underline">
						   Post Title
						</p>
						<p style="font-size:28px"> <b> Explanation of Blog </b> </p>
						<input type=hidden name=postTitle value="Explanation of Blog"/>
					</td>
				</tr>
		
				<!-- creating space -->
				<tr>
					<td><br></td>
				</tr>
				
		
				<tr>
					<td>
						<p style="font-size:20px; text-decoration:underline">
							Post Body<br>
						</p>
						<textarea name="postBody" WRAP=soft COLS=80 ROWS=10></textarea> <br> <br>
					</td>
				</tr>
		
				<tr>
					<td class="centered80W" style="font-size:18px;">
						<p>
							The title of your Blog and the title and content of your first Post.<br> 
							Are editable only by you, the author of the Blog.<br>
							<br>
							<br>
							Additional posts may be made editable by any BlogShare visitor.<br>
							By selecting the option below.<br>
						</p>
					</td>
				</tr>
				
				<tr>
					<td>
						<input type=checkbox name="blogEditableCheckBox"/><p style="font-size:18px;">Allow Public Editing</p>
					</td>
				</tr>
		
				<!-- creating space -->
				<tr>
					<td><br></td>
				</tr>
				
				<tr>
					<td><input type="submit" class=button value="Create Blog">
				</tr>
		
			</table>
		</form>
		<form action="Profile.jsp">
			<input class="centered80W" type="submit" width="wrap_content" value="Cancel" style="font-size:18px;">		
		</form>
	</body>
</html>
