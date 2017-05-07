<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>

<%@page import="java.util.Vector"%>
<%@page import="org.belex.entry.Entry"%>
<%@page import="org.belex.arrival.Arrival"%>
<%@page import="org.belex.product.Product"%>
<html>
<head>
	<%@include file="/WEB-INF/jsp/productEntry/jspf/headHeader.jspf"%>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/css/general.css" type="text/css">
	<script type="text/javascript">
		<!--
			function sendSelectEntryFrm(id){
				window.document.selectEntryFrm.searchValue.value = id;
				window.document.selectEntryFrm.searchType.value = 1;
				window.document.selectEntryFrm.submit();
			}
		//-->
	</script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/productEntry/jspf/bodyHeader.jspf" %>
	<div id="content">
		<font id="pageTitle">
			Marchandises attendues pour ${arrival.supplier.supplierName} (${arrival.supplier.supplierCode})<br>
		</font>

		<hr>

		<form name="selectEntryFrm" action="flowController.htm">
			<input type="hidden" name="searchValue">
			<input type="hidden" name="searchExactMatch" value="1">
			<input type="hidden" name="searchType" value="1">
			<input type="hidden" name="searchOnSupplier" value="${arrival.supplier.supplierCode}">
			<input type="hidden" name="_eventId_selectProductDisplayed">
			<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
		</form>

		<p>
			<form name="backFrm" action="<%= request.getContextPath() %>/iindex.jsp">
				<input class="button" type="submit" value=" << Retour ">
			</form>
		</p>

		<table border="0" width="95%" cellspacing="0" cellpadding="2">
			<%
			Arrival arrival = (Arrival) request.getAttribute("arrival");
			Vector<Entry> requestedEntries = arrival.getSupplier().getRequestedEntries();

			for (int i=0; i < requestedEntries.size() ; i++ ) { 

				Entry currentRequestedEntry = requestedEntries.get(i);
				Product.Unit selectedUnit = utb.getSelectedUnit(currentRequestedEntry.getProduct()); %>

					<tr>
						<td colspan="2" style="padding:10px" 
							onclick="sendSelectEntryFrm('<%=currentRequestedEntry.getProduct().getProductCode()%>')"
						>
							<%= currentRequestedEntry.getProduct().getDescription()%> (<%=currentRequestedEntry.getProduct().getProductCode()%>) - <%=currentRequestedEntry.getNumberOfProduct() %> X (<%= Util.displayContitionnement(selectedUnit.getNumber(), selectedUnit.getConditionnement()) %>)<br>
						</td>
					</tr>
			<% } %>
		</table>
		<p>
			<form name="backFrm" action="<%= request.getContextPath() %>/iindex.jsp">
				<input class="button" type="submit" value=" << Retour ">
			</form>
		</p>
	</div>
</body>
</html>