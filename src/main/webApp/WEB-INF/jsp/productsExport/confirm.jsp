<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>
<%@page import="java.util.*" %>
<%@page import="db.product.*" %>
<%@page import="org.belex.export.*" %>
<%
Object obj = request.getAttribute("export");

if (obj instanceof Export) {
	Export export = (Export) obj;
	if (StringUtils.isNotBlank(export.getErrorMessage())) {
		%>La mise � jour n'a pas abouti - <%=export.getErrorMessage()%><%
	} else {
		%>Mise � jour effectu�e<%
	}
} else {
	%>Probl�me d�tect� lors de la mise � jour<%
}
%>