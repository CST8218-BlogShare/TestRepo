<%@ page language="java" contentType="text/html;" import="com.amzi.dao.Blog, com.amzi.dao.Post, java.util.ResourceBundle"%>
<!DOCTYPE html>
<html>
<meta charset="UTF-8">
<head>
<jsp:include page="BootstrapInclude.html" />
<title>BlogShare - BlogEdit</title>
</head>

<%

 Blog b = (Blog) session.getAttribute("currentBlog");

if(session.getAttribute("language") == null){
	session.setAttribute("language","EN");
}

ResourceBundle lang = ResourceBundle.getBundle("BlogEdit_EN");

if (session.getAttribute("language").toString().equals("FR")){
	lang = ResourceBundle.getBundle("BlogEdit_FR");
} 

%>
<body>

	<jsp:include page="SearchBar.jsp"></jsp:include>

	<div class="FillScreenTextCentered">
		<p style="font-size:48px;"><a href="Blog.jsp" title="<%= lang.getString("linktoblogtitle") %>" style="color:lightblue;"> <b> <%= b.getBlogTitle() %> </b> </a></p>
	</div>
	
	<div class="FillScreenTextCentered">
		<p style="font-size:28px; text-decoration:underline;"><b><%= lang.getString("edittitle")%></b></p>
	</div>
	
	<%  if( request.getAttribute("errorMessage") != null)
		{ %>
		<div class="container">
			<div class="alert alert-danger alert-dismissible" role="alert">
				<button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<%= lang.getString(request.getAttribute("errorMessage").toString()) %>
			</div>
		</div>
	<%	}	%>
	
	<form name="changeBlogTitleForm" action="blogChangeTitleServlet" method="post">
		<table class="FillScreenTextCentered">
			
			<!--  <tr>
				<td colspan=2>
					<p style="font-size:28px; text-decoration:underline;"><b>Edit Blog Title</b></p> <!-- make a css class for titles? 
			</tr> -->
			
			<tr>
				<td style="width:75%;">
					<input type="text" name="newBlogTitle" value="<%=b.getBlogTitle()%>"/>	
				</td>
				
				<td style="width:30%;">
					<input type="submit" value="<%= lang.getString("changetitle")%>"/>
				</td>
			</tr>
		</table>
	</form>

	<!-- making space between the two elements of the page -->	
	<br>
		
	<div class="FillScreenTextCentered"> 
		<p style="font-size:28px; text-decoration:underline;"><b><%= lang.getString("manageposts")%></b></p>
	</div>	
			
	<table class="FillScreenTextCentered blogEditTable" >
		<% for(int i = 0; i < b.getPostCount(); ++i) { %>
			<tr postPosition="<%=i%>">
				<td style="width:50%;">
					<p style="font-size:20px;"> <b> <%= b.getPostAt(i).getPostTitle() %> </b> </p>
				</td>
				<td style="width:15%;" >
					<input type=button class="PostEdit" onClick="goToPostEdit(this)" value="<%= lang.getString("editpost")%>">
				</td>
				<td style="width:15%;">
					<input type=button class="PostEditHistory" onClick="editHistoryClick(this)"  value="<%= lang.getString("edithistory")%>">
				</td>
				<!-- The first post of the blog, functions as an explanation of the blog and because of this cannot be deleted. --> 
				<td style="width:20%;">
				<% if(i != 0){ %>
						<input type=button class="PostDelete"  onClick="deletePostClick(this)" value="<%= lang.getString("deletepost")%>">
				<% } %>
				</td>
			</tr>	
		<% } %>
	</table>
	
	<!-- The form and Javascript function used to open the contents of the associated post in edit mode within PostCreate.jsp. -->
	
	<!-- the attributes of the goToPostEdit form must have the same name as those retrieved from the session in PostCreate.jsp  -->
	<form id="goToPostEdit" action="PostCreate.jsp" method="get">
		<input type="hidden" id="editEnabled" name="editEnabled" value="true">
		<input type="hidden" id="post" name="post" value="">
	</form>
	<script>
		function goToPostEdit(elementClicked){
			var form = document.forms['goToPostEdit'];
			
			/* Retreiving the <td> element, then the <tr> element and finally 
			  the value of the postPosition attribute within the <tr> element */
			form.post.value = elementClicked.parentNode.parentNode.getAttribute("postPosition");
				//alert(form.post.value);
			form.submit();
		}
	</script>
	
	<!-- need to set editMode and toEdit -->
	
	<!-- form used to request a blog by title from getblogservlet -->
	<form id="goToPostEditHistory" action="getPostEditHistoryServlet" method="post">
		<input type="hidden" id="postToRetrieveEditsPostPos" name="postPos" value="">
	</form>
	
	<script>
		function editHistoryClick(elementClicked){
			var form = document.forms['goToPostEditHistory'];
			form.postToRetrieveEditsPostPos.value = elementClicked.parentNode.parentNode.getAttribute("postPosition");
			form.submit();
		}
	</script>
	
	<!-- form used to request a blog by title from getblogservlet -->
	<form id="deletePost" action="postDeleteServlet" method="post">
		<input type="hidden" id="postToDeletePostPos" name="postPos" value="">
	</form>
	
	<script>
		function deletePostClick(elementClicked){
			var form = document.forms['deletePost'];
			form.postToDeletePostPos.value = elementClicked.parentNode.parentNode.getAttribute("postPosition");
				//alert(form.postToDeletePostId.value);
				//alert(form.postToDeletePostPos.value);
			form.submit();
		}
	</script>
	
	
	
</body>
</html>
