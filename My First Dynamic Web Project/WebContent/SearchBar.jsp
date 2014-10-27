<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="java.util.Locale, java.util.ResourceBundle"
    %>
<!-- Navigation and Search Bar -->

<% 
	//EN language is the default, then check if needs to be changed to FR
	String languageSwitch = "FR";
	ResourceBundle lang = ResourceBundle.getBundle("SearchBar_EN");
	if (session.getAttribute("language") != null){
		if (session.getAttribute("language").equals("FR")){
			languageSwitch = "EN";
			lang = ResourceBundle.getBundle("SearchBar_FR");
		}
	}
%>

		<header class="FillScreenTextCentered" style="background-color:lightgrey; height:auto; margin-bottom:2%;">
			<br>
			<form name="searchForm" action="searchServlet" method="post" >
				<table style="width:90%; margin-right:auto; margin-left:auto;">
					<tr style="height:50%;">
						<td><h3>BLOGSHARE</h3></td>
						<td rowspan="2" style="width:25%; font-size:24px;"> <input type=text name=navBarSearchTerm maxlength=100/></td>
						<td style="width:10%"> <input type=checkbox name=navBarBlogsCheck checked="checked"/><% out.println(lang.getString("blogs")); %><p>  </td>
						<td style="width:10%"> <input type=checkbox name=navBarTitleCheck checked="checked"/><% out.println(lang.getString("titles")); %><p></td>
						<td style="width:10%"> <input type=checkbox name=navBarEditableCheck /><% out.println(lang.getString("editable")); %><p> </td>
						<td rowspan="2" style="width:25%">  <input type=submit name=navBarSearch maxlength=100 value="<%=lang.getString("search") %>"/></td>
					</tr>
					<tr style="height:50%;">
						<td> <a href="Profile.jsp"><% out.println(lang.getString("welcome")); %> <%= session.getAttribute("username") %>!</a></td>
						<td style="width:10%"> <input type=checkbox name=navBarPostsCheck checked="checked"/><% out.println(lang.getString("posts")); %><p> </td>
						<td style="width:10%"> <input type=checkbox name=navBarBodyCheck checked="checked"/><% out.println(lang.getString("content")); %><p></td>
						<td style="width:10%"> <input type=checkbox name=navBarUsersCheck /><% out.println(lang.getString("users")); %><p> </td>
					</tr>
				</table>
			</form>
			<br>
			<form name="langForm" action="<%= session.getAttribute("currentpage") %>.jsp" method="post" >
				<input type=hidden name=language value="<%=languageSwitch%>"/>
				<input type=submit name=langbutton maxlength=100 value="<%=lang.getString("gotolang")%>"/>
			</form>
		</header>