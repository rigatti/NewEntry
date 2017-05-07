<%@page import="org.belex.util.Util"%>
<html>
<head>
	<title>Attribution des articles</title>
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/date.js"></script>

	<script type="text/javascript">
		<!--
			function submitForm() {
				var obj = window.document.frmSelectAllocationDate;
				if (validateSearch(obj)) {
					obj.submit();
				}
			}
			function validateSearch(obj) {
				var result = true;
				if (checkDate(obj.dateInput.value, 2) > 0) {
					obj.date.value = strResult;
				} else {
					result = false;
					alert("Veuillez introduire une date de recherche");
				}

				//makeRequest(window.document.frmSelectAllocationDate, true);
				return true;
			}

			function computeAllocationDate(step) {
				if (checkDate(window.document.frmSelectAllocationDate.dateInput.value, 2) > 0) {
					computeDate(strResult, 'D', step);
					window.document.frmSelectAllocationDate.dateInput.value = strResult;
				}
			}
			
			function sendFrmSelectAllocationDate() {
				if (validateSearch(window.document.frmSelectAllocationDate)) {
					window.document.frmSelectAllocationDate.submit();
				}
			}

		//-->
	</script>
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin.css">
</head>

<body class="content" onload="sendFrmSelectAllocationDate();">

	<form name="adminFrm" action="<%= request.getContextPath() %>/admin.jsp" method="post" target="_top">
		<input type="hidden" name="admin" value="1">
	</form>

	<h1 onclick="window.document.adminFrm.submit()">
		Attribution des articles réceptionnés
	</h1>

	<!-- 
   	<p class="instruction-text">
		Instruction text
   	</p>
	-->

	<table border="0" cellpadding="0" cellspacing="0"  width="100%" >
		<tr valign="top"> 
    		<td class="tabs-on" width="1%" nowrap height="19">
		       Sélection
			</td>
		    <td class="blank-tab" width="99%" nowrap height="19">
        		<img src="onepix.gif" width="1" height="27" align="absmiddle" alt="">
		    </td>
		</tr>
	</table>
	
	<form name="frmSelectAllocationDate" action="flowController.htm" method="get" target="rightFrame" onsubmit="return validateSearch(this)">
		<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
		<input type="hidden" name="_eventId_search" value="">
		<input type="hidden" name="date" value="">

		<table border="0" cellpadding="10" cellspacing="0" valign="top" width="100%" summary="Framing Table">
			<tr> 
				<td class="layout-manager">
					<table class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3>
						<tr>
							<td class="column-head-prefs">
								Recherche d'un article
							</td>
						</tr>
						<tr>
							<td class="table-text-bold" nowrap align="center" style="padding-top:10px">
								<input type="button" value=" < " onclick="computeAllocationDate(-1);submitForm()"> <input onchange="submitForm()" type="text" size="10" name="dateInput" value="<%= Util.getNowFormated("dd-MM-yyyy") %>"> <input type="button" value=" > " onclick="computeAllocationDate(1);submitForm()">
								<img align="middle" id="img_calendar" src="<%= request.getContextPath() %>/pic/dlcalendar/dlcalendar_4.gif" alt="calendar" />
								<dlcalendar click_element_id="img_calendar"
							            input_element_id="dateInput"
							            tool_tip="Click to choose a date"
							            start_date="2007-01-01"
							            end_date="2050-02-15"
							            date_format="dd-mm-yyyy"
										navbar_style="background-color: #000066; color:white;font-size:12px; padding-left:10px;padding-right:10px"
										daybar_style="background-color: black; color:white;font-size:12px"
										selecteddate_style="font-size:12px"
										weekenddate_style="font-size:12px"
										othermonthdate_style="font-size:10px"
										regulardate_style="font-size:10px"
										nav_images="<%= request.getContextPath() %>/pic/dlcalendar/dlcalendar_prevyear_white.gif,<%= request.getContextPath() %>/pic/dlcalendar/dlcalendar_prevmonth_white.gif,<%= request.getContextPath() %>/pic/dlcalendar/dlcalendar_nextmonth_white.gif,<%= request.getContextPath() %>/pic/dlcalendar/dlcalendar_nextyear_white.gif"
							            >
						        </dlcalendar><br>
								<center style="padding-top:10px;padding-bottom:10px">
									<input class="button" type="button" value=" Recherche " onclick="submitForm()">
								</center>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
	<script type="text/javascript" language="javascript" src="<%= request.getContextPath() %>/scripts/dlcalendarSmall.js"></script>
</body>
</html>