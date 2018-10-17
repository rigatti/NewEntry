<%@include file="/WEB-INF/jspf/globalHeader.jspf"%>
<%@page import="org.belex.entry.Entry"%>
<%@page import="org.belex.traceability.Traceability"%>
<%@page import="java.util.Vector"%>

<%
Traceability traceability = (Traceability) request.getAttribute("traceability");
RequestParams requestParams = (RequestParams) request.getAttribute("requestParams");

Vector<Entry> entries = new Vector<Entry>();
if (traceability != null) {
	entries = traceability.getEntries();
}
%>

<%@page import="org.belex.requestparams.RequestParams"%>
<%@page import="org.belex.customer.Customer"%>
<%@page import="org.belex.allocation.Allocation"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<html>
<head>
	<title>Belex - Tracabilité</title>

	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin.css">
</head>

<body class="content" style="padding-top:37px">

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr valign="top">
		<td class="tabs-on" width="1%" nowrap height="19">
			Resultats de la recherche<br>
		</td>
		<td class="blank-tab" width="99%" nowrap height="19">
			<img src="onepix.gif" width="1" height="27" align="absmiddle" alt="">
		</td>
	</tr>
</table>

<% if (entries == null || entries.size() == 0) { %>

	<table border="0" cellpadding="10" cellspacing="0" valign="top"	width="100%" summary="Framing Table">
		<tr>
			<td class="layout-manager">
				<table width="100%">
					<tr>
						<td valign="top">
							Aucun article ne correspond à la recherche<br>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

<% } else { 

	Entry entry = entries.get(Integer.valueOf(requestParams.getTraceExportIndexList())); %>

	<table border="0" cellpadding="10" cellspacing="0" valign="top"	width="100%" summary="Framing Table">
		<tr>
			<td class="layout-manager">
				<table class="framing-table" width=95% border=0 cellspacing=1 cellpadding=3 width=100%>
					<tr>
						<td width="20%" class="table-text-bold">
							Produit<br>
						</td>
						<td width="80%" class="table-text">
							<%= entry.getProduct().getDescription() == null ? "-" : entry.getProduct().getDescription() %><br>
						</td>				
					</tr>
					<tr>
						<td width="20%" class="table-text-bold">
							Code<br>
						</td>
						<td width="80%" class="table-text">
							<%= entry.getProduct().getProductCode() %><br>
						</td>				
					</tr>
					<tr>
						<td width="20%" class="table-text-bold">
							Ean<br>
						</td>
						<td width="80%" class="table-text">
							<% if ( entry.getProduct().getUnits() != null && entry.getProduct().getUnits().size() == 1 ) { %>
								<%= entry.getProduct().getUnits().get(0).getEan() %><br>
							<% } %>
						</td>				
					</tr>
					<tr>
						<td width="20%" class="table-text-bold">
							Quantité<br>
						</td>
						<td width="80%" class="table-text">
							<% if (entry.getNumberOfProduct() > 0) { %>
								<%= entry.getNumberOfProduct() %> 
								<% if ( entry.getProduct().getUnits() != null && entry.getProduct().getUnits().size() == 1 ) { %>
									X <%= Util.displayContitionnement(entry.getProduct().getUnits().get(0).getNumber(), entry.getProduct().getUnits().get(0).getConditionnement()) %>
								<% } %>
							<% } %><br>
						</td>				
					</tr>					
					<tr>
						<td width="20%" class="table-text-bold">
							Fournisseur<br>
						</td>
						<td width="80%" class="table-text">
							<%= entry.getSupplier().getSupplierName() %> (<%= entry.getSupplier().getSupplierCode() %>)<br>
						</td>				
					</tr>
					<% if (entry.getSupplierDocumentType() > 0) { %>
						<tr>
							<td width="20%" class="table-text-bold">
							<% if (entry.getSupplierDocumentType() == 1) { %>
								Facture<br>	
							<% } else if (entry.getSupplierDocumentType() == 2) { %>
								Note d'envoi<br>
							<% } else { %>
								Référence fournisseur<br>
							<% } %>
							</td>
							<td width="80%" class="table-text">
								<%= entry.getSupplierDocumentDescription() %>
							</td>
						</tr>
					<% } %>
					<tr>
						<td width="20%" class="table-text-bold">
							Date d'entrée<br>
						</td>
						<td width="80%" class="table-text">
							<%= Util.formatDate(entry.getArrivalDate(), "yyyyMMdd", "dd/MM/yyyy") %><br>
						</td>				
					</tr>
					<tr>
						<td width="20%" class="table-text-bold">
							Date de validité<br>
						</td>
						<td width="80%" class="table-text">
							<%= entry.getProduct().getValidityDate() %><br>
						</td>				
					</tr>
					<tr>
						<td width="20%" class="table-text-bold">
							Numéro de lot<br>
						</td>
						<td width="80%" class="table-text">
							<%= entry.getProduct().getLotNumber() %><br>
						</td>				
					</tr>
				</table>
				<br>
				<table class="framing-table" width=95% border=0 cellspacing=1 cellpadding=3 width=100%>
					<tr>
						<td width="20%" class="table-text-bold">
							Destinataire(s)<br>
						</td>
						<td width="80%" class="table-text">
							<%
							boolean first = true;
							for (Customer customer : entry.getCustomers()) { 
								if ( ! first) {
									%><%= "<br>" %><%
								}
								first = false;
								if ( StringUtils.isNotEmpty(customer.getCode()) ) { %>
									<%= customer.getName() %><br> 
									<%= StringUtils.remove(customer.getAddressFurniture(), "\r\n" + customer.getName()) %><br>
									<% if (StringUtils.isNotEmpty(customer.getCustomerOrder().getFly().getAirportCode())) { %>
										<%= customer.getCustomerOrder().getFly().getAirportCode() + "/" + customer.getCustomerOrder().getFly().getFlyNumber() + "/" + customer.getCustomerOrder().getFly().getLtaNumber() %><br>
									<% } %>
								<% } else if (customer.isStockCustomer()){ %>
									En stock<br>
								<% } else if (customer.isSupplierReturnsCustomer()){ %>
									Retour fournisseur<br>
								<% } 
								if (customer.getCustomerEntry() != null) { %>
									Quantité : <%= customer.getCustomerEntry().getNumberOfUnit() %><br>
								<% }
								if (StringUtils.isNotEmpty(customer.getAllocationDate())) { %>
									Date d'attribution : <%= Util.formatDate(customer.getAllocationDate(), "yyyyMMdd", "dd/MM/yyyy") %><br>
								<% } %>
							<% } 
							if (first) { %>
								-
							<% } %>
						</td>				
					</tr>
				</table>
				<br>
				<center>
					<form name="frmData" action="flowController.htm" method="get">
						<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
						<input type="hidden" name="traceExportIndexList" value=";<%= requestParams.getTraceExportIndexList() %>;">
						<input type="submit" class="button" value=" Exporter " name="_eventId_export">
					</form>
				</center>
			</td>
		</tr>
	</table>

<% } %>

</body>
</html>