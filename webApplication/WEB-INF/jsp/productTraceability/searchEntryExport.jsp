<%@page import="java.io.FileOutputStream"%>
<%@page import="java.util.Vector"%>
<%@page import="org.belex.traceability.Traceability"%>
<%@page import="org.belex.requestparams.RequestParams"%>
<%@page import="org.belex.customer.Customer"%>
<%@page import="org.belex.entry.Entry"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmss");
	String fileName = "report" + sdf.format(new Date())+ ".rtf";
%>

<%@ include file="/WEB-INF/jspf/rtfGeneratorHeader.jspf" %>

<%
	Traceability traceability = (Traceability) request.getAttribute("traceability");
	RequestParams requestParams = (RequestParams) request.getAttribute("requestParams");
	String traceExportIndexList = requestParams.getTraceExportIndexList();

	Vector<Entry> entries = traceability.getEntries();

	paragraph = new Paragraph(
	"Rapport de tracabilité",
	FontFactory.getFont(FontFactory.TIMES_ITALIC, 24,
	Font.BOLDITALIC, new Color(0, 0, 0)));
	paragraph.setSpacingBefore(10.0f);
	paragraph.setSpacingAfter(0.0f);
	paragraph.setAlignment(Paragraph.ALIGN_CENTER);
	document.add(paragraph);

for (int i=0; i < entries.size() ; i++ ) {
	if (traceExportIndexList.equals("") || traceExportIndexList.indexOf(";" + i + ";" ) != -1) {
		Entry entry = entries.get(i);
	
		Table table = new Table(2);
		table.setBorderWidth(20.0f);
		table.setPadding(5.0f);
		table.setSpacing(5.0f);
		
		RtfCell rtfcell = new RtfCell();
		paragraph = new Paragraph(
		entry.getProduct().getDescription(),
		FontFactory.getFont(FontFactory.TIMES_ITALIC, 12,
		Font.BOLD, new Color(0, 0, 0)));
		paragraph.setSpacingAfter(3.0f);
		
		rtfcell.add(paragraph);
		
		rtfcell.setBorders(new RtfBorderGroup(
		Rectangle.BOTTOM, RtfBorder.BORDER_SINGLE,
		5.0f, new Color(0, 0, 0)));
		rtfcell.setColspan(2);
		rtfcell.setHeader(true);
		rtfcell.setVerticalAlignment(RtfCell.ALIGN_CENTER);
		
		table.addCell(rtfcell);
		table.endHeaders();
		
		rtfcell = new RtfCell();
		paragraph = new Paragraph(
		"Code EAN : ", labelFont);	
		rtfcell.add(paragraph);
		table.addCell(rtfcell);
		if ( entry.getProduct().getUnits() != null && entry.getProduct().getUnits().size() == 1 ) {
			rtfcell = new RtfCell(entry.getProduct().getUnits().get(0).getEan());
		} else {
			rtfcell = new RtfCell("");
		}
		table.addCell(rtfcell);
	
		rtfcell = new RtfCell();
		paragraph = new Paragraph(
		"Référence interne : ", labelFont);	
		rtfcell.add(paragraph);
		table.addCell(rtfcell);
	
		rtfcell = new RtfCell(entry.getProduct().getProductCode());
		table.addCell(rtfcell);
	
		rtfcell = new RtfCell();
		paragraph = new Paragraph(
		"Numéro de lot : ", labelFont);	
		rtfcell.add(paragraph);
		table.addCell(rtfcell);
	
		rtfcell = new RtfCell(entry.getProduct().getLotNumber());
		table.addCell(rtfcell);
	
		rtfcell = new RtfCell();
		paragraph = new Paragraph(
		"Date de validité : ", labelFont);	
		rtfcell.add(paragraph);
		table.addCell(rtfcell);
	
		rtfcell = new RtfCell(entry.getProduct().getValidityDate());
		table.addCell(rtfcell);
	
		rtfcell = new RtfCell();
		paragraph = new Paragraph(
		"Conditionnement : ", labelFont);	
		rtfcell.add(paragraph);
		table.addCell(rtfcell);

		if ( entry.getProduct().getUnits() != null && entry.getProduct().getUnits().size() == 1 ) {
			rtfcell = new RtfCell(entry.getNumberOfProduct() + " X " + entry.getProduct().getUnits().get(0).getNumber() + " X "+ entry.getProduct().getUnits().get(0).getConditionnement());
		} else {
			rtfcell = new RtfCell(String.valueOf(entry.getNumberOfProduct()));
		}
		table.addCell(rtfcell);
		
		rtfcell = new RtfCell();
		paragraph = new Paragraph(
		"Date d'entrée : ", labelFont);	
		rtfcell.add(paragraph);
		table.addCell(rtfcell);
		
		rtfcell = new RtfCell(Util.formatDate(entry.getArrivalDate(), "yyyyMMdd", "dd/MM/yyyy"));
		table.addCell(rtfcell);
	
		rtfcell = new RtfCell();
		paragraph = new Paragraph(
		"Fournisseur : ", labelFont);	
		rtfcell.add(paragraph);
		table.addCell(rtfcell);
		
		rtfcell = new RtfCell(entry.getSupplier().getSupplierName() + "(" + entry.getSupplier().getSupplierCode() + ")");
		table.addCell(rtfcell);

		if (entry.getSupplierDocumentType() > 0) {
			rtfcell = new RtfCell();
			if (entry.getSupplierDocumentType() == 1) {
				paragraph = new Paragraph("Facture : ", labelFont);	
			} else if (entry.getSupplierDocumentType() == 2) {
				paragraph = new Paragraph("Note d'envoi : ", labelFont);	
			} else {
				paragraph = new Paragraph("Référence fournisseur : ", labelFont);	
			}
			rtfcell.add(paragraph);
			table.addCell(rtfcell);

			rtfcell = new RtfCell(entry.getSupplierDocumentDescription());
			table.addCell(rtfcell);
		}

		rtfcell = new RtfCell();
		paragraph = new Paragraph(
		"Destinataire(s) : ", labelFont);	
		rtfcell.add(paragraph);
		table.addCell(rtfcell);
	
		String strCustomers = "";
		boolean first = true;
		for (Customer customer : entry.getCustomers()) { 
			if ( ! first) {
				strCustomers += "\n\r\n\r";
			}
			first = false;

			if (customer.isStockCustomer()) {
				strCustomers += "En stock";
			} else if (customer.isSupplierReturnsCustomer()) {
				strCustomers += "Retour fournisseur";
			} else {
				if (customer.getName() != null) {
					strCustomers += customer.getName() + "\n\r" + StringUtils.remove(customer.getAddressFurniture(), "\r\n" + customer.getName());
					if (StringUtils.isNotEmpty(customer.getCustomerOrder().getFly().getAirportCode())) {
						strCustomers += "\n\r" + customer.getCustomerOrder().getFly().getAirportCode() + "/" + customer.getCustomerOrder().getFly().getFlyNumber() + "/" + customer.getCustomerOrder().getFly().getLtaNumber();
					}
				} else { 
					strCustomers += "-";
				}
			}
			if (customer.getCustomerEntry() != null) {
				strCustomers += "\n\r" + "Quantité : " + customer.getCustomerEntry().getNumberOfUnit();
			}
			if (StringUtils.isNotEmpty(customer.getAllocationDate())) {
				strCustomers += "\n\r" + "Date d'attribution : " + Util.formatDate(customer.getAllocationDate(), "yyyyMMdd", "dd/MM/yyyy");
			}
		}
		if (entry.getCustomers() == null || entry.getCustomers().size() == 0) {
			strCustomers = "-";
		}

		rtfcell = new RtfCell(strCustomers);
		table.addCell(rtfcell);
	
		document.add(table);
	} // end if index
}
document.close();
%>
<%@page import="org.belex.util.Util"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<html>
	<head>
		<script type="text/javascript">
			<!--
				function init(){
					window.document.viewFile.submit();
					window.history.back();
				}
			//-->
		</script>
	</head>
	<body onload="init()">
		<form name="viewFile" action="<%= request.getContextPath() %>/jsptmp/<%= fileName %>" target="_new">
		</form>
	</body>
</html>
<%
	/*
	paragraph.setSpacingBefore(20.0f);
	paragraph.setSpacingAfter(10.0f);

	paragraph = new Paragraph();
	paragraph.setSpacingBefore(10.0f);
	paragraph.setSpacingAfter(20.0f);
	Chunk chunk = new Chunk("Abonnez vous!",
			FontFactory.getFont(FontFactory.TIMES_ITALIC, 24,
					Font.BOLDITALIC, new Color(255, 0, 0)));

	paragraph.add(chunk);
	chunk = new Chunk(";-)",
			FontFactory.getFont(FontFactory.TIMES_ITALIC, 24,
					Font.BOLDITALIC, new Color(0, 0, 255)));
	paragraph.add(chunk);
	chunk = new Chunk("",
			FontFactory.getFont(FontFactory.TIMES, 24,
					Font.NORMAL, new Color(0, 0, 0)));
	paragraph.add(chunk);
	document.add(paragraph);
	*/
	
%>