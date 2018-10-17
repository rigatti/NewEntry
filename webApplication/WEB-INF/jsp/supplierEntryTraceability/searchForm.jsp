<%@page import="org.belex.util.Util"%>
<html>
<head>
	<title>Belex - Tracabilité des fournisseurs</title>
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin.css">
	
	<script type="text/javascript">
	<!--
		function validateSearchFrm(id) {
			var frmProductSearch = window.document.frmProductSearch;
			var hasError = false;

			var itemDetected = false;				
			if (frmProductSearch.traceEntrySupplierCodeInput.value != "") {
				frmProductSearch.traceEntrySupplierCode.value = frmProductSearch.traceEntrySupplierCodeInput.value;
				itemDetected = true;	
			} else {
				alert("Veuillez introduire le code d'un fournisseur");
				return false;
			}
			
			if (frmProductSearch.traceEntryStartDateInput.value != "" || frmProductSearch.traceEntryEndDateInput.value != "") {
				frmProductSearch.traceEntryStartDate.value = frmProductSearch.traceEntryStartDateInput.value;
				frmProductSearch.traceEntryEndDate.value = frmProductSearch.traceEntryEndDateInput.value;
				itemDetected = true;
			} else {
				alert("Veuillez introduire au moins une date de recherche");
				return false;
			}			
			
			return true;

		}

		function switchBoxStatus(obj) {
			obj.checked = !obj.checked;
		}
	//-->
	</script>

</head>

<body class="content">
	<form name="adminFrm" action="<%= request.getContextPath() %>/admin.jsp" method="post" target="_top">
		<input type="hidden" name="admin" value="1">
	</form>

	<h1 onclick="window.document.adminFrm.submit()">
		Traçabilité des fournisseurs
	</h1>

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
	<form name="frmProductSearch" action="flowController.htm" method="get" target="rightFrame" onsubmit="return validateSearchFrm(this)">
		<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
		<input type="hidden" name="_eventId_submit" value="">

		<input type="hidden" name="traceEntryStartDate" value="">
		<input type="hidden" name="traceEntryEndDate" value="">
		<input type="hidden" name="traceEntrySupplierCode" value="">

		<table border="0" cellpadding="10" cellspacing="0" valign="top" width="100%" summary="Framing Table">
			<tr> 
				<td class="layout-manager">
					<table class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3 width=100%>
						<tr>
							<td class="column-head-prefs">
								Recherche d'un fournisseur
							</td>
						</tr>
						<tr>
							<td class="table-text-bold" nowrap>
								Code Fournisseur :<br>
								<span style="padding-left:25px">
									<input type="text" name="traceEntrySupplierCodeInput" size="30px" value="">
								</span>
							</td>
						</tr>
						<tr>
							<td class="table-text-bold" nowrap>
								Date début : <br>
								<span style="padding-left:25px">
									<input type="date" name="traceEntryStartDateInput" id="traceEntryStartDateInput" value="">
								</span>
							</td>
						</tr>
						<tr>
							<td class="table-text-bold" nowrap>
								Date de fin : <br>
								<span style="padding-left:25px">
									<input type="date" name="traceEntryEndDateInput" id="traceEntryEndDateInput" value="">
								</span>
							</td>
						</tr>
						<tr>
							<td class="table-text-bold" style="padding-left:5px" nowrap>
								<br>							
								<center style="padding: 10px">
									<input class="button" type="submit" value=" Recherche ">
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