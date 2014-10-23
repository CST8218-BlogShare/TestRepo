<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  
    pageEncoding="ISO-8859-1"%>  
<html>

<!-- Displays a custom error message when a http error occurs  -->

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> 
<head>
<link rel="stylesheet" href="Styles/LookAndFeel.css">
<title>BlogShare - Blog</title>

<%

// the username is used within the navigation bar of the website.
if(session.getAttribute("username") == null){
	session.setAttribute("username","");
}

%>

</head>
	<body>
	
		<!-- Allows the page to identify itself as an error page for handling errors produced by TomCat -->
		<%@ page isErrorPage="true" %>
		
		<!-- navigation bar -->
		<div class="FillScreenTextCentered" style="background-color:lightgrey; height:auto; margin-bottom:2%;">
			<br>
			<table style="width:90%; margin-right:auto; margin-left:auto;">
				<tr style="height:50%;">
					<td><h3><a href="Home.jsp">BLOGSHARE</a></h3></td>
					<td rowspan="2" style="width:25%; font-size:24px;"> <input type=text name=navBarSearchTerm maxlength=100/></td>
					<td style="width:10%"> <input type=checkbox name=navBarBlogsCheck checked="checked"/>Blogs<p>  </td>
					<td style="width:10%"> <input type=checkbox name=navBarTitlesCheck checked="checked"/>Titles<p></td>
					<td style="width:10%"> <input type=checkbox name=navBarUsersCheck checked="checked"/>Users<p> </td>
					<td rowspan="2" style="width:25%">  <input type=button name=navBarSearch maxlength=100 value="Search"/></td>
				</tr>
				<tr style="height:50%;">
					<td><a href="Profile.jsp">Welcome <%= session.getAttribute("username") %>!</a></td>
					<td style="width:10%"> <input type=checkbox name=navBarPostCheck checked="checked"/>Posts<p> </td>
					<td style="width:10%"> <input type=checkbox name=navBarContentCheck checked="checked"/>Content<p></td>
					<td style="width:10%"> <input type=checkbox name=navBarAuthorsCheck checked="checked"/>Authors<p> </td>
				</tr>
			</table>
			<br>
		</div>
		
		
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