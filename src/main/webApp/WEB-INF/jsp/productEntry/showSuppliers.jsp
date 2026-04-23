<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="java.util.Map"%>
<%@page import="org.belex.arrival.Arrival"%>
<%@page import="java.util.Vector"%>
<%@page import="org.belex.supplier.Supplier"%>
<%
Arrival arrival = (Arrival)request.getAttribute("arrival");
Vector<Supplier> suppliers = arrival.getSuppliers();
%>

<!-- DEBUG: Display suppliers count -->
<div style="border: 1px solid red; padding: 10px; margin: 10px; background-color: #ffe6e6;">
	<strong>DEBUG - Suppliers found: <%= (suppliers != null ? suppliers.size() : 0) %></strong>
	<% if (suppliers != null && suppliers.size() > 0) { %>
		<ul>
		<% for (int i = 0; i < suppliers.size(); i++) { %>
			<li><%= suppliers.get(i).getSupplierCode() %> - <%= suppliers.get(i).getSupplierName() %></li>
		<% } %>
		</ul>
	<% } %>
</div>

<!-- Form to select supplier and continue -->
<form name="selectSupplierFrm" method="post" action="${flowExecutionUrl}">
	<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">

	<% if (suppliers != null && suppliers.size() > 0) { %>
		<h3>Select a Supplier:</h3>
		<select name="supplier.supplierCode" id="supplierCode">
			<% for (int i = 0; i < suppliers.size(); i++) { %>
				<option value="<%= suppliers.get(i).getSupplierCode() %>">
					<%= suppliers.get(i).getSupplierCode() %> - <%= suppliers.get(i).getSupplierName() %>
				</option>
			<% } %>
		</select>
		<br><br>
		<!-- Méthode A: Utiliser le nom du bouton pour l'ID d'événement -->
		<input type="submit" name="_eventId_searchEntry" value="Continue" />
	<% } else { %>
		<p>No suppliers available for this date.</p>
	<% } %>
</form>
