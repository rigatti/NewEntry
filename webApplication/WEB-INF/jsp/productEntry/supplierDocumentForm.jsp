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
					alert("Veuillez introduire un libellé");
					inputLabel.focus();
					return false;
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
		Introduction du type de document lié au fournisseur
		
		<form name="nextFrm" action="flowController.htm" method="get" onsubmit="return checkValues();">
			<input type="hidden" name="_eventId_next" value="">
			<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
			<input type="hidden" name="newProductSupplierCode" value="${arrival.supplier.supplierCode}">
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
						<input maxlength="50" size="60" class="inputText" type="text" name="supplierDocumentDescription" id="supplierDocumentDescription" value="" onfocus="this.className='inputTextFocused'" onblur="this.className='inputText'">
					</td>
				</tr>
			</table>
			<p>
				<input class="button" type="button" onclick="validateNext();" value=" Continuer ">
			</p>
		</form>
	</div>
</body>
</html>