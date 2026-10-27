<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>
<html>
<head>
    <title>Traçabilité des fournisseurs</title>
    <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/sort.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/dateSelector.js"></script>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/date-selector.css">
    <script type="text/javascript">
        function validateSearchFrm() {
            var frm = window.document.frmProductSearch;
            frm.traceEntrySupplierCode.value = "";
            frm.traceEntryStartDate.value = "";
            frm.traceEntryEndDate.value = "";
            if (frm.traceEntrySupplierCodeInput.value == "") {
                alert("Veuillez introduire le code d'un fournisseur");
                return false;
            }
            frm.traceEntrySupplierCode.value = frm.traceEntrySupplierCodeInput.value;
            if (frm.traceEntryStartDateInput.value == "" && frm.traceEntryEndDateInput.value == "") {
                alert("Veuillez introduire au moins une date de recherche");
                return false;
            }
            if (frm.traceEntryStartDateInput.value != "") {
                frm.traceEntryStartDate.value = isoToLegacy(frm.traceEntryStartDateInput.value, "/");
            }
            if (frm.traceEntryEndDateInput.value != "") {
                frm.traceEntryEndDate.value = isoToLegacy(frm.traceEntryEndDateInput.value, "/");
            }
            return true;
        }
    </script>
</head>
<body class="content">
<form name="adminFrm" action="<%= request.getContextPath() %>/index.jsp" method="post">
    <input type="hidden" name="admin" value="1">
</form>
<h1 onclick="window.document.adminFrm.submit()">Traçabilité des fournisseurs</h1>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr valign="top">
        <td width="28%" style="padding-right:1%">
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                <tr valign="top">
                    <td class="tabs-on" width="1%" nowrap height="19">Recherche</td>
                    <td class="blank-tab" width="99%" nowrap height="19">
                        <img src="onepix.gif" width="1" height="27" align="absmiddle" alt="">
                    </td>
                </tr>
            </table>
            <form name="frmProductSearch" action="${flowExecutionUrl}" method="post" onsubmit="return validateSearchFrm()">
                <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
                <input type="hidden" name="_eventId_submit" value="">
                <input type="hidden" name="traceEntryStartDate" value="">
                <input type="hidden" name="traceEntryEndDate" value="">
                <input type="hidden" name="traceEntrySupplierCode" value="">
                <table border="0" cellpadding="10" cellspacing="0" width="100%">
                    <tr>
                        <td class="layout-manager">
                            <table class="framing-table" width="100%" border="0" cellspacing="1" cellpadding="3">
                                <tr>
                                    <td class="column-head-prefs">Recherche d'un fournisseur</td>
                                </tr>
                                <tr>
                                    <td class="table-text-bold" nowrap>
                                        Code Fournisseur :<br>
                                        <span style="padding-left:25px">
                                            <input type="text" name="traceEntrySupplierCodeInput" size="18" value="">
                                        </span>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-text-bold" nowrap>
                                        Date début :<br>
                                        <div class="date-selector compact">
                                            <div class="date-controls">
                                                <button type="button" onclick="shiftDate('traceEntryStartDateInput', -1, 'traceEntryStartDateDisplay')">◀</button>
                                                <input type="date" id="traceEntryStartDateInput"
                                                       onchange="refreshDateLabel('traceEntryStartDateInput', 'traceEntryStartDateDisplay')"
                                                       min="2007-01-01" max="2050-12-31">
                                                <button type="button" onclick="shiftDate('traceEntryStartDateInput', 1, 'traceEntryStartDateDisplay')">▶</button>
                                            </div>
                                            <div style="display:none" class="date-display">Date sélectionnée: <span id="traceEntryStartDateDisplay"></span></div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-text-bold" nowrap>
                                        Date de fin :<br>
                                        <div class="date-selector compact">
                                            <div class="date-controls">
                                                <button type="button" onclick="shiftDate('traceEntryEndDateInput', -1, 'traceEntryEndDateDisplay')">◀</button>
                                                <input type="date" id="traceEntryEndDateInput"
                                                       onchange="refreshDateLabel('traceEntryEndDateInput', 'traceEntryEndDateDisplay')"
                                                       min="2007-01-01" max="2050-12-31">
                                                <button type="button" onclick="shiftDate('traceEntryEndDateInput', 1, 'traceEntryEndDateDisplay')">▶</button>
                                            </div>
                                            <div  style="display:none" class="date-display">Date sélectionnée: <span id="traceEntryEndDateDisplay"></span></div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-text-bold" nowrap>
                                        <center style="padding:10px">
                                            <input class="button" type="submit" value=" Recherche ">
                                        </center>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </form>
        </td>
        <td>
            <% if ("detail".equals(request.getAttribute("traceView"))) { %>
                <%@ include file="/WEB-INF/jsp/supplierEntryTraceability/productDet.jsp" %>
            <% } else { %>
                <%@ include file="/WEB-INF/jsp/supplierEntryTraceability/searchEntryResult.jsp" %>
            <% } %>
        </td>
    </tr>
</table>
</body>
</html>
