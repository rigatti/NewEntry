<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="org.belex.traceability.Traceability"%>
<%@page import="org.belex.requestparams.RequestParams"%>
<%@page import="org.belex.entry.Entry"%>
<%@page import="org.belex.customer.Customer"%>
<%@page import="org.belex.product.Product"%>
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
    String fileName = "EntreesFournisseur" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
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
    document.addSubject("Rapport de traçabilité des fournisseurs");
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

    Paragraph title = new Paragraph("Rapport de traçabilité des fournisseurs", titleFont);
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
        table.setWidths(new float[]{40f, 60f});

        String supplierName = entry.getSupplier().getSupplierName();
        if (supplierName == null) {
            supplierName = entry.getSupplier().getSupplierCode();
        }
        PdfPCell header = new PdfPCell(new Phrase(supplierName, headerFont));
        header.setColspan(2);
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setBorder(Rectangle.BOTTOM);
        header.setPadding(6f);
        table.addCell(header);

        addRow(table, "Date d'entrée : ", Util.formatDate(entry.getArrivalDate(), "yyyyMMdd", "dd/MM/yyyy"), labelFont, valueFont);
        addRow(table, "Nombre d'articles : ", String.valueOf(entry.getSupplierEntryNumberOfProducts()), labelFont, valueFont);
        addRow(table, "Aspect des aliments : ", qualityLabel(entry.getSupplierEntryProductIntegrityPrimitive(), entry.getSupplierEntryCommentOnQuality()), labelFont, valueFont);
        addRow(table, "Intégrité des emballages : ", qualityLabel(entry.getSupplierEntryPackagingIntegrityPrimitive(), entry.getSupplierEntryCommentOnQuality()), labelFont, valueFont);
        addRow(table, "DLC ou DDM suffisante : ", qualityLabel(entry.getSupplierEntryDlcDdmValidityPrimitive(), entry.getSupplierEntryCommentOnQuality()), labelFont, valueFont);
        addRow(table, "Température à la réception : ", qualityLabel(entry.getSupplierEntryTemperatureValidityPrimitive(), entry.getSupplierEntryCommentOnQuality()), labelFont, valueFont);
        String comment = entry.getSupplierEntryCommentOnQuality();
        addRow(table, "Commentaire : ", comment != null ? "-" : comment, labelFont, valueFont);
        document.add(table);

        Vector<Entry> products = entry.getGroupedEntries();
        if (products.isEmpty()) {
            products = new Vector<Entry>();
            products.add(entry);
        }
        for (Entry productEntry : products) {
            if (productEntry.getProduct() == null) {
                continue;
            }
            Product product = productEntry.getProduct();
            PdfPTable productTable = new PdfPTable(2);
            productTable.setWidthPercentage(100f);
            productTable.setSpacingBefore(8f);
            productTable.setWidths(new float[]{40f, 60f});

            String description = product.getDescription() == null ? "" : product.getDescription();
            PdfPCell productHeader = new PdfPCell(new Phrase(description, headerFont));
            productHeader.setColspan(2);
            productHeader.setBorder(Rectangle.BOTTOM);
            productHeader.setPadding(4f);
            productTable.addCell(productHeader);

            addRow(productTable, "Code : ", product.getProductCode(), labelFont, valueFont);
            String ean = "";
            String conditioning = "";
            if (product.getUnits() != null && product.getUnits().size() == 1) {
                ean = product.getUnits().get(0).getEan();
                conditioning = Util.displayContitionnement(product.getUnits().get(0).getNumber(), product.getUnits().get(0).getConditionnement());
            }
            addRow(productTable, "EAN : ", ean, labelFont, valueFont);
            String quantity = String.valueOf(productEntry.getNumberOfProduct());
            if (!conditioning.isEmpty()) {
                quantity = quantity + " X " + conditioning;
            }
            addRow(productTable, "Quantité : ", quantity, labelFont, valueFont);
            if (productEntry.getSupplierDocumentType() > 0) {
                String documentLabel = "Référence fournisseur : ";
                if (productEntry.getSupplierDocumentType() == 1) {
                    documentLabel = "Facture : ";
                } else if (productEntry.getSupplierDocumentType() == 2) {
                    documentLabel = "Note d'envoi : ";
                }
                addRow(productTable, documentLabel, productEntry.getSupplierDocumentDescription(), labelFont, valueFont);
            }
            addRow(productTable, "Date de validité : ", product.getValidityDate(), labelFont, valueFont);
            addRow(productTable, "Numéro de lot : ", product.getLotNumber(), labelFont, valueFont);
            addRow(productTable, "Destinataire(s) : ", destinations(productEntry), labelFont, valueFont);
            document.add(productTable);
        }
    }

    document.close();
    response.getOutputStream().flush();
    out.clear();
    out = pageContext.pushBody();
%>
<%!
    private String qualityLabel(Integer value, String comment) {
        if (comment != null) {
            return "-";
        }
        if (value != null && value.intValue() == 0) {
            return "NOT OK";
        }
        return "OK";
    }

    private String destinations(Entry entry) {
        StringBuilder text = new StringBuilder();
        boolean first = true;
        for (Customer customer : entry.getCustomers()) {
            if (!first) {
                text.append("\n");
            }
            first = false;
            if (StringUtils.isNotEmpty(customer.getCode())) {
                text.append(customer.getName() == null ? "" : customer.getName());
                if (customer.getAddressFurniture() != null) {
                    text.append("\n").append(StringUtils.remove(customer.getAddressFurniture(), "\r\n" + customer.getName()));
                }
                if (customer.getCustomerOrder() != null && customer.getCustomerOrder().getFly() != null
                        && StringUtils.isNotEmpty(customer.getCustomerOrder().getFly().getAirportCode())) {
                    text.append("\n").append(customer.getCustomerOrder().getFly().getAirportCode())
                            .append("/").append(customer.getCustomerOrder().getFly().getFlyNumber())
                            .append("/").append(customer.getCustomerOrder().getFly().getLtaNumber());
                }
            } else if (customer.isStockCustomer()) {
                text.append("En stock");
            } else if (customer.isSupplierReturnsCustomer()) {
                text.append("Retour fournisseur");
            }
            if (customer.getCustomerEntry() != null) {
                text.append("\nQuantité : ").append(customer.getCustomerEntry().getNumberOfUnit());
            }
            if (StringUtils.isNotEmpty(customer.getAllocationDate())) {
                text.append("\nDate d'attribution : ").append(Util.formatDate(customer.getAllocationDate(), "yyyyMMdd", "dd/MM/yyyy"));
            }
        }
        return first ? "-" : text.toString();
    }

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
