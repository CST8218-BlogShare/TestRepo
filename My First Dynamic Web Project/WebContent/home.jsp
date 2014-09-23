<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  
    pageEncoding="ISO-8859-1"%>  
<html>
<head>
<link rel="stylesheet" href="LookAndFeel.css">
<title>BlogShare - Home</title>
</head>

	<body>

		<!-- navigation bar -->
		<div class="FillScreenTextCentered" style="background-color:lightgrey; margin-bottom:2%;">
			<br>
			Navigation bar goes here<br>
			<br>
		</div>
	
		<!-- "BlogShare" banner -->
		<div class="FillScreenTextCentered" style="margin-bottom:2%;"> 
			<font size=7>BLOGSHARE </font>
		</div>
	
		<!-- table containing form for user registration and login-->
		<div style="margin-bottom:5%;">
			<form action="loginServlet" method="post">
				<table cellpadding=3 cellspacing=1 bordercolor=lightgrey >
			    	<tr> 
			        	<td colspan=2> <font size=6 > <b> Register </b> </font> </td> 
			        	<td colspan=2> <font size=6> <b> Login </b> </font> </td>       
			    	</tr>
					<tr> 
						 <td><font>Username</font></td> <td><input type=text name=registerUsername maxlength=100/> </td> 
				     	 <td><font>Username</font></td> <td><input type=text name=loginUsername maxlength=100/> </td> 
					</tr>
					<tr> 
						<td class="HomeInputTitle"><font>Password</font></td> <td class="HomeInputContent"> <input type=password name=registerPass  maxlength=100/></td>
				     	<td class="HomeInputTitle"><font>Password</font></td> <td class="HomeInputContent"> <input type=password name=loginUserpass  maxlength=100/>   </td> 
					</tr>
					<tr> 
						<td class="HomeInputTitle"><font>Reenter Password</font></td> <td class="HomeInputContent"> <input type=password name=registerReenterPass  maxlength=100/> </td> 
					 	<td colspan=2></td> <!-- empty column to fill in table -->         
					</tr>
					<tr> 
						<td colspan=2><input class=button type=button value="Register"/></td>         
				     	<td colspan=2><input class=button type=submit value="Login" /></td>    
			    	</tr>
				</table>
			</form>
		</div>
	
		<!--  div for output of error messages related to registration and login -->
		<div>
			<p class="ErrorMessageOutput" id="errorOutput">
				<br>
				Error messages go here.<br>
				<br>
			</p>
		</div>
	
	</body>
</html>