<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title> <%= session.getAttribute("username") %>'s Profile Page</title>

<jsp:include page="BootstrapInclude.html" />

</head>

<body>


	<!-- navigation bar - nav bar is being styled differently, think it has something to do with the external style sheets you are using.  -->
		<div class="FillScreenTextCentered" style="background-color:lightgrey; height:auto; margin-bottom:2%;">
			<br>
			<table style="width:90%; margin-right:auto; margin-left:auto;">
				<tr style="height:50%;">
					<td><h3><a href="Home.jsp">BLOGSHARE</a></h3></td>
					<td rowspan="2" style="width:25%; font-size:24px;"> <input type=text name=navBarSearchTerm maxlength=100/></td>
					<td style="width:10%"> <input type=checkbox name=navBarBlogsCheck checked="checked"/>Blogs<p>  </td>
					<td style="width:10%"> <input type=checkbox name=navBarTitlesCheck checked="checked"/>Titles<p></td>
					<td style="width:10%"> <input type=checkbox name=navBarUsersCheck checked="checked"/>Users<p> </td>
					<td rowspan="2" style="width:25%">  <input type=button name=navBarSearch maxlength=100 value="Search"/></td>
				</tr>
				<tr style="height:50%;">
					<td>Welcome <%= session.getAttribute("username") %>!</td>
					<td style="width:10%"> <input type=checkbox name=navBarPostCheck checked="checked"/>Posts<p> </td>
					<td style="width:10%"> <input type=checkbox name=navBarContentCheck checked="checked"/>Content<p></td>
					<td style="width:10%"> <input type=checkbox name=navBarAuthorsCheck checked="checked"/>Authors<p> </td>
				</tr>
			</table>
			<br>
		</div>

<h1><span class="glyphicon glyphicon-user" style="fontSize:50px"></span> <%= session.getAttribute("username") %>'s Profile Page</h1>
<h3><span class="label label-default">Joined: <%= session.getAttribute("dateRegistered") %></span></h3>
<p style="padding:50px">
	<a href="ProfileEdit.jsp"><button type="button" class="btn btn-default btn-lrg" style="width:500px">Edit Profile</button></a>
	<br style="clear:left;"/>
	<a href="BlogCreate.jsp"><button type="button" class="btn btn-default btn-lrg" style="width:500px">Create Blog</button></a>
</p>
</body>
</html>