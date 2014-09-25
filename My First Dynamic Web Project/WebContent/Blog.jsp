<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  
    pageEncoding="ISO-8859-1"%>  
<html>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> 
<head>
<link rel="stylesheet" href="LookAndFeel.css">
<title>BlogShare - Blog</title>
</head>
	<body>
		<!-- navigation bar -->
		<div class="FillScreenTextCentered" style="background-color:lightgrey; height:auto; margin-bottom:2%;">
			<br>
			<table style="width:90%; margin-right:auto; margin-left:auto; cellspacing:1%">
				<tr style="height:50%;">
					<td><p><h3>BLOGSHARE</h3></td>
					<td rowspan="2" style="width:25%; font-size:24px;"> <input type=text name=navBarSearchTerm maxlength=100/></td>
					<td style="width:10%"> <input type=checkbox name=navBarBlogCheck  maxlength=100/>Blogs<p>  </td>
					<td style="width:10%"> <input type=checkbox name=navBarTitleCheck  maxlength=100/>Titles<p> </td>
					<td style="width:10%"> <input type=checkbox name=navBarReadCheck  maxlength=100/>Read<p></td>
					<td rowspan="2" style="width:25%">  <input type=button name=navBarSearch maxlength=100 value="Search"/></td>
				</tr>
				<tr style="height:50%;">
					<td>Welcome!</td>
					<td style="width:13%"> <input type=checkbox name=navBarPostCheck  maxlength=100/>Posts<p> </td>
					<td style="width:13%"> <input type=checkbox name=navBarContentCheck  maxlength=100/>Content<p> </td>
					<td style="width:13%"> <input type=checkbox name=navBarTitleCheck  maxlength=100/>Edit<p> </td>
				</tr>
			</table>
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
						<a href="BlogEdit.jsp"><img src="images/read.jpg" alt="Edit Enabled, click here"> </a>
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
							<a href="BlogEdit.jsp"><img src="images/read.jpg" alt="Edit Enabled, click here"></a> 
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
							<a href="BlogEdit.jsp"><img src="images/edit.jpg" alt="Edit Enabled, click here"> </a>
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
