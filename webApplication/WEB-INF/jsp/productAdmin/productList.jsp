<%@page import="java.util.Vector"%>
<%@page import="org.belex.product.Product"%>
<%@page import="org.belex.util.Util"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<html>
<head>
	<title>Belex - Attribution des articles réceptionnés</title>
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/sort.js"></script>
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin.css">
	<script type="text/javascript">
	<!--
		function sendDetail(id) {
			window.document.frmDetail.detailIndex.value = id;
			window.document.frmDetail.submit();
		}
	//-->
	</script>
</head>

<body class="content" style="padding-top:37px">

<!-- H1>Résultat de la recherche d'articles</h1-->

<!-- 
<p class="instruction-text">Instruction text</p>
-->

<form name="frmDetail" action="flowController.htm" method="get" target="rightFrame">
	<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
	<input type="hidden" name="_eventId_productDet" value="">
	<input type="hidden" name="detailIndex" value="">
</form>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr valign="top">
		<td class="tabs-on" width="1%" nowrap height="19">
			Attribution
		</td>
		<td class="blank-tab" width="99%" nowrap height="19">
			<img src="onepix.gif" width="1" height="27" align="absmiddle" alt="">
		</td>
	</tr>
</table>

<table border="0" cellpadding="10" cellspacing="0" valign="top"	width="100%" summary="Framing Table">
	<tr>
		<td class="layout-manager">
			<%
			Object obj = request.getAttribute("products");
			if (obj == null || ! (obj instanceof Vector)) { %>
				
				<table class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3>
					<tr>
						<td class="column-head-prefs">
							Veuillez introduire un critère de recherche
						</td>
					</tr>
				</table>
			<% } else {
				
				Vector<Product> products = (Vector<Product>) obj;
				if (products.size() == 0) { %>
				
					<table class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3 width=100%>
						<tr>
							<td class="column-head-prefs">
								Aucun article ne correspond à vos critères de recherche<br>
							</td>
						</tr>
					</table>
					
				<% } else { %>
					
					<table id="productList" class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3 width=100%>
						<thead>
							<tr>
								<td class="column-head-name sortable" onclick="sortTable(document.getElementById('productList'), 0, 'String', null, null, null, null)">
									Libellé
								</td>
								<td class="column-head-name sortable" onclick="sortTable(document.getElementById('productList'), 1, 'String', null, null, null, null)">
									Code produit
								</td>
								<td class="column-head-name">
									Conditionnement et date de dernière modification 
								</td>
							</tr>
						</thead>
						<tbody>
							<% for (int i = 0 ; i < products.size() ; i++) { 
								Product product = products.get(i);
								Vector<Product.Unit> units = product.getUnits();
								if (StringUtils.isNotBlank(product.getDescription())) { %>
									<tr>
										<td class="table-text-bold">
											<%
											String strTmp = product.getDescription().trim();
											if (strTmp.equals("")) {
												strTmp = "Inconnu";
											}
											if (product.getProductCode().endsWith("TBD")) { %>
												<a class="ctextunderlined" href="javascript:sendDetail(<%= i %>)">
											<% } %>
											<%= strTmp %>
											<% if (product.getProductCode().endsWith("TBD")) { %>
												</a>
											<% } %>
										</td>
										<td class="table-text" valign="top">
											<%= product.getProductCode() %>
										</td>
										<td class="table-text">
											<table width="100%" height="100%" cellpadding="0" cellspacing="0">
												<% for (int j = 0 ; j < units.size() ; j ++) {
													Product.Unit unit = units.get(j); %>
													<tr>
														<td class="table-text" width="50%">
															<%=Util.displayContitionnement(unit.getNumber(), unit.getConditionnement()) %><br>
														</td>
														<td class="table-text">
															<%= unit.getEan() %><br>
														</td>
														<td class="table-text">
															<%= unit.getDateLastMofication() %><br>
														</td>
													</tr>
												<% } %>
											</table>
										</td>
									</tr>
							<% }
							} %>
						</tbody>
					</table>
				<% }
			} %>
		</td>
	</tr>
</table>

</body>
</html>
