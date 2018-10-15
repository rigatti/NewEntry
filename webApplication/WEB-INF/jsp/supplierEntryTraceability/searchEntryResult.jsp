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
	<title>Belex - Tracabilité des fournisseurs</title>

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
			Résultats de la recherche<br>
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
									Fournisseur
								</td>
								<td class="column-head-name" width="1%" onclick="sortTable(document.getElementById('sortableTable'), 1, 'Date', null, null, null, null)" nowrap>
									Date d'entrée
								</td>
								<td class="column-head-name" width="1%" onclick="sortTable(document.getElementById('sortableTable'), 2, 'Integer', null, null, null, null)" nowrap>
									Nombre d'articles
								</td>
								<td class="column-head-name" width="1%" onclick="sortTable(document.getElementById('sortableTable'), 3, 'String', null, null, null, null)" nowrap>
									Aspect des aliments
								</td>
								<td class="column-head-name" width="1%" onclick="sortTable(document.getElementById('sortableTable'), 4, 'String', null, null, null, null)" nowrap>
									Intégrité des emballages
								</td>
								<td class="column-head-name" width="1%" onclick="sortTable(document.getElementById('sortableTable'), 5, 'String', null, null, null, null)" nowrap>
									DLC ou DDM suffisante
								</td>
								<td class="column-head-name" width="1%" onclick="sortTable(document.getElementById('sortableTable'), 6, 'String', null, null, null, null)" nowrap>
									Température à la réception
								</td>
								<td class="column-head-name" width="1%" nowrap>
									Commentaire
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
									<td class="table-text-bold" nowrap valign="middle" onclick="switchBoxStatus(<%= i %>)">
										<%= entry.getSupplier().getSupplierName() != null ? entry.getSupplier().getSupplierName() : entry.getSupplier().getSupplierCode() %><br>
									</td>
									<td class="table-text" align="right" nowrap valign="middle" onclick="switchBoxStatus(<%= i %>)">
										<%= Util.formatDate(entry.getArrivalDate(), "yyyyMMdd", "dd/MM/yyyy") %>
									</td>
									<td class="table-text" align="right" nowrap valign="middle" onclick="switchBoxStatus(<%= i %>)">
										<%= entry.getSupplierEntryNumberOfProducts() %><br>
									</td>
									<td class="table-text" align="right" nowrap valign="middle" onclick="switchBoxStatus(<%= i %>)">
										<%= entry.getSupplierEntryCommentOnQuality() != null ? entry.getSupplierEntryProductIntegrity() : "-" %><br>
									</td>
									<td class="table-text" align="right" nowrap valign="middle" onclick="switchBoxStatus(<%= i %>)">
										<%= entry.getSupplierEntryCommentOnQuality() != null ? entry.getSupplierEntryPackagingIntegrity() : "-" %><br>
									</td>
									<td class="table-text" align="right" nowrap valign="middle" onclick="switchBoxStatus(<%= i %>)">
										<%= entry.getSupplierEntryCommentOnQuality() != null ? entry.getSupplierEntryDlcDdmValidity() : "-" %><br>
									</td>
									<td class="table-text" align="right" nowrap valign="middle" onclick="switchBoxStatus(<%= i %>)">
										<%= entry.getSupplierEntryCommentOnQuality() != null ? entry.getSupplierEntryTemperatureValidity() : "-" %><br>
									</td>
									<td class="table-text" align="right" nowrap valign="middle" onclick="switchBoxStatus(<%= i %>)">
										<%= entry.getSupplierEntryCommentOnQuality() != null ? entry.getSupplierEntryCommentOnQuality() : "-" %><br>
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