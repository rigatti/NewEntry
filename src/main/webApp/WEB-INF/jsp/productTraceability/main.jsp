<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>
<html>
<head>
    <title>Traçabilité des articles</title>
    <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/sort.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/dateSelector.js"></script>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/date-selector.css">
    <script type="text/javascript">
        function validateSearchFrm() {
            var frmProductSearch = window.document.frmProductSearch;
            var itemDetected = false;
            frmProductSearch.traceProductCode.value = "";
            frmProductSearch.traceEan.value = "";
            frmProductSearch.traceLot.value = "";
            frmProductSearch.traceValidityDate.value = "";
            if (frmProductSearch.productCodeInput.value != "") {
                frmProductSearch.traceProductCode.value = frmProductSearch.productCodeInput.value;
                itemDetected = true;
            }
            if (frmProductSearch.eanInput.value != "") {
                frmProductSearch.traceEan.value = frmProductSearch.eanInput.value;
                itemDetected = true;
            }
            if (frmProductSearch.lotNumberInput.value != "") {
                frmProductSearch.traceLot.value = frmProductSearch.lotNumberInput.value;
                itemDetected = true;
            }
            if (frmProductSearch.validityDateInput.value != "") {
                frmProductSearch.traceValidityDate.value = isoToLegacy(frmProductSearch.validityDateInput.value, "/");
                itemDetected = true;
            }
            if (!itemDetected) {
                alert("Veuillez introduire un critère de recherche");
                return false;
            }
            return true;
        }
    </script>
</head>
<body class="content">
<form name="adminFrm" action="<%= request.getContextPath() %>/index.jsp" method="post">
    <input type="hidden" name="admin" value="1">
</form>
<h1 onclick="window.document.adminFrm.submit()">Traçabilité des articles</h1>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr valign="top">
        <td width="25%" style="padding-right:1%">
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
                <input type="hidden" name="traceProductCode" value="">
                <input type="hidden" name="traceEan" value="">
                <input type="hidden" name="traceLot" value="">
                <input type="hidden" name="traceValidityDate" value="">
                <table border="0" cellpadding="10" cellspacing="0" width="100%">
                    <tr>
                        <td class="layout-manager">
                            <table class="framing-table" width="100%" border="0" cellspacing="1" cellpadding="3">
                                <tr>
                                    <td class="column-head-prefs">Recherche d'un article</td>
                                </tr>
                                <tr>
                                    <td class="table-text-bold" nowrap>
                                        Code Produit :<br>
                                        <span style="padding-left:25px"><input type="text" name="productCodeInput" size="22" value=""></span>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-text-bold" nowrap>
                                        EAN :<br>
                                        <span style="padding-left:25px"><input type="text" name="eanInput" size="22" value=""></span>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-text-bold" nowrap>
                                        Numéro de lot :<br>
                                        <span style="padding-left:25px"><input type="text" name="lotNumberInput" size="22" value=""></span>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-text-bold" nowrap>
                                        Date de validité :<br>
                                        <div class="date-selector compact">
                                            <div class="date-controls">
                                                <button type="button" onclick="shiftDate('validityDateInput', -1, 'validityDateDisplay')">◀</button>
                                                <input type="date" name="validityDateInput" id="validityDateInput"
                                                       onchange="refreshDateLabel('validityDateInput', 'validityDateDisplay')"
                                                       min="2007-01-01" max="2050-12-31">
                                                <button type="button" onclick="shiftDate('validityDateInput', 1, 'validityDateDisplay')">▶</button>
                                            </div>
                                            <div  style="display:none" class="date-display">
                                                Date sélectionnée: <span id="validityDateDisplay"></span>
                                            </div>
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
                <%@ include file="/WEB-INF/jsp/productTraceability/productDet.jsp" %>
            <% } else { %>
                <%@ include file="/WEB-INF/jsp/productTraceability/searchEntryResult.jsp" %>
            <% } %>
        </td>
    </tr>
</table>
</body>
</html>
