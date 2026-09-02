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
        .form-group label { display: block; font-weight: bold; margin-bottom: 5px; font-size: 13px; color: #333; }
        .form-group input[type="date"] { width: 100%; padding: 8px; border: 1px solid #000066; border-radius: 3px; font-size: 13px; }
        .form-group input[type="date"]:focus { outline: none; background-color: #f0f0ff; }

        .date-nav-buttons { display: flex; gap: 5px; margin-top: 10px; }
        .date-nav-buttons button { flex: 1; padding: 8px; background-color: #000066; color: white; border: none; border-radius: 3px; cursor: pointer; font-size: 12px; }
        .date-nav-buttons button:hover { background-color: #000099; }

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
        function changeDateBy(days) {
            var dateInput = document.getElementById("allocationDate");
            var currentDate = new Date(dateInput.value);
            currentDate.setDate(currentDate.getDate() + days);
            var year = currentDate.getFullYear();
            var month = String(currentDate.getMonth() + 1).padStart(2, '0');
            var day = String(currentDate.getDate()).padStart(2, '0');
            dateInput.value = year + '-' + month + '-' + day;
            formatAndSubmitDate();
        }

        function formatAndSubmitDate() {
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
<body>
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

                    <div class="form-group">
                        <label for="allocationDate">Date d'allocation :</label>
                        <input type="date" id="allocationDate"
                               value="<%= today %>"
                               onchange="formatAndSubmitDate();">
                    </div>

                    <div class="date-nav-buttons">
                        <button type="button" onclick="changeDateBy(-1);">◀ Prev</button>
                        <button type="button" onclick="changeDateBy(1);">Next ▶</button>
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