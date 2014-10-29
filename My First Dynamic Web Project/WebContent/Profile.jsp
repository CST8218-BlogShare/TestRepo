<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="java.util.ArrayList, java.io.IOException, com.amzi.dao.User"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="BootstrapInclude.html" />

<%
	User u =  (User) session.getAttribute("currentUser");
%>


<title><%=u.getUsername()%>'s Profile Page</title>
</head>
<body>

	<!-- Navigation and Search Bar -->
		<header class="FillScreenTextCentered" style="background-color:lightgrey; height:auto; margin-bottom:2%;">
			<br>
			<form name="searchForm" action="searchServlet" method="post" >
				<table style="width:90%; margin-right:auto; margin-left:auto;">
					<tr style="height:50%;">
						<td><h3><a href="Home.jsp">BLOGSHARE</a></h3></td>
						<td rowspan="2" style="width:25%; font-size:24px;"> <input type=text name=navBarSearchTerm maxlength=100/></td>
						<td style="width:10%"> <input type=checkbox name=navBarBlogsCheck checked="checked"/>Blogs<p>  </td>
						<td style="width:10%"> <input type=checkbox name=navBarTitleCheck checked="checked"/>Titles<p></td>
						<td style="width:10%"> <input type=checkbox name=navBarEditableCheck />Editable<p> </td>
						<td rowspan="2" style="width:25%">  <input type=submit name=navBarSearch value="Search"/></td>
					</tr>
					<tr style="height:50%;">
						<td>Welcome <%= u.getUsername() %>!</td>
						<td style="width:10%"> <input type=checkbox name=navBarPostsCheck checked="checked"/>Posts<p> </td>
						<td style="width:10%"> <input type=checkbox name=navBarBodyCheck checked="checked"/>Content<p></td>
						<td style="width:10%"> <input type=checkbox name=navBarUsersCheck />Users<p> </td>
					</tr>
				</table>
			</form>
			<br>
		</header>
		<br>

	<h1>
		<span class="glyphicon glyphicon-user" style="fontSize: 50px"></span>
		<%=u.getUsername()%>'s Profile Page
	</h1>
	<h3>
		<span class="label label-default">Joined: <%=u.getDateRegistered()%></span>
	</h3>

	<p style="padding: 50px">
		<a href="ProfileEdit.jsp"><button type="button"
				class="btn btn-default btn-lrg" style="width: 500px">Edit
				Profile</button></a> <br style="clear: left;" />
				
	 <a href="BlogCreate.jsp"><button
				type="button" class="btn btn-default btn-lrg" style="width: 500px">Create
				Blog</button></a>
	</p>

	<!-- the dynamic list of user blogs is generated here -->
	<div class="list-group">
		<%
			//ArrayList<String> userBlogList = (ArrayList<String>) session.getAttribute("userBlogList");
		
			ArrayList<String> userBlogList = u.getUserBlogs(u.getUserId());

			if (userBlogList != null) {
				for (String blogTitle: userBlogList){
					out.print("<li");
					out.println( " class=\"blog-link list-group-item\" blogTitle=\"" + blogTitle + "\">"+ blogTitle +"</li>");
				}		
			} else {
			out.println("<li class=\"list-group-item\">No Blogs Found</li>");
			}
		%>
	</div>

	<!-- form used to request a blog by title from getblogservlet -->
	<form id="goToBlog" action="GetBlogServlet" method="post">
		<input type="hidden" id="goToBlogName" name="blogTitle" value="">
	</form>

	<!-- Attach an onclick event to list elements with class blog-link -->
	<!-- this event gets the list item's blogTitle attribute and places it into the input with id gotoblog -->
	<!-- it then submits the gotoblog form -->
	<script>
	$('li.blog-link').click(function(){
		
		$('input#goToBlogName').val($(this).attr('blogTitle'));
		$('form#goToBlog').submit();
		$(this).toggleClass('active');
	});

	</script>
</body>
</html>