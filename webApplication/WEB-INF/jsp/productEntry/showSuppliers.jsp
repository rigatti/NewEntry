<%@include file="/WEB-INF/jspf/globalHeader.jspf"%>
<%@page import="org.belex.arrival.Arrival"%>
<%@page import="java.util.Vector"%>
<%@page import="org.belex.supplier.Supplier"%>
<%
Arrival arrival = (Arrival)request.getAttribute("arrival");
Vector<Supplier> suppliers = arrival.getSuppliers();
%>
<html>
<head>
	<%@include file="/WEB-INF/jsp/productEntry/jspf/headHeader.jspf"%>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/css/general.css" type="text/css">
	<script type="text/javascript">
		<!--
			function sendSupplier(id, name) {
				var newEntryFrm = window.document.newEntryFrm;

				document.getElementById("supplier.supplierCode").value = id;
				document.getElementById("supplier.supplierName").value = name;

				newEntryFrm.submit();
			}
			function inverseShowDiv(divId) {
				var divObj = window.document.getElementById(divId);
				if (divObj.style.visibility == "hidden"){
					divObj.style.visibility = "visible";
					divObj.style.display = "block";
				} else {
					divObj.style.visibility = "hidden";
					divObj.style.display = "none";
				}
			}
		//-->
	</script>
</head>
<body>

	<% if ( suppliers.size() > 0 ) { %>

		<form name="newEntryFrm" action="flowController.htm" method="post" target="_top">
			<input type="hidden" value="submit" name="_eventId_searchEntry">
			<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
			<input type="hidden" name="supplier.supplierCode" id="supplier.supplierCode" value="">
			<input type="hidden" name="supplier.supplierName" id="supplier.supplierName" value="">
		</form>
		<%
			boolean allowEntry = true;//false;
			if (arrival.getSearchSupplierDate().equals(Util.formatDate(null,null,"dd/MM/yyyy"))) {
				allowEntry = true;
			}
		%>
		<table width="100%" border="2" cellpadding="5" style="border-style: double;<%= allowEntry?"borderolor:#00DD00;":"borderolor:#FF0000;" %>" cellspacing="5">
			<% 
			int nbrOfColumn = 3; 
			if (suppliers.size() < 3) {
				nbrOfColumn = suppliers.size();
			}
			String width = (100 / nbrOfColumn) + "%";
			%>
			<tr>
			<% for(int i = 0; i < suppliers.size(); i++) { 
				
				Supplier supplier = suppliers.get(i); 
				%>
				
				<td width="<%=width%>" 
					<% if (allowEntry && (supplier.isOpened() || supplier.isPending() || supplier.isClosed()) ) { %>
						onclick="sendSupplier('<%= supplier.getSupplierCode() %>', '<%= supplier.getSupplierName() %>')" 
					<% } 
					if (supplier.isPending()) { %>
						bgcolor="#FF8000"
					<% } else if (supplier.isClosed()) { %>
						bgcolor="#00D000" 
					<% } else if (supplier.isOpened()) { %>
						bgcolor="#FF0000" 
					<% } %>
				>
					<%= supplier.getSupplierName() %>
				</td>
					
				<% if ( i > 0 && (i % (nbrOfColumn-1) == 0) ) { %>
					</tr>
					<% if ((i + 1) < suppliers.size() ) { %>
						<tr>
					<% } %>
				<% } %>
		
			<% } %>
		</table>
		
		<div style="padding-top:20px" class="lalign" onclick="inverseShowDiv('legend')">
			<u>Légende</u>
		</div>

		<div class="lalign" id="legend" style="display: none;visibility: hidden;">
			<table>
				<!-- tr>
					<td>
						<table width="100%" border="2" cellpadding="5" style="border-style: double;border-color:#00DD00;" cellspacing="5">
							<tr>
								<td>
									Encodage autorisé pour tout arrivage non clôturé
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="2" cellpadding="5" style="border-style: double;border-color:#FF0000;" cellspacing="5">
							<tr>
								<td>
									Uniquement consultation de l'encodage en cours ou clôturé est autorisé
								</td>
							</tr>
						</table>
					</td>
				</tr-->
				<tr>
					<td bgcolor="#FF0000">
						Encodage de l'arrivage de marchandise non commencé<br>
					</td>
				</tr>
				<tr>
					<td bgcolor="#FF8000">
						Encodage de l'arrivage de marchandise en cours et non clôturé<br>
					</td>
				</tr>
				<tr>
					<td bgcolor="#00D000">
						Encodage de l'arrivage de marchandise clôturé<br>
					</td>
				</tr>
				
			</table>
		</div>
	
	<% } %>

</body>
</html>