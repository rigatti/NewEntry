<%-- make sure we have a session --%>
<%@ page session="true" %>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@include file="/WEB-INF/jspf/globalHeader.jspf"%>
<%
String admin = request.getParameter("admin");
String password = request.getParameter("password");
if (StringUtils.isEmpty(password) && StringUtils.isEmpty(admin)) {
	request.getRequestDispatcher("/index.jsp").forward(request, response);
}
String rememberMe = request.getParameter("rememberMe");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
	<HEAD>
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin.css">
		<% isAdmin = true; %>
		<%@include file="/WEB-INF/jspf/messaging.jspf"%>
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
						newCookie("belexIndexPwd","admin",1);
					}
				<% } %>
			//-->
		</script>
</head>
<body>
	<div align="center">
		<img alt="bVc" src="<%= request.getContextPath() %>/pic/bvc.gif" width="50px" height="40px">
	</div>

<body class="content" style="padding-top:37px">

	<form id="frmForum" name="frmForum" target="_blank" action="/jforum/forums/show/1.page"> </form>

	<center>
		<H1>Menu</h1>
		
		<!-- 
		<p class="instruction-text">Instruction text</p>
		-->
		<table border="0" cellpadding="10" cellspacing="0" valign="top" width="30%" summary="Framing Table">
			<tr>
				<td class="layout-manager-notabs">
					<table class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3 width=100%>
						<tr>
							<td class="table-text-bold">
								<a class="ctextunderlined" href="flowController.htm?_flowId=productEntry">Arrivée de marchandise</a><br>
							</td>
						</tr>
						<tr>
							<td class="table-text-bold">
								<a class="ctextunderlined" href="flowController.htm?_flowId=productAllocation">Attribution des marchandises reçues</a><br>
							</td>
						</tr>
						<tr>
							<td class="table-text-bold">
								<a class="ctextunderlined" href="flowController.htm?_flowId=productAdmin">Administration des produits</a><br>
							</td>
						</tr>
						<tr>
							<td class="table-text-bold">
								<a class="ctextunderlined" href="flowController.htm?_flowId=productTraceability">Traçabilité des produits</a><br>
							</td>
						</tr>
						<!-- tr>
							<td class="table-text-bold">
								<a class="ctextunderlined" href="flowController.htm?_flowId=entryTraceability">Traçabilité des entrées fournisseurs</a><br>
							</td>
						</tr-->
					</table>
				</td>
			</tr>
			<tr>
				<td class="layout-manager-notabs">
					<table class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3 width=100%>
						<tr>
							<td class="table-text-bold" onclick="frmForum.submit();">
								<a class="ctextunderlined" href="javascript:void(0);">Messagerie</a>
								<div id="messagingShortInfo" style="display:none;padding-top:10px;">
									Dernier message du <i><span id="messagingDate"></span></i><br>
									Posté par <i><span id="messagingUsername"></span></i><br>
								</div><br>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>

		<br>
		
		<% if (StringUtils.equalsIgnoreCase(password, "2belex")) { %>
			<table border="0" cellpadding="10" cellspacing="0" valign="top" width="30%" summary="Framing Table">
				<tr>
					<td class="layout-manager-notabs">
						<table class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3 width=100%>
							<tr>
								<td class="table-text-bold">
									<form name="frmCleanDb" method="get" action="flowController.htm" target="_BLANK">
										<input type="hidden" name="_flowId" value="productCleaning">
									</form>
									<script>
										<!--
											function sendFrm() {
												if (confirm('Etes-vous sûr?')) {
													window.document.frmCleanDb.submit();
												}
											}
										//-->
									</script>
									<a class="ctextunderlined" href="javascript:void(0)" onclick="sendFrm();">Nettoyage DB</a><br>
								</td>
							</tr>
							<tr>
								<td class="table-text-bold">
									<form name="frmExportProduct" method="get" action="flowController.htm" target="_BLANK">
										<input type="hidden" name="_flowId" value="productsExport">
									</form>
									<script>
										<!--
											function sendFrmExportProduct() {
												alert("Request not allowed");
												//if (confirm('Etes-vous sûr?')) {
												//	window.document.frmExportProduct.submit();
												//}
											}
										//-->
									</script>
									<a class="ctextunderlined" href="javascript:void(0)" onclick="sendFrmExportProduct();">Export Product</a><br>
								</td>
							</tr>
							<tr>
								<td class="table-text-bold">
									<form name="frmExportCustomer" method="get" action="flowController.htm" target="_BLANK">
										<input type="hidden" name="_flowId" value="customersExport">
									</form>
									<script>
										<!--
											function sendFrmExportCustomer() {
												alert("Request not allowed");
												//if (confirm('Etes-vous sûr?')) {
												//	window.document.frmExportCustomer.submit();
												//}
											}
										//-->
									</script>
									<a class="ctextunderlined" href="javascript:void(0)" onclick="sendFrmExportCustomer();">Export Customer</a><br>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		<% } %>
	</center>
</body>
</html>