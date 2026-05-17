<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>
<html>
<head>
	<%@include file="/WEB-INF/jsp/productEntry/jspf/headHeader.jspf"%>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/css/general.css" type="text/css">
	<script type="text/javascript">
		<!--
			function sendNextFrm(eventName) {
				var confirmed = true;
				if (eventName === "delete") {
					confirmed = confirm("Etes-vous sûr de supprimer l'encodage?");
				}
				if (confirmed) {
					// Create hidden input with event name
					var eventInput = document.createElement("input");
					eventInput.type = "hidden";
					eventInput.name = "_eventId_" + eventName;
					eventInput.value = "";
					document.nextFrm.appendChild(eventInput);
					document.nextFrm.submit();
				}
			}
		//-->
	</script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/productEntry/jspf/bodyHeader.jspf" %>
	<div>
		<font id="pageTitle">
			Arrivage de marchandises - ${arrival.supplier.supplierName} (${arrival.supplier.supplierCode})<br>
		</font>
	
		<hr>
			
		<!-- WebFlow 2.5.1: Submit via POST with event ID -->
		<form name="nextFrm" action="${flowExecutionUrl}" method="post">
			<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
		</form>

		<form method="post">
			<table>
				<tr>
					<td colspan="2">
						<html:errors/>
					</td>
				</tr>
				<tr>
					<td align="center">
						Une arrivée similaire non confirmée a été détectée.<br>
					</td>
				</tr>
				<tr>
					<td style="padding-top:20px">
						<input class="button" name="btn1" onclick="sendNextFrm('continue')" type="button" value=" Continuer l'arrivage "> 
						<input class="button" name="btn2" onclick="sendNextFrm('delete')" type="button" value=" Supprimer l'encodage en suspens ">
					</td>
				</tr>
			</table>
		</form>

		<c:if test="${fn:length(arrival.savedEntries) > 0}">
				<table border="0" width="95%" cellspacing="0" cellpadding="2">
					<c:forEach items="${arrival.savedEntries}" var="currentEntry">
						<tr> 
							<td colspan="2" style="padding:5px">

								${currentEntry.product.description} (${currentEntry.product.productCode})<br>

							</td>
						</tr>
					</c:forEach>
				</table>
		</c:if>

	</div>

</body>
</html>
