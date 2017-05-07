<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>
<html>
<head>
	<%@include file="/WEB-INF/jsp/productEntry/jspf/headHeader.jspf"%>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/css/general.css" type="text/css">
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>

	<script type="text/javascript">
		<!--
			function afterBarCodeEvent(data){
				window.document.getElementById('newProductEan').value = data;
			}

			function checkValues(obj) {

				var inputLabel = window.document.getElementById("newProductLabel");
				if (inputLabel.value == "") {
					alert("Veuillez introduire un libellé de produit");
					inputLabel.focus();
					return false;
				}
				
				var inputNumberOfUnit = window.document.getElementById("newProductNumberOfUnit");
				if (inputNumberOfUnit.value == "" || !IsNumeric(inputNumberOfUnit.value)) {
					alert("Veuillez introduire un nombre de conditionnement correct");
					inputNumberOfUnit.focus();
					return false;
				}
				
				var inputUnit = window.document.getElementById("newProductUnitConditionnement");
				if (inputUnit.value == "") {
					alert("Veuillez introduire un conditionnement");
					inputUnit.focus();
					return false;
				}

				return true;
			}
		//-->
	</script>
</head>
<body onload="CheckTC();readdata()" onunload="stop_listen()">
<%@ include file="/WEB-INF/jsp/productEntry/jspf/bodyHeader.jspf" %>
	<div>
		<font id="pageTitle">
			Arrivage de marchandises - ${arrival.supplier.supplierName} (${arrival.supplier.supplierCode})<br>
		</font>
	
		<hr>
		Introduction d'un nouveau produit
		
		<form name="nextFrm" action="flowController.htm" method="get" onsubmit="return checkValues();">
			<input type="hidden" name="_eventId_submit" value="">
			<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
			<input type="hidden" name="newProductSupplierCode" value="${arrival.supplier.supplierCode}">
			<table>
				<tr>
					<td width="25%">
						Libellé:<br>
					</td>
					<td>
						<input maxlength="50" size="60" class="inputText" type="text" name="newProductLabel" value="" onfocus="this.className='inputTextFocused'" onblur="this.className='inputText'">
					</td>
				</tr>
				<tr>
					<td>
						Code EAN:<br>
					</td>
					<td>
						<input maxlength="13" class="inputText" type="text" name="newProductEan" id="newProductEan" value="" onfocus="this.className='inputTextFocused'" onblur="this.className='inputText'">
					</td>
				</tr>
				<tr>
					<td>
						Conditionnement:<br>
					</td>
					<td>
						<input maxlength="8" class="inputText" type="text" size="4" name="newProductNumberOfUnit" value="" onfocus="this.className='inputTextFocused'" onblur="this.className='inputText'">
						 X 
						<input maxlength="20" class="inputText" type="text" size="25" name="newProductUnitConditionnement" value="" onfocus="this.className='inputTextFocused'" onblur="this.className='inputText'">
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center" style="padding-top:20px">
						<input class="button" type="button" value=" << Retour " onclick="window.history.back();"> &nbsp; 
						<input class="button" type="submit" value=" Sauvegarde ">

					</td>
				</tr>
			</table>
		</form>
	</div>

	<%@ include file="/WEB-INF/jsp/productEntry/jspf/barCode/simulator.jspf" %>
</body>
</html>