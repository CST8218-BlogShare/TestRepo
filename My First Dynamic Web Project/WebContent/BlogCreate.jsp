<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" import="com.amzi.dao.User, java.util.Locale, java.util.ResourceBundle"%>
<html>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<head>
		<jsp:include page="BootstrapInclude.html" />
<%

//for generating the french/english page link
session.setAttribute("currentPage","BlogCreate");

if(getServletContext().getAttribute("errorCode") == null){
	getServletContext().setAttribute("errorCode",0);
}

if(getServletContext().getAttribute("errorMessage") == null){
	getServletContext().setAttribute("errorMessage","");	
}

//used to set keep the language consistent between pages
if(session.getAttribute("language") == null){
	session.setAttribute("language","EN");
}

ResourceBundle lang = ResourceBundle.getBundle("BlogCreate_EN");

//if the session language is FR switch to french, otherwise remains english as set above
if (session.getAttribute("language").toString().equals("FR")){
	lang = ResourceBundle.getBundle("BlogCreate_FR");
} 

//if the user clicked change language, set to appropriate language
if (request.getParameter("language") != null){	
	if (request.getParameter("language").equals("FR")){
		lang = ResourceBundle.getBundle("BlogCreate_FR");
		session.setAttribute("language","FR");
	} else {
		lang = ResourceBundle.getBundle("BlogCreate_EN");
		session.setAttribute("language","EN");
	}
}		

%>
		<title><%= lang.getString("title")%></title>
	</head>
	
	<body>
	
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
		 <!--table to hold pages content -->
		<form name="blogCreateForm" action="BlogCreateServlet" method="post">
			<table class="FillScreenTextCentered">
		
				<!--  description for user about what to do on the page -->
				<tr>
					<td>
						<div class="FillScreenTextCentered" style="margin-bottom:3%;">
							<p style="font-size:32px;">
							   	<b><%= lang.getString("createblog")%></b> <br>
							</p> 
						</div>
					</td>
				</tr>
				
				<!-- blog title -->
				<tr style="margin-bottom: 5%;">
					<td>
						<p style="font-size:20px; text-decoration:underline"> 
						<%= lang.getString("blogTitle")%>
						</p>
						<input type=text name=blogTitle maxlength=100 />
					</td>
				</tr>
		
				<!-- creating space -->
				<tr>
					<td><br></td>
				</tr>
				
				<tr>
					<td>
						<div class="FillScreenTextCentered" style="margin-bottom:2%;">
							<p style="font-size:18px">
								<%= lang.getString("instructions1")%>
								<br>
							</p>
						</div>
					<td>
				<tr>
				
		
				<!-- first post -->
				<tr>
					<td>
						<p style="font-size:20px; text-decoration:underline">
						   <%= lang.getString("postTitle")%>
						</p>
						<p style="font-size:28px"> <b> <%= lang.getString("explanation")%> </b> </p>
						<input type=hidden name=postTitle value="<%= lang.getString("explanation")%>"/>
					</td>
				</tr>
		
				<!-- creating space -->
				<tr>
					<td><br></td>
				</tr>
				
		
				<tr>
					<td>
						<p style="font-size:20px; text-decoration:underline">
							 <%= lang.getString("postBody")%><br>
						</p>
						<textarea name="postBody" WRAP=soft COLS=80 ROWS=10></textarea> <br> <br>
					</td>
				</tr>
		
				<tr>
					<td class="FillScreenTextCentered" style="font-size:18px;">
						<p>
							<%= lang.getString("instructions2")%><br>
						</p>
					</td>
				</tr>
				
				<tr>
					<td>
						<input type=checkbox name="blogEditableCheckBox"/><p style="font-size:18px;"><%= lang.getString("allow")%></p>
					</td>
				</tr>
		
				<!-- creating space -->
				<tr>
					<td><br></td>
				</tr>
				
				<tr>
					<td>
						<input type="submit" class=button style="margin-bottom:1%;" value="<%= lang.getString("createblog")%>">
					</td>
				</tr>
				
				<tr>
					<td>
						<input class="button" type="button" onClick="blogCreateClick()" value="<%= lang.getString("cancel")%>">
					</td>
				</tr>
			</table>
		</form>		
	</body>
	
	<form name="BlogCreateCancel" action="Profile.jsp" method="get"></form>
	
	<script>
		function blogCreateClick(){
			var form = document.forms["BlogCreateCancel"];
			form.submit();
		}
	</script>
	
</html>
