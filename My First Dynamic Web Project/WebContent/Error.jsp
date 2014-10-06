<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  
    pageEncoding="ISO-8859-1"%>  
<html>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> 
<head>
<link rel="stylesheet" href="Styles/LookAndFeel.css">
<title>BlogShare - Blog</title>
</head>
	<body>
	
		<!-- Allows the page to identify itself as an error page for handling errors produced by TomCat -->
			
		<%@ page isErrorPage="true" %>
		
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
		
		
		<div  class="FillScreenTextCentered" style="height:60%;  margin-top:auto; marign-bottom:auto; background-color:lightgrey;">
		
		<!--creating space -->
		
		<br>
		<br>
		
		<!-- Adapted implementation for errors from  information and code sample found at 
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
		    
		            // Output details about the HTTP error
		            // (this should show error code 404, and the name of the missing page)
		            out.println("<p /> An error has occurred <br /><br />");
		            
		            switch(ed.getStatusCode()){
		            	case 500:
		            		errorDescription = "Internal Server Error";
		            		
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