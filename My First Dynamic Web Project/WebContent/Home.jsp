<%@ page language="java"  
    import="com.amzi.dao.User"%>  

<!DOCTYPE html>
<html>
<!-- The home page of BLOGSHARE, the user is brought here when the site is accessed. -->
<head>
<meta charset="UTF-8">
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

%>

</head>
<body>

	<script>
	
		function BlogClicked(elementClicked){
			var postCheckBox = document.getElementById("navBarPostsCheck");
			var titleCheckBox = document.getElementById("navBarTitleCheck");
			var bodyCheckBox = document.getElementById("navBarBodyCheck");
			var editCheckBox = document.getElementById("navBarEditableCheck");
			
			//if post is not activated, else do nothing.
			if(postCheckBox.checked == false){
				
				//if blog is being activated
				if(elementClicked.checked == true){
					//enable and check title checkbox
					titleCheckBox.checked=true;
					titleCheckBox.disabled=false;
					
					//uncheck and disable body checkbox
					bodyCheckBox.checked=false;
					bodyCheckBox.disabled=true;
					
					//enabling editable checkbox
					editCheckBox.disabled = false;
				
				//if blog is being deactivated
				}else{
					//uncheck and disable title Checkbox
					titleCheckBox.checked = false;
					titleCheckBox.disabled = true;
					
					//uncheck and disable editable checkbox
					editCheckBox.checked = false;
					editCheckBox.disabled = true;
				}
			}
		}
	
		//title and content only available if post is clicked.
		function PostClicked(elementClicked){	
			var blogCheckBox = document.getElementById("navBarBlogsCheck");
			var titleCheckBox = document.getElementById("navBarTitleCheck");
			var bodyCheckBox = document.getElementById("navBarBodyCheck");
			var editCheckBox = document.getElementById("navBarEditableCheck");
			
			//if activating post
			if(elementClicked.checked == true){

				//check and enable title checkbox
				titleCheckBox.checked = true;
				titleCheckBox.disabled = false;
				
				//check and enable body checkbox
				bodyCheckBox.checked = true;
				bodyCheckBox.disabled = false;
				
				//enable editable checkbox
				editCheckBox.disabled = false;

			//if deactivating post
			}else{
				//uncheck body checkbox but leave enabled
				bodyCheckBox.checked = false;
				bodyCheckBox.disabled = true;
				
				//if blog isn't currently enabled
				if(blogCheckBox.checked == false){
					
					//uncheck and disable title checkbox
					titleCheckBox.checked = false;
					titleCheckBox.disabled = true;
					
					//uncheck and disable editable checkbox
					editCheckBox.checked = false;
					editCheckBox.disabled = true;
						
				}
			}
		}
	</script>

		<!-- Navigation and Search Bar -->
		<header class="FillScreenTextCentered" style="background-color:lightgrey; height:auto; margin-bottom:2%;">
			<br>
			<form name="searchForm" action="searchServlet" method="post" >
				<table style="width:90%; margin-right:auto; margin-left:auto;">
					<tr style="height:50%;">
						<td><h3>BLOGSHARE</h3></td>
						<td rowspan="2" style="width:25%; font-size:24px;"> <input type=text name=navBarSearchTerm maxlength=100/></td>
						<td style="width:10%"> <input type=checkbox id="navBarBlogsCheck" name="navBarBlogsCheck" checked="checked" OnClick="BlogClicked(this)"/>Blogs<p>  </td>
						<td style="width:10%"> <input type=checkbox id="navBarTitleCheck" name="navBarTitleCheck" checked="checked"/>Titles<p></td>
						<td style="width:10%"> <input type=checkbox id="navBarEditableCheck" name="navBarEditableCheck" />Editable<p> </td>
						<td rowspan="2" style="width:25%">  <input type=submit name=navBarSearch value="Search"/></td>
					</tr> 
					<tr style="height:50%;">
						<% if(session.getAttribute("currentUser") == null){ %>
							<td> Welcome!</td>
						<% }else{ %>
							<td> <a href="Profile.jsp">Welcome <%= ((User)session.getAttribute("currentUser")).getUsername() %>!</a></td>
						<% } %>
						<td style="width:10%"> <input type=checkbox id="navBarPostsCheck" name="navBarPostsCheck" OnClick="PostClicked(this)" checked="checked"/>Posts<p> </td>
						<td style="width:10%"> <input type=checkbox id="navBarBodyCheck" name="navBarBodyCheck" checked="checked"/>Body<p></td>
						<td style="width:10%"> <input type=checkbox id="navBarUsersCheck" name="navBarUsersCheck"/>Users<p> </td>
					</tr>
				</table>
			</form>
			<br>
		</header>
	
		<!-- "BlogShare" banner -->
		<div class="FillScreenTextCentered" style="margin-bottom:2%;"> 
			<font size=7>BLOGSHARE </font>
		</div>

		<!--  A brief explanation of the user's options -->
		<div class="FillScreenTextCentered">
			<font size=4>
				 If this is your first time visiting the site. <br>
				 Feel free to use the search tools above to explore our content. <br>
				 <br>
				 By default content is only available to be read by the public, <br>
				 but certain authors' may have enabled public editing of their work.<br>
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