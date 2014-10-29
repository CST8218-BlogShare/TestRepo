<%@ page language="java" 
	import = "com.amzi.dao.User"%>  

<!-- Displays a custom error message when a http error occurs  -->
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="Styles/LookAndFeel.css">
<title>BlogShare - Blog</title>

</head>
	<body>
	
		<!-- Allows the page to identify itself as an error page for handling errors produced by TomCat -->
		<%@ page isErrorPage="true" %>
		
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
		
		
		<div  class="FillScreenTextCentered" style="height:60%;  margin-top:auto; marign-bottom:auto; background-color:lightgrey;">
		
		<!--creating space -->
		
		<br>
		<br>
		
		<!--
		 	
		 	 Gathering and displaying the information on the HTTP error
		 	 
		 	 Adapted implementation from information and code sample found at 
		     http://wiki.metawerx.net/wiki/CustomErrorPagesInTomcat  -->	
		
		<%
		    boolean handled = false; // Set to true after handling the error
		    String errorDescription = "";
		    
		    // Get the PageContext
		    if(pageContext != null) {
		    
		        // Get the ErrorData
		        ErrorData ed = null;
		        try {
		            ed = pageContext.getErrorData();
		        } catch(NullPointerException ne) {
		            // If the error page was accessed directly, a NullPointerException
		            // is thrown at (PageContext.java:514).
		            // Catch and ignore it... it effectively means we can't use the ErrorData
		        }
		
		        // Display error details for the user
		        if(ed != null) {
		            out.println("<p /> An error has occurred <br /><br />");
		            
		            switch(ed.getStatusCode()){
		            	case 400:
		            		errorDescription="Bad Request Error";
		            		break;
		          		case 404:
		            		errorDescription="Page not found";
		            		break;
		          		case 405:
		          			errorDescription="Method not allowed";
		          			break;
		          		case 408:
		          			errorDescription="Request timeout error";
		          			break;
		          		case 413:
		          			errorDescription="Request entity too large error";
		          			break;
		          		case 414:
		          			errorDescription="Request-uri too large error";
		          			break;
		            	case 500:
		            		errorDescription = "Internal server error";
		            		break;
		            	case 501:
		            		errorDescription = "Not implemented error";
		            		break;
		            	case 502:
		            		errorDescription = "Bad gateway error";
		            		break;
		            	case 503:
		            		errorDescription = "Service unavailable error";
		            		break;
		            	case 504:
		            		errorDescription = "Gateway time-out error";
		            		break;
		            	case 505:
		            		errorDescription = "HTTP version not supported error";
		            }
		            
		            out.println("Description: " + errorDescription);
		            out.println("<br/ >ErrorCode: " + ed.getStatusCode());
		            out.println("<br />URL: " + ed.getRequestURI());
		    
		            // Error handled successfully, set a flag
		            handled = true;
		        }
		    }
		    
		    // Check if the error was handled
		    if(!handled){
		    	out.println("<p />No information about this error is available.");
		    }
		%>
		
		</div>
</body>
</html>