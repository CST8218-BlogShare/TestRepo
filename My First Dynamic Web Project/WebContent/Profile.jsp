<%@ page language="java" 
contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="java.util.ArrayList, java.io.IOException, com.amzi.dao.User, java.util.Locale, java.util.ResourceBundle"  %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="BootstrapInclude.html" />

<%
	User u = null;
	Boolean usersProfile;
	String test =(String) session.getAttribute("currentPage");
	

	session.setAttribute("currentPage","Profile");
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
	
	u = (User) session.getAttribute("currentUser");
	usersProfile = true;
	
	/* If the user has navigated to Profile.jsp from searchResults.jsp, currentProfile will be initailized 
	   as an attempt will be made to display the profile associated with the search result.  
	   
	   If not then the profile page has been accessed by a logged in user, using the functionality of SearchBar.jsp. 
	*/
	if(session.getAttribute("currentProfile") != null){
		//If the user object inialized with the currentUser attribute is null, this means that an user has not logged in. 
		if(u == null){
			u = (User) session.getAttribute("currentProfile"); //The profile associated with the search result will be used to populate the page. 
			usersProfile = false; //Set to false to prevent display of the additional functionality related to the logged in user. 
		}else if(u != null){ //If there is a currently logged in user. 
			//If the logged in user is not the same as the user profile that is associated with the current search result.
			if(!u.getUsername().contentEquals(((User) session.getAttribute("currentProfile")).getUsername())){
				u = (User) session.getAttribute("currentProfile");
				usersProfile = false; 
			}
		}
	}
	
	ArrayList<String> userBlogList = u.getUserBlogs(u.getUserId());
%>

<title><%=u.getUsername()%> Profile Page</title>
</head>
<body>

	<div class="container">
		<jsp:include page="SearchBar.jsp"></jsp:include>
	
		<h1 class="row" style="color:white">
			<div class="col-sm-1"></div><!-- end col-sm-1-->
			<span class="glyphicon glyphicon-user" style="fontSize:50px; color:lightgrey"></span>
			<%=u.getUsername()%> <% out.println(lang.getString("profilepage")); %>
		</h1>		
		
		<div class="row">
			<div class="col-sm-1"></div><!-- end col-sm-1-->
		
			<div class="col-sm-11 FillScreen90">
				<div class="panel panel-default">
					<div class="panel-heading">
							<div class="panel-title"> <%= lang.getString("joined") %>: <%=u.getDateRegistered()%></div>
					</div>
					<% 	if(usersProfile == true) { %>
						<div class="panel-body">
							<button type="button" data-toggle="modal" data-target="#editProfileModal" class="btn btn-default btn-lrg" style="width: 100%">
								<% out.println(lang.getString("edit")); %>
							</button>
							<br style="clear: left;" /> 
							<a href="BlogCreate.jsp">
							<button type="button" class="btn btn-default btn-lrg" style="width: 100%">
								<% out.println(lang.getString("create")); %>
							</button></a>
					<% } %>
						</div>
				</div>
			</div><!-- end col-sm-11-->
		</div><!-- End row -->
		<div class="row">
			<div class="col-sm-1"></div><!-- end col-sm-1-->
				<div class="col-sm-10 FillScreen90">
					<div class="panel panel-default">
						<div class="panel-heading">
							<div class="panel-title"><%=lang.getString("blogs") %>:</div>
						</div>
						<div class="panel-body FillScreenTextCentered"> <!-- need to center this content -->
							<table style="width:100%;">
								<% if(userBlogList != null){ 
										for(String blogTitle: userBlogList){ %>
											<tr>
												<td style="width:80%">
													<div class="list-group">
														<li class="blog-link list-group-item" blogTitle="<%=blogTitle%>"> <%=blogTitle %></li>
													</div>
												</td>
											<% if(usersProfile == true){ %>
												
												<!-- creating horizontal space between the Blog Title and Edit Button -->
												<td style="width:2%;">
												  
												</td>
												
												<td style="width:18%;">
													<div class="list-group">
														<li class="list-group-item"> Edit Blog </li>
													</div>
												</td>
											<% } %>
											</tr>
								<%		}
								    } else { %> 
											<tr>
												<td>
								    				<div class="list-group">
								    					<%= lang.getString("noblog") %>
								    				</div>
								    			</td>
								    		</tr>
								<%  } %>
							</table>
						</div>
					</div>
				</div><!-- end col-sm-10-->
			<div class="col-sm-1"></div><!-- end col-sm-1-->
		</div><!-- End row -->
	</div><!-- End container fluid -->	

	<!-- form used to request a blog by title from getblogservlet -->
	<form id="goToBlog" action="GetBlogServlet" method="post">
		<input type="hidden" id="goToBlogName" name="blogTitle" value="">
	</form>
	<form id="reloadProfileForm" action="LoadProfileServlet" method="post"></form>
	
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
						<%= u.getUsername() %> <%=lang.getString("profileedit")%>
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
							<div id="alert" class="alert alert-warning hidden" role="alert" style="color:black"><b></b></div>
						</div>
				</div>
			</div>
		</div>
	</div>

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
	
	//This selects the submit button in profileedit and adds a method to its onclick event
	$('#submitChangesButton').click(function(){
		//if the passwords dont match display error, otherwise send post to edituserservlet
		if ($('#newPass').val() != $('#newPassConfirm').val()){
			$("#alert").html("<%=lang.getString("alert.notpassmatch")%>").attr('class', 'alert alert-warning');
		} else {
			//AJAX Post method - sends a post to edituserservlet with javascript instead of the browser
			$.post("/My_First_Dynamic_Web_Project/EditUserServlet", {
					loginUsername: $('#loginUsername').val(),
					newUsername: $('#newUsername').val(),
					loginPassword: $('#loginPassword').val(),
					newPass: $('#newPass').val()
				},
				function (response) {
					//edituserservlet responds with one of three basic strings: SUCCESS, WRONG_PASS, SQL_ERROR
					//load the appropriate error message into the alert and make the alert visible
					//if success then reload the profile page
					if (response == "SUCCESS"){
						$("#alert").html("<%=lang.getString("alert.success")%>").attr('class', 'alert alert-success');
						$('#reloadProfileForm').submit();
					} else if (response == "WRONG_PASS")
						$("#alert").html("<%=lang.getString("alert.wrongpass")%>").attr('class', 'alert alert-danger');
					else if (response == "SQL_ERROR")
						$("#alert").html("<%=lang.getString("alert.sqlerror")%>").attr('class', 'alert alert-danger');
			});	
		}
	});

	</script>
</body>

<% session.setAttribute("currentProfile", null); %>

</html>