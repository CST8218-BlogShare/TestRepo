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
							<font size=10>
								Create Your Blog<br>
							</font> 
						</div>
					</td>
				</tr>
		
				<!-- blog title -->
				<tr style="margin-bottom: 5%;">
					<td>
						<font size="5"> 
							   Enter Blog Title<br>
						 </font>
						 <font size="18"><input type=text name=blogTitle maxlength=100 /></font>
					</td>
				</tr>
		
				<!-- creating space -->
				<tr>
					<td><br></td>
				</tr>
				
				<tr>
					<td>
						<div class="centered80W" style="margin-bottom:2%;">
							<font size=4>
								Please use the first post to describe the purpose of your Blog.<br>
							</font> 
						</div>
					<td>
				<tr>
				
		
				<!-- first post -->
				<tr>
					<td>
						<font size="5">
							Enter Post Title<br>
						</font>
						
						<input type=text name=postTitle maxlength=100 />
					</td>
				</tr>
		
				<!-- creating space -->
				<tr>
					<td><br></td>
				</tr>
				
		
				<tr>
					<td>
						<font size="5">
							Enter Post Body<br>
						</font>
						<textarea name="postBody" WRAP=soft COLS=80 ROWS=10></textarea> <br> <br>
					</td>
				</tr>
		
				<tr>
					<td class="centered80W">
						<font size=4>
							The title of your Blog and the title and content of your first Post.<br> 
							Are editable only by you, the author of the Blog.<br>
						</font> 
					<td>
				<tr>
				
				
				<!-- creating space -->
				<tr>
					<td><br></td>
				</tr>
				
				<tr>
					<td class="centered80W">
						<font size=4>
							Additional posts may be made editable by any BlogShare visitor.<br>
							By selecting the option below.<br>
						</font>
					</td>
				</tr>
				
				<tr>
					<td>
						<input type=checkbox name="blogEditableCheckBox"/><font>Allow Public Editing</font><p>
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
