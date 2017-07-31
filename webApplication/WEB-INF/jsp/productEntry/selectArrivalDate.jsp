<%@include file="/WEB-INF/jspf/globalHeader.jspf"%>
<%@page import="org.belex.arrival.Arrival"%>
<%
	Arrival arrival = (Arrival) request.getAttribute("arrival");
%>
<html>
<head>
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
				var searchDate = window.document.getElementById("searchSupplierDate").value;
				if (searchDate != currentDate) {
					checkDate(searchDate,2);
					window.document.searchPlannedSupplierFrm.searchSupplierDate.value = strResult;
					window.document.searchPlannedSupplierFrm.submit();
				}
			}
		//-->
	</script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/productEntry/jspf/bodyHeader.jspf" %>
	
		<form name="searchEntryFrm" action="flowController.htm" method="post">
			<input type="hidden" value="submit" name="_eventId_searchEntry">
			<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
		</form>
		<center>
			<form name="searchPlannedSupplierFrm" action="flowController.htm" method="get">
				<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
				<input type="hidden" name="_eventId_searchPlannedSupplier" value="">
	
				<c:if test="${fn:length(arrival.suppliers) > 0}">
					Arrivage de marchandises - Sélection du fournisseur
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
							<input class="button" type="button" value=" << " onclick="window.document.getElementById('searchSupplierDate').value='<%= Util.formatDate(Util.computeDate(1, utb.getStringValue("arrival.searchSupplierDate"), -1, "yyyyMMdd"), "yyyyMMdd", "dd/MM/yyyy") %>';searchPlannedSupplier();">
						</td>
						<td valign="middle">
							<%= Util.formatDate(arrival.getSearchSupplierDate(), "yyyyMMdd", "dd/MM/yyyy") %><br>
						</td>
						<td>
						 	<input class="button" type="button" value=" >> " onclick="window.document.getElementById('searchSupplierDate').value='<%= Util.formatDate(Util.computeDate(1, utb.getStringValue("arrival.searchSupplierDate"), 1, "yyyyMMdd"), "yyyyMMdd", "dd/MM/yyyy") %>';searchPlannedSupplier();">
						 </td>
					</tr>
				</table>
			</form>
			<!-- A href="javascript:sendSearchEntryFrm()" submitForm(false);>
				Recherche de marchandises<br>
			</A-->
			<c:if test="${fn:length(arrival.suppliers) > 0}">
				<iframe frameborder="0" width="98%" height="89%" style="border:0;" scrolling="auto" src="flowController.htm?_flowExecutionKey=${flowExecutionKey}&_eventId_showSuppliers">
				</iframe>
			</c:if>
		</center>
	<script type="text/javascript" language="javascript" src="<%= request.getContextPath() %>/scripts/dlcalendar.js"></script>
</body>
</html>
