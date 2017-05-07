<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>
<%@page import="java.util.*" %>
<%@page import="db.product.*" %>
<%@page import="org.belex.export.*" %>
<%
	Object obj = request.getAttribute("export");

	if (obj instanceof Export) {
		Export export = (Export) obj;
		List<CustomerToExport> customers = export.getCustomers();
		for(CustomerToExport customer : customers) {
			out.print(customer.getName() + ";");
			out.print(customer.getNameAdditionalPart() + ";");
			out.print(customer.getEmail() + ";");
			
			if (customer.getGroups().size() > 0) {
				out.print(StringUtils.removeEnd(customer.getGroupsCodes(),",") + ";");
				/*String groups = customer.getGroups().toString();
				groups = StringUtils.remove(groups, "[");
				groups = StringUtils.remove(groups, "]");
				groups = StringUtils.remove(groups, " ");
				out.print(groups + ";");
				*/
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
