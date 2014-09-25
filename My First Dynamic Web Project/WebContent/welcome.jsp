<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  
    pageEncoding="ISO-8859-1"%>  
<html>  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">  
<title>Welcome <%=session.getAttribute("name")%></title>  
</head>  
<body>  
    <h3>Login successful!!!</h3>  
    <h4>  
        Hello,  
        <%=session.getAttribute("name")%></h4> 
        <p><a href="Profile.jsp"> <%= session.getAttribute("name") %>'s Profile Page</a></p>
        <form name="NavForm" action="navServlet" method="post">
        	<table>
				<tr>
				 <td colspan=2><input class=button type=submit value="Go to Profile" onClick="error()"/></td>
				 <td><input type="hidden" name=goTo value="profile" maxlength=100/></td>     
				</tr>
			</table>
		</form>
		
</body>  
</html>  