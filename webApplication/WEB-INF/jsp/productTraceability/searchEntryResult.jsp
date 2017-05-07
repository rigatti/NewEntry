<%@include file="/WEB-INF/jspf/globalHeader.jspf"%>
<%@page import="org.belex.entry.Entry"%>
<%@page import="org.belex.traceability.Traceability"%>
<%@page import="java.util.Vector"%>

<%
Traceability traceability = (Traceability) request.getAttribute("traceability");
Vector<Entry> entries = new Vector<Entry>();
if (traceability != null) {
	entries = traceability.getEntries();
}
%>

<%@page import="org.apache.commons.lang.StringUtils"%>
<html>
<head>
	<title>Belex - Tracabilité</title>

	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/sort.js"></script>
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin.css">

	<script type="text/javascript">
	<!--
		function exportRequest(obj) {
			var exportIndexList = "";

			<% for (int i=0 ; i < entries.size() ; i++) { %>
				if (obj.entryToExport_<%= i %>.checked) {
					exportIndexList += ";<%= i %>;";
				}
			<% } %>

			if (exportIndexList == "") {
				alert("Veuillez sélectionner un produit à exporter.");
				return false;
			}

			obj.traceExportIndexList.value = exportIndexList;

			return true;

		}

		function sendDetailFrm(id) {
			var obj = window.document.frmDetail;
			obj.traceExportIndexList.value = id;
			obj.submit();
		}

		function switchBoxStatus(id) {
			if ( id >= 0) {
				var obj = window.document.getElementById("entryToExport_" + id);
				obj.checked = ! obj.checked;
			} else {
				var frmData = window.document.frmData;
				<% for (int i=0 ; i < entries.size() ; i++) { %>
					frmData.entryToExport_<%= i %>.checked = frmData.entryToExport_Global.checked;
				<% } %>
			}
		}
	//-->
	</script>
</head>

<body class="content" style="padding-top:37px">

<form name="frmDetail" action="flowController.htm" method="post">
	<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
	<input type="hidden" name="_eventId_productDet" value="">
	<input type="hidden" name="traceExportIndexList" value="">
</form>

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

<table border="0" cellpadding="10" cellspacing="0" valign="top"	width="100%" summary="Framing Table">
	<tr>
		<td class="layout-manager">
			<% if (traceability == null) { %>

				<table class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3 width=100%>
					<tr>
						<td class="column-head-prefs">
							Veuillez introduire un critère de recherche
						</td>
					</tr>
				</table>

			<% } else if (entries == null || entries.size() == 0) { %>
			
					<table class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3 width=100%>
						<tr>
							<td class="column-head-prefs">
								Aucun article ne correspond à la recherche<br>
							</td>
						</tr>
					</table>
			
			<% } else { %>
			
				<form name="frmData" action="flowController.htm" method="get" onsubmit="return exportRequest(this)">
					<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
					<input type="hidden" name="traceExportIndexList" value="">

					<table id="sortableTable" class="framing-table" width="20%" border=0 cellspacing=1 cellpadding=3>
						<thead>
							<tr>
								<td class="column-head-name" width="1%" onclick="sortTable(document.getElementById('sortableTable'), 0, 'String', null, null, null, null)" nowrap>
									Produit
								</td>
								<td class="column-head-name" width="1%" onclick="sortTable(document.getElementById('sortableTable'), 1, 'String', null, null, null, null)" nowrap>
									Code
								</td>
								<td class="column-head-name" width="1%" align="right" onclick="sortTable(document.getElementById('sortableTable'), 2, 'String', null, null, null, null)" nowrap>
									Ean
								</td>
								<td class="column-head-name" width="1%" align="right" onclick="sortTable(document.getElementById('sortableTable'), 3, 'String', null, null, null, null)" nowrap>
									Quantité
								</td>
								<td class="column-head-name" width="1%" onclick="sortTable(document.getElementById('sortableTable'), 4, 'Date', null, null, null, null)" nowrap>
									Date d'entrée
								</td>
								<td class="column-head-name" width="1%" onclick="sortTable(document.getElementById('sortableTable'), 5, 'String', null, null, null, null)" nowrap>
									Date de validité
								</td>
								<td class="column-head-name" width="1%" onclick="sortTable(document.getElementById('sortableTable'), 6, 'String', null, null, null, null)" nowrap>
									Numéro de lot
								</td>
								<td class="column-head-name" width="1%" nowrap>
									<input type="checkbox" name="entryToExport_Global" onclick="switchBoxStatus(-1)"><br>
								</td>
							</tr>
						</thead>
						<tbody>
							<%
							for (int i=0 ; i < entries.size() ; i++) { 
								Entry entry = entries.get(i); %>
								<tr>
									<td class="table-text-bold-middle" nowrap>
										<a class="ctextunderlined" href="javascript:sendDetailFrm(<%= i %>)"><%= entry.getProduct().getDescription() %></a><br>
									</td>
									<td class="table-text" nowrap valign="middle" onclick="switchBoxStatus(<%= i %>)">
										<%= entry.getProduct().getProductCode() %><br>
									</td>
									<td class="table-text" align="right" nowrap valign="middle" onclick="switchBoxStatus(<%= i %>)">
										<% if ( entry.getProduct().getUnits() != null && entry.getProduct().getUnits().size() == 1 ) { %>
											<%= entry.getProduct().getUnits().get(0).getEan() %><br>
										<% } %>
									</td>
									<td class="table-text" align="right" nowrap valign="middle" onclick="switchBoxStatus(<%= i %>)">
										<%= entry.getNumberOfProduct() %> 
										<% if ( entry.getProduct().getUnits() != null && entry.getProduct().getUnits().size() == 1 ) { %>
											X <%= Util.displayContitionnement(entry.getProduct().getUnits().get(0).getNumber(), entry.getProduct().getUnits().get(0).getConditionnement()) %>
										<% } %><br>
									</td>
									<td class="table-text" align="right" nowrap valign="middle" onclick="switchBoxStatus(<%= i %>)">
										<%= Util.formatDate(entry.getArrivalDate(), "yyyyMMdd", "dd/MM/yyyy") %><br>
									</td>
									<td class="table-text" align="right" nowrap valign="middle" onclick="switchBoxStatus(<%= i %>)">
										<%= StringUtils.replace(entry.getProduct().getValidityDate(), "-", "/") %><br>
									</td>
									<td class="table-text" align="right" nowrap valign="middle" onclick="switchBoxStatus(<%= i %>)">
										<%= entry.getProduct().getLotNumber() %><br>
									</td>
									<td class="table-text" align="right" nowrap valign="middle">
										<input type="checkbox" name="entryToExport_<%= i %>"><br>
									</td>
								</tr>
							<% } %>
						</tbody>
					</table>
					<br>
					<center>
						<input type="submit" class="button" value=" Exporter " name="_eventId_export">
					</center>
				</form>
			<% } %>
		</td>
	</tr>
</table>

</body>
</html>