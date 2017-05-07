<%-- make sure we have a session --%>
<%@ page session="true" %>
<%@include file="/WEB-INF/jspf/globalHeader.jspf"%>
<%
String rememberMe = request.getParameter("rememberMe");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="org.apache.commons.lang.StringUtils"%>
<HTML>
	<HEAD>
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin.css">
		<script type="text/javascript">
			<!--

			function newCookie(name,value,newDays) {
				 var days = 10;   // the number at the left reflects the number of days for the cookie to last
				                 // modify it according to your needs
				 if (newDays) {
				 	days = newDays;
				 }
				 if (days) {
				   var date = new Date();
				   date.setTime(date.getTime()+(days*24*60*60*1000));
				   var expires = "; expires="+date.toGMTString(); 
		 	     } else 
		 	     	var expires = "";
	
				   document.cookie = name+"="+value+expires+"; path=/"; 

			}
			
			function readCookie(name) {
			   var nameSG = name + "=";
			   var nuller = '';
			  if (document.cookie.indexOf(nameSG) == -1)
			    return nuller;
			
			   var ca = document.cookie.split(';');
			 	 for(var i=0; i<ca.length; i++) {
			    var c = ca[i];
			    while (c.charAt(0)==' ') c = c.substring(1,c.length);
			  	if (c.indexOf(nameSG) == 0) return c.substring(nameSG.length,c.length); }
			    return null; 
			}
			
				<% if (StringUtils.isNotEmpty(rememberMe)) { %>
					if (!readCookie("belexIndexPwd")) {
						newCookie("belexIndexPwd","belex",1);
					}
				<% } %>

				window.location.href="flowController.htm?_flowId=productEntry";
			//-->
		</script>
</head>
<body class="content" style="padding-top:37px">
	<div align="center">
		<img alt="bVc" src="<%= request.getContextPath() %>/pic/bvc.gif" width="50px" height="40px">
	</div>
	
	<center>
		<H1>Menu</h1>
		<table border="0" cellpadding="10" cellspacing="0" valign="top" width="30%" summary="Framing Table">
			<tr>
				<td class="layout-manager-notabs">
					<table class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3 width=100%>
						<tr>
							<td class="table-text-bold">
								<a class="ctextunderlined" href="flowController.htm?_flowId=productEntry">Arrivée de marchandise</a><br>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</center>
</body>
</html>