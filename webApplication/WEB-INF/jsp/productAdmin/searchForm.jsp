<%@page import="java.util.Vector"%>
<%@page import="org.belex.supplier.Supplier"%>
<%@page import="java.util.Enumeration"%>
<%@page import="org.belex.util.Util"%>
<html>
<head>
<%
	Vector<Supplier> suppliers = (Vector<Supplier>) request.getAttribute("suppliers");
%>
	<title>Gestion d'arcticle</title>
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>
	<script type="text/javascript">
	<!--
		function validateProductSearch(obj) {
			var dataFrm = window.document.frmProductSearch;
			var searchType = getRadioValue(dataFrm, "searchType");

			if (searchType == 1) {
				if (dataFrm.productCodeInput.value == "") {
					alert("Veuillez introduire un critère de recherche");
					dataFrm.productCodeInput.focus();
					return false;
				}
				dataFrm.searchValue.value = dataFrm.productCodeInput.value;
			} else {
				if (dataFrm.labelInput.value == "") {
					alert("Veuillez introduire un critère de recherche");
					dataFrm.labelInput.focus();
					return false;
				}
				dataFrm.searchValue.value = dataFrm.labelInput.value;
			}

			return true;
		}
		function sendTempProductSearch() {
			window.document.frmTempProductSearch.submit();
		}
		function switchBoxStatus(obj) {
			obj.checked = !obj.checked;
		}
	//-->
	</script>

	<script type="text/javascript" src="/scripts/general.js"></script>
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin.css">

</head>

<body class="content">

	<form name="adminFrm" action="<%= request.getContextPath() %>/admin.jsp" method="post" target="_top">
		<input type="hidden" name="admin" value="1">
	</form>

	<h1 onclick="window.document.adminFrm.submit()">
		Gestion des articles
	</h1>

	<!-- 
   	<p class="instruction-text">
		Instruction text
   	</p>
	-->

	<table border="0" cellpadding="0" cellspacing="0"  width="100%" >
		<tr valign="top"> 
    		<td class="tabs-on" width="1%" nowrap height="19">
		       Recherche
			</td>
		    <td class="blank-tab" width="99%" nowrap height="19">
        		<img src="onepix.gif" width="1" height="27" align="absmiddle" alt="">
		    </td>
		</tr>
	</table>
	<form name="frmTempProductSearch" action="flowController.htm" method="get" target="rightFrame">
		<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
		<input type="hidden" name="_eventId_productSearch" value="">
		<input type="hidden" name="searchValue" value="TBD">
		<input type="hidden" name="searchType" value="1">
		<input type="hidden" name="searchExactMatch" value="">
		<input type="hidden" name="searchOnSupplier" value="">		
	</form>	
	<form name="frmProductSearch" action="flowController.htm" method="get" target="rightFrame" onsubmit="return validateProductSearch(this)">
		<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
		<input type="hidden" name="_eventId_productSearch" value="">
		<input type="hidden" name="searchValue">
		
		<table border="0" cellpadding="10" cellspacing="0" valign="top" width="100%" summary="Framing Table">
			<tr> 
				<td class="layout-manager">
					<table class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3 width=100%>
						<tr>
							<td class="column-head-prefs">
								Recherche d'un article
							</td>
						</tr>
						<tr>
							<td class="table-text-bold" nowrap>
								<input type="radio" value="1" name="searchType"> <a class="ctext" href="javascript:void(0)" onclick="setRadioState(window.document.frmProductSearch,'searchType',0, true)">Code Produit :</a><br>
								<span style="padding-left:25px">
									<input type="text" name="productCodeInput" size="100px" value="" onfocus="setRadioState(window.document.frmProductSearch,'searchType',0, true)">
								</span>
							</td>
						</tr>
						<tr>
							<td class="table-text-bold" nowrap>
								<input type="radio" value="2" name="searchType"> <a class="ctext" href="javascript:void(0)" onclick="setRadioState(window.document.frmProductSearch,'searchType',1, true)">Libellé :</a><br>
								<span style="padding-left:25px">
									<input type="text" name="labelInput" size="100px" value="" onfocus="setRadioState(window.document.frmProductSearch,'searchType',1, true)">
								</span>
							</td>
						</tr>
					</table>
					<table class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3 width=100%>
						<tr>
							<td class="column-head-prefs" style="padding-left:10px">
								+ Options supplémrentaires
							</td>
						</tr>
						<tr>
							<td class="table-text-bold" nowrap>
								<input type="checkbox" value="1" name="searchExactMatch"> <a class="ctext" href="javascript:void(0)" onclick="switchBoxStatus(window.document.frmProductSearch.searchExactMatch)">Code produit exact</a><br>
							</td>
						</tr>
						<tr>
							<td class="table-text-bold" style="padding-left:5px" nowrap>
								Pour le fournisseur suivant : <br>
								<select name="searchOnSupplier">
									<option value="">-</option>
									<%
									Enumeration<Supplier> enumSuppliers = suppliers.elements();
									while (enumSuppliers.hasMoreElements()) { 
										Supplier supplier = enumSuppliers.nextElement(); %>
										<option value="<%= supplier.getSupplierCode() %>"><%= Util.getShortDisplayable(supplier.getSupplierName(), 25) %></option>
									<% } %>
								</select>
								<br>							
								<center style="padding: 10px">
									<input class="button" type="submit" value=" Recherche ">
								</center>
							</td>
						</tr>
					</table>
					<br>
					<br>
					<table class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3 width=100%>
						<tr>
							<td class="column-head-prefs">
								Recherche des articles temporaires
							</td>
						</tr>
						<tr>
							<td class="table-text" style="padding: 10px" nowrap>
								<center>
									<input class="button" type="button" value=" Recherche " onclick="sendTempProductSearch()">
								</center>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>