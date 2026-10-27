<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="org.belex.export.Export"%>
<%@page import="org.belex.export.CustomerToExport"%>
<%@page import="java.util.List"%>
<%
    Export export = (Export) request.getAttribute("export");
%>
<html>
<head>
    <title>Export Customer</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin.css">
</head>
<body class="content">
    <h1>Export Customer</h1>
    <table border="0" cellpadding="10" cellspacing="0" width="100%">
        <tr>
            <td class="layout-manager">
                <table class="framing-table" width="100%" border="0" cellspacing="1" cellpadding="3">
                    <tr>
                        <td class="column-head-prefs">Clients</td>
                    </tr>
                    <tr>
                        <td class="table-text">
                            <%
                            if (export != null && export.getCustomers() != null) {
                                List<CustomerToExport> customers = export.getCustomers();
                                for (CustomerToExport customer : customers) {
                                    out.print(customer.getName() + ";");
                                    out.print(customer.getNameAdditionalPart() + ";");
                                    out.print(customer.getEmail() + ";");
                                    if (customer.getGroups() != null && customer.getGroups().size() > 0) {
                                        out.print(StringUtils.removeEnd(customer.getGroupsCodes(), ",") + ";");
                                    } else {
                                        out.print(";");
                                    }
                                    if (customer.getBuyRate() > 0) {
                                        out.print(customer.getBuyRate());
                                    }
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
