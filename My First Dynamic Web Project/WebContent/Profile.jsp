<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title> <%= session.getAttribute("name") %>'s Profile Page</title>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

</head>

<body>

<h1><span class="glyphicon glyphicon-user" style="fontSize:50px"></span> <%= session.getAttribute("name") %>'s Profile Page</h1>
<h3><span class="label label-default">Joined: <%= session.getAttribute("dateRegistered") %></span></h3>
<p style="padding:50px">
	<button type="button" class="btn btn-default btn-lrg" style="width:500px">Edit Profile</button>
	<br style="clear:left;"/>
	<button type="button" class="btn btn-default btn-lrg" style="width:500px">Create Blog</button>
</p>
</body>
</html>