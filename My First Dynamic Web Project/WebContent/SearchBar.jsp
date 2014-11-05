<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="java.util.Locale, java.util.ResourceBundle, com.amzi.dao.User, com.amzi.dao.SearchResult"
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
	
	SearchResult searchResult = (SearchResult) session.getAttribute("currentSearchResult");
%>

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
						<td><h3><a href="Home.jsp">BLOGSHARE</a></h3></td>
						<td rowspan="2" style="width:25%; font-size:24px;"> <input type=text name="navBarSearchTerm" maxlength=100 <% /* If a search has been made, keep the search term available */ if(searchResult != null) { %> value=<%=searchResult.getSearchTerm() %> <% } %>/></td>
						<td style="width:10%"> <input type=checkbox id="navBarBlogsCheck" name="navBarBlogsCheck" checked="checked" OnClick="BlogClicked(this)"/><% out.println(lang.getString("blogs")); %>  </td>
						<td style="width:10%"> <input type=checkbox id="navBarTitleCheck" name="navBarTitleCheck" checked="checked"/><% out.println(lang.getString("titles")); %></td>
						<td style="width:10%"> <input type=checkbox id="navBarEditableCheck" name="navBarEditableCheck" /><% out.println(lang.getString("editable")); %> </td>
						<td rowspan="2" style="width:25%">  <input type=submit name=navBarSearch value="<%=lang.getString("search") %>"/></td>
					</tr>
					<tr style="height:50%;">
						<% /* If the user is not logged in, display a basic greeting. 
							  If not display a personal greeting. */
							
						   if(session.getAttribute("currentUser") == null){ %>
						   		<td> Welcome!</td>
						<% }else{ %>
								<td> <a href="Profile.jsp"> <% out.println(lang.getString("welcome")); %> <%= ((User)session.getAttribute("currentUser")).getUsername() %>!</a></td>
						<% } %>
						
						<td style="width:10%"> <input type=checkbox id="navBarPostsCheck" name="navBarPostsCheck" checked="checked" OnClick="PostClicked(this)"/><% out.println(lang.getString("posts")); %><p> </td>
						<td style="width:10%"> <input type=checkbox id="navBarBodyCheck" name="navBarBodyCheck" checked="checked"/><% out.println(lang.getString("content")); %><p></td>
						<td style="width:10%"> <input type=checkbox name="navBarUsersCheck" checked="checked" /><% out.println(lang.getString("users")); %><p> </td>
					</tr>
				</table>
			</form>
			<br>
	
			<!-- enables the user to change the language preference for the pages content -->
			<form name="langForm" action="<%= session.getAttribute("currentpage") %>.jsp" method="post" >
				<input type=hidden name=language value="<%=languageSwitch%>"/>
				<input type=submit name=langbutton maxlength=100 value="<%=lang.getString("gotolang")%>"/>
			</form>
			
			<!-- If a user is currently logged in, a button is displayed allowing the user to log out of the application 
			     this also causes the current session object to be invalidated and database connection to be closed. --> 
			<% if(session.getAttribute("currentUser") != null){ %>
			<form name="logoutForm" action="logoutServlet" method="post">
				<input type=submit name=navBarSearch value="Logout"/>
			</form>
			<% } %>
		</header>