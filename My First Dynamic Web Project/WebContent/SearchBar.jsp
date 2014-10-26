<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="java.util.Locale, java.util.ResourceBundle"
    %>
<!-- Navigation and Search Bar -->
		<header class="FillScreenTextCentered" style="background-color:lightgrey; height:auto; margin-bottom:2%;">
			<br>
			<form name="searchForm" action="searchServlet" method="post" >
				<table style="width:90%; margin-right:auto; margin-left:auto;">
					<tr style="height:50%;">
						<td><h3>BLOGSHARE</h3></td>
						<td rowspan="2" style="width:25%; font-size:24px;"> <input type=text name=navBarSearchTerm maxlength=100/></td>
						<td style="width:10%"> <input type=checkbox name=navBarBlogsCheck checked="checked"/>Blogs<p>  </td>
						<td style="width:10%"> <input type=checkbox name=navBarTitleCheck checked="checked"/>Titles<p></td>
						<td style="width:10%"> <input type=checkbox name=navBarEditableCheck />Editable<p> </td>
						<td rowspan="2" style="width:25%">  <input type=submit name=navBarSearch maxlength=100 value="Search"/></td>
					</tr>
					<tr style="height:50%;">
						<td> <a href="Profile.jsp">Welcome <%= session.getAttribute("username") %>!</a></td>
						<td style="width:10%"> <input type=checkbox name=navBarPostsCheck checked="checked"/>Posts<p> </td>
						<td style="width:10%"> <input type=checkbox name=navBarBodyCheck checked="checked"/>Content<p></td>
						<td style="width:10%"> <input type=checkbox name=navBarUsersCheck />Users<p> </td>
					</tr>
				</table>
			</form>
			<br>
			<form name="langForm" action="<%= session.getAttribute("currentpage") %>.jsp" method="post" >
				<% 
				//EN is the default so set languageSwitch to FR, then check if needs to be changed to EN
				String languageSwitch = "FR";
				if (session.getAttribute("language") != null){
					if (session.getAttribute("language").equals("FR"))
						languageSwitch = "EN";
				}
				%>
				<input type=hidden name=language value="<%=languageSwitch	%>"/>
				<input type=submit name=langbutton maxlength=100 value="Go to <%=languageSwitch	%> page"/>
			</form>
		</header>