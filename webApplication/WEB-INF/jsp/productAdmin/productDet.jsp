<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>

<%@page import="org.belex.product.Product"%>
<%@page import="java.util.Vector"%>
<%@page import="org.belex.requestparams.RequestParams"%>
<%@page import="org.belex.supplier.Supplier"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<html>
<head>
	<title>Belex - Détail d'un article</title>
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin.css">
	<script type="text/javascript">
	<!--
		function validateUpdateProductCode() {
			var frm = window.document.frmUpdateProductCode;
			if (frm.adminNewProductCode.value == "") {
				alert("Veuillez introduire un code produit")
				return false;
			}
			return confirm("Etes vous certain de modifier ce code produit ?");
		}
	//-->
	</script>
</head>

<body class="content" style="padding-top:37px">

<!-- H1>Résultat de la recherche d'articles</h1-->

<!-- 
<p class="instruction-text">Instruction text</p>
-->

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr valign="top">
		<td class="tabs-on" width="1%" nowrap height="19">
			Détail d'un article
		</td>
		<td class="blank-tab" width="99%" nowrap height="19">
			<img src="onepix.gif" width="1" height="27" align="absmiddle" alt="">
		</td>
	</tr>
</table>

<table border="0" cellpadding="10" cellspacing="0" valign="top"
	width="100%" summary="Framing Table">
	<tr>
		<td class="layout-manager">
			<%
			Object objProducts = request.getAttribute("products");
			Object objSuppliers = request.getAttribute("suppliers");
			Object objRequestParams = request.getAttribute("requestParams");
			
			if (objRequestParams == null || ! (objRequestParams instanceof RequestParams) || objProducts == null || ! (objProducts instanceof Vector) || objSuppliers == null || ! (objSuppliers instanceof Vector)) { %>

				<table class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3 width=100%>
					<tr>
						<td class="column-head-prefs">
							Suite à un problème technique, pas de détail disponible.<br>
						</td>
					</tr>
				</table>
				
			<% } else {

				request.getAttribute("products");
				Vector<Product> products = (Vector<Product>) objProducts;
				Vector<Supplier> suppliers = (Vector<Supplier>) objSuppliers;

				if (products.size() == 0) { %>
				
					<table class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3 width=100%>
						<tr>
							<td class="column-head-prefs">
								Suite à un problème technique, il n'y a pas de détail disponible.<br>
							</td>
						</tr>
					</table>
					
				<% } else { 
					
					RequestParams requestParams = (RequestParams) objRequestParams;
					Product product = products.get(Integer.parseInt(requestParams.getDetailIndex())); 
					%>
					
					<table class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3 width=100%>
						<tr>
							<td width="20%" class="table-text-bold">
								Description<br>
							</td>
							<td width="80%" class="table-text">
								<%= product.getDescription() %><br>
							</td>				
						</tr>
						<tr>
							<td width="20%" style="vertical-align: middle;" class="table-text-bold">
								Code<br>
							</td>
							<td width="80%" class="table-text">
								<% if (product.getProductCode().endsWith("TBD")) { %>

									<form name="frmUpdateProductCode" action="flowController.htm" method="get" target="rightFrame" onsubmit="return validateUpdateProductCode()">
										<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
										<input type="hidden" name="_eventId_updateProductCode" value="">
										<input type="hidden" name="adminOldProductCode" value="<%= product.getProductCode() %>">
										<input type="text"   name="adminNewProductCode" value="<%= product.getProductCode() %>" maxlength="10"> 
											ENTER après modification
									</form>
									
									<form name="frmdeleteProduct" action="flowController.htm" method="get" target="rightFrame" onsubmit="return validateUpdateProductCode()">
										<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
										<input type="hidden" name="_eventId_deleteProductCode" value="">
										<input type="hidden" name="adminOldProductCode" value="<%= product.getProductCode() %>">
										<input type="submit" value=" Suppression "> 
									</form>

								<% } else { %>
									<%= product.getProductCode() %><br>
								<% } %>
							</td>
						</tr>
					</table>
					<%
					Vector<Product.Unit> units = product.getUnits();
					for (int j = 0 ; j < units.size() ; j ++) {
						Product.Unit unit = units.get(j); %>
						<table class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3 width=100%>
							<tr>
								<td class="column-head-name" colspan="2">
									Conditionnement <%= j + 1 %><br>
								</td>				
							</tr>
							<tr>
								<td class="table-text-bold" width="20%">
									Nombre d'article par conditionnement unitaire
								</td>
								<td class="table-text" width="80%">
									<%= unit.getNumber() %>
								</td>
							</tr>
							<tr>
								<td class="table-text-bold">
									Conditionnement unitaire
								</td>
								<td class="table-text">
									<%= unit.getConditionnement() %>
								</td>
							</tr>
							<tr>
								<td class="table-text-bold">
									Fournisseur
								</td>
								<td class="table-text">
									<%= utb.getSupplierName(suppliers, unit.getSupplierCode()) %>
								</td>
							</tr>
							<tr>
								<td class="table-text-bold">
									Code ean
								</td>
								<td class="table-text">
									<%= unit.getEan() %>
								</td>
							</tr>
						</table>
					<% } %>
				<% } %>
			<% } %>
		</td>
	</tr>
</table>

</body>
</html>
