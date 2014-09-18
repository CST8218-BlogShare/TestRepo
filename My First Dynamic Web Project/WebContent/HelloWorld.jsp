<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>HELLO WORLD APP</title>
</head>
<body>
<h1>Hello World!</h1>
<h3>Jan was here haha</h3>
<%
out.println("Your IP address is " + request.getRemoteAddr() + "<br/>");
out.println("Your user agent is " + request.getHeader("user-agent") + "<br/>");
%>
</body>
</html>