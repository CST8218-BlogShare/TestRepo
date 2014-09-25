<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  
    pageEncoding="ISO-8859-1"%>  
<html>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> 
<head>
<link rel="stylesheet" href="LookAndFeel.css">
<title>BlogShare - Home</title>
</head>

	<body>

		<!-- navigation bar -->
		<div class="FillScreenTextCentered" style="background-color:lightgrey; height:auto; margin-bottom:2%;">
			<br>
			<table style="width:90%; margin-right:auto; margin-left:auto;">
				<tr style="height:50%;">
					<td><p><h3>BLOGSHARE</h3></td>
					<td rowspan="2" style="width:25%; font-size:24px;"> <input type=text name=navBarSearchTerm maxlength=100/></td>
					<td style="width:10%"> <input type=checkbox name=navBarBlogCheck  maxlength=100/>Blogs<p>  </td>
					<td style="width:10%"> <input type=checkbox name=navBarTitleCheck  maxlength=100/>Titles<p> </td>
					<td style="width:10%"> <input type=checkbox name=navBarReadCheck  maxlength=100/>Read<p></td>
					<td rowspan="2" style="width:25%">  <input type=button name=navBarSearch maxlength=100 value="Search"/></td>
				</tr>
				<tr style="height:50%;">
					<td>Welcome!</td>
					<td style="width:13%"> <input type=checkbox name=navBarPostCheck  maxlength=100/>Posts<p> </td>
					<td style="width:13%"> <input type=checkbox name=navBarContentCheck  maxlength=100/>Content<p> </td>
					<td style="width:13%"> <input type=checkbox name=navBarTitleCheck  maxlength=100/>Edit<p> </td>
				</tr>
			</table>
			<br>
		</div>
	
		<!-- "BlogShare" banner -->
		<div class="FillScreenTextCentered" style="margin-bottom:2%;"> 
			<font size=7>BLOGSHARE </font>
		</div>
	
		<!-- table containing form for user registration and login-->
		<div style="margin-bottom:5%;">
			<table style="width:80%;  margin-left:10%; marin-right:10%;">
				<tr>
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
						
					<td  style="width:40%;">
						<form name="LoginForm" action="loginServlet" method="post">
							<table>
								<tr>
									<td colspan=2> <font size=6> <b> Login </b> </font> </td> 
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
	
		<!--  div for output of error messages related to registration and login -->
		<div>
			<p class="ErrorMessageOutput" id="errorOutput">
				<br>
				Error messages go here<br>
				<br>
			</p>
		</div>	
	</body>
</html>