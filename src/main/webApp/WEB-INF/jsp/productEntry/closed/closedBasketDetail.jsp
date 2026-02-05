<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>
<%@page import="java.util.Vector"%>
<%@page import="org.belex.customer.Customer"%>
<%@page import="org.belex.entry.Entry"%>
<%@page import="org.belex.product.Product"%>
<%@page import="org.belex.customer.CustomerEntry"%>
<%@page import="org.belex.arrival.Arrival"%>
<%
Arrival arrival = (Arrival)request.getAttribute("arrival");
Entry entry = arrival.getEntry();
Product product = entry.getProduct();

Product.Unit unit = product. new Unit();
for (int i=0; i < product.getUnits().size(); i++) { 
	if (product.getUnits().get(i).isSelected()) { 
		unit = product.getUnits().get(i);
		break;
	}
}
%>
<html>
<head>
	<%@include file="/WEB-INF/jsp/productEntry/jspf/headHeader.jspf"%>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/css/general.css" type="text/css">
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/general.js"></script>
</head>
	<body>

		<%@ include file="/WEB-INF/jsp/productEntry/jspf/bodyHeader.jspf" %>

		<div>
			<font id="pageTitle">
				<%= product.getDescription() %> ( <%= product.getProductCode() %> )<br>
			</font>
		</div>

		<hr>

		<center>
			<form name="backFrm" action="flowController.htm">
				<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
				<input class="button" type="submit" value=" << Retour " name="_eventId_back">
			</form>
		</center>

		<table width="98%">
			<% 
			Vector<Customer> customers = arrival.getEntry().getCustomers();
			
			for (int i = 0 ; i < customers.size() ; i++) { 
							
				Customer customer = customers.get(i);
				CustomerEntry ce = customer.getCustomerEntry();
				
				if (ce != null && ce.getFromBasketNumber() != 0) { %>
					<tr>
						<td colspan="2">
							<font style="color: red;font-weight: bold;"> Colis : <%= ce.getFromBasketNumber() %>
								<% if (ce.getFromBasketNumber() != ce.getToBasketNumber() && ce.getToBasketNumber() != 0) { %>
									à <%= ce.getToBasketNumber() %>
								<% } %>
							</font><br>
							<font style="color: green;font-weight: bold;"> Destination : <%= customer.getCustomerOrder().getFly().getAirportCode() %></font><br>
							<font style="color: green;font-weight: bold;"> Code client : <%=customer.getCustomerOrder().getOrderCode()%> - <%=customer.getCustomerOrder().getOrderNumber()%></font><br>
							<% if (customer.getName() != null && ! customer.getName().equals("")) { %>
								<font style="color: green;font-weight: bold;"> Nom du client : <%=customer.getName()%></font><br>
							<% }
							
							if (customer.getAddressFurniture() != null && ! customer.getAddressFurniture().equals("")) { %>
								<font style="color: green;font-weight: bold;"> Adresse de Livraison : <%= customer.getAddressFurniture().replaceAll("\r\n", " ") %></font><br>
							<% }

							String unitConditionnementRequested = customer.getCustomerEntry().getUnitConditionnement();
							int numberOfUnitRequested = customer.getCustomerEntry().getNumberOfUnit();
							String unitConditionnementInput = unit.getConditionnement();
							int numberOfUnitInput = unit.getNumber();
							
							//boolean green = false;

							if (  unitConditionnementRequested.equalsIgnoreCase(unitConditionnementInput) && 
								  numberOfUnitRequested == numberOfUnitInput
								) { %>
								<font style="color: green;font-weight: bold;">
							<% } else { %>
								<font style="color: red;font-weight: bold;">
							<% } %>
								Conditionnements : <%=numberOfUnitRequested%> X <%=unitConditionnementRequested%>
							</font><br>
							<font style="color: green;font-weight: bold;"> Nombre de conditionnements : <%=customer.getCustomerEntry().getNumberOfConditionnement()%></font><br>
							<% if (customer.getCustomerEntry().isNeedFridge()) { %>
								<font style="color: red;font-weight: bold;"> SURGELE</font><br>
							<% } %>
							<br>
						</td>
					</tr>
			<% } else { %>
				<tr>
					<td align="center" colspan="2">
						<font style="color: red;font-weight: bold;">ATTENTION AUCUN COLIS N'A ETE ATTRIBUE A CET ATRICLE.</font><br>
					</td>
				</tr>
				<% break;
			}
		} %>
	</table>
	<center>
		<form name="backFrm" action="flowController.htm">
			<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
			<input class="button" type="submit" value=" << Retour " name="_eventId_back">
		</form>
	</center>
</body>
</html>