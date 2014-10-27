<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="java.util.ArrayList, java.io.IOException, com.amzi.dao.User"
    import="java.util.Locale, java.util.ResourceBundle"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="BootstrapInclude.html" />

<%

	session.setAttribute("currentpage","Profile");
	ResourceBundle lang = ResourceBundle.getBundle("Profile_EN");
	
	//if the session language is FR switch to french, otherwise remains english as set above
	if (session.getAttribute("language").toString().equals("FR")){
		lang = ResourceBundle.getBundle("Profile_FR");
	} 
	
	//if the user clicked change language, set to appropriate language
	if (request.getParameter("language") != null){	
		if (request.getParameter("language").equals("FR")){
			lang = ResourceBundle.getBundle("Profile_FR");
			session.setAttribute("language","FR");
		} else {
			lang = ResourceBundle.getBundle("Profile_EN");
			session.setAttribute("language","EN");
		}
	}		

	User u =  (User) session.getAttribute("currentUser");
%>


<title><%=u.getUsername()%>'s Profile Page</title>
</head>
<body>

	<jsp:include page="SearchBar.jsp"></jsp:include>

	<h1>
		<span class="glyphicon glyphicon-user" style="fontSize: 50px"></span>
		<%=u.getUsername()%>'s <% out.println(lang.getString("profilepage")); %>
	</h1>
	<h3>
		<span class="label label-default"><% out.println(lang.getString("joined")); %>: <%=u.getDateRegistered()%></span>
	</h3>

	<p style="padding: 50px">
		<a href="ProfileEdit.jsp"><button type="button"
				class="btn btn-default btn-lrg" style="width: 500px"><% out.println(lang.getString("edit")); %>
				</button></a> <br style="clear: left;" />
				
	 <a href="BlogCreate.jsp"><button
				type="button" class="btn btn-default btn-lrg" style="width: 500px"><% out.println(lang.getString("create")); %>
				</button></a>
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