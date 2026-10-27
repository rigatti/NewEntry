<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>
<%@page import="java.util.Vector"%>
<%@page import="org.belex.supplier.Supplier"%>
<%@page import="org.belex.product.Product"%>
<%@page import="org.belex.requestparams.RequestParams"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%
    Vector<Supplier> suppliers = new Vector<Supplier>();
    Object suppliersObj = request.getAttribute("suppliers");
    if (suppliersObj instanceof Vector) {
        suppliers = (Vector<Supplier>) suppliersObj;
    }
    RequestParams requestParams = (RequestParams) request.getAttribute("requestParams");
    String adminView = (String) request.getAttribute("adminView");
    boolean showDetail = "detail".equals(adminView);
    Object productsObj = request.getAttribute("products");
%>
<html>
<head>
    <title>Gestion des articles</title>
    <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/sort.js"></script>
    <script type="text/javascript">
        function validateProductSearch(obj) {
            var searchType = getRadioValue(obj, "searchType");
            if (searchType == 1) {
                if (obj.productCodeInput.value == "") {
                    alert("Veuillez introduire un critère de recherche");
                    obj.productCodeInput.focus();
                    return false;
                }
                obj.searchValue.value = obj.productCodeInput.value;
            } else {
                if (obj.labelInput.value == "") {
                    alert("Veuillez introduire un critère de recherche");
                    obj.labelInput.focus();
                    return false;
                }
                obj.searchValue.value = obj.labelInput.value;
            }
            return true;
        }
        function sendTempProductSearch() {
            window.document.frmTempProductSearch.submit();
        }
        function switchBoxStatus(obj) {
            obj.checked = !obj.checked;
        }
        function sendDetail(id) {
            window.document.frmDetail.detailIndex.value = id;
            window.document.frmDetail.submit();
        }
        function validateUpdateProductCode() {
            var frm = window.document.frmUpdateProductCode;
            if (frm.adminNewProductCode.value == "") {
                alert("Veuillez introduire un code produit");
                return false;
            }
            return confirm("Etes vous certain de modifier ce code produit ?");
        }
        function confirmDelete() {
            return confirm("Etes vous certain de supprimer ce code produit ?");
        }
    </script>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin.css">
</head>
<body class="content">

<form name="adminFrm" action="<%= request.getContextPath() %>/index.jsp" method="post">
    <input type="hidden" name="admin" value="1">
</form>

<h1 onclick="window.document.adminFrm.submit()">Gestion des articles</h1>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr valign="top">
        <td width="28%">
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                <tr valign="top">
                    <td class="tabs-on" width="1%" nowrap height="19">Recherche</td>
                    <td class="blank-tab" width="99%" nowrap height="19">
                        <img src="onepix.gif" width="1" height="27" align="absmiddle" alt="">
                    </td>
                </tr>
            </table>

            <form name="frmTempProductSearch" action="${flowExecutionUrl}" method="post">
                <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
                <input type="hidden" name="_eventId_productSearch" value="">
                <input type="hidden" name="searchValue" value="TBD">
                <input type="hidden" name="searchType" value="1">
                <input type="hidden" name="searchExactMatch" value="">
                <input type="hidden" name="searchOnSupplier" value="">
            </form>

            <form name="frmProductSearch" action="${flowExecutionUrl}" method="post" onsubmit="return validateProductSearch(this)">
                <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
                <input type="hidden" name="_eventId_productSearch" value="">
                <input type="hidden" name="searchValue" value="">
                <table border="0" cellpadding="10" cellspacing="0" width="100%">
                    <tr>
                        <td class="layout-manager">
                            <table class="framing-table" width="100%" border="0" cellspacing="1" cellpadding="3">
                                <tr>
                                    <td class="column-head-prefs">Recherche d'un article</td>
                                </tr>
                                <tr>
                                    <td class="table-text-bold" nowrap>
                                        <input type="radio" value="1" name="searchType" checked> <a class="ctext" href="javascript:void(0)" onclick="setRadioState(window.document.frmProductSearch,'searchType',0, true)">Code Produit :</a><br>
                                        <span style="padding-left:25px">
                                            <input type="text" name="productCodeInput" size="22" value="" onfocus="setRadioState(window.document.frmProductSearch,'searchType',0, true)">
                                        </span>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-text-bold" nowrap>
                                        <input type="radio" value="2" name="searchType"> <a class="ctext" href="javascript:void(0)" onclick="setRadioState(window.document.frmProductSearch,'searchType',1, true)">Libellé :</a><br>
                                        <span style="padding-left:25px">
                                            <input type="text" name="labelInput" size="22" value="" onfocus="setRadioState(window.document.frmProductSearch,'searchType',1, true)">
                                        </span>
                                    </td>
                                </tr>
                            </table>
                            <table class="framing-table" width="100%" border="0" cellspacing="1" cellpadding="3">
                                <tr>
                                    <td class="column-head-prefs">+ Options supplémentaires</td>
                                </tr>
                                <tr>
                                    <td class="table-text-bold" nowrap>
                                        <input type="checkbox" value="1" name="searchExactMatch"> <a class="ctext" href="javascript:void(0)" onclick="switchBoxStatus(window.document.frmProductSearch.searchExactMatch)">Code produit exact</a>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-text-bold" nowrap>
                                        Pour le fournisseur suivant :<br>
                                        <select name="searchOnSupplier">
                                            <option value="">-</option>
                                            <% for (int s = 0; s < suppliers.size(); s++) {
                                                Supplier supplier = suppliers.get(s); %>
                                            <option value="<%= supplier.getSupplierCode() %>"><%= Util.getShortDisplayable(supplier.getSupplierName(), 25) %></option>
                                            <% } %>
                                        </select>
                                        <br>
                                        <center style="padding:10px">
                                            <input class="button" type="submit" value=" Recherche ">
                                        </center>
                                    </td>
                                </tr>
                            </table>
                            <br>
                            <table class="framing-table" width="100%" border="0" cellspacing="1" cellpadding="3">
                                <tr>
                                    <td class="column-head-prefs">Recherche des articles temporaires</td>
                                </tr>
                                <tr>
                                    <td class="table-text" style="padding:10px" nowrap>
                                        <center>
                                            <input class="button" type="button" value=" Recherche " onclick="sendTempProductSearch()">
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
            <% if (!showDetail) { %>
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                <tr valign="top">
                    <td class="tabs-on" width="1%" nowrap height="19">Articles</td>
                    <td class="blank-tab" width="99%" nowrap height="19">
                        <img src="onepix.gif" width="1" height="27" align="absmiddle" alt="">
                    </td>
                </tr>
            </table>
            <form name="frmDetail" action="${flowExecutionUrl}" method="post">
                <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
                <input type="hidden" name="_eventId_productDet" value="">
                <input type="hidden" name="detailIndex" value="">
            </form>
            <table border="0" cellpadding="10" cellspacing="0" width="100%">
                <tr>
                    <td class="layout-manager">
                        <% if (!(productsObj instanceof Vector)) { %>
                        <table class="framing-table" width="100%" border="0" cellspacing="1" cellpadding="3">
                            <tr>
                                <td class="column-head-prefs">Veuillez introduire un critère de recherche</td>
                            </tr>
                        </table>
                        <% } else {
                            Vector<Product> products = (Vector<Product>) productsObj;
                            if (products.size() == 0) { %>
                        <table class="framing-table" width="100%" border="0" cellspacing="1" cellpadding="3">
                            <tr>
                                <td class="column-head-prefs">Aucun article ne correspond à vos critères de recherche</td>
                            </tr>
                        </table>
                        <% } else { %>
                        <table id="productList" class="framing-table" width="100%" border="0" cellspacing="1" cellpadding="3">
                            <thead>
                            <tr>
                                <td class="column-head-name sortable" onclick="sortTable(document.getElementById('productList'), 0, 'String', null, null, null, null)">Libellé</td>
                                <td class="column-head-name sortable" onclick="sortTable(document.getElementById('productList'), 1, 'String', null, null, null, null)">Code produit</td>
                                <td class="column-head-name">Conditionnement et date de dernière modification</td>
                            </tr>
                            </thead>
                            <tbody>
                            <% for (int i = 0; i < products.size(); i++) {
                                Product product = products.get(i);
                                Vector<Product.Unit> units = product.getUnits();
                                if (StringUtils.isNotBlank(product.getDescription())) {
                                    String strTmp = product.getDescription().trim();
                                    if (strTmp.equals("")) {
                                        strTmp = "Inconnu";
                                    } %>
                            <tr>
                                <td class="table-text-bold">
                                    <% if (product.getProductCode().endsWith("TBD")) { %>
                                    <a class="ctextunderlined" href="javascript:sendDetail(<%= i %>)"><%= strTmp %></a>
                                    <% } else { %>
                                    <%= strTmp %>
                                    <% } %>
                                </td>
                                <td class="table-text" valign="top"><%= product.getProductCode() %></td>
                                <td class="table-text">
                                    <table width="100%" cellpadding="0" cellspacing="0">
                                        <% if (units != null) {
                                            for (int j = 0; j < units.size(); j++) {
                                                Product.Unit unit = units.get(j); %>
                                        <tr>
                                            <td class="table-text" width="50%"><%= Util.displayContitionnement(unit.getNumber(), unit.getConditionnement()) %></td>
                                            <td class="table-text"><%= unit.getEan() %></td>
                                            <td class="table-text"><%= unit.getDateLastMofication() %></td>
                                        </tr>
                                        <% }
                                        } %>
                                    </table>
                                </td>
                            </tr>
                            <% }
                            } %>
                            </tbody>
                        </table>
                        <% }
                        } %>
                    </td>
                </tr>
            </table>
            <% } else { %>
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                <tr valign="top">
                    <td class="tabs-on" width="1%" nowrap height="19">Détail d'un article</td>
                    <td class="blank-tab" width="99%" nowrap height="19">
                        <img src="onepix.gif" width="1" height="27" align="absmiddle" alt="">
                    </td>
                </tr>
            </table>
            <table border="0" cellpadding="10" cellspacing="0" width="100%">
                <tr>
                    <td class="layout-manager">
                        <%
                        boolean detailOk = requestParams != null
                                && productsObj instanceof Vector
                                && suppliersObj instanceof Vector
                                && requestParams.getDetailIndex() != null
                                && requestParams.getDetailIndex().length() > 0;
                        Vector<Product> products = detailOk ? (Vector<Product>) productsObj : null;
                        int detailIndex = detailOk ? Integer.parseInt(requestParams.getDetailIndex()) : -1;
                        if (!detailOk || products.size() == 0 || detailIndex < 0 || detailIndex >= products.size()) { %>
                        <table class="framing-table" width="100%" border="0" cellspacing="1" cellpadding="3">
                            <tr>
                                <td class="column-head-prefs">Suite à un problème technique, pas de détail disponible.</td>
                            </tr>
                        </table>
                        <% } else {
                            Product product = products.get(detailIndex); %>
                        <table class="framing-table" width="100%" border="0" cellspacing="1" cellpadding="3">
                            <tr>
                                <td width="20%" class="table-text-bold">Description</td>
                                <td width="80%" class="table-text"><%= product.getDescription() %></td>
                            </tr>
                            <tr>
                                <td class="table-text-bold">Code</td>
                                <td class="table-text">
                                    <% if (product.getProductCode().endsWith("TBD")) { %>
                                    <form name="frmUpdateProductCode" action="${flowExecutionUrl}" method="post" onsubmit="return validateUpdateProductCode()">
                                        <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
                                        <input type="hidden" name="_eventId_updateProductCode" value="">
                                        <input type="hidden" name="adminOldProductCode" value="<%= product.getProductCode() %>">
                                        <input type="text" name="adminNewProductCode" value="<%= product.getProductCode() %>" maxlength="10">
                                        ENTER après modification
                                    </form>
                                    <form name="frmdeleteProduct" action="${flowExecutionUrl}" method="post" onsubmit="return confirmDelete()">
                                        <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
                                        <input type="hidden" name="_eventId_deleteProductCode" value="">
                                        <input type="hidden" name="adminOldProductCode" value="<%= product.getProductCode() %>">
                                        <input type="submit" value=" Suppression ">
                                    </form>
                                    <% } else { %>
                                    <%= product.getProductCode() %>
                                    <% } %>
                                </td>
                            </tr>
                        </table>
                        <%
                        Vector<Product.Unit> units = product.getUnits();
                        if (units != null) {
                            for (int j = 0; j < units.size(); j++) {
                                Product.Unit unit = units.get(j); %>
                        <table class="framing-table" width="100%" border="0" cellspacing="1" cellpadding="3">
                            <tr>
                                <td class="column-head-name" colspan="2">Conditionnement <%= j + 1 %></td>
                            </tr>
                            <tr>
                                <td class="table-text-bold" width="20%">Nombre d'article par conditionnement unitaire</td>
                                <td class="table-text" width="80%"><%= unit.getNumber() %></td>
                            </tr>
                            <tr>
                                <td class="table-text-bold">Conditionnement unitaire</td>
                                <td class="table-text"><%= unit.getConditionnement() %></td>
                            </tr>
                            <tr>
                                <td class="table-text-bold">Fournisseur</td>
                                <td class="table-text"><%= utb.getSupplierName(suppliers, unit.getSupplierCode()) %></td>
                            </tr>
                            <tr>
                                <td class="table-text-bold">Code ean</td>
                                <td class="table-text"><%= unit.getEan() %></td>
                            </tr>
                        </table>
                        <% }
                        }
                        } %>
                    </td>
                </tr>
            </table>
            <% } %>
        </td>
    </tr>
</table>
</body>
</html>
