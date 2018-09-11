<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>
<%@page import="java.util.Vector"%>
<%@page import="org.belex.customer.Customer"%>
<%@page import="org.belex.entry.Entry"%>
<%@page import="org.belex.product.Product"%>
<%@page import="org.belex.util.*"%>
<%
Arrival arrival = (Arrival)request.getAttribute("arrival");
Entry entry = arrival.getEntry();
Product product = entry.getProduct();

String condToDisplay = "";
String condEan = "";
boolean unitNewConditionnement = false;
Product.Unit unit = product. new Unit();
for (int i=0; i < product.getUnits().size(); i++) { 
	if (product.getUnits().get(i).isSelected()) { 
		unit = product.getUnits().get(i);
		if (unit.getConditionnement().equals("")) {
			unitNewConditionnement = true;
		} else {
			condToDisplay = Util.displayContitionnement(unit.getNumber(), unit.getConditionnement());
			condEan = unit.getEan();
		}
		break;
	}
}
%>
<%@page import="org.belex.customer.CustomerEntry"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<html>
<head>
	<%@include file="/WEB-INF/jsp/productEntry/jspf/headHeader.jspf"%>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/css/general.css" type="text/css">
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/date.js"></script>
	<script type="text/javascript">
		<!--
			function afterBarCodeEvent(data){
				window.document.getElementById("entry.product.newProductEan").value = data;
			}

			function addItem(nbr) {
				var inputField = window.document.getElementById("entry.numberOfProduct");
				var inputFieldValue = Math.abs(inputField.value) + nbr;
				if (inputFieldValue >= 0) {
					inputField.value = inputFieldValue;
				}
			}
			function setValue(nbr) {
				var inputField = window.document.getElementById("entry.numberOfProduct");
				inputField.value = nbr;
			}
			function checkFrm(obj){
				if (getRadioValue(obj, "searchTypeInput") == 1) {
					obj.searchValue.value = obj.searchCodeInput.value;
					obj.searchType.value = 1;
					if (obj.searchExactMatchInput.checked){
						obj.searchExactMatch.value = 1;
					} else {
						obj.searchExactMatch.value = 0;
					}
				} else {
					obj.searchValue.value = obj.searchLabelInput.value;
					obj.searchExactMatch.value = 0;
					obj.searchType.value = 2;
				}
				<%/* if ( arrival.getSupplier().getSupplierCode().equals(Constants.SUPPLIER_STOCK) ) { */%>
					//obj.searchOnSupplier.value = "<%= Constants.SUPPLIER_STOCK %>";
				<%/* } else { */%>
					if (obj.searchOnSupplierBox.checked){
						obj.searchOnSupplier.value = "${arrival.supplier.supplierCode}";
					} else {
						obj.searchOnSupplier.value = "";
					}
				<%/* } */%>
				
				
				if (strtrim(obj.searchValue.value).length < 2) {
					alert("Selection trop large, veuillez introduire plus de critères de recherche.");
					return false;
				}
				
				return true;
			}
			function sendNextFrm(id) {
				if (id == "submitDontSaveUnit") {

					var inputField = window.document.getElementById("entry.numberOfProduct");
					if (inputField.value == "" || !IsNumeric(inputField.value) || inputField.value < "1" ) {
						alert("Veuillez introduire une quantité");
						inputField.focus();
						return false;
					}

					var inputField = window.document.getElementById("entry.product.validityDate");
					if (checkDate(inputField.value, 2) > 0) {
						inputField.value = strResult.substring(6,8) + "/" + strResult.substring(4,6) + "/" + strResult.substring(0,4) ;
					}

					window.document.getElementById("eventId").name = "_eventId_" + id;
					window.document.nextFrm.submit();

				} else if (id == "updateProductUnitAndSubmit") {

					var inputField = window.document.getElementById("entry.product.newProductEan");

					<% if ( ! unitNewConditionnement ){ %>

						if (inputField.value == "<%= condEan %>") {
							alert("Vous n'avez pas introduit de modification");
							return false;
						}
	
						if (inputField.value == "") {
							if ( ! confirm("Attention, vous êtes sur le point de supprimer le code ean attribué à ce conditionnement")) {
								return false;
							}
						} 
						//else if (!IsNumeric(inputField.value) || inputField.value < 1) {
						//	alert("Veuillez introduire un code ean correct");
						//	inputField.focus();
						//	return false;
						//}

					<% } else { %>

						if (inputField.value != "" && (inputField.value < 1 || !IsNumeric(inputField.value)) ) {
							alert("Veuillez introduire un code ean correct");
							inputField.focus();
							return false;
						}
	
						var conditionnementInputField = window.document.getElementById("entry.product.newProductUnitConditionnement");
						var numberInputField = window.document.getElementById("entry.product.newProductNumberOfUnit");
						if (conditionnementInputField.value == "" || numberInputField.value == "") {
							alert("Veuillez introduire un conditionnement");
							return false;
						}

					<% } %>

					var inputField = window.document.getElementById("entry.numberOfProduct");
					if (inputField.value == "" || !IsNumeric(inputField.value) || inputField.value < "1" ) {
						alert("Veuillez introduire une quantité");
						inputField.focus();
						return false;
					}
					
					var inputField = window.document.getElementById("entry.product.validityDate");
					if (checkDate(inputField.value, 2) > 0) {
						inputField.value = strResult.substring(6,8) + "/" + strResult.substring(4,6) + "/" + strResult.substring(0,4) ;
					}
					
					window.document.getElementById("eventId").name = "_eventId_" + id;
					window.document.nextFrm.submit();
				
				} else if (id = "back"){
					
					window.document.backFrm.submit();
					
				} else {
				
					window.document.getElementById("eventId").name = "_eventId_" + id;
					window.document.nextFrm.submit();
				}
			}
		//-->
	</script>
</head>
	<body onload="CheckTC();readdata();setFocus()" onunload="stop_listen()">

		<%@ include file="/WEB-INF/jsp/productEntry/jspf/bodyHeader.jspf" %>
				
		<div style="valign:middle;">
		
			<img id="imageToModal"  src="http://cargosrv1/productsimages/<%= product.getProductCode() %>.jpg" alt="<%=product.getProductCode() %>" height="65px" width="65px">
		
			<font id="pageTitle">
				Nouvel arrivage de marchandises - ${arrival.supplier.supplierName} (${arrival.supplier.supplierCode})
			</font>
		</div>

		<%@ include file="/WEB-INF/jsp/productEntry/jspf/showImgModal.jspf" %>

		<hr>

		<%
		String objFormName = "window.document.searchFrm";
		boolean writeTableHeader = false;
		%>
		<table>
			<tr>
				<form name="searchFrm" action="flowController.htm" method="get" onsubmit="return checkFrm(this)">
					<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
					<input type="hidden" name="_eventId_newSearch" value="">
					<input type="hidden" name="searchValue">
					<input type="hidden" name="searchExactMatch">
					<input type="hidden" name="searchType">
					<input type="hidden" name="searchOnSupplier">

					<%@include file="/WEB-INF/jsp/productEntry/jspf/searchInput.jspf" %>
					<td>
						<input class="button" type="submit" value=" Recherche ">  
					</td>
				</form>
			</tr>
		</table>

		<hr>

		<form name="nextFrm" action="flowController.htm" method="get">
			<table>
				<tr>
					<td colspan="3">
						<%= product.getDescription() %> ( <%= product.getProductCode() %> )<br>
					</td>
				</tr>
				<tr>
					<td align="right">
						Ean : <br>
					</td>
					<td colspan="2">
						<input name="entry.product.newProductEan" id="entry.product.newProductEan" type="text" value="<%= condEan %>" class="inputText" onfocus="this.className='inputTextFocused'" onblur="this.className='inputText'">
						<input type="hidden" name="entry.product.newProductLabel" value="">
						<input type="hidden" name="entry.product.newProductSupplierCode" value="">
					</td>
				</tr>

				<tr>
					<td width="10%" valign="middle" align="right">
						Conditionnement : <br>
					</td>
					<td width="20%" nowrap valign="middle">

						<% if ( unitNewConditionnement ) { %>

							<input type="text" size="4" name="entry.product.newProductNumberOfUnit" id="entry.product.newProductNumberOfUnit" value="" class="inputText" onfocus="this.className='inputTextFocused'" onblur="this.className='inputText'">
							X
							<input type="text" size="25" name="entry.product.newProductUnitConditionnement" id="entry.product.newProductUnitConditionnement" value="" class="inputText" onfocus="this.className='inputTextFocused'" onblur="this.className='inputText'">

						<% } else { %>

							<input type="hidden" name="entry.product.newProductNumberOfUnit" id="entry.product.newProductNumberOfUnit" value="">
							<input type="hidden" name="entry.product.newProductUnitConditionnement" id="entry.product.newProductUnitConditionnement" value="">

						<% } %>

						<%/* if (StringUtils.equals(arrival.getSupplier().getSupplierCode(), Constants.SUPPLIER_STOCK)) { */%>
							<!--span style="color:red;font-weight:bold;"-->
						<%/* } */%>
						<%= condToDisplay %>
 					 	X 
						<%/* if (StringUtils.equals(arrival.getSupplier().getSupplierCode(), Constants.SUPPLIER_STOCK)) { */%>
							<!--/span-->
						<%/* } */%>

						<input type="text" value="" size="10" name="entry.numberOfProduct" id="entry.numberOfProduct" class="inputText" onfocus="this.className='inputTextFocused'" onblur="this.className='inputText'">

					</td>
					<td wdth="70%">
						<table width="100%">
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
				<!-- tr>
					<td colspan="2">
					</td>
					<td>
						<input class="button" type="button" value=" +10 " onclick="addItem(10)"> <input class="button" type="button" value=" +20 " onclick="addItem(20)"> <input class="button" type="button" value=" +50 " onclick="addItem(50)"> <input class="button" type="button" value=" +100 " onclick="addItem(100)"> <input class="button" type="button" value=" Reset to 0 " onclick="window.document.getElementById('entry.numberOfProduct').value=0">
					</td>
				</tr -->
				<tr>
					<td  align="right">
						Date de validité : <br>
					</td>
					<td colspan="2">
						<input name="entry.product.validityDate" id="entry.product.validityDate" type="text" value="" class="inputText" onfocus="this.className='inputTextFocused'" onblur="this.className='inputText'">
						<img align="middle" id="img_calendar" src="<%= request.getContextPath() %>/pic/dlcalendar/dlcalendar_4.gif" alt="calendar" />
						<dlcalendar click_element_id="img_calendar"
				            input_element_id="entry.product.validityDate"
				            tool_tip="Click to choose a validity date"
				            start_date="<%= Util.getNowFormated("yyyy-MM-dd") %>"
				            date_format="dd/mm/yyyy"
							navbar_style="background-color: #000066; color:white;font-size:20px; padding-left:20px;padding-right:20px"
							daybar_style="background-color: black; color:white;font-size:20px"
							selecteddate_style="font-size:20px"
							weekenddate_style="font-size:20px"
							othermonthdate_style="font-size:20px"
							regulardate_style="font-size:20px"
							nav_images="<%= request.getContextPath() %>/pic/dlcalendar/dlcalendar_prevyear_white.gif,<%= request.getContextPath() %>/pic/dlcalendar/dlcalendar_prevmonth_white.gif,<%= request.getContextPath() %>/pic/dlcalendar/dlcalendar_nextmonth_white.gif,<%= request.getContextPath() %>/pic/dlcalendar/dlcalendar_nextyear_white.gif"
				            >
				        </dlcalendar>
					</td>
				</tr>
				<tr>
					<td align="right">
						Numéro de lot : <br>
					</td>
					<td colspan="2">
						<input name="entry.product.lotNumber" type="text" value="" class="inputText" onfocus="this.className='inputTextFocused'" onblur="this.className='inputText'">
					</td>
				</tr>
				<!-- tr>
					<td>
						Données complémentaires : <br>
					</td>
					<td colspan="2">
						<textarea name="entry.product.additionalData" row="2" cols="60" value="" class="inputText" onfocus="this.className='inputTextFocused'" onblur="this.className='inputText'"></textarea>
					</td>
				</tr -->
				<input type="hidden" name="entry.product.additionalData" value="">
				<tr>
					<td colspan="3" align="center" style="padding-top:15px">
						<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
						<input type="hidden" id="eventId" value="">
						<input class="button" type="button" value=" << Retour " onclick="sendNextFrm('back');">
						&nbsp;&nbsp;&nbsp;
						<% if ( unitNewConditionnement ) { %>
							<input class="button" type="button" value=" Valider SANS sauver le produit" onclick="sendNextFrm('submitDontSaveUnit');">
						<% } else { %>
							<input class="button" type="button" value=" Valider SANS sauver le ean" onclick="sendNextFrm('submitDontSaveUnit');">
						<% } %>
						<%/* if ( ! arrival.getSupplier().getSupplierCode().equals(Constants.SUPPLIER_STOCK) ) { */%>
							&nbsp;&nbsp;&nbsp;
							<% if ( unitNewConditionnement ) { %>
								<input class="button" type="button" value=" Valider ET sauver le produit " onclick="sendNextFrm('updateProductUnitAndSubmit');">
							<% } else { %>
								<input class="button" type="button" value=" Valider ET sauver le ean " onclick="sendNextFrm('updateProductUnitAndSubmit');">
							<% } %>
						<%/* } */%>
					</td>
				</tr>
			</table>
		</form>
		<form name="backFrm" action="flowController.htm" method="get" onsubmit="return checkFrm(this)">
			<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
			<input type="hidden" name="_eventId_back" value="">
			<input type="hidden" name="searchValue"	value="">
			<input type="hidden" name="searchExactMatch" value="">
			<input type="hidden" name="searchType" value="">
			<input type="hidden" name="searchOnSupplier" value="">
			<input type="hidden" name="products" value="">
		</form>
		<hr>

		<table width="98%">
			<% 
			Vector<Customer> customers = arrival.getEntry().getCustomers();
			
			for (int i = 0 ; i < customers.size() ; i++) { 
							
				Customer customer = customers.get(i);
				CustomerEntry ce = customer.getCustomerEntry();
				
				if (ce != null && ce.getFromBasketNumber() != 0) { %>
					<tr>
						<td colspan="2">
							<font style="color: red;font-weight: bold;"> Colis : <%= ce.getFromBasketNumber() %>
								<% if (ce.getFromBasketNumber() != ce.getToBasketNumber() && ce.getToBasketNumber() != 0) { %>
									à <%= ce.getToBasketNumber() %>
								<% } %>
							</font><br>
							<font style="color: green;font-weight: bold;"> Destination : <%= customer.getCustomerOrder().getFly().getAirportCode() %></font><br>
							<font style="color: green;font-weight: bold;"> Code client : <%=customer.getCustomerOrder().getOrderCode()%> - <%=customer.getCustomerOrder().getOrderNumber()%></font><br>
							<% if (customer.getName() != null && ! customer.getName().equals("")) { %>
								<font style="color: green;font-weight: bold;"> Nom du client : <%=customer.getName()%></font><br>
							<% }
							
							if (customer.getAddressFurniture() != null && ! customer.getAddressFurniture().equals("")) { %>
								<font style="color: green;font-weight: bold;"> Adresse de Livraison : <%= customer.getAddressFurniture().replaceAll("\r\n", " ") %></font><br>
							<% }

							String unitConditionnementRequested = customer.getCustomerEntry().getUnitConditionnement();
							int numberOfUnitRequested = customer.getCustomerEntry().getNumberOfUnit();
							//String unitConditionnementInput = arrival.getEntry().getProduct().getUnitConditionnement();
							//String numberOfUnitInput = arrival.getEntry().getProduct().getNumberOfUnit();
							String unitConditionnementInput = unit.getConditionnement();
							int numberOfUnitInput = unit.getNumber();
							
							//boolean green = false;

							if (  unitConditionnementRequested.equalsIgnoreCase(unitConditionnementInput) && 
								  numberOfUnitRequested == numberOfUnitInput
								) { %>
								<font style="color: green;font-weight: bold;">
							<% } else { %>
								<font style="color: red;font-weight: bold;">
							<% } %>
								Conditionnements : <%=numberOfUnitRequested%> X <%=unitConditionnementRequested%>
							</font><br>
							<font style="color: green;font-weight: bold;"> Nombre de conditionnements : <%=customer.getCustomerEntry().getNumberOfConditionnement()%></font><br>
							<% if (customer.getCustomerEntry().isNeedFridge()) { %>
								<font style="color: red;font-weight: bold;"> SURGELE</font><br>
							<% } %>
							<br>
						</td>
					</tr>
			<% } else { %>
				<tr>
					<td align="center" colspan="2">
						<font style="color: red;font-weight: bold;">ATTENTION AUCUN COLIS N'A ETE ATTRIBUE A CET ATRICLE.</font><br>
					</td>
				</tr>
				<% break;
			}
		} %>
	</table>

	<script type="text/javascript">
		<!--
			function setFocus() {
				<% if ( unitNewConditionnement ) { %>
					window.document.getElementById("entry.product.newProductNumberOfUnit").focus();
				<% } else { %>
					window.document.getElementById("entry.numberOfProduct").focus();
				<% } %>
			}
		//-->
	</script>
	
	<%@ include file="/WEB-INF/jsp/productEntry/jspf/barCode/simulator.jspf" %>
		
	<script type="text/javascript" language="javascript" src="<%= request.getContextPath() %>/scripts/dlcalendar.js"></script>
</body>
</html>