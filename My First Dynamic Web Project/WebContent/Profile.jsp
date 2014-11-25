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
			if(!u.getUsername().equals(((User) session.getAttribute("currentProfile")).getUsername())){
				u = (User) session.getAttribute("currentProfile");
				usersProfile = false; 
			}
		}
	}
	
	ArrayList<String> userBlogList = u.getUserBlogs(u.getUserId());
%>

<%  
	if( request.getAttribute("errorMessage") != null)
	{ %>
		<div class="container">
			<div class="alert alert-danger alert-dismissible" role="alert">
				<button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<%= request.getAttribute("errorMessage") %>
			</div>
		</div>
	<%	}	%>


<title><%=u.getUsername()%> Profile Page</title>
</head>
<body>

	<jsp:include page="SearchBar.jsp"></jsp:include>
	<div class="container">
	
		<h1 class="row" style="color:lightblue; margin-left:1%">
			<!--  <div class="col-sm-1"></div><!-- end col-sm-1-->
			<%=u.getUsername()%> <% out.println(lang.getString("profilepage")); %>
		</h1>		
		
		<!-- making space -->
		<br>
		
		<div class="row">
			<!--  <div class="col-sm-1"></div><!-- end col-sm-1-->
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
			<!--  <div class="col-sm-1"></div><!-- end col-sm-1-->
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
												<td style="width:60%">
													<div class="list-group">
														<li class="blog-link list-group-item" blogTitle="<%=blogTitle%>"> <%=blogTitle %></li>
													</div>
												</td>
											<% if(usersProfile == true){ %>
												
												<!-- creating horizontal space between the Blog Title and Edit buttons -->
												<td style="width:2%;">
												  
												</td>
												
												<td style="width:18%;">
													<div class="list-group">
														<li class="blogedit-link list-group-item" blogTitle="<%=blogTitle%>"><%=lang.getString("editbtn") %></li>
													</div>
												</td>
												
												<!-- creating horizontal space between the Edit and Delete buttons -->
												<td style="width:2%;">
												  
												</td>
												
												<td style="width:18%;">
													<div class="list-group">
														<li class="blogdelete-link list-group-item" blogTitle="<%=blogTitle%>"><%=lang.getString("deletebtn") %></li>
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
			<!--<div class="col-sm-1"></div><!-- end col-sm-1-->
		</div><!-- End row -->
	</div><!-- End container fluid -->	

	<!-- Form used to call GetBlogServlet in order to initialize a Blog object based on title and then load Blog.jsp -->
	<form id="goToBlog" action="getBlogServlet" method="post">
		<input type="hidden" id="goToBlogName" name="blogTitle" value="">
	</form>
	
	
	<form id="goToEditBlog" action="getBlogServlet" method="post">
		<input type="hidden" id="goToEditBlogName" name="blogTitle" value="">
		<!-- used in GetBlogServlet to determine in the page being loaded is Blog or BlogEdit -->
		<input type="hidden" id="goToEditBlogIsEdit" name="isBlogEdit" value="true">
	</form>
	
	<form id="deleteBlog" action="blogDeleteServlet" method="post">
		<input type="hidden" id="blogToDeleteName" name="blogTitle" value="">
	</form>
	
	<form id="reloadProfileForm" action="profileReloadAfterEditServlet" method="post">
		<input type="hidden" id="ProfileReloadUserId" name="userId" value="<%=u.getUserId()%>">
	</form>
	
	<!-- profileedit is shown in this modal window -->
	<div class="modal fade" id="editProfileModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" style="background-color:blue">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h3 class="modal-title" id="myModalLabel" style="color:lightblue;">
						<!-- <span class="glyphicon glyphicon-user" style="fontSize:50px"></span> -->
						<%=lang.getString("profileedit")%>
					</h3>
				</div>
				<div class="modal-body">
					<h3><span class="label label-default"><%=lang.getString("joined")%>: <%= u.getDateRegistered() %></span></h3>
						<div style="padding:30px">	
							<div class="input-group">
							  	<span class="input-group-addon" style="min-width:<%=lang.getString("mintextfieldwidth") %>"><%=lang.getString("username")%>:</span>
							 	<input id="loginUsername" name="loginUsername" type="text" class="form-control" readonly value="<%= u.getUsername() %>" >
							</div>
							<div class="input-group">
							  	<span class="input-group-addon" style="min-width:<%=lang.getString("mintextfieldwidth") %>"><%=lang.getString("newusername")%>:</span>
							 	<input id="newUsername" name="newUsername" type="text" class="form-control">
							</div>
							<div class="input-group">
							  	<span class="input-group-addon" style="min-width:<%=lang.getString("mintextfieldwidth") %>"><%=lang.getString("password")%>:</span>
							 	<input id="loginPassword" name="loginPassword" type="password" class="form-control" >
							</div>
							<div class="input-group">
							  	<span class="input-group-addon" style="min-width:<%=lang.getString("mintextfieldwidth") %>"><%=lang.getString("newpassword")%>:</span>
							 	<input id="newPass" name="newPass" type="password" class="form-control" >
							</div>
							<div class="input-group">
							  	<span class="input-group-addon" style="min-width:<%=lang.getString("mintextfieldwidth") %>"><%=lang.getString("reenter")%>: </span>
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
	
	//$('#test').css('border', '2px solid blue');
	
	//Attach an onclick event to list elements with class blog-link: turns the dynamic blog list into links
	//this event gets the list item's blogTitle attribute and places it into the input with id gotoblog
	//it then submits the gotoblog form
	$('li.blog-link').click(function(){
		
		$('input#goToBlogName').val($(this).attr('blogTitle'));
		$('form#goToBlog').submit();
		$(this).toggleClass('active');
	});
	
	$('li.blogedit-link').click(function(){
		$('input#goToEditBlogName').val($(this).attr('blogTitle'));
		$('form#goToEditBlog').submit();
		$(this).toggleClass('active');
	});
	
	$('li.blogdelete-link').click(function(){
		$('input#blogToDeleteName').val($(this).attr('blogTitle'));
		$('form#deleteBlog').submit();
		$(this).toggleClass('active');
	});
	
	//This selects the submit button in profileedit and adds a method to its onclick event
	$('#submitChangesButton').click(function(){
		//if the passwords dont match display error, otherwise send post to edituserservlet
		if ($('#newPass').val() != $('#newPassConfirm').val()){
			$("#alert").html("<%=lang.getString("alert.notpassmatch")%>").attr('class', 'alert alert-warning');
		} else {
			//AJAX Post method - sends a post to edituserservlet with javascript instead of the browser
			$.post("/My_First_Dynamic_Web_Project/editUserServlet", {
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
					}else if (response == "BLANK_NAME"){
						$("#alert").html("<%=lang.getString("alert.blankname")%>").attr('class', 'alert alert-danger');
					}else if (response == "BLANK_PASS"){
						$("#alert").html("<%=lang.getString("alert.blankpass")%>").attr('class', 'alert alert-danger');
					}else if (response == "WRONG_PASS"){
						$("#alert").html("<%=lang.getString("alert.wrongpass")%>").attr('class', 'alert alert-danger');
					}else if (response == "CONNECT_ERROR"){
						$("#alert").html("<%=lang.getString("alert.connecterror")%>").attr('class', 'alert alert-danger');
					}else if (response == "SQL_ERROR"){
						$("#alert").html("<%=lang.getString("alert.sqlerror")%>").attr('class', 'alert alert-danger');
					}
			});	
		}
	});

	</script>
</body>

<!-- Removing the value for current Profile, since it should only be viewed once by the current user. -->
<% session.setAttribute("currentProfile", null); %>

</html>