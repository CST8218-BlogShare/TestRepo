<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="java.util.ArrayList, java.io.IOException, com.amzi.dao.User"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="BootstrapInclude.html" />

<%
	User u =  (User) session.getAttribute("currentUser");
%>


<title><%=u.getUsername()%>'s Profile Page</title>
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
		<br>

	<h1>
		<span class="glyphicon glyphicon-user" style="fontSize: 50px"></span>
		<%=u.getUsername()%>'s Profile Page
	</h1>
	<h3>
		<span class="label label-default">Joined: <%=u.getDateRegistered()%></span>
	</h3>

	<p style="padding: 50px">
		<a href="ProfileEdit.jsp"><button type="button"
				class="btn btn-default btn-lrg" style="width: 500px">Edit
				Profile</button></a> <br style="clear: left;" />
				
	 <a href="BlogCreate.jsp"><button
				type="button" class="btn btn-default btn-lrg" style="width: 500px">Create
				Blog</button></a>
	</p>

	<!-- the dynamic list of user blogs is generated here -->
	<div class="list-group">
		<%
			//ArrayList<String> userBlogList = (ArrayList<String>) session.getAttribute("userBlogList");
		
			ArrayList<String> userBlogList = u.getUserBlogs(u.getUserId());

			if (userBlogList != null) {
				for (String blogTitle: userBlogList){
					out.print("<li");
					out.println( " class=\"blog-link list-group-item\" blogTitle=\"" + blogTitle + "\">"+ blogTitle +"</li>");
				}		
			} else {
			out.println("<li class=\"list-group-item\">No Blogs Found</li>");
			}
		%>
	</div>

	<!-- form used to request a blog by title from getblogservlet -->
	<form id="goToBlog" action="GetBlogServlet" method="post">
		<input type="hidden" id="goToBlogName" name="blogTitle" value="">
	</form>

	<!-- Attach an onclick event to list elements with class blog-link -->
	<!-- this event gets the list item's blogTitle attribute and places it into the input with id gotoblog -->
	<!-- it then submits the gotoblog form -->
	<script>
	$('li.blog-link').click(function(){
		
		$('input#goToBlogName').val($(this).attr('blogTitle'));
		$('form#goToBlog').submit();
		$(this).toggleClass('active');
	});

	</script>
</body>
</html>