<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>
<%@page import="java.util.Vector"%>
<%@page import="org.belex.product.Product"%>
<%@page import="org.belex.arrival.Arrival"%>
<%@page import="org.belex.entry.Entry"%>
<%
Arrival arrival = (Arrival)request.getAttribute("arrival");
Vector<Entry> entries = arrival.getSavedEntries();
%>
<%@page import="org.belex.requestparams.RequestParams"%>
<html>
<head>
	<%@include file="/WEB-INF/jsp/productEntry/jspf/headHeader.jspf"%>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/css/general.css" type="text/css">
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>
	<script type="text/javascript">
		<!--
			function addItem(nbr) {
				var inputField = dataFrm.newNumberOfProduct;
				var inputFieldValue = Math.abs(inputField.value) + nbr;
				if (inputFieldValue >= 0) {
					inputField.value = inputFieldValue;
				}
			}

			function setValue(nbr) {
				var inputField = dataFrm.newNumberOfProduct;
				inputField.value = nbr;
			}

			function sendRemoveEntryFrm() {
				window.document.removeEntryFrm.submit();
			}
			function sendModifyEntryFrm() {
				var dataFrm = window.document.dataFrm;
				var numberOfProduct = dataFrm.newNumberOfProduct.value;
				if (numberOfProduct == "" || !IsNumeric(numberOfProduct) || numberOfProduct < "1" ) {
					alert("Veuillez introduire une quantité");
					dataFrm.newNumberOfProduct.focus();
					return false;
				} else {
					window.document.modifyEntryFrm.entryNumberOfProduct.value = dataFrm.newNumberOfProduct.value;
				}

				window.document.modifyEntryFrm.entryValidityDate.value = dataFrm.newValidityDate.value
				window.document.modifyEntryFrm.entryLotNumber.value = dataFrm.newLotNumber.value

				window.document.modifyEntryFrm.submit();

				return false;
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
			Modification d'un article introduit pour cette session <br>
		</font>
		
		<%
			if (entries.size() == 0) {
			%>
			Aucun article introduit
	
			<%
			} else { 
	
				Entry currentEntry = null;
				RequestParams requestParam = (RequestParams) request.getAttribute("requestParams");
				int entryId = requestParam.getEntryId();
				if (entryId <= (entries.size() - 1)) {
					currentEntry = entries.get(entryId);
				}

				if (currentEntry != null) {

					Product currentProduct = currentEntry.getProduct();
					Product.Unit selectedUnit = utb.getSelectedUnit(currentProduct);
			%>
	
				<form name="removeEntryFrm" action="flowController.htm">
					<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
					<input type="hidden" value="" name="_eventId_removeEntry">
					<input type="hidden" name="removeEntryId" value="<%= entryId %>">
				</form>
	
				<form name="modifyEntryFrm" action="flowController.htm">
					<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
					<input type="hidden" value="" name="_eventId_confirmModifyEntry">
					<input type="hidden" name="entryId" value="<%= entryId %>">
					<input type="hidden" name="entryNumberOfProduct" value="">
					<input type="hidden" name="entryValidityDate" value="">
					<input type="hidden" name="entryLotNumber" value="">
				</form>
	
				<form name="dataFrm" onsubmit="sendModifyEntryFrm();">
					<table border="0" width="95%" cellspacing="0" cellpadding="2">
						<tr> 
							<td colspan="2" style="padding:10px">
		
								<%= currentProduct.getDescription() %> (<%=currentProduct.getProductCode()%>)<br>
		
							</td>
						</tr>
						<tr style="border-bottom: 1px">
							<td width="60%">
								<table border="0" cellspacing="2" cellpadding="2">
									<tr> 
										<td id="header">
											Quantité<br>
										</td>
							        	<td id="header">
											Conditionnement<br>
										</td>
										<td id="header">
											Validité &nbsp;<img align="middle" id="img_calendar" src="<%= request.getContextPath() %>/pic/dlcalendar/dlcalendar_4.gif" alt="calendar" />
											<dlcalendar click_element_id="img_calendar"
									            input_element_id="newValidityDate"
									            tool_tip="Click to choose a validity date"
									            start_date="<%= Util.getNowFormated("yyyy-MM-dd") %>"
									            date_format="dd-mm-yyyy"
												navbar_style="background-color: #000066; color:white;font-size:20px; padding-left:20px;padding-right:20px"
												daybar_style="background-color: black; color:white;font-size:20px"
												selecteddate_style="font-size:20px"
												weekenddate_style="font-size:20px"
												othermonthdate_style="font-size:20px"
												regulardate_style="font-size:20px"
												nav_images="<%= request.getContextPath() %>/pic/dlcalendar/dlcalendar_prevyear_white.gif,<%= request.getContextPath() %>/pic/dlcalendar/dlcalendar_prevmonth_white.gif,<%= request.getContextPath() %>/pic/dlcalendar/dlcalendar_nextmonth_white.gif,<%= request.getContextPath() %>/pic/dlcalendar/dlcalendar_nextyear_white.gif"
									            >
									        </dlcalendar>
											<br>
										</td>
										<td id="header">
											Lot<br>
										</td>
									</tr>
									<tr>
							        	<td id="item">
											<input type="text" name="newNumberOfProduct" value="<%= currentEntry.getNumberOfProduct() %>" class="inputText" onfocus="this.className='inputTextFocused'" onblur="this.className='inputText'"><br>
										</td>
							        	<td id="item">
											<b><%= Util.displayContitionnement(selectedUnit.getNumber(), selectedUnit.getConditionnement()) %></b>
										</td>
										<td id="item">
											<input name="newValidityDate" type="text" value="<%= currentProduct.getValidityDate() %>" class="inputText" onfocus="this.className='inputTextFocused'" onblur="this.className='inputText'">
										</td>
										<td id="item">
											<input type="text" name="newLotNumber" value="<%= currentProduct.getLotNumber() %>" class="inputText" onfocus="this.className='inputTextFocused'" onblur="this.className='inputText'"><br>
										</td>
									</tr>
								</table>
							</td>
							<td align="center">
								<input class="button" type="button" value=" Supprimer " onclick="sendRemoveEntryFrm()">
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<table width="65%">
									<tr>
										<td>
											<input class="button" style="width: 40px; text-align: center" type="button" value="+" onclick="addItem(1)">
										</td>
										<td>
											<input class="button" style="width: 40px; text-align: center" type="button" value="2" onclick="addItem(2)">
										</td>
										<td>
											<input class="button" style="width: 40px; text-align: center" type="button" value="3" onclick="addItem(3)">
										</td>
										<td>
											<input class="button" style="width: 40px; text-align: center" type="button" value="4" onclick="addItem(4)">
										</td>
										<td>
											<input class="button" style="width: 40px; text-align: center" type="button" value="5" onclick="addItem(5)">
										</td>
										<td>
											<input class="button" style="width: 40px; text-align: center" type="button" value="6" onclick="addItem(6)">
										</td>
										<td>
											<input class="button" style="width: 40px; text-align: center" type="button" value="7" onclick="addItem(7)">
										</td>
										<td>
											<input class="button" style="width: 40px; text-align: center" type="button" value="8" onclick="addItem(8)">
										</td>
										<td>
											<input class="button" style="width: 40px; text-align: center" type="button" value="9" onclick="addItem(9)">
										</td>
										<td>
											<input class="button" style="width: 40px; text-align: center" type="button" value="10" onclick="addItem(10)">
										</td>
										<td>
											<input class="button" style="width: 40px; text-align: center" type="button" value="12" onclick="addItem(12)">
										</td>
									</tr>
									<tr>
										<td style="padding-top: 10px;padding-bottom: 10px">
											<input class="button" style="width: 40px; text-align: center" type="button" value="-" onclick="addItem(-1)">
										</td>
										<td>
											<input class="button" style="width: 40px; text-align: center" type="button" value="15" onclick="addItem(15)">
										</td>								
										<td>
											<input class="button" style="width: 40px; text-align: center" type="button" value="18" onclick="addItem(18)">
										</td>
										<td>
											<input class="button" style="width: 40px; text-align: center" type="button" value="20" onclick="addItem(20)">
										</td>
										<td>
											<input class="button" style="width: 40px; text-align: center" type="button" value="24" onclick="addItem(24)">
										</td>
										<td>
											<input class="button" style="width: 40px; text-align: center" type="button" value="25" onclick="addItem(25)">
										</td>
										<td>
											<input class="button" style="width: 40px; text-align: center" type="button" value="30" onclick="addItem(30)">
										</td>
										<td>
											<input class="button" style="width: 40px; text-align: center" type="button" value="32" onclick="addItem(32)">
										</td>
										<td>
											<input class="button" style="width: 40px; text-align: center" type="button" value="48" onclick="addItem(48)">
										</td>
										<td>
											<input class="button" style="width: 40px; text-align: center" type="button" value="50" onclick="addItem(50)">
										</td>
										<td>
											<input class="button" style="width: 60px; text-align: center" type="button" value="100" onclick="addItem(100)">
										</td>
									</tr>
									<tr>
										<td colspan="5">
											<input class="button" type="button" value=" Reset to 1 " onclick="setValue(1)">
										</td>
									</tr>	
								</table>
							</td>
						</tr>
					</table>
				</form>
			<% } %>
		<% } %>
	
		<p id="lalign">
			<form name="listOfConfirmedFrm" action="flowController.htm">
				<input class="button" type="submit" value=" << Retour " name="_eventId_listOfConfirmedEntry"> 
				<input class="button" type="button" value=" Valider " onclick="sendModifyEntryFrm()"> 
				<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
			</form>
		</p>
	</div>
	<script type="text/javascript" language="javascript" src="<%= request.getContextPath() %>/scripts/dlcalendar.js"></script>
</body>
</html>
