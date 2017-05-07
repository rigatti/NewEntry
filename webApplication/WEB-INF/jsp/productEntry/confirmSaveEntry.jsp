<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>
<html>
<head>
	<%@include file="/WEB-INF/jsp/productEntry/jspf/headHeader.jspf"%>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/css/general.css" type="text/css">
	<script type="text/javascript">
		<!--
			function redirect(){
				window.setTimeout("sendNewEntryFrm()", 2000);
			}
			function sendNewEntryFrm(){
				window.document.newEntryFrm.submit();
			}
		//-->
	</script>
</head>
<body onload="redirect()">
<%@ include file="/WEB-INF/jsp/productEntry/jspf/bodyHeader.jspf" %>
	<div>
		<font id="pageTitle">
			Nouvel arrigage de marchandises - ${arrival.supplier.supplierName} (${arrival.supplier.supplierCode})
		</font>
		<hr>
		<p>
			Votre encodage a été correctement enregistré<br>
		</p>
	</div>

	<form name="newEntryFrm" action="flowController.htm" method="get">
		<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
		<input type="hidden" name="_eventId_submit">
	</form>

</body>
</html>