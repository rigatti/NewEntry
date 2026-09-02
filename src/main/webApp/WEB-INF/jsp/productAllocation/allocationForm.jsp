<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>
<%@page import="org.belex.allocation.Allocation"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.belex.allocation.AllocationEntry"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.belex.util.Util"%>
<%@page import="org.belex.requestparams.RequestParams"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="java.util.HashSet"%>
<%
    Allocation allocation = (Allocation) request.getAttribute("allocation");
    RequestParams requestParams = (RequestParams) request.getAttribute("requestParams");

    HashSet<String> suppliers = new HashSet<String>();
    if (requestParams != null && requestParams.getAllocationEntryOrders() != null) {
        StringTokenizer stOrders = new StringTokenizer(requestParams.getAllocationEntryOrders(), "#");
        while (stOrders.hasMoreElements()) {
            StringTokenizer stOrder = new StringTokenizer((String) stOrders.nextElement(), ";");
            if (stOrder.countTokens() == 3) {
                String strTmp = (String) stOrder.nextElement();
                String supplierCode = strTmp.substring(strTmp.indexOf("=") + 1);
                if (!suppliers.contains(supplierCode)) {
                    suppliers.add(supplierCode);
                }
            }
        }
    }
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Attribution - Allocation des articles</title>
    <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/sort.js"></script>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/general.css" type="text/css">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: Arial, sans-serif; background-color: #f5f5f5; }
        .container { display: flex; height: 100vh; }

        .left-panel {
            width: 28%;
            background-color: #f9f9f9;
            border-right: 2px solid #ccc;
            overflow-y: auto;
            padding: 20px;
            box-shadow: inset -2px 0 5px rgba(0,0,0,0.05);
        }

        .right-panel {
            flex: 1;
            background-color: white;
            overflow-y: auto;
            padding: 20px;
        }

        .section { margin-bottom: 25px; }
        .section h3 { background-color: #000066; color: white; padding: 10px 15px; border-radius: 3px; margin-bottom: 15px; font-size: 14px; }
        .section p { margin: 10px 0; font-size: 13px; color: #666; }

        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; font-weight: bold; margin-bottom: 5px; font-size: 13px; }
        .form-group input, .form-group select { width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 3px; font-size: 13px; }
        .form-group input:focus, .form-group select:focus { outline: none; border-color: #000066; background-color: #f0f0ff; }

        .button-group { display: flex; gap: 10px; flex-direction: column; }
        .button { padding: 10px; background-color: #000066; color: white; border: none; border-radius: 3px; cursor: pointer; font-weight: bold; text-align: center; }
        .button:hover { background-color: #000099; }
        .button-secondary { background-color: #666; }
        .button-secondary:hover { background-color: #888; }

        table { width: 100%; border-collapse: collapse; margin-top: 15px; }
        th { background-color: #000066; color: white; padding: 10px; text-align: left; }
        td { padding: 8px; border-bottom: 1px solid #ddd; }
        tr:hover { background-color: #f5f5ff; }

        .checkbox-cell { text-align: center; }
        .info-box { background-color: #f0f0ff; border-left: 4px solid #000066; padding: 10px; margin: 10px 0; border-radius: 3px; }

        @media (max-width: 768px) {
            .container { flex-direction: column; height: auto; }
            .left-panel { width: 100%; border-right: none; border-bottom: 2px solid #ccc; }
        }
    </style>
    <script type="text/javascript">
        function checkReportRequest() {
            var checks = [
                document.getElementById("allocReportSubstitution"),
                document.getElementById("allocReportStock"),
                document.getElementById("allocReportSupplier"),
                document.getElementById("allocReportDeletedOrder"),
                document.getElementById("allocReportRenewedOrder"),
                document.getElementById("allocReportStandard")
            ];

            for (var i = 0; i < checks.length; i++) {
                if (checks[i] && checks[i].checked) return true;
            }
            alert("Veuillez sélectionner au moins un type de rapport");
            return false;
        }
    </script>
</head>
<body>
    <div class="container">
        <!-- LEFT PANEL: Control Panel -->
        <div class="left-panel">
            <div class="section">
                <h3>📊 Rapport d'allocation</h3>
                <form name="reportFrm" action="${flowExecutionUrl}" method="post" onsubmit="return checkReportRequest();">
                    <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
                    <input type="hidden" name="_eventId_generateReport" value="">

                    <div class="form-group">
                        <label><input type="checkbox" id="allocReportSubstitution" name="allocReportSubstitution"> Substitution</label>
                        <label><input type="checkbox" id="allocReportStock" name="allocReportStock"> Stock</label>
                        <label><input type="checkbox" id="allocReportSupplier" name="allocReportSupplier"> Fournisseur</label>
                        <label><input type="checkbox" id="allocReportStandard" name="allocReportStandard"> Standard</label>
                        <label><input type="checkbox" id="allocReportRenewedOrder" name="allocReportRenewedOrder"> Recommande</label>
                        <label><input type="checkbox" id="allocReportDeletedOrder" name="allocReportDeletedOrder"> Suppression</label>
                    </div>

                    <div class="button-group">
                        <button type="submit" class="button">📄 Générer rapport</button>
                    </div>
                </form>
            </div>

            <div class="section button-group">
                <form action="${flowExecutionUrl}" method="post" style="margin: 0;">
                    <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
                    <button type="submit" name="_eventId_confirmAllocation" class="button">✔ Confirmer</button>
                </form>
                <form action="${flowExecutionUrl}" method="post" style="margin: 0;">
                    <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
                    <button type="submit" name="_eventId_back" class="button button-secondary">❌ Annuler</button>
                </form>
            </div>
        </div>

        <!-- RIGHT PANEL: Content -->
        <div class="right-panel">
            <div class="section">
                <h3>📦 Attribution des articles réceptionnés</h3>
                <% if (allocation != null && suppliers.size() > 0) { %>
                    <div class="info-box">
                        <strong>Fournisseur(s):</strong>
                        <%
                        boolean isFirst = true;
                        for (String supplierCode : suppliers) {
                            if (!isFirst) out.print(", ");
                            out.print("<strong>" + allocation.getSupplierName(supplierCode) + "</strong>");
                            isFirst = false;
                        }
                        %>
                    </div>
                    <%@ include file="/WEB-INF/jsp/productAllocation/jspf/allocationFormWaiting.jspf" %>
                <% } else { %>
                    <div class="info-box" style="background-color: #ffe6e6; border-left-color: #cc0000;">
                        ❌ Veuillez sélectionner les critères de recherche
                    </div>
                <% } %>
            </div>
        </div>
    </div>
</body>
</html>