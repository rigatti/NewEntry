<%@page import="org.belex.util.Util"%>
<%@page import="org.belex.allocation.AllocationEntry"%>
<%@page import="org.belex.supplier.Supplier"%>
<%@page import="java.util.Vector"%>
<%@page import="org.belex.allocation.Allocation"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>

<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<html>
<head>
	<title>Attribution des articles</title>
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>

	<script type="text/javascript">
	<!--
		function validateFrmEntryOrders(obj) {
			var formElements = obj.elements;
			obj.allocationEntryOrders.value = "";
			for (i=0; i < formElements.length; i++) {
				if (formElements[i].type == "checkbox") {
					if (formElements[i].checked == true) {
						obj.allocationEntryOrders.value += formElements[i].name + "#";
					}
				}
			}
			obj.allocationEntrySuppliers.value = obj.allocationEntryOrders.value;
			return true;
		}
	
		String.prototype.startsWith = function(strChar)
		{
		 if (!strChar) {return false;}
		 strChar += '';
		 var intLength = strChar.length;
		 return (this.substring(0, intLength) == strChar);
		}
		
		function switchDisplay(id) {
			var obj = window.document.getElementById(id);
			if (obj.style.visibility == "hidden"){
				obj.style.visibility = "visible";
				obj.style.display = "block";
			} else {
				obj.style.visibility = "hidden";
				obj.style.display = "none";
			}
		}
	
		function changeAllChexBox(id, idStartingWith) {
			var els = window.document.frmEntryOrders.elements;
			var globalChekbox = window.document.getElementById(id);
			for (count = 0; count < els.length; count++) {
				if ( els.item(count).id.startsWith(idStartingWith) ) {
					els.item(count).checked = globalChekbox.checked;
				}
			}
		}
	
		function changeCheckBox(id) {
			var checkbox = window.document.getElementById(id);
			checkbox.checked = (!checkbox.checked);
		}
	
	//-->
	</script>

	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin.css">
</head>

<body class="content" style="padding-top:37px">

	<form name="adminFrm" action="<%= request.getContextPath() %>/admin.jsp" method="post" target="_top">
		<input type="hidden" name="admin" value="1">
	</form>

	<table border="0" cellpadding="0" cellspacing="0" width="100%">
		<tr valign="top"> 
	   		<td class="tabs-on" width="1%" nowrap height="19">
		       Fournisseurs
			</td>
		    <td class="blank-tab" width="99%" nowrap height="19">
	       		<img src="onepix.gif" width="1" height="27" align="absmiddle" alt="">
		    </td>
		</tr>
	</table>
	
	<table border="0" cellpadding="10" cellspacing="0" valign="top"	width="100%" summary="Framing Table">
		<tr>
			<td class="layout-manager">
			<%
				boolean displayNoResult = false;
				Object obj = request.getAttribute("allocation");

				List<AllocationEntry> entries = new ArrayList<AllocationEntry>();

				if (obj == null || !(obj instanceof Allocation)) { 
				
					displayNoResult = true;

				} else {

					Allocation allocation = (Allocation) obj;
					entries = allocation.getEntries();

					if (entries.size() == 0) {
						displayNoResult = true;
					} else {
						// sort entries array by supplier name
						Collections.sort(entries, new Comparator<AllocationEntry>(){
							public int compare(AllocationEntry ae0, AllocationEntry ae1) {
								return ae0.getSupplier().getSupplierName().compareToIgnoreCase(ae1.getSupplier().getSupplierName());
							}
						});
					}
				} 
				
				if ( ! displayNoResult ) { %>

					<table class="framing-table" border="0" cellspacing="1" cellpadding="3" width="100%">			
						<tr>
							<td class="column-head-prefs">
								Arrivées à attribuer
							</td>
						</tr>
						<tr>
							<td nowrap class="table-text">
								<form name="frmEntryOrders" action="flowController.htm" method="post" target="_top" onsubmit="return validateFrmEntryOrders(this);">
									<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
									<input type="hidden" name="_eventId_getEntryOrders" value="">
									<input type="hidden" name="allocationEntryOrders" value="">
									<input type="hidden" name="allocationEntrySuppliers" value="">

									<table width="100%">
										<%
										for (int i = 0 ; i < entries.size() ; i++ ) {
											AllocationEntry entry = entries.get(i);
											Supplier supplier = entry.getSupplier();
											if (supplier.getOrders() != null) {
												Vector<Supplier.Order> orders = supplier.getOrders();
												if ((i%2) == 0) { %>
													<tr>
												<% } %>
													<td width="45%" class="table-text">
														<a class="ctext" href="javascript:switchDisplay('div_<%= i %>')"><b><i><%= Util.getShortDisplayable(supplier.getSupplierName(), 20) %></i></b> <font size="0.5"><%= Util.formatDate(entry.getTime(), "hhmmss", "hh:mm:ss") %></font></a> <input type="checkbox" id="checkbox_<%= i %>" onclick="changeAllChexBox('checkbox_<%= i %>', 'Supplier=<%= supplier.getSupplierCode() %>;')"><br>
														<span id="div_<%= i %>" style="visibility=hidden;display=none;">
															<%
															int cnt = 0;
															for (int j=0 ; j < orders.size() ; j++) { 
																Supplier.Order order = orders.get(j);
																String currentId = "Supplier=" + supplier.getSupplierCode() + ";Number=" + order.getNumber() + ";Letter=" + order.getLetter(); %>
																<input type="checkBox" name="<%= currentId %>" id="<%= currentId %>"><a class="ctext" href="javascript:changeCheckBox('<%= currentId %>')"><%= order.getNumber() + " " + order.getLetter()%></a>
																<% if (cnt++ > 1 && j+1 < orders.size()) { 
																	cnt = 0;%>
																	<br>
																<% } else { %>
																	&nbsp;&nbsp;
																<% } %>
															<% } %>
														</span>
													</td>
												<% if ((i%2) == 1) { %>
													</tr>
												<% } %>
											<% } %>
										<% } %>
									</table>		
									<center style="padding:10px">
										<input type="submit" class="button" value=" Recherche ">
									</center>
								</form>
							</td>
						</tr>
					</table>

				<% } else { %>

					<table class="framing-table" border="0" cellspacing="1" cellpadding="3" width="100%">
						<tr>
							<td class="column-head-prefs">
								Aucune arrivée pour cette date
							</td>
						</tr>
					</table>
					
				<% } %>
			</td>
		</tr>
	</table>
</body>
</html>