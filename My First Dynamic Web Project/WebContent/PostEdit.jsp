<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  
    pageEncoding="ISO-8859-1"%>  
<html>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> 
<head>
<link rel="stylesheet" href="Styles/LookAndFeel.css">
<title>BlogShare - PostEdit</title>
</head>
		<!--table to hold pages content -->
		<table style="width:80%;  margin-left:10%; marin-right:10%;">
			 	
			 	<!-- blog title -->
			 	<tr style="margin-bottom:5%;">
					<td>
						<label> Blog Title goes in this label </label>
					</td>
				</tr>
				
				<!-- creating space -->
				<tr>
					<td>
						<br>
					</td>
				</tr>
				
				<!-- first post -->
				<tr>
						<td>
							<label> First post Title </label>
						</td>
				</tr>
				
				<!-- creating space -->
				<tr>
					<td>
						<br>
					</td>
				</tr>
				
				
				<tr>
						<td>
							<textarea NAME="postBody" READONLY="readonly" WRAP=soft COLS=80 ROWS=10>Post Content Goes Here Uneditable</textarea>
							<br>
							<br>
						</td>
				</tr>
				
				<!-- creating space -->
				<tr>
					<td>
						<br>
					</td>
				</tr>
				
		
				<!-- additional posts -->
				<tr>
					<td>
						<p>Post Title Below</p>
						<input type=text name=postTitle  maxlength=100/>
						<p>NPost Content Below</p>
						<textarea NAME="postBody" WRAP=soft COLS=80 ROWS=10>Post Content Goes Here Editable</textarea>
					</td>
				</tr>
			 
			 	<!-- save button that links back to Blog.jsp -->
				<tr>
					<td>
						<form action="Blog.jsp">
	   						<input type="submit" value="Save">
						</form>
					</td>
				</tr>
					
					<!-- creating space -->
				<tr>
					<td>
						<form action="Profile.jsp">
   							<input type="submit" width="wrap_content" value="Cancel">
						</form>
					</td>
				</tr>
		</table>
	<body>
</html>
