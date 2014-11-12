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
		int postEditsPos = (int) session.getAttribute("currentPostEditPos");
		String currentPostTitle = (String) session.getAttribute("currentPostTitle");
		String currentPostContent = (String) session.getAttribute("currentPostContent");
		ArrayList<PostEdit> postEdits = (ArrayList<PostEdit>) session.getAttribute("currentPostEditList");		
	%>
	
	<p class="FillScreenTextCentered" style="font-size:36px">Edit History</p>
	
	<br>	
	
	<table class="FillScreen90">
		<tr>
			<td style="width:35%; ">
				<table>
					<%  for(int i = 0; i < postEdits.size(); ++i){ 	%>
						<tr>
							<td>
								<!-- include info on 
									
									author
									date of modification
									
								 -->
								 <div style="text-align:left"><p> <b> Edit By: </b> <%= postEdits.get(i).getAuthor() %></p></div>
								 <div style="text-align:right"><p> <b> Edit Date: </b> <%= postEdits.get(i).getEditDateTime() %></p></div>
								<br> <!-- making space between each row -->
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
									<b> <%=postEdits.get(postEditsPos).getTitleBeforeEdit() %> </b>
								</p>
							</td>
						</tr>
						<tr>
							<td style="background:white; text-align:left;">
								<p style="margin:5%; color:black;"> 
									<%=postEdits.get(postEditsPos).getContentBeforeEdit() %>
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
									<b><% if(postEditsPos == postEdits.size()-1){ %>
															 
											 <%=currentPostTitle %> 	
																 		
									   <% }else{ %>
															 	
											 <%=postEdits.get(postEditsPos+1).getTitleBeforeEdit()%>
															 	
									   <% } %>
									</b>
								</p>	
							</td>
						</tr>
						<tr>
							<td style="background:white; text-align:left;">
								<p style="margin:5%; color:black;"> 
									<% if(postEditsPos == postEdits.size()-1){ %>
										
										<%= currentPostContent %>
									
									 <% }else{ %>
									 
											<%=postEdits.get(postEditsPos+1).getContentBeforeEdit() %> 	
									 
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