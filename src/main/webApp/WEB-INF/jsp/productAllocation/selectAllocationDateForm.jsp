<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>
<%@page import="org.belex.util.Util"%>
<%@page import="org.belex.allocation.AllocationEntry"%>
<%@page import="org.belex.allocation.Allocation"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%
    String today = Util.getNowFormated("yyyy-MM-dd");
    Allocation allocation = (Allocation) request.getAttribute("allocation");
    List<AllocationEntry> entries = new ArrayList<AllocationEntry>();

    if (allocation != null && allocation.getEntries() != null) {
        entries = allocation.getEntries();
        Collections.sort(entries, new Comparator<AllocationEntry>(){
            public int compare(AllocationEntry ae0, AllocationEntry ae1) {
                return ae0.getSupplier().getSupplierName().compareToIgnoreCase(ae1.getSupplier().getSupplierName());
            }
        });
    }
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Attribution - Sélection de la date</title>
    <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/dateSelector.js"></script>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/general.css" type="text/css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/date-selector.css" type="text/css">
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

        .button-group { display: flex; gap: 10px; flex-direction: column; }
        .button { padding: 10px; background-color: #000066; color: white; border: none; border-radius: 3px; cursor: pointer; font-weight: bold; text-align: center; }
        .button:hover { background-color: #000099; }
        .button-secondary { background-color: #666; }
        .button-secondary:hover { background-color: #888; }

        .supplier-entry { background-color: #f9f9f9; border-left: 4px solid #000066; padding: 10px; margin: 8px 0; cursor: pointer; border-radius: 3px; }
        .supplier-entry:hover { background-color: #f0f0ff; }
        .supplier-link { color: #000066; text-decoration: none; font-weight: bold; display: block; }
        .supplier-time { font-size: 0.85em; color: #888; }
        .no-entries { text-align: center; padding: 20px; color: #666; font-style: italic; }

        @media (max-width: 768px) {
            .container { flex-direction: column; height: auto; }
            .left-panel { width: 100%; border-right: none; border-bottom: 2px solid #ccc; }
        }
    </style>
    <script type="text/javascript">
        function formatAndSubmitDate() {
            refreshDateLabel("allocationDate", "allocationDateDisplay");
            // Convert date from yyyy-MM-dd to yyyyMMdd format for requestParams.date
            var dateInput = document.getElementById("allocationDate");
            var dateParts = dateInput.value.split('-');
            var dateFormatted = dateParts[0] + dateParts[1] + dateParts[2];

            // Set the hidden input with the formatted date
            document.getElementById("dateFormatted").value = dateFormatted;

            // Submit the form
            document.dateFilterFrm.submit();
        }

        function selectSupplier(supplierCode) {
            document.getElementById('supplierCodeInput').value = supplierCode;
            document.selectSupplierFrm.submit();
        }
    </script>
</head>
<body onload="refreshDateLabel('allocationDate', 'allocationDateDisplay')">
    <div class="container">
        <!-- LEFT PANEL: Date Selection -->
        <div class="left-panel">
            <div class="section">
                <h3>📅 Sélectionner la date</h3>
                <form name="dateFilterFrm" action="${flowExecutionUrl}" method="post">
                    <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
                    <input type="hidden" name="_eventId_search" value="">
                    <!-- Hidden field to pass formatted date to server (yyyyMMdd format) -->
                    <input type="hidden" name="date" id="dateFormatted" value="">

                    <div class="date-selector">
                        <div class="date-controls">
                            <button type="button" onclick="shiftDate('allocationDate', -1, 'allocationDateDisplay', formatAndSubmitDate);">◀ Jour précédent</button>
                            <input type="date" id="allocationDate"
                                   value="<%= today %>"
                                   onchange="formatAndSubmitDate();"
                                   min="2007-01-01" max="2050-12-31">
                            <button type="button" onclick="shiftDate('allocationDate', 1, 'allocationDateDisplay', formatAndSubmitDate);">Jour suivant ▶</button>
                        </div>
                        <div class="date-display">
                            Date sélectionnée: <span id="allocationDateDisplay"></span>
                        </div>
                    </div>
                </form>
            </div>

            <div class="section button-group">
                <form action="${flowExecutionUrl}" method="post" style="margin: 0;">
                    <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
                    <button type="submit" name="_eventId_back" class="button button-secondary">❌ Retour menu</button>
                </form>
            </div>
        </div>

        <!-- RIGHT PANEL: Suppliers List -->
        <div class="right-panel">
            <div class="section">
                <h3>📦 Fournisseurs à allouer</h3>
                <% if (entries.isEmpty()) { %>
                    <div class="no-entries">
                        ❌ Aucune entrée de marchandise disponible pour cette date
                    </div>
                <% } else { %>
                    <p>Cliquez sur un fournisseur pour voir ses articles (<%=entries.size()%> fournisseur(s))</p>
                    <% for (int i = 0; i < entries.size(); i++) {
                        AllocationEntry entry = entries.get(i);
                    %>
                        <div class="supplier-entry" onclick="selectSupplier('<%= entry.getSupplier().getSupplierCode() %>');">
                            <span class="supplier-link">📦 <%= entry.getSupplier().getSupplierCode() %> - <%= Util.getShortDisplayable(entry.getSupplier().getSupplierName(), 35) %></span>
                            <span class="supplier-time"><%= Util.formatDate(entry.getTime(), "hhmmss", "hh:mm:ss") %></span>
                        </div>
                    <% } %>
                <% } %>
            </div>
        </div>
    </div>

    <!-- Hidden form for supplier selection -->
    <form name="selectSupplierFrm" id="selectSupplierFrm" action="${flowExecutionUrl}" method="post" style="display: none;">
        <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
        <input type="hidden" name="_eventId_getEntryOrders" value="">
        <input type="hidden" name="allocation.supplierCode" id="supplierCodeInput" value="">
    </form>
</body>
</html>