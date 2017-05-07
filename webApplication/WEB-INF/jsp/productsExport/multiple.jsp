<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>
<%@page import="java.util.*" %>
<%@page import="db.product.*" %>
<%@page import="org.belex.export.*" %>

<%
	Object obj = request.getAttribute("export");

	if (obj instanceof Export) {
		Export export = (Export) obj;
		List<ProductToExport> products = export.getProducts();
		for(ProductToExport product : products) {
			out.print(product.getProductCode() + ";");
			out.print(product.getDescription_fr() + ";");
			out.print(product.getFamilyCode() + ";");
			//out.print(product.getUnitConditioning() + ";");
			//out.print(product.getUnitNumber() + ";");
			//out.print(product.getUnitPrice() + ";");
			if (StringUtils.equals(product.getEditionTypeLabel(), "PRO")) {
				out.print("ignoreMe;");
			} else {
				out.print(product.getMarginForStandardCustomer() + ";");
			}
			out.print(product.getTotalConditioning() + ";");
			out.print(product.getTotalPrice() + ";");
			out.print(product.getEditionTypeLabel() + ";");
			out.print(product.getEditionTypeId() + ";");
			// fake quantity
			out.print(1000);
			out.print("<br>");
		}
	}
%>
