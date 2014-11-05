<%@ page language="java"  
    import="com.amzi.dao.User, java.util.Locale, java.util.ResourceBundle"%>  
<!DOCTYPE html>
<html>
<!-- The home page of BLOGSHARE, the user is brought here when the site is accessed. -->
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="Styles/LookAndFeel.css">
<title>BlogShare - Home</title>


<!-- initialization variables needed for page -->
<%

//for generating the french/english page link
session.setAttribute("currentPage","Home");

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

ResourceBundle lang = ResourceBundle.getBundle("Home_EN");

//if the session language is FR switch to french, otherwise remains english as set above
if (session.getAttribute("language").toString().equals("FR")){
	lang = ResourceBundle.getBundle("Home_FR");
} 

//if the user clicked change language, set to appropriate language
if (request.getParameter("language") != null){	
	if (request.getParameter("language").equals("FR")){
		lang = ResourceBundle.getBundle("Home_FR");
		session.setAttribute("language","FR");
	} else {
		lang = ResourceBundle.getBundle("Home_EN");
		session.setAttribute("language","EN");
	}
}		


%>

</head>
<body>

	<jsp:include page="SearchBar.jsp"></jsp:include>
		
		<!-- "BlogShare" banner -->
		<div class="FillScreenTextCentered" style="margin-bottom:2%;"> 
			<p style="font-size:48px;">BLOGSHARE </p>
		</div>

		
<!--  A brief explanation of the user's options -->
		<div class="FillScreenTextCentered">
			<p style="font-size:18px;">
				<% out.println(lang.getString("content.1")); %>
			</p> 
		</div>
				
		<br>
		<br>
	
		<!-- If the user is logged in and has navigated back to the home page, display a greeting message that directs them back to their profile -->
		
		<% if(session.getAttribute("currentUser") != null){%>
			
			<div class="FillScreenTextCentered" style="color:LightBlue;">
				<p style="font-size:18px;"> 	<%= lang.getString("content.4")%>, <%= ((User) session.getAttribute("currentUser")).getUsername() %>.<br>
				<%= lang.getString("content.5")%></p>
			</div>
			
			
		<% } %>
	
		<!-- if the user is not logged in, display the login and registration tables on the page  -->
		
		<% if(session.getAttribute("currentUser") == null){ %>
	
		<div class="FillScreenTextCentered">
			<table style="width:80%;  margin-left:5%; margin-right:5%;">
				<tr>
					<td>
						<p style="font-size:17px;"> <% out.println(lang.getString("content.2")); %> </p>
					</td>
					<td>
						<p style="font-size:17px;"> <% out.println(lang.getString("content.3")); %>	</p>
					</td>	
				</tr>
			</table>
		</div>
		
		<!-- table containing form for user registration and login-->
		<div>														   
			<table class="centered80W">
				<tr>
				<!--Registration Form -->
					<td style="width:40%;">
						<form name="RegisterForm" action="registerServlet" method="post">
							<table>
								<tr> 
										 <td colspan=2> <p style="font-size:22px;"><b><% out.println(lang.getString("register")); %></b> </p> </td> 
								</tr>
								<tr>
										 <td class="HomeInputTitle"><p><% out.println(lang.getString("username")); %></p></td> <td  class="HomeInputContent"><input type=text name=registerUsername value="" maxlength=100/> </td> 
								</tr>
								<tr>
										 <td class="HomeInputTitle"><p><% out.println(lang.getString("password")); %></p></td> <td class="HomeInputContent"> <input type=password name=registerUserPass  maxlength=100/></td>
								</tr>
								<tr>
										<td class="HomeInputTitle"><p><% out.println(lang.getString("reenter")); %></p></td> <td class="HomeInputContent"> <input type=password name=registerReenterPass  maxlength=100/> </td> 
								</tr>
								<tr>
										 <td colspan=2><input class=button type=submit value="<%= lang.getString("register") %>"/></td>     
								</tr>
							</table>
						</form>
					</td>
						
					<!--  Login Form -->
					<td  style="width:40%;">
						<form name="LoginForm" action="loginServlet" method="post">
							<table>
								<tr>
									<td colspan=2> <p style="font-size:24px;"> <b><% out.println(lang.getString("login")); %></b> </p> </td> 
								</tr>
							    <tr>
									<td class="HomeInputTitle"><p><% out.println(lang.getString("username")); %></p></td> <td><input type=text name=loginUsername value="" maxlength=100/> </td> 
								</tr>
								<tr>
									<td class="HomeInputTitle"><p><% out.println(lang.getString("password")); %></p></td> <td class="HomeInputContent"> <input type=password name=loginUserpass  maxlength=100/>   </td> 
								</tr>
								<tr>
									<td colspan=2> <br><br> </td> <!-- empty column to fill in table -->  
								</tr>
								<tr>
									<td colspan=2><input class=button type=submit value="<%= lang.getString("login") %>"/></td>   
								</tr>
							</table>
						</form>
					</td>
				</tr>	
			</table>
		</div>
	
		<% } %>
		<br>
		<br>
		<!--Output area for error messages related to registration and login -->
		<div>
			<p class="ErrorMessageOutput" id="errorOutput">
				<br>
				
				<%  
					//Clearing the previous error
					if( (int)getServletContext().getAttribute("errorCode") == 0)
					{
						getServletContext().setAttribute("errorMessage", "");
					}
				%>
				
				<%= getServletContext().getAttribute("errorMessage") %>
				<br>
				<br>
			</p>
		</div>
	</body>
</html>