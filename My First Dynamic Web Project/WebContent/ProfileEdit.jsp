<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<jsp:include page="BootstrapInclude.html" />

<title> <%= session.getAttribute("name") %>'s Password Edit</title>
</head>
<body>
	<h1><span class="glyphicon glyphicon-user" style="fontSize:50px"></span> <%= session.getAttribute("name") %>'s Password Edit</h1>
	<h3><span class="label label-default">Joined: <%= session.getAttribute("dateRegistered") %></span></h3>
	<div style="padding:30px">	
		<div class="input-group">
		  	<span class="input-group-addon" style="min-width:200px">Username:</span>
		 	<input type="text" class="form-control" value="<%=session.getAttribute("name") %>" >
		</div>
		<div class="input-group">
		  	<span class="input-group-addon" style="min-width:200px">Password:</span>
		 	<input type="text" class="form-control" >
		</div>
		<div class="input-group">
		  	<span class="input-group-addon" style="min-width:200px">New Password:</span>
		 	<input type="text" class="form-control" >
		</div>
		<div class="input-group">
		  	<span class="input-group-addon" style="min-width:200px">Repeat New Password: </span>
		 	<input type="text" class="form-control" >
		</div>
		<div class="btn-group" style="padding:30px">
			  <a href="Profile.jsp"><button type="button" class="btn btn-default btn-lrg">Submit Changes</button></a>
			  <a href="Profile.jsp"><button type="button" class="btn btn-default btn-lrg">Cancel</button></a>
		</div>
	</div>
	<br style="clear:left;"/>

</body>
</html>