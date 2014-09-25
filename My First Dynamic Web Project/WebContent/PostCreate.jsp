<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  
    pageEncoding="ISO-8859-1"%>  
<html>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> 
<head>
<link rel="stylesheet" href="LookAndFeel.css">
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
						<p>New Post Title Below</p>
						<input type=text name=postTitle  maxlength=100/>
						<p>New Post Content Below</p>
						<textarea NAME="postBody" WRAP=soft COLS=80 ROWS=10>Post Content Goes Here Editable</textarea>
					</td>
				</tr>
			 
					<tr>
						<td>
							<button NAME=save>SAVE</button>
						</td>
						<td>
						</td>
					</tr>
					
					<!-- creating space -->
				<tr>
					<td>
						<button NAME=save>Cancel</button>
						<button NAME=save>Submit</button>
					</td>
				</tr>
				
			
		</table>
	<body>
</html>
