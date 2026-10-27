<%@page import="org.belex.util.Util"%>
<html>
<head>
	<title>Belex - Tracabilité</title>
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/dateSelector.js"></script>
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin.css">
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/date-selector.css">

	<script type="text/javascript">
	<!--
		function validateSearchFrm(id) {
			var frmProductSearch = window.document.frmProductSearch;
			var hasError = false;

			var itemDetected = false;				
			if (frmProductSearch.productCodeInput.value != "") {
				frmProductSearch.traceProductCode.value = frmProductSearch.productCodeInput.value;
				itemDetected = true;	
			}
			if (frmProductSearch.eanInput.value != "") {
				frmProductSearch.traceEan.value = frmProductSearch.eanInput.value;
				itemDetected = true;
			}
			if (frmProductSearch.lotNumberInput.value != "") {
				frmProductSearch.traceLot.value = frmProductSearch.lotNumberInput.value;
				itemDetected = true;
			}
			if (frmProductSearch.validityDateInput.value != "") {
				frmProductSearch.traceValidityDate.value = isoToLegacy(frmProductSearch.validityDateInput.value, "/");
				itemDetected = true;
			}

			if ( ! itemDetected) {
				alert("Veuillez introduire un critère de recherche");
			} else {
				window.document.frmProductSearch.submit();
			}

		}

		function switchBoxStatus(obj) {
			obj.checked = !obj.checked;
		}
	//-->
	</script>

</head>

<body class="content">
	<form name="adminFrm" action="<%= request.getContextPath() %>/index.jsp" method="post" target="_top">
	</form>

	<h1 onclick="window.document.adminFrm.submit()">
		Traçabilité des articles
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

		<input type="hidden" name="traceProductCode" value="">
		<input type="hidden" name="traceEan" value="">
		<input type="hidden" name="traceLot" value="">
		<input type="hidden" name="traceValidityDate" value="">

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
								Code Produit :<br>
								<span style="padding-left:25px">
									<input type="text" name="productCodeInput" size="30px" value="">
								</span>
							</td>
						</tr>
						<tr>
							<td class="table-text-bold" nowrap>
								EAN :<br>
								<span style="padding-left:25px">
									<input type="text" name="eanInput" size="30px" value="">
								</span>
							</td>
						</tr>
						<tr>
							<td class="table-text-bold" nowrap>
								Numéro de lot :<br>
								<span style="padding-left:25px">
									<input type="text" name="lotNumberInput" size="30px" value="">
								</span>
							</td>
						</tr>
						<tr>
							<td class="table-text-bold" nowrap>
								Date de validité : <br>
								<div class="date-selector compact">
									<div class="date-controls">
										<button type="button" onclick="shiftDate('validityDateInput', -1, 'validityDateDisplay')">◀ Jour précédent</button>
										<input type="date" name="validityDateInput" id="validityDateInput"
											   onchange="refreshDateLabel('validityDateInput', 'validityDateDisplay')"
											   min="2007-01-01" max="2050-12-31">
										<button type="button" onclick="shiftDate('validityDateInput', 1, 'validityDateDisplay')">Jour suivant ▶</button>
									</div>
									<div class="date-display">
										Date sélectionnée: <span id="validityDateDisplay"></span>
									</div>
								</div>
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