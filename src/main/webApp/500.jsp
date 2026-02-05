<%-- make sure we have a session --%>
<%@ page session="true" %>
<%@include file="/WEB-INF/jspf/globalHeader.jspf"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
	<HEAD>
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin.css">
		<script type="text/javascript">
			<!--
				//window.location.href="flowController.htm?_flowId=productEntry";
			//-->
		</script>
</head>
<body>
	<div align="center">
		<img alt="bVc" src="<%= request.getContextPath() %>/pic/bvc.gif" width="50px" height="40px">
	</div>

<body class="content" style="padding-top:37px">
	<center>
		<H1>Erreur système</h1>
		
		<!-- 
		<p class="instruction-text">Instruction text</p>
		-->
		<table border="0" cellpadding="10" cellspacing="0" valign="top" width="30%" summary="Framing Table">
			<tr>
				<td class="layout-manager-notabs">
					<table class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3 width=100%>
						<tr>
							<td class="table-text-bold">
								Suite à un problème technique, votre requête ne peut aboutir normalement.<br> <br>
								Veuillez nous en excuser.<br> <br>
								
								Contact technique : 0477/23.47.93<br>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</center>
</body>
</html>