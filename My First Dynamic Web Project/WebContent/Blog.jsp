<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  
    pageEncoding="ISO-8859-1"%>  
<html>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> 
<head>
<link rel="stylesheet" href="LookAndFeel.css">
<title>BlogShare - Blog</title>
</head>
	<body>
		<!-- navbar -->
		<div class="FillScreenTextCentered" style="background-color:lightgrey; margin-bottom:2%;">
			<br>
			Navigation bar goes here<br>
			<br>
		</div>

		<!--table to hold pages content -->
		<table style="width:80%;  margin-left:10%; marin-right:10%;">
			 	
			 	<!-- blog title -->
			 	<tr style="margin-bottom:5%;">
					<td>
						<p> Blog Title </p>
					</td>
					<td>
						<!-- space for edit logo -->
						<img src="images/read.jpg" alt="Edit Enabled, click here"> 
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
							<p> First Post Title </p>
						</td>
						<td>
							<img src="images/read.jpg" alt="Edit Enabled, click here"> 
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
							<textarea NAME="post1Content" READONLY="readonly" WRAP=soft COLS=80 ROWS=10></textarea>
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
							<p> Second Post Title </p>
						</td>
						<td>
							<img src="images/edit.jpg" alt="Edit Enabled, click here"> 
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
							<textarea NAME="post1Content" WRAP=soft  READONLY="readonly" COLS=80 ROWS=10></textarea>
						</td>
					</tr>	
			
		</table>
	<body>
</html>
