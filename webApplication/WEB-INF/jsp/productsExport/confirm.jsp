<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>
<%@page import="java.util.*" %>
<%@page import="db.product.*" %>
<%@page import="org.belex.export.*" %>
<%
Object obj = request.getAttribute("export");

if (obj instanceof Export) {
	Export export = (Export) obj;
	if (StringUtils.isNotBlank(export.getErrorMessage())) {
		%>La mise à jour n'a pas abouti - <%=export.getErrorMessage()%><%
	} else {
		%>Mise à jour effectuée<%
	}
} else {
	%>Problème détecté lors de la mise à jour<%
}
%>