<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="org.belex.traceability.Traceability"%>
<%@page import="org.belex.requestparams.RequestParams"%>
<%@page import="org.belex.customer.Customer"%>
<%@page import="org.belex.entry.Entry"%>
<%@page import="org.belex.util.Util"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.io.File"%>
<%@page import="java.awt.Color"%>
<%@page import="com.lowagie.text.Document"%>
<%@page import="com.lowagie.text.Element"%>
<%@page import="com.lowagie.text.Font"%>
<%@page import="com.lowagie.text.FontFactory"%>
<%@page import="com.lowagie.text.Image"%>
<%@page import="com.lowagie.text.PageSize"%>
<%@page import="com.lowagie.text.Paragraph"%>
<%@page import="com.lowagie.text.Phrase"%>
<%@page import="com.lowagie.text.Rectangle"%>
<%@page import="com.lowagie.text.pdf.PdfPCell"%>
<%@page import="com.lowagie.text.pdf.PdfPTable"%>
<%@page import="com.lowagie.text.pdf.PdfWriter"%>
<%
    String fileName = "Tracabilite" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
    Traceability traceability = (Traceability) request.getAttribute("traceability");
    RequestParams requestParams = (RequestParams) request.getAttribute("requestParams");
    String traceExportIndexList = requestParams.getTraceExportIndexList() == null ? "" : requestParams.getTraceExportIndexList();
    Vector<Entry> entries = traceability.getEntries();

    Font titleFont = FontFactory.getFont(FontFactory.TIMES_ITALIC, 18, Font.BOLDITALIC, new Color(0, 0, 0));
    Font headerFont = FontFactory.getFont(FontFactory.TIMES_ITALIC, 12, Font.BOLD, new Color(0, 0, 0));
    Font labelFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 10, Font.BOLD, new Color(0, 0, 0));
    Font valueFont = FontFactory.getFont(FontFactory.TIMES, 10, Font.NORMAL, new Color(0, 0, 0));

    response.reset();
    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

    Document document = new Document(PageSize.A4, 50f, 50f, 50f, 50f);
    PdfWriter.getInstance(document, response.getOutputStream());
    document.addCreator("Belex");
    document.addSubject("Rapport de traçabilité des produits");
    document.open();

    String logoPath = getServletContext().getRealPath("/pic/bvcSmall.jpg");
    if (logoPath != null && new File(logoPath).isFile()) {
        Image image = Image.getInstance(logoPath);
        image.scaleToFit(120f, 60f);
        document.add(image);
    }
    document.add(new Paragraph("Chaussée de Haecht 1313\n1130 Belgique", valueFont));
    Paragraph dateParagraph = new Paragraph(new SimpleDateFormat("dd/MM/yyyy").format(new Date()), valueFont);
    dateParagraph.setAlignment(Element.ALIGN_RIGHT);
    document.add(dateParagraph);

    Paragraph title = new Paragraph("Rapport de traçabilité des produits", titleFont);
    title.setSpacingBefore(10f);
    title.setAlignment(Element.ALIGN_CENTER);
    document.add(title);

    for (int i = 0; i < entries.size(); i++) {
        if (!(traceExportIndexList.equals("") || traceExportIndexList.indexOf(";" + i + ";") != -1)) {
            continue;
        }
        Entry entry = entries.get(i);
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(12f);
        table.setWidths(new float[]{35f, 65f});

        String productDescription = entry.getProduct().getDescription() == null ? "-" : entry.getProduct().getDescription();
        PdfPCell header = new PdfPCell(new Phrase(productDescription, headerFont));
        header.setColspan(2);
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setBorder(Rectangle.BOTTOM);
        header.setPadding(6f);
        table.addCell(header);

        String ean = "";
        String conditioning = "";
        if (entry.getNumberOfProduct() > 0) {
            conditioning = String.valueOf(entry.getNumberOfProduct());
        }
        if (entry.getProduct().getUnits() != null && entry.getProduct().getUnits().size() == 1) {
            ean = entry.getProduct().getUnits().get(0).getEan();
            if (entry.getNumberOfProduct() > 0) {
                conditioning = entry.getNumberOfProduct() + " X " + entry.getProduct().getUnits().get(0).getNumber() + " X " + entry.getProduct().getUnits().get(0).getConditionnement();
            }
        }
        addRow(table, "Code EAN : ", ean, labelFont, valueFont);
        addRow(table, "Référence interne : ", entry.getProduct().getProductCode(), labelFont, valueFont);
        addRow(table, "Numéro de lot : ", entry.getProduct().getLotNumber(), labelFont, valueFont);
        addRow(table, "Date de validité : ", entry.getProduct().getValidityDate(), labelFont, valueFont);
        addRow(table, "Conditionnement : ", conditioning, labelFont, valueFont);
        addRow(table, "Date d'entrée : ", Util.formatDate(entry.getArrivalDate(), "yyyyMMdd", "dd/MM/yyyy"), labelFont, valueFont);
        addRow(table, "Fournisseur : ", entry.getSupplier().getSupplierName() + " (" + entry.getSupplier().getSupplierCode() + ")", labelFont, valueFont);

        if (entry.getSupplierDocumentType() > 0) {
            String docLabel = "Référence fournisseur : ";
            if (entry.getSupplierDocumentType() == 1) {
                docLabel = "Facture : ";
            } else if (entry.getSupplierDocumentType() == 2) {
                docLabel = "Note d'envoi : ";
            }
            addRow(table, docLabel, entry.getSupplierDocumentDescription(), labelFont, valueFont);
        }

        StringBuilder customers = new StringBuilder();
        if (entry.getCustomers() == null || entry.getCustomers().size() == 0) {
            customers.append("-");
        } else {
            boolean first = true;
            for (Customer customer : entry.getCustomers()) {
                if (!first) {
                    customers.append("\n\n");
                }
                first = false;
                if (customer.isStockCustomer()) {
                    customers.append("En stock");
                } else if (customer.isSupplierReturnsCustomer()) {
                    customers.append("Retour fournisseur");
                } else if (customer.getName() != null) {
                    customers.append(customer.getName());
                    if (customer.getAddressFurniture() != null) {
                        customers.append("\n").append(StringUtils.remove(customer.getAddressFurniture(), "\r\n" + customer.getName()));
                    }
                    if (customer.getCustomerOrder() != null && customer.getCustomerOrder().getFly() != null
                            && StringUtils.isNotEmpty(customer.getCustomerOrder().getFly().getAirportCode())) {
                        customers.append("\n").append(customer.getCustomerOrder().getFly().getAirportCode())
                                .append("/").append(customer.getCustomerOrder().getFly().getFlyNumber())
                                .append("/").append(customer.getCustomerOrder().getFly().getLtaNumber());
                    }
                } else {
                    customers.append("-");
                }
                if (customer.getCustomerEntry() != null) {
                    customers.append("\nQuantité : ").append(customer.getCustomerEntry().getNumberOfUnit());
                }
                if (StringUtils.isNotEmpty(customer.getAllocationDate())) {
                    customers.append("\nDate d'attribution : ").append(Util.formatDate(customer.getAllocationDate(), "yyyyMMdd", "dd/MM/yyyy"));
                }
            }
        }
        addRow(table, "Destinataire(s) : ", customers.toString(), labelFont, valueFont);
        document.add(table);
    }

    document.close();
    response.getOutputStream().flush();
    out.clear();
    out = pageContext.pushBody();
%>
<%!
    private void addRow(com.lowagie.text.pdf.PdfPTable table, String label, String value, com.lowagie.text.Font labelFont, com.lowagie.text.Font valueFont) {
        com.lowagie.text.pdf.PdfPCell labelCell = new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Phrase(label, labelFont));
        labelCell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        labelCell.setPadding(4f);
        table.addCell(labelCell);
        com.lowagie.text.pdf.PdfPCell valueCell = new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Phrase(value == null ? "" : value, valueFont));
        valueCell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        valueCell.setPadding(4f);
        table.addCell(valueCell);
    }
%>
