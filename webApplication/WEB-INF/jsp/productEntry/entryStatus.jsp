<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>

<%@page import="java.util.Vector"%>
<%@page import="org.belex.entry.Entry"%>
<%@page import="org.belex.arrival.Arrival"%>
<%@page import="org.belex.product.Product"%>
<%@page import="org.belex.util.*"%>

<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.belex.product.Product.Unit"%>
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
			Arrivage de marchandises - ${arrival.supplier.supplierName} (${arrival.supplier.supplierCode})<br>
		</font>
	
		<hr>
	
		<font id="pageSubTitle">
			Articles commandés et reçus<br>
		</font>
		<% Arrival arrival = (Arrival) request.getAttribute("arrival"); %>
		
			<form name="selectEntryFrm" action="flowController.htm">
				<input type="hidden" name="searchValue">
				<input type="hidden" name="searchExactMatch" value="1">
				<input type="hidden" name="searchType" value="1">
				<%-- if ( StringUtils.equals(arrival.getSupplier().getSupplierCode(),"01") ) { --%>
						<!-- input type="hidden" name="searchOnSupplier" value=""-->
				<%-- } else { --%>
						<input type="hidden" name="searchOnSupplier" value="${arrival.supplier.supplierCode}">
				<%-- } --%>
				<input type="hidden" name="_eventId_selectProductDisplayed">
				<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
			</form>
	
			<c:if test="${fn:length(arrival.savedEntries) == 0}">
				Aucun arrivage d'article introduit 
			</c:if>
	
			<c:if test="${fn:length(arrival.supplier.requestedEntries) > 0}">
	
				<table border="0" width="95%" cellspacing="0" cellpadding="2">
					<%
					Vector<Entry> requestedEntries = arrival.getSupplier().getRequestedEntries();

					for (int i=0; i < requestedEntries.size() ; i++ ) { 

						Entry currentRequestedEntry = requestedEntries.get(i);
						Product.Unit selectedUnit = utb.getSelectedUnit(currentRequestedEntry.getProduct());
						
						boolean linkAllowed = false;

						if (Util.containsExactEntry(arrival.getSavedEntries(), currentRequestedEntry)) { %>
							<tr> 							
								<td colspan="2" style="padding:10px">
									<font size="4px" color="green">
						<% } else { %>
							<tr> 
								<td colspan="2" style="padding:10px" 
									<% 
									Vector<Product.Unit> units = currentRequestedEntry.getProduct().getUnits();
									if (request.getServerName().indexOf("127.0.0.1") != -1 ||
										( units != null && units.size() == 1 && 
										  StringUtils.isEmpty(units.get(0).getEan())
										 ) 
										) { 
										linkAllowed = true; %>
										onclick="sendSelectEntryFrm('<%=currentRequestedEntry.getProduct().getProductCode()%>')"
									<% } %>
								>
									<font size="4px" color="red">
						<% } %>
										<% if (linkAllowed) { %>
											=> 
										<% } %>
										<%= currentRequestedEntry.getProduct().getDescription()%> (<%=currentRequestedEntry.getProduct().getProductCode()%>) - <%=currentRequestedEntry.getNumberOfProduct() %> X (<%= Util.displayContitionnement(selectedUnit.getNumber(), selectedUnit.getConditionnement()) %>)<br>
									</font>
								</td>
							</tr>
					<% } %>
				</table>
		</c:if>
		<p>
			<form name="backFrm" action="flowController.htm">
				<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
				<input class="button" type="submit" value=" << Retour " name="_eventId_entryForm">
			</form>
		</p>
	</div>
</body>
</html>