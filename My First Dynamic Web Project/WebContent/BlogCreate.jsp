<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  
    pageEncoding="ISO-8859-1"%>  
<html>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> 
<head>
<link rel="stylesheet" href="LookAndFeel.css">
<title>BlogShare - BlogCreate</title>
</head>
		<!--table to hold pages content -->
		<table style="width:80%;  margin-left:10%; marin-right:10%;">
			 	
			 	<!-- blog title -->
			 	<tr style="margin-bottom:5%;">
					<td>
						<p> Enter Blog Title </p>
						<font size="18"><input type=text name=blogTitle maxlength=100/></font>
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
							<p>Enter Post Title </p>
							<input type=text name=blogTitle  maxlength=100/>	
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
							<p>Enter Post Body</p>
							<textarea NAME="postBody" WRAP=soft COLS=80 ROWS=10></textarea>
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
							<button NAME=save>SAVE</button>
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
