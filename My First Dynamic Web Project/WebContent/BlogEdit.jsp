<%@ page language="java" contentType="text/html;" import="com.amzi.dao.Blog, com.amzi.dao.Post"%>
<!DOCTYPE html>
<html>
<meta charset="UTF-8">
<head>
<link rel="stylesheet" href="Styles/LookAndFeel.css">
<title>BlogShare - BlogEdit</title>
</head>

<%

 Blog b = (Blog) session.getAttribute("currentBlog");

%>
<body>

	<jsp:include page="SearchBar.jsp"></jsp:include>

	<div class="FillScreenTextCentered">
		<p style="font-size:36px;"><a href="Blog.jsp" style="color:lightblue;"> <b> <%= b.getBlogTitle() %> </b> </a></p>
	</div>
	
	<form name="changeBlogTitleForm" action="blogChangeTitleServlet" method="post">
		<table class="FillScreenTextCentered">
			<tr>
				<td colspan=2>
					<p style="font-size:28px"><b>Blog Title</b></p>
			</tr>
			
			<tr>
				<td style="width:75%;">
					<input type="text" name="newBlogTitle" value="<%=b.getBlogTitle()%>"/>	
				</td>
				
				<td style="width:30%;">
					<input type="submit" value="Change Title"/>
				</td>
			</tr>
		</table>
	</form>

	<!-- making space between the two elements of the page -->	
	<br>
		
	<table class="FillScreenTextCentered">
		<tr>
			<td colspan="4">
				<p style="font-size:28px;"><b>Posts</b></p>
			</td>
		</tr>
		<% for(int i = 0; i < b.getPostCount(); ++i) { %>
			<tr postPosition="<%=i%>">
				<td style="width:50%;">
					<p style="font-size:20px;"> <b> <%= b.getPostAt(i).getPostTitle() %> </b> </p>
				</td>
				<td style="width:15%;" >
					<input type=button class="PostEdit" onClick="goToPostEdit(this)" value="Edit Post">
				</td>
				<td style="width:15%;">
					<input type=button class="PostEditHistory" onClick="editHistoryClick(this)"  value="Edit History">
				</td>
				<!-- The first post of the blog, functions as an explanation of the blog and because of this cannot be deleted. --> 
				<% if(i != 0){ %>
					<td style="width:20%;">
						<input type=button class="PostDelete"  onClick="deletePostClick(this)" value="Delete Post">
					</td>
				<% } %>
			</tr>	
		<% } %>
	</table>
	
	<!-- The form and Javascript function used to open the contents of the associated post in edit mode within PostCreate.jsp. -->
	
	<!-- the attributes of the goToPostEdit form must have the same name as those retrieved from the session in PostCreate.jsp  -->
	<form id="goToPostEdit" action="PostCreate.jsp" method="get">
		<input type="hidden" id="editEnabled" name="editEnabled" value="true">]
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
				alert(form.postToRetrieveEditsPostId.value);
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
