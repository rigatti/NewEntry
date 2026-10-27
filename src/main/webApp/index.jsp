<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@include file="/WEB-INF/jspf/globalHeader.jspf"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
	<HEAD>
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin.css">
</head>
<body>
	<div align="center">
		<img alt="bVc" src="<%= request.getContextPath() %>/pic/bvc.gif" width="50px" height="40px">
	</div>

<body class="content" style="padding-top:37px">

	<center>
		<H1>Menu</h1>
		
		<table border="0" cellpadding="10" cellspacing="0" valign="top" width="30%" summary="Framing Table">
			<tr>
				<td class="layout-manager-notabs">
					<table class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3 width=100%>
						<tr>
							<td class="table-text-bold">
								<a class="ctextunderlined" href="<%= request.getContextPath() %>/flow/arrival">Arrivée de marchandise</a><br>
							</td>
						</tr>
						<tr>
							<td class="table-text-bold">
								<a class="ctextunderlined" href="<%= request.getContextPath() %>/flow/allocation">Attribution des marchandises reçues</a><br>
							</td>
						</tr>
						<tr>
							<td class="table-text-bold">
								<a class="ctextunderlined" href="flowController.htm?_flowId=productAdmin">Administration des produits</a><br>
							</td>
						</tr>
						<tr>
							<td class="table-text-bold">
								<a class="ctextunderlined" href="flowController.htm?_flowId=productTraceability">Traçabilité des produits</a><br>
							</td>
						</tr>
						<tr>
							<td class="table-text-bold">
								<a class="ctextunderlined" href="flowController.htm?_flowId=supplierEntryTraceability">Traçabilité des entrées fournisseurs</a><br>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>

		<br>
		
        <table border="0" cellpadding="10" cellspacing="0" valign="top" width="30%" summary="Framing Table">
            <tr>
                <td class="layout-manager-notabs">
                    <table class="framing-table" width=100% border=0 cellspacing=1 cellpadding=3 width=100%>
                        <tr>
                            <td class="table-text-bold">
                                <form name="frmCleanDb" method="get" action="flowController.htm" target="_BLANK">
                                    <input type="hidden" name="_flowId" value="productCleaning">
                                </form>
                                <script>
                                    <!--
                                        function sendFrm() {
                                            if (confirm('Etes-vous sûr?')) {
                                                window.document.frmCleanDb.submit();
                                            }
                                        }
                                    //-->
                                </script>
                                <a class="ctextunderlined" href="javascript:void(0)" onclick="sendFrm();">Nettoyage DB</a><br>
                            </td>
                        </tr>
                        <tr>
                            <td class="table-text-bold">
                                <form name="frmExportProduct" method="get" action="flowController.htm" target="_BLANK">
                                    <input type="hidden" name="_flowId" value="productsExport">
                                </form>
                                <script>
                                    <!--
                                        function sendFrmExportProduct() {
                                            alert("Request not allowed");
                                            //if (confirm('Etes-vous sûr?')) {
                                            //	window.document.frmExportProduct.submit();
                                            //}
                                        }
                                    //-->
                                </script>
                                <a class="ctextunderlined" href="javascript:void(0)" onclick="sendFrmExportProduct();">Export Product</a><br>
                            </td>
                        </tr>
                        <tr>
                            <td class="table-text-bold">
                                <form name="frmExportCustomer" method="get" action="flowController.htm" target="_BLANK">
                                    <input type="hidden" name="_flowId" value="customersExport">
                                </form>
                                <script>
                                    <!--
                                        function sendFrmExportCustomer() {
                                            alert("Request not allowed");
                                            //if (confirm('Etes-vous sûr?')) {
                                            //	window.document.frmExportCustomer.submit();
                                            //}
                                        }
                                    //-->
                                </script>
                                <a class="ctextunderlined" href="javascript:void(0)" onclick="sendFrmExportCustomer();">Export Customer</a><br>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
	</center>
</body>
</html>