<%@page import="org.belex.allocation.Allocation"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.belex.allocation.AllocationEntry"%>
<%@page import="org.belex.customer.CustomerOrder"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.belex.util.Util"%>
<%@page import="org.belex.product.ProductTraceable"%>
<%@page import="org.belex.product.ProductTraceable.Destination"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.belex.requestparams.RequestParams"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="java.util.HashSet"%>
<html>
<head>
	<title>Belex - Liste d'articles</title>
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/sort.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/toolTip.js"></script>
	<script type="text/javascript">
		<!--
			var entryProducts = new Array(new Array);
			var customerOrders = new Array(new Array);

			function assign(step) {

				var entryProductItem = getRadioValue(window.document.frmData, "entryProductItem");
				
				if (entryProductItem == "" && step != "deleteOrder" && step != "renewOrder") {
					alert("Veuillez sélectionner un produit à attribuer");
					return false;
				}

				if (step != "deleteOrder" && step != "renewOrder") {
					<%-- to supplier entry (product obj) --%>
					window.document.frmAssign.allocSupplierCode.value = entryProducts[entryProductItem][0];
					window.document.frmAssign.allocProductCode.value = entryProducts[entryProductItem][1];
	
					window.document.frmAssign.allocNumberOfUnit.value = window.document.getElementById("numberOfUnit_" + entryProductItem).value;
				} else {
					window.document.frmAssign.allocNumberOfUnit.value = 0;
				}

				window.document.frmAssign.allocationStep.value = step;

				if (step == "substitution" || step == "deleteOrder" || step == "renewOrder") { 
					var customerOrderItem = getRadioValue(window.document.frmData, "customerOrderItem");

					if (customerOrderItem == "" && step == "substitution") {
						alert("Veuillez sélectionner un produit pour la substitution");
						return false;
					}
					
					if (customerOrderItem == "" && step == "deleteOrder") {
						alert("Veuillez sélectionner une commande client à annuler");
						return false;
					}

					if (customerOrderItem == "" && step == "renewOrder") {
						alert("Veuillez sélectionner une commande client à repasser");
						return false;
					}

					<%-- from customer order --%>
					window.document.frmAssign.allocOriginSupplierCode.value = customerOrders[customerOrderItem][0];
					window.document.frmAssign.allocSupplierOrderNumber.value = customerOrders[customerOrderItem][1];
					window.document.frmAssign.allocSupplierOrderLetter.value = customerOrders[customerOrderItem][2];
					window.document.frmAssign.allocCustomerOrderCode.value = customerOrders[customerOrderItem][3];
					window.document.frmAssign.allocCustomerOrderNumber.value = customerOrders[customerOrderItem][4];
					window.document.frmAssign.allocOriginProduct.value = customerOrders[customerOrderItem][5];
					window.document.frmAssign.allocOriginNumberOfProduct.value = customerOrders[customerOrderItem][6];
					window.document.frmAssign.allocOriginNumberOfUnit.value = customerOrders[customerOrderItem][7];
					window.document.frmAssign.allocOriginUnitConditionnement.value = customerOrders[customerOrderItem][8];
					window.document.frmAssign.allocCustomerCode.value = customerOrders[customerOrderItem][9];
					window.document.frmAssign.allocCustomerName.value = customerOrders[customerOrderItem][10];
					window.document.frmAssign.allocPackagingFromBasket.value = customerOrders[customerOrderItem][11];
					window.document.frmAssign.allocPackagingToBasket.value = customerOrders[customerOrderItem][12];
				} 

				if (step == "stock" || step == "supplier" ) { 
					// send back to supplier
					// or for the stock
					window.document.frmAssign.allocOriginSupplierCode.value = "";
					window.document.frmAssign.allocSupplierOrderNumber.value = 0;
					window.document.frmAssign.allocSupplierOrderLetter.value = "";
					window.document.frmAssign.allocCustomerOrderCode.value = "";
					window.document.frmAssign.allocCustomerOrderNumber.value = 0;
					window.document.frmAssign.allocOriginProduct.value = "";
					window.document.frmAssign.allocOriginNumberOfProduct.value = 0;
					window.document.frmAssign.allocOriginNumberOfUnit.value = 0;
					window.document.frmAssign.allocOriginUnitConditionnement.value = "";
					window.document.frmAssign.allocCustomerCode.value = "";
					window.document.frmAssign.allocCustomerName.value = "";
					window.document.frmAssign.allocPackagingFromBasket.value = 0;
					window.document.frmAssign.allocPackagingToBasket.value = 0;
				}

				window.document.frmAssign.submit();
			}

			function changeBgd(objectPrefix, productsIndex) {
				// reset all td bgd
				var i = 0;
				while (true) {
					if (window.document.getElementById(objectPrefix + i) != null) {
						window.document.getElementById(objectPrefix + i).className = "table-text-bold-middle";
					} else {
						break;
					} 
					i++;
				}

				var radioTd = window.document.getElementById(objectPrefix + productsIndex);
				radioTd.className = "column-head-name";
			}

			function checkReportRequest() {
				if ( 
					window.document.getElementById("allocReportSubstitution").checked == false && 
					window.document.getElementById("allocReportStock").checked == false &&
					window.document.getElementById("allocReportSupplier").checked == false &&
					window.document.getElementById("allocReportDeletedOrder").checked == false &&
					window.document.getElementById("allocReportRenewedOrder").checked == false &&
					window.document.getElementById("allocReportStandard").checked == false
					) {
					alert("Veuillez sélectionner un type de produit pour la génération du raport de colisage.");
					return false;
				}
				return true;
			}
		//-->
	</script>
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin.css">
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/toolTip.css">
</head>

<body class="content">

<!-- H1>Résultat de la recherche d'articles</h1-->

<!-- 
<p class="instruction-text">Instruction text</p>
-->
	<form name="adminFrm" action="<%= request.getContextPath() %>/admin.jsp" method="post" target="_top">
		<input type="hidden" name="admin" value="1">
	</form>
	<%
	Allocation allocation = (Allocation) request.getAttribute("allocation"); 

	// retrieve the suppliers requested for the current screen.
	RequestParams requestParams = (RequestParams) request.getAttribute("requestParams"); 

	HashSet<String> suppliers = new HashSet<String>(); 
	StringTokenizer stOrders = new StringTokenizer(requestParams.getAllocationEntryOrders(), "#");
	while (stOrders.hasMoreElements()) {
		StringTokenizer stOrder = new StringTokenizer((String) stOrders.nextElement(), ";");
		if (stOrder.countTokens() == 3) {
			String strTmp = (String) stOrder.nextElement();
			String supplierCode = strTmp.substring(strTmp.indexOf("=") + 1);
			if ( ! suppliers.contains(supplierCode)) {
				suppliers.add(supplierCode);
			}
		}
	} %>


	<h1 onclick="window.document.adminFrm.submit()">
		Attribution des articles réceptionnés 
		<% if (allocation != null && suppliers.size() > 0) { %>
			: 
			<% 
			boolean isFirst = true;
			for (String supplierCode : suppliers) {
				if ( ! isFirst) { %>, <% } %>
				<%= allocation.getSupplierName(supplierCode) %>
				<% if (isFirst) {
					isFirst = false;
				}
			}
		} %>
	</h1>

<form name="frmAssign" action="flowController.htm" method="get">
	<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
	<input type="hidden" name="_eventId_updateAllocation" value="">

	<input type="hidden" name="allocationStep" value="">
	<input type="hidden" name="allocOriginSupplierCode" value="">
	<input type="hidden" name="allocSupplierOrderNumber" value="">
	<input type="hidden" name="allocSupplierOrderLetter" value="">
	<input type="hidden" name="allocCustomerOrderCode" value="">
	<input type="hidden" name="allocCustomerOrderNumber" value="">
	<input type="hidden" name="allocOriginProduct" value="">
	<input type="hidden" name="allocOriginNumberOfUnit" value="0">
	<input type="hidden" name="allocOriginNumberOfProduct" value="0">
	<input type="hidden" name="allocOriginUnitConditionnement" value="">
	<input type="hidden" name="allocPackagingFromBasket" value="">
	<input type="hidden" name="allocPackagingToBasket" value="">

	<input type="hidden" name="allocSupplierCode" value="">
	<input type="hidden" name="allocNumberOfUnit" value="">
	<input type="hidden" name="allocProductCode" value="">
	<input type="hidden" name="allocCustomerCode" value="">
	<input type="hidden" name="allocCustomerName" value="">
</form>

<%
if (allocation == null) { %>

	<table border="0" cellpadding="0" cellspacing="0" width="100%">
		<tr valign="top">
			<td class="tabs-on" width="1%" nowrap height="19">
				Traitement des articles après clôture d'arrivage de marchandise
			</td>
			<td class="blank-tab" width="99%" nowrap height="19">
				<img src="onepix.gif" width="1" height="27" align="absmiddle" alt="">
			</td>
		</tr>
	</table>

	<table border="0" cellpadding="10" cellspacing="0" valign="top"	width="100%" summary="Framing Table">
		<tr>
			<td class="layout-manager">
				<table class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3 width=100%>
					<tr>
						<td class="column-head-prefs">
							Veuillez introduire un critère de recherche
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	
<% } else {

	List<CustomerOrder> cotas = allocation.getCustomerOrdersToAllocates();
	List<CustomerOrder> coas = allocation.getCustomerOrdersAllocateds();

	List<AllocationEntry> entries = allocation.getEntries();

	// check if data available
	boolean dataFound = true;
/*	
	if ( entries == null || entries.size() == 0 || 
				( (coas == null || coas.size() == 0) 
						&& 
				  (cotas == null || cotas.size() == 0) )
	    ) {

		dataFound = false;

	} 
*/
	if ( ! dataFound ) { %>
	
		<table border="0" cellpadding="10" cellspacing="0" valign="top"	width="100%" summary="Framing Table">
			<tr>
				<td class="layout-manager">
					<table class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3 width=100%>
						<tr>
							<td class="column-head-prefs">
									Aucune arrivée de marchandise/commande enregistrée pour les critères introduits
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br>
	
	<% } else { %>

		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr valign="top">
				<td width="1%" align="right">
					<input type="checkbox" id="checkboxWaiting" onclick="switchDisplay('spanFullWaiting');" checked> 
				</td>
				<td style="font-size: 60%" onclick="switchDisplay('spanFullWaiting');switchCheckBoxValue('checkboxWaiting');">
					Articles en attente de traitement <span id="subTitleWaiting"></span>
				</td>
				<td width="1%" align="right">
					<input type="checkbox" id="checkboxAssigned" onclick="switchDisplay('spanFullAssigned');">
				</td>
				<td style="font-size: 60%" onclick="switchDisplay('spanFullAssigned');switchCheckBoxValue('checkboxAssigned');">
					Articles assignés pour substitution <span id="subTitleAssigned"></span>
				</td>
				<td width="1%" align="right">
					<input type="checkbox" id="checkboxSupplier" onclick="switchDisplay('spanFullSupplier');">
				</td>
				<td style="font-size: 60%" onclick="switchDisplay('spanFullSupplier');switchCheckBoxValue('checkboxSupplier');">
					Articles assignés pour retour fournisseur <span id="subTitleSupplier"></span>
				</td>
				<td width="1%" align="right">
					<input type="checkbox" id="checkboxStock" onclick="switchDisplay('spanFullStock');">
				</td>
				<td style="font-size: 60%" onclick="switchDisplay('spanFullStock');switchCheckBoxValue('checkboxStock');">
					Articles assignés au stock <span id="subTitleStock"></span>
				</td>
				<td width="1%" align="right">
					<input type="checkbox" id="checkboxDeletedOrder" onclick="switchDisplay('spanFullDeletedOrder');">
				</td>
				<td style="font-size: 60%" onclick="switchDisplay('spanFullDeletedOrder');switchCheckBoxValue('checkboxDeletedOrder');">
					Commandes supprimées<span id="subTitleDeletedOrder"></span>
				</td>
				<td width="1%" align="right">
					<input type="checkbox" id="checkboxRenewedOrder" onclick="switchDisplay('spanFullRenewedOrder');">
				</td>
				<td style="font-size: 60%" onclick="switchDisplay('spanFullRenewedOrder');switchCheckBoxValue('checkboxRenewedOrder');">
					Nouvelles commandes<span id="subTitleRenewedOrder"></span>
				</td>
				<td width="1%" align="right">
					<input type="checkbox" id="checkboxAutomatic" onclick="switchDisplay('spanFullAutomatic');">
				</td>
				<td style="font-size: 60%" onclick="switchDisplay('spanFullAutomatic');switchCheckBoxValue('checkboxAutomatic');">
					Articles correctement attribués <span id="subTitleAutomatic"></span>
				</td>
			</tr>
		</table>

		<div id="waitingForTreatment">
			<%@ include file="/WEB-INF/jsp/productAllocation/jspf/allocationFormWaiting.jspf" %>
		</div>

		<div id="assignForSubstitution">
			<%@ include file="/WEB-INF/jsp/productAllocation/jspf/allocationFormAssigned.jspf" %>
		</div>

		<div id="assignForSupplier">
			<%@ include file="/WEB-INF/jsp/productAllocation/jspf/allocationFormSupplier.jspf" %>
		</div>

		<div id="assignForStock">
			<%@ include file="/WEB-INF/jsp/productAllocation/jspf/allocationFormStock.jspf" %>
		</div>

		<div id="deletedOrder">
			<%@ include file="/WEB-INF/jsp/productAllocation/jspf/allocationFormDeletedOrder.jspf" %>
		</div>

		<div id="renewedOrder">
			<%@ include file="/WEB-INF/jsp/productAllocation/jspf/allocationFormRenewedOrder.jspf" %>
		</div>
		
		<div id="automaticAssigment">
			<%@ include file="/WEB-INF/jsp/productAllocation/jspf/allocationFormAutomatic.jspf" %>
		</div>

		<br>

		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr valign="top">
				<td class="tabs-on" width="1%" nowrap height="19">
					Rapport
				</td>
				<td class="blank-tab" width="99%" nowrap height="19">
					<img src="onepix.gif" width="1" height="27" align="absmiddle" alt="">
				</td>
			</tr>
		</table>

		<form name="frmColisage" action="flowController.htm" method="get" target="rightFrame" onsubmit="return checkReportRequest()">
			<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
			<input type="hidden" name="_eventId_generateReport" value="">
			<table border="0" cellpadding="10" cellspacing="0" valign="top"	width="100%" summary="Framing Table">
				<tr>
					<td class="layout-manager">
						<table id="assignedProductsList" class="framing-table" width="20%" border=0 cellspacing=1 cellpadding=3 width=100%>
							<tr>
								<td colspan="2" class="column-head-prefs" nowrap>
									Sélection du type d'articles à inclure dans le rapport
								</td>
							</tr>
							<tr>
								<td class="table-text" witdh="50%" nowrap>
									<input type="checkBox" name="allocReportSubstitution">
									<span onclick="changeCheckBox('allocReportSubstitution')">Substitution</span>
								</td>
								<td class="table-text" witdh="50%"¨nowrap>
									<input type="checkBox" name="allocReportStock">
									<span onclick="changeCheckBox('allocReportStock')">Stock</span>
								</td>
							</tr>
							<tr>
								<td class="table-text" nowrap>
									<input type="checkBox" name="allocReportSupplier">
									<span onclick="changeCheckBox('allocReportSupplier')">Fournisseur</span>
								</td>
								<td class="table-text" nowrap>
									<input type="checkBox" name="allocReportStandard">
									<span onclick="changeCheckBox('allocReportStandard')">Traitement standard</span>
								</td>
							</tr>
							<tr>
								<td class="table-text" witdh="50%" nowrap>
									<input type="checkBox" name="allocReportRenewedOrder">
									<span onclick="changeCheckBox('allocReportRenewedOrder')">Recommande</span>
								</td>
								<td class="table-text" witdh="50%"¨nowrap>
									<input type="checkBox" name="allocReportDeletedOrder">
									<span onclick="changeCheckBox('allocReportDeletedOrder')">Suppression</span>
								</td>
							</tr>
						</table>
						<br>
						<input type="submit" class="button" value=" Génération du document ">
					</td>
				</tr>
			</table>
		</form>
		<br>

	<% } // end if datafound
} // end if allocation != null %>
<center>
<form name="frmBack" action="flowController.htm" method="get" target="_top">
	<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
	<input type="hidden" name="_eventId_back" value="">
	<input type="submit" class="button" value=" << Retour ">
</form>
</center>
</body>
</html>