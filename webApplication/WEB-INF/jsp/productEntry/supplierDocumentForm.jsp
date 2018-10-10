<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>
<html>
<head>
	<%@include file="/WEB-INF/jsp/productEntry/jspf/headHeader.jspf"%>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/css/general.css" type="text/css">
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>

	<script type="text/javascript">
		<!--
			function checkValues(obj) {

				var inputLabel = window.document.getElementById("supplierDocumentDescription");
				if (inputLabel.value == "") {
					alert("Veuillez introduire un libell�");
					inputLabel.focus();
					return false;
				}

				var askConfirmation = false;
				var checkboxToInspect = window.document.getElementById("inputSupplierEntryProductIntegrity");
				var supplierEntryProductIntegrity = window.document.getElementById("supplierEntryProductIntegrity");
				if (checkboxToInspect.checked == false){
					askConfirmation = true;
					supplierEntryProductIntegrity.value = 0;
				} else {
					supplierEntryProductIntegrity.value = 1;
				}
				
				checkboxToInspect = window.document.getElementById("inputSupplierEntryPackagingIntegrity");
				var supplierEntryPackagingIntegrity = window.document.getElementById("supplierEntryPackagingIntegrity");
				if (checkboxToInspect.checked == false){
					askConfirmation = true;
					supplierEntryPackagingIntegrity.value = 0;
				} else {
					supplierEntryPackagingIntegrity.value = 1;
				}
				
				checkboxToInspect = window.document.getElementById("inputSupplierEntryDlcDdmValidity");
				var supplierEntryDlcDdmValidity = window.document.getElementById("supplierEntryDlcDdmValidity");
				if (checkboxToInspect.checked == false){
					askConfirmation = true;
					supplierEntryDlcDdmValidity.value = 0;
				} else {
					supplierEntryDlcDdmValidity.value = 1;
				}
				
				checkboxToInspect = window.document.getElementById("inputSupplierEntryTemperatureValidity");
				var supplierEntryTemperatureValidity = window.document.getElementById("supplierEntryTemperatureValidity");
				if (checkboxToInspect.checked == false){
					askConfirmation = true;
					supplierEntryTemperatureValidity.value = 0;
				} else {
					supplierEntryTemperatureValidity.value = 1;
				}
				
				if (askConfirmation) {
					return confirmAction();
				}
				return true;
			}
			function validateNext() {
				var obj = window.document.nextFrm;
				if (checkValues(obj)) {
					obj.submit();
				}
			}
		//-->
	</script>
</head>
<body onload="window.document.nextFrm.supplierDocumentDescription.focus()">
	<%@ include file="/WEB-INF/jsp/productEntry/jspf/bodyHeader.jspf" %>
	<div>
		<font id="pageTitle">
			Arrivage de marchandises - ${arrival.supplier.supplierName} (${arrival.supplier.supplierCode})<br>
		</font>
	
		<hr>
		Introduction du type de document li� au fournisseur
		
		<p>
		
		<form name="nextFrm" action="flowController.htm" method="get" onsubmit="return checkValues();">
			<input type="hidden" name="_eventId_next" value="">
			<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
			<input type="hidden" name="newProductSupplierCode" value="${arrival.supplier.supplierCode}">
			<input type="hidden" name="supplierEntryProductIntegrity" id="supplierEntryProductIntegrity" value="0">
			<input type="hidden" name="supplierEntryPackagingIntegrity" id="supplierEntryPackagingIntegrity" value="0">
			<input type="hidden" name="supplierEntryDlcDdmValidity" id="supplierEntryDlcDdmValidity" value="0">
			<input type="hidden" name="supplierEntryTemperatureValidity" id="supplierEntryTemperatureValidity" value="0">
			<table align="center">
				<tr>
					<td>
						<select class="inputText" name="supplierDocumentType">
							<option value="1">Facture
							<option value="2">Note d'envoi
						</select><br>
					</td>
					<td valign="middle">
						:<br>
					</td>
					<td>
						<input maxlength="50" size="60" class="inputText inputCheckbox" type="text" name="supplierDocumentDescription" id="supplierDocumentDescription" value="" onfocus="this.className='inputTextFocused'" onblur="this.className='inputText'">
					</td>
				</tr>
			</table>
			<p>
				<table align="center">
					<tr>
						<td>
							<br>
						</td>
						<td style="padding-left:20px;">
							OK?
						</td>
					</tr>
					<tr>
						<td colspan="2"><hr></td>
					</tr>
					<tr>
						<td>
							Aspect des aliments<br>
						</td>
						<td style="padding-left:20px;">
							<input class="inputText inputCheckbox" type="checkbox" name="inputSupplierEntryProductIntegrity" id="inputSupplierEntryProductIntegrity">
						</td>
					</tr>
					<tr>
						<td>
							Int�grit� des emballages<br>
						</td>
						<td style="padding-left:20px;">
							<input class="inputText inputCheckbox" type="checkbox" name="inputSupplierEntryPackagingIntegrity" id="inputSupplierEntryPackagingIntegrity">
						</td>
					</tr>
					<tr>
						<td>
							DLC ou DDM suffisantes<br>
						</td>
						<td style="padding-left:20px;">
							<input class="inputText inputCheckbox" type="checkbox" name="inputSupplierEntryDlcDdmValidity" id="inputSupplierEntryDlcDdmValidity">
						</td>
					</tr>
					<tr>
						<td>
							Temp�rature correcte � la r�ception<br>
						</td>
						<td style="padding-left:20px;">
							<input class="inputText inputCheckbox" type="checkbox" name="inputSupplierEntryTemperatureValidity" id="inputSupplierEntryTemperatureValidity">
						</td>
					</tr>
					<tr>
						<td colspan="2"><hr></td>
					</tr>
				</table>
			</p>
			<p>
				<input class="button" type="button" onclick="validateNext();" value=" Continuer ">
			</p>
		</form>
		</p>
	</div>
</body>
</html>