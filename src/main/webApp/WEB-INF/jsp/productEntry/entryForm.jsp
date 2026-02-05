<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>
<%@page import="java.util.Vector"%>
<%@page import="org.belex.product.Product"%>
<%@page import="org.belex.product.Product.Unit"%>
<%
Arrival arrival = (Arrival)request.getAttribute("arrival");
%>
<html>
<head>
	<%@include file="/WEB-INF/jsp/productEntry/jspf/headHeader.jspf"%>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/css/general.css" type="text/css">
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>
	<script type="text/javascript">
		<!--
			function afterBarCodeEvent(data){
				window.document.getElementById('searchCodeInput').value = data;
				if (checkFrm(window.document.nextFrm)) {
					window.document.nextFrm.submit();
				}
			}

			function sendListOfConfirmed(){
				window.document.listOfConfirmedFrm.submit();
			}

			function checkFrm(obj) {

				if (getRadioValue(obj, "searchTypeInput") == 1) {
					obj.searchValue.value = obj.searchCodeInput.value;
					obj.searchType.value = 1;
					if (obj.searchExactMatchInput.checked){
						obj.searchExactMatch.value = 1;
					} else {
						obj.searchExactMatch.value = 0;
					}
				} else {
					obj.searchValue.value = obj.searchLabelInput.value;
					obj.searchExactMatch.value = 0;
					obj.searchType.value = 2;
				}
				
				<%/* if ( arrival.getSupplier().getSupplierCode().equals(Constants.SUPPLIER_STOCK) ) { */%>
					//obj.searchOnSupplier.value = "<%= Constants.SUPPLIER_STOCK %>";
				<%/* } else { */%>
					if (obj.searchOnSupplierBox.checked){
						obj.searchOnSupplier.value = "${arrival.supplier.supplierCode}";
					} else {
						obj.searchOnSupplier.value = "";
					}
				<%/* } */%>

				if (strtrim(obj.searchValue.value).length < 2) {
					alert("Selection trop large, veuillez introduire plus de critères de recherche.");
					return false;
				}

				return true;
			}

			function sendCondtionnementFrm(productCodeSelected, unitIndexSelected) {
				var dataFrm = window.document.dataFrm;
				dataFrm.productCodeSelected.value = productCodeSelected;
				dataFrm.unitIndexSelected.value = unitIndexSelected;
				dataFrm.submit();				
			}
			function sendNewProductFrm(){
				window.document.newProductFrm.submit();
			}
			function setFocus() {
				window.document.getElementById('searchCodeInput').focus();
			}
		//-->
	</script>
</head>
<body onload="CheckTC();readdata();setFocus();" onunload="stop_listen()">
	<%@ include file="/WEB-INF/jsp/productEntry/jspf/bodyHeader.jspf" %>
	<div>
		<font id="pageTitle">
			Arrivage de marchandises - ${arrival.supplier.supplierName} (${arrival.supplier.supplierCode})<br>
		</font>
	
		<hr>
		
		<c:if test="${fn:length(arrival.savedEntries) > 0}">
			<font id="pageSubTitle">
				Nombre d'<a href="javascript:sendListOfConfirmed()">article introduit</a> pour cette session : ${fn:length(arrival.savedEntries)}
			</font>
			<form name="listOfConfirmedFrm" action="flowController.htm">
				<input type="hidden" name="_eventId_listOfConfirmedEntry">
				<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
			</form>
		</c:if>

		<form name="nextFrm" action="flowController.htm" method="get" onsubmit="return checkFrm(this);">
			<input type="hidden" name="_eventId_submit" value="">
			<input type="hidden" name="searchValue">
			<input type="hidden" name="searchExactMatch">
			<input type="hidden" name="searchType">
			<input type="hidden" name="searchOnSupplier">
			<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
			
			<%
			String objFormName = "window.document.nextFrm";
			boolean writeTableHeader = true;
			%>
			<%@include file="/WEB-INF/jsp/productEntry/jspf/searchInput.jspf" %>

			<br>

			<input class="button" type="submit" value=" Rechercher "> <input class="button" type="button" value=" Nouveau produit " onclick="sendNewProductFrm()">
		</form>

		<form name="newProductFrm" action="flowController.htm">
			<input type="hidden" name="_eventId_newProduct">
			<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
		</form>

		<c:if test="${fn:length(arrival.searchValue) != 0}">
			<c:if test="${fn:length(products) == 0}">
				<H1 style="color: #FF0000">Produit inconnu</H1>
			</c:if>
			<c:if test="${fn:length(products) > 0}">
			
				<form id="dataFrm" name="dataFrm" action="flowController.htm" method="get">
					<input type="hidden" name="productCodeSelected">
					<input type="hidden" name="unitIndexSelected">
					<input type="hidden" name="_eventId_productSelection">
					<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
				</form>
	
				<table border="1" width="95%" cellspacing="0" cellpadding="2">
					<%
					Vector<Product> products = (Vector<Product>)request.getAttribute("products");
					for (int i = 0; i < products.size(); i++) { 
						Product currentProduct = products.get(i); 
						%>
					
						<tr> 
							<td colspan="2" style="padding:10px">

								<%= currentProduct.getDescription() %> <%= currentProduct.getProductCode() %><br>
	
								<table border="2" cellspacing="10" cellpadding="15">
									<tr> 
										<% if (currentProduct.getUnits().size() > 0) { 
											Vector<Product.Unit> units = currentProduct.getUnits();
											
											for (int j=0; j < units.size(); j++) { 
												Unit unit = units.get(j); %>
									        	<td id="item" onclick="sendCondtionnementFrm('<%= currentProduct.getProductCode() %>', <%= j %>);">
													<%= unit.getNumber() %> 
													X <%= unit.getConditionnement() %>
													<br>
												</td>
											<% } %>
								        	<!--td id="item" onclick="sendCondtionnementFrm('<%= currentProduct.getProductCode() %>', <%= units.size() %>);">
												Autre<br>
											</td-->
										<% } else { %>
								        	<td id="item" onclick="sendCondtionnementFrm('<%= currentProduct.getProductCode() %>', 0);">
												Conditionnement non répertorié<br>
											</td>
										<% } %>
									</tr>
								</table>
							</td>
						</tr>
					<% } %>
				</table>
			</c:if>
		</c:if>
	</div>
	<div style="padding-top:30px">
		<table>
			<tr>
				<td>
					<form action="flowController.htm">
						<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
						<input class="button" type="submit" value=" Status de la commande " name="_eventId_entryStatus">
					</form>
				</td>
				<%-- c:if test="${fn:length(arrival.savedEntries) > 0}" --%>
					<td>
						<form action="flowController.htm" onsubmit="return confirm('Etes-vous certain de le clôturer?')">
							<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
							<input class="button" type="submit" value=" Clôture de l'encodage " name="_eventId_saveEntry">
						</form>
					</td>
				<%-- /c:if --%>
			</tr>
		</table>
	</div>

	<%@ include file="/WEB-INF/jsp/productEntry/jspf/barCode/simulator.jspf" %>

</body>
</html>
