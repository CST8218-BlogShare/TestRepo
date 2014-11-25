<%@ page language="java" contentType="text/html;" import="java.util.ArrayList, com.amzi.dao.Post, com.amzi.dao.PostEdit, java.util.ResourceBundle"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="BootstrapInclude.html" />
<title>BlogShare - Post Edit History</title>
</head>
<body>
<%

	
	if(session.getAttribute("language") == null){
		session.setAttribute("language","EN");
	}
	
	ResourceBundle lang = ResourceBundle.getBundle("PostEditHistory_EN");
	
	if (session.getAttribute("language").toString().equals("FR")){
		lang = ResourceBundle.getBundle("PostEditHistory_FR");
	} 
%>

	<% 
		int postEditPos = (int) session.getAttribute("currentPostEditPos");
		Post currentPost= (Post) session.getAttribute("currentPost");
		ArrayList<PostEdit> postEdits = (ArrayList<PostEdit>) session.getAttribute("currentPostEditList");	
		session.setAttribute("currentPostEdit", postEdits.get(postEditPos));
	%>
	
	<%  
		if( request.getAttribute("errorMessage") != null)
		{ %>
		<div class="container">
			<div class="alert alert-danger alert-dismissible" role="alert">
				<button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<%= request.getAttribute("errorMessage") %>
			</div>
		</div>
	<%	}	%>
	
	<p class="FillScreenTextCentered" style="font-size:36px"><b><%= lang.getString("editfor") %> <%=currentPost.getPostTitle() %></b></p>
	
	<br>	
	
	<table class="FillScreen90">
		<tr>
			<td style="width:35%; ">
				<table class="FillScreen90">				
					<%  for(int i = 0; i < postEdits.size(); ++i){ 	%>
						<tr postEditPos = <%=i%> >
							<td class="postEditListElement whiteDashedBorder" onClick="postEditListClick(this)" <% if(i == postEditPos) { %> style="background:black;" <% } %>  >
								 <div style="text-align:left; margin:2%;"><p> <b> <%= lang.getString("editby") %> </b> <%= postEdits.get(i).getAuthor() %></p></div>
								 <div style="text-align:left; margin:2%;"><p> <b> <%= lang.getString("editdate") %> </b> <%= postEdits.get(i).getEditDateTime() %></p></div>
							</td>
						</tr>						
					<% } %>
				</table>
			</td>
			
			
			<td class="whiteDashedBorder" style="width:65%;">
				<form name="reversePostEditForm" action="reversePostEditServlet" method="post">
					<table class="FillScreen90">
						<tr>
							<td>
								<p style="font-size:32px; text-decoration:underline;"><b><%= lang.getString("contentbeforeedit") %></b></p>
							</td>
						</tr>
						<tr>
							<td>
								<p style="font-size:24px">	
									<b> <%=postEdits.get(postEditPos).getTitleBeforeEdit() %> </b>
								</p>
							</td>
						</tr>
						<tr>
							<td style="background:white; text-align:left;">
								<p style="margin:5%; color:black;"> 
									<%=postEdits.get(postEditPos).getContentBeforeEdit() %>
								</p>
							</td>
						</tr>
						
						<!-- creating space -->
						<tr>
							<td>
								<br>
							</td>
						</tr>
						
						<tr>
							<td>
								<p style="font-size:32px; text-decoration:underline;"><b><%= lang.getString("contentafteredit") %></b></p>
							</td>
						</tr>
						
						<tr>	
							<td>
								<p style="font-size:24px">
									<b><% if(postEditPos == 0){ %>
															 
											 <%=currentPost.getPostTitle() %> 	
																 		
									   <% }else{ %>
															 	
											 <%=postEdits.get(postEditPos-1).getTitleBeforeEdit()%>
															 	
									   <% } %>
									</b>
								</p>	
							</td>
						</tr>
						<tr>
							<td style="background:white; text-align:left;">
								<p style="margin:5%; color:black;"> 
									<% if(postEditPos == 0){ %>
										
											<%= currentPost.getPostBody() %>
									
									 <% }else{ %>
									 
											<%=postEdits.get(postEditPos-1).getContentBeforeEdit() %> 	
									 
									 <% } %>
									
								</p>
							</td>
						</tr>
						
						<!-- creating space -->
						<tr>
							<td>
								<br>
							</td>
						</tr>
						
						<tr>
							<td>
								<input class=button style="margin-bottom:1%;" type=submit value="<%= lang.getString("revertedit")%>"/>
							</td>
						</tr>						
						<tr>
							<td> <!-- possibly make all button have the same margin -->
								<input class=button style="margin-bottom:1%;" type="button" value="<%= lang.getString("exit")%>" onClick="cancelClick(this)"/>
							</td>
						</tr>
					</table>
				</form>
			</td>
		</tr>
	</table>
	
	<!-- form used to request a blog by title from getblogservlet -->
	<form id="switchCurrentPostEdit" action="switchPostEditServlet" method="post">
		<input type="hidden" id="postEditPos" name="postEditPos" value="">
	</form>
	
	<script>
		function postEditListClick(elementClicked){
			var form = document.forms['switchCurrentPostEdit'];
			form.postEditPos.value = elementClicked.parentNode.getAttribute("postEditPos");
			form.submit();
		}
	</script>
	
	<form id="backToBlogEdit" action="BlogEdit.jsp" method="get"></form>
	
	<script>
		function cancelClick(elementClicked){
			var form = document.forms['backToBlogEdit'];
			form.submit();
		}
	</script>

</body>
</html>