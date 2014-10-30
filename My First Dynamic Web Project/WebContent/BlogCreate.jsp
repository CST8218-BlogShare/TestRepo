<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<head>
		<link rel="stylesheet" href="Styles/LookAndFeel.css">
		<title>BlogShare - BlogCreate</title>
	</head>
	
	<body>
	
	
		 <!--table to hold pages content -->
		<form name="blogCreateForm" action="BlogCreateServlet" method="post">
			<table class="centered80W">
		
				<!--  description for user about what to do on the page -->
				<tr>
					<td>
						<div class="centered80W" style="margin-bottom:3%;">
							<p>
								Create Your Blog<br>
							</p> 
						</div>
					</td>
				</tr>
		
				<!-- blog title -->
				<tr style="margin-bottom: 5%;">
					<td>
						<p> 
							   Enter Blog Title<br>
						 </p>
						 <p><input type=text name=blogTitle maxlength=100 /></p>
					</td>
				</tr>
		
				<!-- creating space -->
				<tr>
					<td><br></td>
				</tr>
				
				<tr>
					<td>
						<div class="centered80W" style="margin-bottom:2%;">
							<p>
								Please use the first post to describe the purpose of your Blog.<br>
							</p>
						</div>
					<td>
				<tr>
				
		
				<!-- first post -->
				<tr>
					<td>
						<p>
							Enter Post Title<br>
						</p>
						
						<input type=text name=postTitle maxlength=100 />
					</td>
				</tr>
		
				<!-- creating space -->
				<tr>
					<td><br></td>
				</tr>
				
		
				<tr>
					<td>
						<p>
							Enter Post Body<br>
						</p>
						<textarea name="postBody" WRAP=soft COLS=80 ROWS=10></textarea> <br> <br>
					</td>
				</tr>
		
				<tr>
					<td class="centered80W">
						<p>
							The title of your Blog and the title and content of your first Post.<br> 
							Are editable only by you, the author of the Blog.<br>
						</p> 
					<td>
				<tr>
				
				
				<!-- creating space -->
				<tr>
					<td><br></td>
				</tr>
				
				<tr>
					<td class="centered80W">
						<p>
							Additional posts may be made editable by any BlogShare visitor.<br>
							By selecting the option below.<br>
						</p>
					</td>
				</tr>
				
				<tr>
					<td>
						<input type=checkbox name="blogEditableCheckBox"/><p>Allow Public Editing</p>
					</td>
				</tr>
		
				<!-- creating space -->
				<tr>
					<td><br></td>
				</tr>
				
				<tr>
					<td><input type="submit" class=button value="Create Blog">
					<td></td>
				</tr>
		
			</table>
		</form>
		<form action="Profile.jsp">
			<input class="centered80W" type="submit" width="wrap_content" value="Cancel" style="font-size:18px;">		
		</form>
	</body>
</html>
