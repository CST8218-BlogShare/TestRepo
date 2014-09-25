<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title> <%= session.getAttribute("name") %>'s Profile Page</title>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="LookAndFeel.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

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
		
		<div>
		<h1><span class="glyphicon glyphicon-user" style="fontSize:50px"></span> <%= session.getAttribute("name") %>'s Profile Page</h1>
		<h3><span class="label label-default">Joined: <%= session.getAttribute("dateRegistered") %></span></h3>
		<p style="padding:50px">
			<form action="editProfile.jsp">
				<input type="submit" width="wrap_content" value="Edit Profile" class="btn btn-default btn-lrg" style="width:500px">
			</form>
			<br style="clear:left;"/>
			<form action="BlogCreate.jsp">
   				<input type="submit" width="wrap_content" value="Create Blog" class="btn btn-default btn-lrg" style="width:500px">
			</form>
			
		</div>
	</body>
</html>