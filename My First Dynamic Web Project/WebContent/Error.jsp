<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  
    pageEncoding="ISO-8859-1"%>  
<html>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> 
<head>
<link rel="stylesheet" href="Styles/LookAndFeel.css">
<title>BlogShare - Blog</title>
</head>
	<body>
		<!-- navigation bar -->
		<div class="FillScreenTextCentered" style="background-color:lightgrey; height:auto; margin-bottom:2%;">
			<br>
			<table style="width:90%; margin-right:auto; margin-left:auto;">
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
		
		
		<div  class="FillScreenTextCentered" style="height:60%;  margin-top:auto; marign-bottom:auto; background-color:lightgrey;">
		
			Error Messages go here.
		
		</div>
</body>
</html>