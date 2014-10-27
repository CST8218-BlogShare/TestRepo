<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  
    pageEncoding="ISO-8859-1"
    import="java.util.Locale, java.util.ResourceBundle"
    %>  
<html>

<!-- The home page of BLOGSHARE, the user is brought here when the site is accessed. -->

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> 
<head>
<link rel="stylesheet" href="Styles/LookAndFeel.css">
<title>BlogShare - Home</title>


<!-- initialization variables needed for page -->
<%

//for generating the french/english page link
session.setAttribute("currentpage","Home");

if(getServletContext().getAttribute("errorCode") == null){
	getServletContext().setAttribute("errorCode",0);
}

if(getServletContext().getAttribute("errorMessage") == null){
	getServletContext().setAttribute("errorMessage","");	
}

// the username is used within the navigation bar of the website. 
if(session.getAttribute("username") == null){
	session.setAttribute("username","");
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
			<font size=7>BLOGSHARE </font>
		</div>

		<!--  A brief explanation of the user's options -->
		<div class="FillScreenTextCentered">
			<font size=4>
				<% out.println(lang.getString("content.1")); %>
			</font> 
				<br>
				<br>
				<table style="width:80%;  margin-left:5%; margin-right:5%;">
					<tr>
						<td>
							<font size=4>
								<% out.println(lang.getString("content.2")); %>
							</font>
						</td>
						<td>
							<font size=4>
								<% out.println(lang.getString("content.3")); %>
							</font>
						</td>	
					</tr>
				</table>
		</div>
	
		
		<!-- table containing form for user registration and login-->
		<div>														   
			<table class="centered80W" style="border-spacing:0.5in 0.2in; /* 1 in = 1 inch = 2.54 cm  */  border-collapse: separate;">
				<tr>
				<!--Registration Form -->
					<td style="width:40%;">
						<form name="RegisterForm" action="registerServlet" method="post">
							<table>
								<tr> 
										 <td colspan=2> <font size=6 > <b> <% out.println(lang.getString("register")); %> </b> </font> </td> 
								</tr>
								<tr>
										 <td class="HomeInputTitle"><font><% out.println(lang.getString("username")); %></font></td> <td  class="HomeInputContent"><input type=text name=registerUsername value="" maxlength=100/> </td> 
								</tr>
								<tr>
										 <td class="HomeInputTitle"><font><% out.println(lang.getString("password")); %></font></td> <td class="HomeInputContent"> <input type=password name=registerUserPass  maxlength=100/></td>
								</tr>
								<tr>
										<td class="HomeInputTitle"><font><% out.println(lang.getString("reenter")); %></font></td> <td class="HomeInputContent"> <input type=password name=registerReenterPass  maxlength=100/> </td> 
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
									<td colspan=2> <font size=6> <b><% out.println(lang.getString("login")); %></b> </font> </td> 
								</tr>
							    <tr>
									<td class="HomeInputTitle"><font><% out.println(lang.getString("username")); %></font></td> <td><input type=text name=loginUsername value="" maxlength=100/> </td> 
								</tr>
								<tr>
									<td class="HomeInputTitle"><font><% out.println(lang.getString("password")); %></font></td> <td class="HomeInputContent"> <input type=password name=loginUserpass  maxlength=100/>   </td> 
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