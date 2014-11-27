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
		
		<jsp:include page="SearchBar.jsp"></jsp:include>
		
		<div  class="FillScreenTextCentered" style="height:60%;  margin-top:auto; marign-bottom:auto; background-color:lightgrey;">
		
		<!--creating space -->
		
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
		            
		            out.println("<p class=\"ErrorMessageOutput\" /> An error has occurred <br /><br />");
		            out.println("Description: " + errorDescription);
		            out.println("<br/ >ErrorCode: " + ed.getStatusCode());
		            out.println("<br />URL: " + ed.getRequestURI());
		    
		            // Error handled successfully, set a flag
		            handled = true;
		        }
		    }
		    
		    // Check if the error was handled
		    if(!handled){
		    	out.println("<p class=\"ErrorMessageOutput\" style=\"font-size:16px;\" />No information about this error is available.");
		    }
		%>
		<br>
		<br>
		</div>
</body>
</html>