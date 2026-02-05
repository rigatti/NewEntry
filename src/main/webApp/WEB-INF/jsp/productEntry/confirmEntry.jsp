<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>
<%@page import="org.belex.arrival.Arrival"%>
<%
Arrival arrival = (Arrival) request.getAttribute("arrival");
%>
<%@page import="org.belex.product.Product"%>
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
			${arrival.entry.product.description} (${arrival.entry.product.productCode})<br>
		</p>
		<table>
			<tr>
				<td id="header">
					Conditionnement : <br>
				</td>
				<td id="item">
					<%
						Product.Unit selectedUnit = utb.getSelectedUnit(arrival.getEntry().getProduct());
					%>
					<%= Util.displayContitionnement(selectedUnit.getNumber(), selectedUnit.getConditionnement()) %>
					X ${arrival.entry.numberOfProduct}<br>
				</td>
			</tr>		
			<tr>
				<td id="header">
					Date de validité : <br>
				</td>
				<td id="item">
					${arrival.entry.product.validityDate}<br>
				</td>
			</tr>
			<tr>
				<td id="header">
					Numéro de lot : <br>
				</td>
				<td id="item">
					${arrival.entry.product.lotNumber}<br>
				</td>
			</tr>
		</table>
	
	</div>

	<form name="newEntryFrm" action="flowController.htm" method="get">
		<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
		<input type="hidden" name="_eventId_submit">
	</form>

</body>
</html>