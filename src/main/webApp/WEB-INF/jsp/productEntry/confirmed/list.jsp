<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>
<%@page import="java.util.Vector"%>
<%@page import="org.belex.product.Product"%>
<%@page import="org.belex.arrival.Arrival"%>
<%@page import="org.belex.entry.Entry"%>
<%
Arrival arrival = (Arrival)request.getAttribute("arrival");
Vector<Entry> entries = arrival.getSavedEntries();
%>
<html>
<head>
	<%@include file="/WEB-INF/jsp/productEntry/jspf/headHeader.jspf"%>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/css/general.css" type="text/css">
	<script type="text/javascript">
		<!--
			function sendBackFrm() {
				window.document.backFrm.submit();
			}
			function sendRemoveEntryFrm(entryId) {
				window.document.removeEntryFrm.entryId.value = entryId;
				window.document.removeEntryFrm.submit();
			}
			function sendModifyEntryFrm(entryId) {
				window.document.modifyEntryFrm.entryId.value = entryId;
				window.document.modifyEntryFrm.submit();
			}
		//-->
	</script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/productEntry/jspf/bodyHeader.jspf" %>
	<div id="content">
		<font id="pageTitle">
			Arrivage de marchandises - ${arrival.supplier.supplierName} (${arrival.supplier.supplierCode})<br>
		</font>
	
		<hr>
	
		<font id="pageSubTitle">
			Articles introduits pour cette session <br>
		</font>
	
		<%
		if (entries.size() == 0) {
		%>
			Aucun article introduit
	
		<%
		} else {
		%>
	
			<form name="modifyEntryFrm" action="flowController.htm">
				<input type="hidden" name="entryId">
				<input type="hidden" value="" name="_eventId_modifyEntry">
				<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
			</form>
	
			<form name="removeEntryFrm" action="flowController.htm">
				<input type="hidden" name="entryId">
				<input type="hidden" value="" name="_eventId_removeEntry">
				<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
			</form>

			<form name="backFrm" action="flowController.htm">
				<input type="hidden" value="" name="_eventId_entryForm">
				<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
			</form>
	
			<p>
				<input class="button" type="button" value=" << Retour " onclick="sendBackFrm()">
			</p>
			
			<table border="0" width="95%" cellspacing="0" cellpadding="2">
				<%
				for (int i=0; i < entries.size(); i++) {
					Entry currentEntry = entries.get(i);
					Product currentProduct = currentEntry.getProduct();
					Product.Unit selectedUnit = utb.getSelectedUnit(currentProduct);
				%>
					<tr> 
						<td colspan="2" style="padding:10px">
	
							<%= currentProduct.getDescription() %> (<%=currentProduct.getProductCode()%>)<br>
	
						</td>
					</tr>
					<tr style="border-bottom: 1px">
						<td width="60%">
							<table border="0" width="100%" cellspacing="2" cellpadding="2">
								<tr> 
									<td id="header">
										Quantité<br>
									</td>
						        	<td id="header">
										Conditionnement<br>
									</td>
									<td id="header">
										Date de validité<br>
									</td>
									<td id="header">
										Numéro de lot<br>
									</td>
								</tr>
								<tr>
						        	<td id="item">
										<%= currentEntry.getNumberOfProduct() %>
									</td>
						        	<td id="item">
						        		<%= Util.displayContitionnement(selectedUnit.getNumber(), selectedUnit.getConditionnement()) %>
									</td>
									<td id="item">
										<%= currentProduct.getValidityDate() %><br>
									</td>
									<td id="item">
										<%= currentProduct.getLotNumber() %><br>
									</td>
								</tr>
							</table>
						</td>
						<td align="center">
							<input class="button" type="button" value=" Modifier " onclick="sendModifyEntryFrm(<%= i %>)">
							<input class="button" type="button" value=" Annuler l'entrée " onclick="sendRemoveEntryFrm(<%= i %>)">
						</td>
					</tr>
	
				<% } %>
	
			</table>
	
		<% } %>
	
		<p>
			<input class="button" type="button" value=" << Retour " onclick="sendBackFrm()">
		</p>
	</div>
</body>
</html>
