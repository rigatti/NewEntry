<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="org.belex.util.Util"%>
<%@page import="org.belex.allocation.AllocationEntry"%>
<%@page import="org.belex.supplier.Supplier"%>
<%@page import="java.util.Vector"%>
<%@page import="org.belex.allocation.Allocation"%>
<%@page import="org.belex.requestparams.RequestParams"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%
    RequestParams requestParams = (RequestParams) request.getAttribute("requestParams");
    String dateInput = Util.getNowFormated("dd-MM-yyyy");
    if (requestParams != null && requestParams.getDate() != null && requestParams.getDate().length() == 8) {
        String storedDate = requestParams.getDate();
        dateInput = storedDate.substring(6, 8) + "-" + storedDate.substring(4, 6) + "-" + storedDate.substring(0, 4);
    }

    boolean displayNoResult = false;
    Allocation allocation = (Allocation) request.getAttribute("allocation");
    List<AllocationEntry> entries = new ArrayList<AllocationEntry>();
    if (allocation == null || allocation.getEntries() == null || allocation.getEntries().size() == 0) {
        displayNoResult = true;
    } else {
        entries = allocation.getEntries();
        Collections.sort(entries, new Comparator<AllocationEntry>() {
            public int compare(AllocationEntry ae0, AllocationEntry ae1) {
                return ae0.getSupplier().getSupplierName().compareToIgnoreCase(ae1.getSupplier().getSupplierName());
            }
        });
    }
%>
<html>
<head>
    <title>Attribution des articles</title>
    <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/date.js"></script>
    <script type="text/javascript">
        function submitForm() {
            var obj = window.document.frmSelectAllocationDate;
            if (validateSearch(obj)) {
                obj.submit();
            }
        }
        function validateSearch(obj) {
            if (checkDate(obj.dateInput.value, 2) > 0) {
                obj.date.value = strResult;
                return true;
            }
            alert("Veuillez introduire une date de recherche");
            return false;
        }
        function computeAllocationDate(step) {
            if (checkDate(window.document.frmSelectAllocationDate.dateInput.value, 2) > 0) {
                computeDate(strResult, "D", step);
                window.document.frmSelectAllocationDate.dateInput.value = strResult;
            }
        }
        function validateFrmEntryOrders(obj) {
            var formElements = obj.elements;
            obj.allocationEntryOrders.value = "";
            for (var i = 0; i < formElements.length; i++) {
                if (formElements[i].type == "checkbox" && formElements[i].checked == true && formElements[i].name.indexOf("Supplier=") == 0) {
                    obj.allocationEntryOrders.value += formElements[i].name + "#";
                }
            }
            obj.allocationEntrySuppliers.value = obj.allocationEntryOrders.value;
            return true;
        }
        String.prototype.startsWith = function (strChar) {
            if (!strChar) { return false; }
            strChar += "";
            return this.substring(0, strChar.length) == strChar;
        };
        function switchDisplay(id) {
            var obj = window.document.getElementById(id);
            if (obj.style.display == "none") {
                obj.style.display = "block";
            } else {
                obj.style.display = "none";
            }
        }
        function changeAllChexBox(id, idStartingWith) {
            var els = window.document.frmEntryOrders.elements;
            var globalChekbox = window.document.getElementById(id);
            for (var count = 0; count < els.length; count++) {
                if (els.item(count).id && els.item(count).id.startsWith(idStartingWith)) {
                    els.item(count).checked = globalChekbox.checked;
                }
            }
        }
        function changeCheckBox(id) {
            var checkbox = window.document.getElementById(id);
            checkbox.checked = !checkbox.checked;
        }
    </script>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin.css">
</head>
<body class="content">

    <form name="adminFrm" action="<%= request.getContextPath() %>/index.jsp" method="post">
    </form>

    <h1 onclick="window.document.adminFrm.submit()">Attribution des articles réceptionnés</h1>

    <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr valign="top">
            <td width="25%">
                <table border="0" cellpadding="0" cellspacing="0" width="100%">
                    <tr valign="top">
                        <td class="tabs-on" width="1%" nowrap height="19">Sélection</td>
                        <td class="blank-tab" width="99%" nowrap height="19">
                            <img src="onepix.gif" width="1" height="27" align="absmiddle" alt="">
                        </td>
                    </tr>
                </table>
                <form name="frmSelectAllocationDate" action="${flowExecutionUrl}" method="post" onsubmit="return validateSearch(this)">
                    <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
                    <input type="hidden" name="_eventId_search" value="">
                    <input type="hidden" name="date" value="">
                    <table border="0" cellpadding="10" cellspacing="0" width="100%">
                        <tr>
                            <td class="layout-manager">
                                <table class="framing-table" width="100%" border="0" cellspacing="1" cellpadding="3">
                                    <tr>
                                        <td class="column-head-prefs">Recherche d'un article</td>
                                    </tr>
                                    <tr>
                                        <td class="table-text-bold" nowrap align="center" style="padding-top:10px">
                                            <input type="button" value=" &lt; " onclick="computeAllocationDate(-1);submitForm()">
                                            <input onchange="submitForm()" type="text" size="10" name="dateInput" id="dateInput" value="<%= dateInput %>">
                                            <input type="button" value=" &gt; " onclick="computeAllocationDate(1);submitForm()">
                                            <br>
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
            </td>
            <td>
                <table border="0" cellpadding="0" cellspacing="0" width="100%">
                    <tr valign="top">
                        <td class="tabs-on" width="1%" nowrap height="19">Fournisseurs</td>
                        <td class="blank-tab" width="99%" nowrap height="19">
                            <img src="onepix.gif" width="1" height="27" align="absmiddle" alt="">
                        </td>
                    </tr>
                </table>
                <table border="0" cellpadding="10" cellspacing="0" width="100%">
                    <tr>
                        <td class="layout-manager">
                            <% if (!displayNoResult) { %>
                            <table class="framing-table" border="0" cellspacing="1" cellpadding="3" width="100%">
                                <tr>
                                    <td class="column-head-prefs">Arrivées à attribuer</td>
                                </tr>
                                <tr>
                                    <td nowrap class="table-text">
                                        <form name="frmEntryOrders" action="${flowExecutionUrl}" method="post" onsubmit="return validateFrmEntryOrders(this);">
                                            <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
                                            <input type="hidden" name="_eventId_getEntryOrders" value="">
                                            <input type="hidden" name="allocationEntryOrders" value="">
                                            <input type="hidden" name="allocationEntrySuppliers" value="">
                                            <table width="100%">
                                                <%
                                                for (int i = 0; i < entries.size(); i++) {
                                                    AllocationEntry entry = entries.get(i);
                                                    Supplier supplier = entry.getSupplier();
                                                    if (supplier.getOrders() == null) {
                                                        continue;
                                                    }
                                                    Vector<Supplier.Order> orders = supplier.getOrders();
                                                    if ((i % 2) == 0) { %>
                                                <tr>
                                                    <% } %>
                                                    <td width="45%" class="table-text">
                                                        <a class="ctext" href="javascript:switchDisplay('div_<%= i %>')"><b><i><%= Util.getShortDisplayable(supplier.getSupplierName(), 20) %></i></b> <font size="0.5"><%= Util.formatDate(entry.getTime(), "hhmmss", "hh:mm:ss") %></font></a>
                                                        <input type="checkbox" id="checkbox_<%= i %>" onclick="changeAllChexBox('checkbox_<%= i %>', 'Supplier=<%= supplier.getSupplierCode() %>;')"><br>
                                                        <span id="div_<%= i %>" style="display:none;">
                                                            <%
                                                            int cnt = 0;
                                                            for (int j = 0; j < orders.size(); j++) {
                                                                Supplier.Order order = orders.get(j);
                                                                String currentId = "Supplier=" + supplier.getSupplierCode() + ";Number=" + order.getNumber() + ";Letter=" + order.getLetter();
                                                            %>
                                                            <input type="checkbox" name="<%= currentId %>" id="<%= currentId %>"><a class="ctext" href="javascript:changeCheckBox('<%= currentId %>')"><%= order.getNumber() + " " + order.getLetter() %></a>
                                                            <% if (cnt++ > 1 && j + 1 < orders.size()) {
                                                                cnt = 0; %>
                                                            <br>
                                                            <% } else { %>
                                                            &nbsp;&nbsp;
                                                            <% } %>
                                                            <% } %>
                                                        </span>
                                                    </td>
                                                    <% if ((i % 2) == 1) { %>
                                                </tr>
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
                                    <td class="column-head-prefs">Aucune arrivée pour cette date</td>
                                </tr>
                            </table>
                            <% } %>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</body>
</html>
