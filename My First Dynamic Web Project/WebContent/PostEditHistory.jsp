<%@ page language="java" contentType="text/html;" import="java.util.ArrayList, com.amzi.dao.Post, com.amzi.dao.PostEdit"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="BootstrapInclude.html" />
<title>Post Edit History</title>
</head>
<body>
	<% 
		int postEditPos = (int) session.getAttribute("currentPostEditPos");
		Post currentPost= (Post) session.getAttribute("currentPost");
		ArrayList<PostEdit> postEdits = (ArrayList<PostEdit>) session.getAttribute("currentPostEditList");	
		session.setAttribute("currentPostEdit", postEdits.get(postEditPos));
	%>
	
	<p class="FillScreenTextCentered" style="font-size:36px">Edit History</p>
	
	<br>	
	
	<table class="FillScreen90">
		<tr>
			<td style="width:35%; ">
				<table>				
					<%  for(int i = 0; i < postEdits.size(); ++i){ 	%>
						<tr>
							<td class="postEditListElement" <% if(i == postEditPos) { %> style="background:black;" <% } %>  >
								 <div style="text-align:left"><p> <b> Edit By: </b> <%= postEdits.get(i).getAuthor() %></p></div>
								 <div style="text-align:left"><p> <b> Edit Date: </b> <%= postEdits.get(i).getEditDateTime() %></p></div>
							</td>
						</tr>	
						
						<!-- making space -->
						<tr>
							<td>
								<br>
							</td>
						</tr>
					
					<% } %>
				</table>
			</td>
			
			
			<td style="width:65%;">
				<form name="reversePostEditForm" action="reversePostEditServlet" method="post">
					<table>
						<tr>
							<td>
								<p style="font-size:32px"><b>Content Before Edit</b></p>
								<br>
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
							<br>
							</td>
						</tr>
						
						<tr>
							<td>
								<p style="font-size:32px"><b>Content After Edit</b></p>
								<br>
							</td>
						</tr>
						
						<tr>	
							<td>
								<p style="font-size:24px">
									<b><% if(postEditPos == 0){ %>
															 
											 <%=currentPost.getPostTitle() %> 	
																 		
									   <% }else{ %>
															 	
											 <%=postEdits.get(postEditPos+1).getTitleBeforeEdit()%>
															 	
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
									 
											<%=postEdits.get(postEditPos+1).getContentBeforeEdit() %> 	
									 
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
								<input class=button type=submit value="Reverse Edit"/>
							</td>
						</tr>
					</table>
				</form>
			</td>
		</tr>
	</table>
	

</body>
</html>