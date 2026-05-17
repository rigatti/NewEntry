<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jspf/globalHeader.jspf"%>
<%@page import="org.belex.arrival.Arrival"%>
<%@page import="org.belex.supplier.Supplier"%>
<%@page import="java.util.Vector"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%
    Arrival arrival = (Arrival) request.getAttribute("arrival");

    // Format current date for HTML5 date input (yyyy-MM-dd)
    String currentDateStr = arrival.getSearchSupplierDate();
    String dateForInput = "";
    if (currentDateStr != null && currentDateStr.length() == 8) {
        dateForInput = currentDateStr.substring(0, 4) + "-"
                      + currentDateStr.substring(4, 6) + "-"
                      + currentDateStr.substring(6, 8);
    }
%>
<html>
<head>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/jsp/productEntry/jspf/headHeader.jspf"%>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/general.css" type="text/css">
    <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>
    <style>
        .date-selector {
            margin: 20px 0;
            padding: 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
            background-color: #f9f9f9;
        }
        .date-controls {
            display: flex;
            align-items: center;
            gap: 15px;
            justify-content: center;
            flex-wrap: wrap;
        }
        .date-controls button {
            padding: 8px 16px;
            background-color: #000066;
            color: white;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            font-size: 14px;
            font-weight: bold;
        }
        .date-controls button:hover {
            background-color: #000099;
        }
        .date-display {
            font-size: 18px;
            font-weight: bold;
            color: #000066;
            min-width: 150px;
            text-align: center;
        }
        input[type="date"] {
            padding: 8px;
            font-size: 14px;
            border: 1px solid #000066;
            border-radius: 3px;
            cursor: pointer;
        }
        .suppliers-section {
            margin-top: 30px;
            padding: 15px;
            border: 2px solid #000066;
            border-radius: 5px;
            background-color: #f0f0ff;
        }
        .suppliers-list {
            margin: 15px 0;
        }
        .suppliers-list ul {
            list-style-type: none;
            padding: 0;
        }
        .suppliers-list li {
            padding: 10px;
            background-color: white;
            margin: 5px 0;
            border-left: 4px solid #000066;
            padding-left: 15px;
        }
        .no-suppliers {
            text-align: center;
            padding: 20px;
            color: #666;
            font-style: italic;
        }
        .view-suppliers-btn {
            display: block;
            margin: 20px auto;
            padding: 12px 30px;
            background-color: #000066;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            font-weight: bold;
        }
        .view-suppliers-btn:hover {
            background-color: #000099;
        }
    </style>
    <script type="text/javascript">
        // Calculate date by adding/subtracting days
        function changeDateBy(days) {
            var dateInput = document.getElementById("searchSupplierDate");
            var currentDate = new Date(dateInput.value);
            currentDate.setDate(currentDate.getDate() + days);

            // Format back to yyyy-MM-dd
            var year = currentDate.getFullYear();
            var month = String(currentDate.getMonth() + 1).padStart(2, '0');
            var day = String(currentDate.getDate()).padStart(2, '0');
            dateInput.value = year + '-' + month + '-' + day;

            // Update display
            updateDateDisplay();
        }

        // Update the displayed date
        function updateDateDisplay() {
            var dateInput = document.getElementById("searchSupplierDate");
            if (dateInput.value) {
                var dateObj = new Date(dateInput.value);
                var options = { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' };
                document.getElementById("dateDisplay").textContent = dateObj.toLocaleDateString('fr-FR', options);
            }
        }

        // Submit form when date changes or button clicked
        function searchSuppliers() {
            updateDateDisplay();
            // Convert date from yyyy-MM-dd to yyyyMMdd format for searchSupplierDate
            var dateInput = document.getElementById("searchSupplierDate");
            var dateParts = dateInput.value.split('-');
            var dateForSubmit = dateParts[0] + dateParts[1] + dateParts[2];

            // Set a hidden input with the formatted date
            document.getElementById("searchSupplierDateFormatted").value = dateForSubmit;

            // Submit the form
            document.searchPlannedSupplierFrm.submit();
        }
    </script>
</head>
<body>
    <%@ include file="/WEB-INF/jsp/productEntry/jspf/bodyHeader.jspf" %>

    <center>
        <h2>Arrivage de marchandises - Sélection de la date</h2>

        <!-- Main form for date selection -->
        <form name="searchPlannedSupplierFrm" action="${flowExecutionUrl}" method="post">
            <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
            <input type="hidden" name="_eventId_searchPlannedSupplier" value="">
            <!-- Hidden field to pass formatted date to server -->
            <input type="hidden" name="searchSupplierDate" id="searchSupplierDateFormatted" value="">

            <!-- Date selector section -->
            <div class="date-selector">
                <h3>Sélectionnez une date</h3>

                <div class="date-controls">
                    <!-- Previous day button -->
                    <button type="button" value=" ◀ Jour précédent " onclick="changeDateBy(-1); searchSuppliers();">
                        ◀ Jour précédent
                    </button>

                    <!-- Date picker input (HTML5) -->
                    <input type="date" id="searchSupplierDate"
                           value="<%= dateForInput %>"
                           onchange="searchSuppliers();"
                           min="2007-01-01" max="2050-12-31">

                    <!-- Next day button -->
                    <button type="button" value=" Jour suivant ▶ " onclick="changeDateBy(1); searchSuppliers();">
                        Jour suivant ▶
                    </button>
                </div>
                <br>
                <!-- Display current date in readable format -->
                <div class="date-display">
                    Date sélectionnée: <span id="dateDisplay"></span>
                </div>
            </div>
        </form>

        <!-- Suppliers section -->
        <div class="suppliers-section">
            <c:if test="${fn:length(arrival.suppliers) == 0}">
                <div class="no-suppliers">
                    <strong>Aucun fournisseur n'est planifié pour la date sélectionnée</strong>
                </div>
            </c:if>

            <c:if test="${fn:length(arrival.suppliers) > 0}">
                <h3>- Fournisseurs prévus pour cette date -</h3>

                <div class="suppliers-list">
                    <strong>Nombre total: <%= (arrival.getSuppliers() != null ? arrival.getSuppliers().size() : 0) %> fournisseur(s)</strong>
                    <ul>
                    <% Vector<Supplier> suppliers = arrival.getSuppliers();
                       if (suppliers != null && suppliers.size() > 0) {
                           for (int i = 0; i < suppliers.size(); i++) { %>
                                <li
                                <% if (suppliers.get(i).isPending()) { %>
                                    style="background-color:#FF8000"
                                <% } else if (suppliers.get(i).isClosed()) { %>
                                    style="background-color:#00D000"
                                <% } else if (suppliers.get(i).isOpened()) { %>
                                    style="background-color:#FF0000"
                                <% } %>
                                 ><a href="#" onclick="document.getElementById('supplierCode').value='<%= suppliers.get(i).getSupplierCode() %>'; document.getElementById('selectSupplierForm').submit();">
                                    <strong><%= suppliers.get(i).getSupplierCode() %></strong> - <%= suppliers.get(i).getSupplierName() %>
                                    </a></li>
                    <%  }
                       } else { %>
                            <p class="no-suppliers">Aucun fournisseur n'est planifié pour cette date sélectionnée.</p>
                       <% } %>
                    </ul>
                </div>

                <!-- Form to show suppliers -->
                <!--form name="showSuppliersFrm" method="post" action="${flowExecutionUrl}">
                    <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
                    <input type="submit" name="_eventId_showSuppliers" class="view-suppliers-btn" value="Afficher les fournisseurs" />
                </form-->
                <form id="selectSupplierForm" action="${flowExecutionUrl}" method="post">
                    <input type="hidden" name="supplier.supplierCode" id="supplierCode" value=""/>
                    <input type="hidden" name="_eventId_supplierSelected" value=""/>
                </form>

            </c:if>
        </div>
    </center>

    <script type="text/javascript">
        // Initialize date display on page load
        window.addEventListener('load', function() {
            updateDateDisplay();
        });
    </script>
</body>
</html>

