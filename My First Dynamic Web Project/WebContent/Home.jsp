<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  
    pageEncoding="ISO-8859-1"%>  
<html>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> 
<head>
<link rel="stylesheet" href="Styles/LookAndFeel.css">
<title>BlogShare - Home</title>


<!-- initialization variables needed for page -->
<%

if(getServletContext().getAttribute("errorCode") == null){
	getServletContext().setAttribute("errorCode",0);
}

if(getServletContext().getAttribute("errorMessage") == null){
	getServletContext().setAttribute("errorMessage","");	
}

if(session.getAttribute("username") == null){
	session.setAttribute("username","");
}

%>

</head>

	<body>

		<!-- navigation bar -->
		<div class="FillScreenTextCentered" style="background-color:lightgrey; height:auto; margin-bottom:2%;">
			<br>
			<table style="width:90%; margin-right:auto; margin-left:auto;">
				<tr style="height:50%;">
					<td><h3>BLOGSHARE</h3></td>
					<td rowspan="2" style="width:25%; font-size:24px;"> <input type=text name=navBarSearchTerm maxlength=100/></td>
					<td style="width:10%"> <input type=checkbox name=navBarBlogsCheck checked="checked"/>Blogs<p>  </td>
					<td style="width:10%"> <input type=checkbox name=navBarTitlesCheck checked="checked"/>Titles<p></td>
					<td style="width:10%"> <input type=checkbox name=navBarUsersCheck checked="checked"/>Users<p> </td>
					<td rowspan="2" style="width:25%">  <input type=button name=navBarSearch maxlength=100 value="Search"/></td>
				</tr>
				<tr style="height:50%;">
					<td> <a href="Profile.jsp">Welcome <%= session.getAttribute("username") %>!</a></td>
					<td style="width:10%"> <input type=checkbox name=navBarPostCheck checked="checked"/>Posts<p> </td>
					<td style="width:10%"> <input type=checkbox name=navBarContentCheck checked="checked"/>Content<p></td>
					<td style="width:10%"> <input type=checkbox name=navBarAuthorsCheck checked="checked"/>Authors<p> </td>
				</tr>
			</table>
			<br>
		</div>
	
		<!-- "BlogShare" banner -->
		<div class="FillScreenTextCentered" style="margin-bottom:2%;"> 
			<font size=7>BLOGSHARE </font>
		</div>

		<!--  A brief explanation of the user's options -->
		<div class="FillScreenTextCentered">
			<font size=4>
				 If this is your first time visiting the site. <br>
				 Feel free to use the search tools above to explore our public content. <br>
			</font> 
				<br>
				<br>
				<table style="width:80%;  margin-left:5%; margin-right:5%;">
					<tr>
						<td>
							<font size=4>
								If you wish to create your own Blogs. <br>
								Please fill out the registration form below.
							</font>
						</td>
						<td>
							<font size=4>
								If you have previously registered <br>
								Please login below.
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
										 <td colspan=2> <font size=6 > <b> Register </b> </font> </td> 
								</tr>
								<tr>
										 <td class="HomeInputTitle"><font>Username</font></td> <td  class="HomeInputContent"><input type=text name=registerUsername value="" maxlength=100/> </td> 
								</tr>
								<tr>
										 <td class="HomeInputTitle"><font>Password</font></td> <td class="HomeInputContent"> <input type=password name=registerUserPass  maxlength=100/></td>
								</tr>
								<tr>
										<td class="HomeInputTitle"><font>Reenter Password</font></td> <td class="HomeInputContent"> <input type=password name=registerReenterPass  maxlength=100/> </td> 
								</tr>
								<tr>
										 <td colspan=2><input class=button type=submit value="Register"/></td>     
								</tr>
							</table>
						</form>
					</td>
						
					<!--  Login Form -->
					<td  style="width:40%;">
						<form name="LoginForm" action="loginServlet" method="post">
							<table>
								<tr>
									<td colspan=2> <font size=6> <b>Login</b> </font> </td> 
								</tr>
							    <tr>
									<td class="HomeInputTitle"><font>Username</font></td> <td><input type=text name=loginUsername value="" maxlength=100/> </td> 
								</tr>
								<tr>
									<td class="HomeInputTitle"><font>Password</font></td> <td class="HomeInputContent"> <input type=password name=loginUserpass  maxlength=100/>   </td> 
								</tr>
								<tr>
									<td colspan=2> <br><br> </td> <!-- empty column to fill in table -->  
								</tr>
								<tr>
									<td colspan=2><input class=button type=submit value="Login"/></td>   
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