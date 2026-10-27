<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="org.belex.export.Export"%>
<%@page import="org.belex.export.ProductToExport"%>
<%@page import="java.util.List"%>
<%
    Export export = (Export) request.getAttribute("export");
%>
<html>
<head>
    <title>Export Product</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin.css">
</head>
<body class="content">
    <h1>Export Product</h1>
    <table border="0" cellpadding="10" cellspacing="0" width="100%">
        <tr>
            <td class="layout-manager">
                <table class="framing-table" width="100%" border="0" cellspacing="1" cellpadding="3">
                    <tr>
                        <td class="column-head-prefs">Produits</td>
                    </tr>
                    <tr>
                        <td class="table-text">
                            <%
                            if (export != null && export.getProducts() != null) {
                                List<ProductToExport> products = export.getProducts();
                                for (ProductToExport product : products) {
                                    out.print(product.getProductCode() + ";");
                                    out.print(product.getDescription_fr() + ";");
                                    out.print(product.getFamilyCode() + ";");
                                    if (StringUtils.equals(product.getEditionTypeLabel(), "PRO")) {
                                        out.print("ignoreMe;");
                                    } else {
                                        out.print(product.getMarginForStandardCustomer() + ";");
                                    }
                                    out.print(product.getTotalConditioning() + ";");
                                    out.print(product.getTotalPrice() + ";");
                                    out.print(product.getEditionTypeLabel() + ";");
                                    out.print(product.getEditionTypeId() + ";");
                                    out.print(1000);
                                    out.print("<br>");
                                }
                            }
                            %>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</body>
</html>
