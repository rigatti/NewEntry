<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jspf/globalHeader.jspf"%>
<%@page import="org.belex.arrival.Arrival"%>
<%@page import="org.belex.supplier.Supplier"%>
<%@page import="java.util.Vector"%>
<%
	Arrival arrival = (Arrival) request.getAttribute("arrival");
	//arrival.setSearchSupplierDate("20170523");
%>
<html>
<head>
	<meta charset="UTF-8">
	<%@include file="/WEB-INF/jsp/productEntry/jspf/headHeader.jspf"%>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/css/general.css" type="text/css">
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/date.js"></script>
	<script type="text/javascript">
		<!--
			function submitForm(isPlannedSupplier) {
				var newEntryFrm = window.document.newEntryFrm;
				if (isPlannedSupplier) {
					var selectObj = document.getElementById("plannedSupplier");
					var selIndex = selectObj.selectedIndex;
					document.getElementById("supplier.supplierCode").value = selectObj.options[selIndex].value;
					document.getElementById("supplier.supplierName").value = selectObj.options[selIndex].text;
				} else {
					var selectObj = document.getElementById("supplier");
					var selIndex = selectObj.selectedIndex;
					document.getElementById("supplier.supplierCode").value = selectObj.options[selIndex].value;
					document.getElementById("supplier.supplierName").value = selectObj.options[selIndex].text;
				}
				newEntryFrm.submit();
			}
			
			function sendSearchEntryFrm(){
				window.document.searchEntryFrm.submit();
			}

		var currentDate = "<%= Util.formatDate(arrival.getSearchSupplierDate(), "yyyyMMdd", "dd/MM/yyyy")%>";
		function searchPlannedSupplier() {
			var searchDate = document.getElementById("searchSupplierDate").value;
			console.log("searchPlannedSupplier called with searchDate: " + searchDate + ", currentDate: " + currentDate);

			if (searchDate != currentDate) {
				console.log("Date changed, validating...");
				checkDate(searchDate, 2);
				console.log("After checkDate, strResult: " + strResult);

				// Update the input value with the validated date
				var newDate = strResult; // strResult comes from checkDate() function
				document.getElementById("searchSupplierDate").value = newDate;
				document.getElementById("currentDateDisplay").innerHTML = newDate;
				console.log("Date updated to: " + newDate);
			} else {
				console.log("Date unchanged, just submitting form");
			}

			// Always submit the form to trigger searchPlannedSupplier event
			console.log("Submitting searchPlannedSupplierFrm");
			document.searchPlannedSupplierFrm.submit();
		}
		//-->
	</script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/productEntry/jspf/bodyHeader.jspf" %>
	
		<form name="searchEntryFrm" action="${pageContext.request.contextPath}/flow/arrival" method="post">
			<input type="hidden" name="_eventId_showSuppliers" value="submit">
			<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
		</form>
		<center>
			<form name="searchPlannedSupplierFrm" action="${pageContext.request.contextPath}/flow/arrival" method="post">
				<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
				<input type="hidden" name="_eventId_searchPlannedSupplier" value="">

				<c:if test="${fn:length(arrival.suppliers) > 0}">
					Arrivage de marchandises - S�lection du fournisseur
				</c:if>
				<c:if test="${fn:length(arrival.suppliers) == 0}">
					Aucun fournisseur n'est planifié pour la date sélectionnée
				</c:if>
					<input name="searchSupplierDate" id="searchSupplierDate" type="hidden" onchange="searchPlannedSupplier();" value="<%= Util.formatDate(arrival.getSearchSupplierDate(), "yyyyMMdd", "dd/MM/yyyy") %>">
					<img align="middle" id="img_calendar" src="<%= request.getContextPath() %>/pic/dlcalendar/dlcalendar_4.gif" alt="calendar" />
					<dlcalendar click_element_id="img_calendar" 
						input_element_id="searchSupplierDate" 
			            start_date="2007-01-01"
			            end_date="2050-02-15"
						date_format="dd/mm/yyyy" 
						navbar_style="background-color: #000066; color:white;font-size:20px; padding-left:20px;padding-right:20px" 
						daybar_style="background-color: black; color:white;font-size:20px" 
						selecteddate_style="font-size:20px" 
						weekenddate_style="font-size:20px" 
						othermonthdate_style="font-size:20px" 
						regulardate_style="font-size:20px" 
						nav_images="<%= request.getContextPath() %>/pic/dlcalendar/dlcalendar_prevyear_white.gif,<%= request.getContextPath() %>/pic/dlcalendar/dlcalendar_prevmonth_white.gif,<%= request.getContextPath() %>/pic/dlcalendar/dlcalendar_nextmonth_white.gif,<%= request.getContextPath() %>/pic/dlcalendar/dlcalendar_nextyear_white.gif"
						>
				     </dlcalendar><br>
			<table>
				<tr>
					<td>
						<input class="button" type="button" value=" << " onclick="document.getElementById('searchSupplierDate').value='<%= Util.formatDate(Util.computeDate(1, utb.getStringValue("arrival.searchSupplierDate"), -1, "yyyyMMdd"), "yyyyMMdd", "dd/MM/yyyy") %>';searchPlannedSupplier();" />
					</td>
					<td valign="middle">
						<span id="currentDateDisplay"><%= Util.formatDate(arrival.getSearchSupplierDate(), "yyyyMMdd", "dd/MM/yyyy") %></span><br>
					</td>
					<td>
					 	<input class="button" type="button" value=" >> " onclick="document.getElementById('searchSupplierDate').value='<%= Util.formatDate(Util.computeDate(1, utb.getStringValue("arrival.searchSupplierDate"), 1, "yyyyMMdd"), "yyyyMMdd", "dd/MM/yyyy") %>';searchPlannedSupplier();" />
					 </td>
				</tr>
			</table>
			</form>
			<!-- A href="javascript:sendSearchEntryFrm()" submitForm(false);>
				Recherche de marchandises<br>
			</A-->
			bla
		<c:if test="${fn:length(arrival.suppliers) > 0}">
<%
    Vector<Supplier> suppliers = arrival.getSuppliers();
%>
			<!-- DEBUG: Display suppliers count -->
			<div style="border: 1px solid red; padding: 10px; margin: 10px; background-color: #ffe6e6;">
				<strong>DEBUG - Suppliers found: <%= (suppliers != null ? suppliers.size() : 0) %></strong>
				<% if (suppliers != null && suppliers.size() > 0) { %>
					<ul>
					<% for (int i = 0; i < suppliers.size(); i++) { %>
						<li><%= suppliers.get(i).getSupplierCode() %> - <%= suppliers.get(i).getSupplierName() %></li>
					<% } %>
					</ul>
				<% } %>
			</div>

			<!-- Form to show suppliers - Direct transition, NO iframe -->
			<form name="showSuppliersFrm" method="post" action="${flowExecutionUrl}">
				<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
				<!-- Envoyez l'événement via le nom du bouton (Méthode A) -->
				<input type="submit" name="_eventId_showSuppliers" value="View Suppliers" />
			</form>
		</c:if>
		</center>
	<script type="text/javascript" language="javascript" src="<%= request.getContextPath() %>/scripts/dlcalendar.js"></script>
</body>
</html>
