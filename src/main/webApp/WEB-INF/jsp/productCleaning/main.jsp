<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Nettoyage DB</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin.css">
</head>
<body class="content">
    <form name="adminFrm" action="<%= request.getContextPath() %>/index.jsp" method="post">
    </form>
    <h1 onclick="window.document.adminFrm.submit()">Nettoyage DB</h1>
    <table border="0" cellpadding="10" cellspacing="0" width="100%">
        <tr>
            <td class="layout-manager">
                <table class="framing-table" width="100%" border="0" cellspacing="1" cellpadding="3">
                    <tr>
                        <td class="column-head-prefs">Db nettoyée avec succès</td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</body>
</html>
