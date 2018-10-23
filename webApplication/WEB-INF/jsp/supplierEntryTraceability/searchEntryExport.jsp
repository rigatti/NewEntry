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
	"Rapport de tracabilité des fournisseurs",
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
		
		String supplierName = entry.getSupplier().getSupplierName();
		if (supplierName == null) {
			supplierName = entry.getSupplier().getSupplierCode();
		}
		RtfCell rtfcell = new RtfCell();
		paragraph = new Paragraph(
		supplierName,
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
		"Date d'entrée : ", labelFont);	
		rtfcell.add(paragraph);
		table.addCell(rtfcell);
		rtfcell = new RtfCell(Util.formatDate(entry.getArrivalDate(), "yyyyMMdd", "dd/MM/yyyy"));
		table.addCell(rtfcell);
	
		rtfcell = new RtfCell();
		paragraph = new Paragraph(
		"Nombre d'articles : ", labelFont);	
		rtfcell.add(paragraph);
		table.addCell(rtfcell);
	
		rtfcell = new RtfCell(String.valueOf(entry.getSupplierEntryNumberOfProducts()));
		table.addCell(rtfcell);
	
		rtfcell = new RtfCell();
		paragraph = new Paragraph(
		"Aspect des aliments : ", labelFont);	
		rtfcell.add(paragraph);
		table.addCell(rtfcell);
	
		String strTmp = entry.getSupplierEntryProductIntegrityPrimitive() == 0 ? "NOT OK" : "OK";
		if (entry.getSupplierEntryCommentOnQuality() != null) {
			strTmp = "-";
		}
		rtfcell = new RtfCell(strTmp);
		table.addCell(rtfcell);
	
		rtfcell = new RtfCell();
		paragraph = new Paragraph(
		"Intégrité des emballages : ", labelFont);	
		rtfcell.add(paragraph);
		table.addCell(rtfcell);
	
		strTmp = entry.getSupplierEntryPackagingIntegrityPrimitive() == 0 ? "NOT OK" : "OK";
		if (entry.getSupplierEntryCommentOnQuality() != null) {
			strTmp = "-";
		}
		rtfcell = new RtfCell(strTmp);
		table.addCell(rtfcell);
	
		rtfcell = new RtfCell();
		paragraph = new Paragraph(
		"DLC ou DDM suffisante : ", labelFont);	
		rtfcell.add(paragraph);
		table.addCell(rtfcell);
		
		strTmp = entry.getSupplierEntryDlcDdmValidityPrimitive() == 0 ? "NOT OK" : "OK";
		if (entry.getSupplierEntryCommentOnQuality() != null) {
			strTmp = "-";
		}
		rtfcell = new RtfCell(strTmp);
		table.addCell(rtfcell);
		
		rtfcell = new RtfCell();
		paragraph = new Paragraph(
		"Température à la réception : ", labelFont);	
		rtfcell.add(paragraph);
		table.addCell(rtfcell);
		
		strTmp = entry.getSupplierEntryTemperatureValidityPrimitive() == 0 ? "NOT OK" : "OK";
		if (entry.getSupplierEntryCommentOnQuality() != null) {
			strTmp = "-";
		}
		rtfcell = new RtfCell(strTmp);
		table.addCell(rtfcell);
	
		rtfcell = new RtfCell();
		paragraph = new Paragraph(
		"Commentaire : ", labelFont);	
		rtfcell.add(paragraph);
		table.addCell(rtfcell);
		
		strTmp = entry.getSupplierEntryCommentOnQuality();
		if (entry.getSupplierEntryCommentOnQuality() != null) {
			strTmp = "-";
		}
		rtfcell = new RtfCell(strTmp);
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