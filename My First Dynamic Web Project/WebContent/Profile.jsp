<%@ page language="java" 
contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="java.util.ArrayList, java.io.IOException, com.amzi.dao.User,java.util.Locale, java.util.ResourceBundle"  %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
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
		<button type="button" data-toggle="modal" data-target="#editProfileModal" class="btn btn-default btn-lrg" style="width: 500px">
			<% out.println(lang.getString("edit")); %>
		</button>
		<br style="clear: left;" /> 
		<a href="BlogCreate.jsp">
		<button type="button" class="btn btn-default btn-lrg" style="width: 500px">
				<% out.println(lang.getString("create")); %>
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

	<!-- profileedit is shown in this modal window -->
	<div class="modal fade" id="editProfileModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" style="background-color:blue">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h3 class="modal-title" id="myModalLabel">
						<span class="glyphicon glyphicon-user" style="fontSize:50px"></span> 
						<%= u.getUsername() %>'s <%=lang.getString("profileedit")%>
					</h3>
				</div>
				<div class="modal-body">
					<h3><span class="label label-default"><%=lang.getString("joined")%>: <%= u.getDateRegistered() %></span></h3>
						<div style="padding:30px">	
							<div class="input-group">
							  	<span class="input-group-addon" style="min-width:200px"><%=lang.getString("username")%>:</span>
							 	<input id="loginUsername" name="loginUsername" type="text" class="form-control" readonly="true" value="<%= u.getUsername() %>" >
							</div>
							<div class="input-group">
							  	<span class="input-group-addon" style="min-width:200px"><%=lang.getString("newusername")%>:</span>
							 	<input id="newUsername" name="newUsername" type="text" class="form-control">
							</div>
							<div class="input-group">
							  	<span class="input-group-addon" style="min-width:200px"><%=lang.getString("password")%>:</span>
							 	<input id="loginPassword" name="loginPassword" type="password" class="form-control" >
							</div>
							<div class="input-group">
							  	<span class="input-group-addon" style="min-width:200px"><%=lang.getString("newpassword")%>:</span>
							 	<input id="newPass" name="newPass" type="password" class="form-control" >
							</div>
							<div class="input-group">
							  	<span class="input-group-addon" style="min-width:200px"><%=lang.getString("reenter")%>: </span>
							 	<input id="newPassConfirm" name="newPassConfirm" type="password" class="form-control" >
							</div>
							<div class="btn-group" style="padding:30px">
								 <button id="submitChangesButton" class="btn btn-default btn-lrg" type="button"><%=lang.getString("submit")%></button>
								 <button type="button" data-dismiss="modal" class="btn btn-default btn-lrg"><%=lang.getString("close")%></button>
							</div>							
							<div id="alert" class="alert alert-warning" role="alert" style="color:black"><b>ALAAAAARM!</b></div>
						</div>
				</div>
			</div>
		</div>
	</div>

	<!-- form used to request a blog by title from getblogservlet -->
	<form id="goToBlog" action="GetBlogServlet" method="post">
		<input type="hidden" id="goToBlogName" name="blogTitle" value="">
	</form>
	<form id="reloadProfileForm" action="LoadProfileServlet" method="post"></form>

	<!-- this is all the javascript which controls profile.jsp -->
	<script>
	
	//Attach an onclick event to list elements with class blog-link: turns the dynamic blog list into links
	//this event gets the list item's blogTitle attribute and places it into the input with id gotoblog
	//it then submits the gotoblog form
	$('li.blog-link').click(function(){
		
		$('input#goToBlogName').val($(this).attr('blogTitle'));
		$('form#goToBlog').submit();
		$(this).toggleClass('active');
	});
	
	$('#submitChangesButton').click(function(){
		
		$.post("/My_First_Dynamic_Web_Project/EditUserServlet", {
				loginUsername: $('#loginUsername').val(),
				newUsername: $('#newUsername').val(),
				loginPassword: $('#loginPassword').val(),
				newPass: $('#newPass').val()
			},
			function ( response) {
				$("#alert").html(response);
				
				if (response == "SUCCESS"){
					$("#alert").html("<%=lang.getString("alert.success")%>").attr('class', 'alert alert-success');
					$('#reloadProfileForm').submit();
				} else if (response == "WRONG_PASS")
					$("#alert").html("<%=lang.getString("alert.wrongpass")%>").attr('class', 'alert alert-danger');
				else if (response == "SQL_ERROR")
					$("#alert").html("<%=lang.getString("alert.sqlerror")%>").attr('class', 'alert alert-danger');
				

		});
		
		//$('#editPassForm').submit();
		
	});

	
	</script>
</body>
</html>