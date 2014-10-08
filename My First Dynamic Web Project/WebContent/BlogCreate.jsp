<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<head>
		<link rel="stylesheet" href="Styles/LookAndFeel.css">
		<title>BlogShare - BlogCreate</title>
	</head>
	
	<body>
	
	
		<!--  description for user about what to do on the page -->
		
		<div class="FillScreenTextCentered" style="margin-bottom:3%;">
			<font size=10>
				Create Your Blog<br>
			</font> 
		</div>
		
		 <!--table to hold pages content -->
		<form name="blogCreateForm" action="blogServlet" method="post">
			<table style="width: 80%; margin-left: 10%; margin-right: 10%;">
		
				<!-- blog title -->
				<tr style="margin-bottom: 5%;">
					<td>
						<font size="5"> 
							   Enter Blog Title<br>
							   <br>
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
						<div class="FillScreenTextCentered" style="margin-bottom:2%;">
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
							<br>
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
							<br>
						</font>
						<textarea NAME="postBody" WRAP=soft COLS=80 ROWS=10></textarea> <br> <br>
					</td>
				</tr>
		
				<tr>
					<td>
						<div class="FillScreenTextCentered" style="margin-bottom:2%;">
							<font size=4>
								The title of your Blog<br>
								and the title and content of your first Post<br> 
								are editable only by YOU, the author of the Blog.<br>
							</font> 
						</div>
					<td>
				<tr>
				
				
				<!-- creating space -->
				<tr>
					<td><br></td>
				</tr>
		
				<tr>
					<td><input type="submit" class = button value="Create Blog">
					<td></td>
				</tr>
		
			</table>
		</form>
		<form action="Profile.jsp">
			<table style="width: 80%; margin-left: 10%; margin-right: 10%;">
				<tr>
					<td>
						<input type="submit" class = button width="wrap_content" value="Cancel">
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
