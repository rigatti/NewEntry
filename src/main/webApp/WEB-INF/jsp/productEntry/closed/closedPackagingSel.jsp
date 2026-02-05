<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>
<%@page import="java.util.Vector"%>
<%@page import="org.belex.product.Product"%>
<%@page import="org.belex.product.Product.Unit"%>
<html>
<head>
	<%@include file="/WEB-INF/jsp/productEntry/jspf/headHeader.jspf"%>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/css/general.css" type="text/css">
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>
	<script type="text/javascript">
		<!--
			function sendCondtionnementFrm(productCodeSelected, unitIndexSelected) {
				var dataFrm = window.document.dataFrm;
				dataFrm.productCodeSelected.value = productCodeSelected;
				dataFrm.unitIndexSelected.value = unitIndexSelected;
				dataFrm.submit();				
			}
		//-->
	</script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/productEntry/jspf/bodyHeader.jspf" %>
	<div>
		<font id="pageTitle">
			Séléction du type de conditionnement du produit recherché<br>
		</font>
	
		<hr>
		
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
</body>
</html>
