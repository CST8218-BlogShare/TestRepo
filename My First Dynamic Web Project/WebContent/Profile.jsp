<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title> <%= session.getAttribute("name") %>'s Profile Page</title>

<jsp:include page="BootstrapInclude.html" />

</head>

<body>

<h1><span class="glyphicon glyphicon-user" style="fontSize:50px"></span> <%= session.getAttribute("name") %>'s Profile Page</h1>
<h3><span class="label label-default">Joined: <%= session.getAttribute("dateRegistered") %></span></h3>
<p style="padding:50px">
	<a href="ProfileEdit.jsp"><button type="button" class="btn btn-default btn-lrg" style="width:500px">Edit Profile</button></a>
	<br style="clear:left;"/>
	<a href="Blog.jsp"><button type="button" class="btn btn-default btn-lrg" style="width:500px">Create Blog</button></a>
</p>
</body>
</html>