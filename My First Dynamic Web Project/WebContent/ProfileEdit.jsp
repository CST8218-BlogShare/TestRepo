<%@ page language="java" 
		contentType="text/html; charset=ISO-8859-1"
    	pageEncoding="ISO-8859-1"
    	import="com.amzi.dao.User"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<% User u = (User) session.getAttribute("currentUser"); %>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<jsp:include page="BootstrapInclude.html" />

<title> <%= u.getUsername() %>'s Password Edit</title>
</head>
<body>
	<h1><span class="glyphicon glyphicon-user" style="fontSize:50px"></span> <%= u.getUsername() %>'s Profile Edit</h1>
	<h3><span class="label label-default">Joined: <%= u.getDateRegistered() %></span></h3>
	<div style="padding:30px">	
		<form name="editPassForm" action="EditUserServlet" method="post">
		<div class="input-group">
		  	<span class="input-group-addon" style="min-width:200px">Username:</span>
		 	<input name="loginUsername" type="text" class="form-control" value="<%= u.getUsername() %>" >
		</div>
		<div class="input-group">
		  	<span class="input-group-addon" style="min-width:200px">New Username:</span>
		 	<input name="newUsername" type="text" class="form-control">
		</div>
		<div class="input-group">
		  	<span class="input-group-addon" style="min-width:200px">Password:</span>
		 	<input name="loginPassword" type="password" class="form-control" >
		</div>
		<div class="input-group">
		  	<span class="input-group-addon" style="min-width:200px">New Password:</span>
		 	<input name="newPass" type="password" class="form-control" >
		</div>
		<div class="input-group">
		  	<span class="input-group-addon" style="min-width:200px">Repeat New Password: </span>
		 	<input name="newPassConfirm" type="password" class="form-control" >
		</div>
		<div class="btn-group" style="padding:30px">
			 <button type="submit" class="btn btn-default btn-lrg">Submit Changes</button>
			 <a href="Profile.jsp"> <button type="button" class="btn btn-default btn-lrg">Cancel</button></a>
		</div>
		</form>
	</div>
	<br style="clear:left;"/>

</body>
</html>