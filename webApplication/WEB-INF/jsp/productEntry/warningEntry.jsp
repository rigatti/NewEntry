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
</head>
<body>
	<%@ include file="/WEB-INF/jsp/productEntry/jspf/bodyHeader.jspf" %>
	<div>
		<font id="pageTitle">
			Arrivage de marchandises - ${arrival.supplier.supplierName} (${arrival.supplier.supplierCode})<br>
		</font>

		<hr>

		<form name="listOfConfirmedFrm" action="flowController.htm">
			<input type="hidden" name="_eventId_listOfConfirmedEntry">
			<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
		</form>
		
		ATTENTION, un produit similaire a déjà été introduit lors de cet session d'encodage<br>
		<i>(Conditionnement identiques)</i><br>

		<p>
			Produit à confirmer : <br>
		</p>
		<p>
			
			<%= arrival.getEntry().getProduct().getDescription() %><br>
	
			<% Product.Unit selectedUnit = utb.getSelectedUnit(arrival.getEntry().getProduct());%>
			
			<%= Util.displayContitionnement(selectedUnit.getNumber(), selectedUnit.getConditionnement()) %> 
			X <%= arrival.getEntry().getNumberOfProduct() %><br>

		</p>
		
		<p>
			<form action="flowController.htm">
				<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
				<input class="button" type="submit" name="_eventId_confirm" value=" Confirmer l'encodage ">
				<input class="button" type="button" onclick="window.history.back()" value=" Annuler ">
			</form>
		</p>

	</div>

</body>
</html>
