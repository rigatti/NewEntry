<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="org.belex.allocation.Allocation"%>
<%@page import="org.belex.allocation.AllocationEntry"%>
<%@page import="org.belex.product.ProductTraceable"%>
<%@page import="org.belex.requestparams.RequestParams"%>
<%@page import="org.belex.customer.CustomerOrder"%>
<%@page import="org.belex.util.Util"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
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
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
    String fileName = "Colisage" + sdf.format(new Date()) + ".pdf";

    Allocation allocation = (Allocation) request.getAttribute("allocation");
    RequestParams requestParams = (RequestParams) request.getAttribute("requestParams");

    boolean reportAllocated = "on".equals(requestParams.getAllocReportStandard());
    boolean reportAssigned = "on".equals(requestParams.getAllocReportSubstitution());
    boolean reportStock = "on".equals(requestParams.getAllocReportStock());
    boolean reportSupplier = "on".equals(requestParams.getAllocReportSupplier());
    boolean reportDeletedOrder = "on".equals(requestParams.getAllocReportDeletedOrder());
    boolean reportRenewedOrder = "on".equals(requestParams.getAllocReportRenewedOrder());

    StringBuffer title = new StringBuffer();
    boolean titleFilled = false;
    if (reportAllocated) {
        title.append(" ATTRIBUTION STANDARD ");
        titleFilled = true;
    }
    if (reportAssigned) {
        if (titleFilled) title.append("/");
        title.append(" SUBSTITUTION ");
        titleFilled = true;
    }
    if (reportStock) {
        if (titleFilled) title.append("/");
        title.append(" ENTREE EN STOCK ");
        titleFilled = true;
    }
    if (reportSupplier) {
        if (titleFilled) title.append("/");
        title.append(" RETOUR FOURNISSEUR ");
        titleFilled = true;
    }
    if (reportDeletedOrder) {
        if (titleFilled) title.append("/");
        title.append(" SUPPRESSION ");
        titleFilled = true;
    }
    if (reportRenewedOrder) {
        if (titleFilled) title.append("/");
        title.append(" NOUVELLE COMMANDE ");
    }

    Font titleFont = FontFactory.getFont(FontFactory.TIMES_ITALIC, 18, Font.BOLDITALIC, new Color(0, 0, 0));
    Font headerFont = FontFactory.getFont(FontFactory.TIMES_ITALIC, 12, Font.BOLD, new Color(0, 0, 0));
    Font labelFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 10, Font.BOLD, new Color(0, 0, 0));
    Font valueFont = FontFactory.getFont(FontFactory.TIMES, 10, Font.NORMAL, new Color(0, 0, 0));
    Font addressFont = FontFactory.getFont(FontFactory.TIMES, 10, Font.NORMAL, new Color(0, 0, 0));
    Font emptyFont = FontFactory.getFont(FontFactory.TIMES_ITALIC, 14, Font.ITALIC, new Color(0, 0, 0));

    response.reset();
    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

    Document document = new Document(PageSize.A4, 50f, 50f, 50f, 50f);
    PdfWriter.getInstance(document, response.getOutputStream());
    document.addAuthor("");
    document.addCreator("Belex");
    document.addSubject("Rapport de colisage");
    document.addTitle(title.toString());
    document.open();

    String logoPath = getServletContext().getRealPath("/pic/bvcSmall.jpg");
    if (logoPath != null && new File(logoPath).isFile()) {
        Image image = Image.getInstance(logoPath);
        image.setAlignment(Image.ALIGN_LEFT);
        image.scaleToFit(120f, 60f);
        document.add(image);
    }

    document.add(new Paragraph("Chaussée de Haecht 1313\n1130 Belgique", addressFont));
    Paragraph dateParagraph = new Paragraph(new SimpleDateFormat("dd/MM/yyyy").format(new Date()), addressFont);
    dateParagraph.setAlignment(Element.ALIGN_RIGHT);
    document.add(dateParagraph);

    Paragraph titleParagraph = new Paragraph(title.toString(), titleFont);
    titleParagraph.setSpacingBefore(10f);
    titleParagraph.setSpacingAfter(8f);
    titleParagraph.setAlignment(Element.ALIGN_CENTER);
    document.add(titleParagraph);

    boolean showEmptyReportMessage = true;

    if (reportAllocated || reportAssigned || reportStock || reportSupplier) {
        ArrayList<ProductTraceable> products = new ArrayList<ProductTraceable>();
        for (AllocationEntry entry : allocation.getEntries()) {
            if (entry.isSelected()) {
                products.addAll(entry.getProducts());
            }
        }

        Collections.sort(products, new Comparator<ProductTraceable>() {
            public int compare(ProductTraceable pt0, ProductTraceable pt1) {
                int supplierCodeComp = pt0.getSupplierCode().compareToIgnoreCase(pt1.getSupplierCode());
                if (supplierCodeComp == 0) {
                    return pt0.getDescription().compareToIgnoreCase(pt1.getDescription());
                }
                return supplierCodeComp;
            }
        });

        for (int i = 0; i + 1 < products.size(); i++) {
            ProductTraceable currentProduct = products.get(i);
            ProductTraceable nextProduct = products.get(i + 1);
            if (currentProduct.getProductCode().equals(nextProduct.getProductCode())) {
                for (ProductTraceable.Destination nextProductDest : nextProduct.getDestinations()) {
                    boolean similarDestinationDetected = false;
                    for (int j = 0; j < currentProduct.getDestinations().size(); j++) {
                        if (currentProduct.getDestinations().get(j).equals(nextProductDest)) {
                            similarDestinationDetected = true;
                        }
                    }
                    if (!similarDestinationDetected) {
                        currentProduct.addDestination(nextProductDest);
                    }
                }
                products.remove(i + 1);
                i = i - 1;
            }
        }

        for (ProductTraceable product : products) {
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100f);
            table.setSpacingBefore(10f);
            table.setWidths(new float[]{40f, 60f});

            boolean first = true;
            for (ProductTraceable.Destination dest : product.getDestinations()) {
                boolean isRequestedDestination = false;
                if (reportAllocated && !dest.isStockItem() && !dest.isSubstitutionItem() && !dest.isSupplierReturnItem() && !dest.getCustomerCode().equals("")) {
                    isRequestedDestination = true;
                }
                if (reportAssigned && dest.isSubstitutionItem()) {
                    isRequestedDestination = true;
                }
                if (reportStock && dest.isStockItem()) {
                    isRequestedDestination = true;
                }
                if (reportSupplier && dest.isSupplierReturnItem()) {
                    isRequestedDestination = true;
                }
                if (!isRequestedDestination) {
                    continue;
                }

                if (first) {
                    PdfPCell header = new PdfPCell(new Phrase(product.getSupplierName() + " - " + product.getDescription(), headerFont));
                    header.setColspan(2);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setBorder(Rectangle.BOTTOM);
                    header.setBorderWidthBottom(1f);
                    header.setPadding(6f);
                    header.setPaddingBottom(4f);
                    table.addCell(header);
                } else {
                    PdfPCell spacer = new PdfPCell(new Phrase(" "));
                    spacer.setColspan(2);
                    spacer.setBorder(Rectangle.NO_BORDER);
                    spacer.setFixedHeight(8f);
                    table.addCell(spacer);
                }
                first = false;

                if (dest.isStockItem()) {
                    addLabelValue(table, "Code Produit : ", String.valueOf(product.getProductCode()), labelFont, valueFont);
                }

                addLabelValue(table, "Nombre et conditionnement : ", dest.getNumberOfUnit() + " X " + product.getUnitConditionnement(), labelFont, valueFont);

                if (dest.isStockItem()) {
                    if (reportAllocated || reportAssigned || reportSupplier || reportDeletedOrder || reportRenewedOrder) {
                        addSpan(table, "POUR STOCKAGE", valueFont);
                    }
                } else if (dest.isSupplierReturnItem()) {
                    addSpan(table, "RETOUR FOURNISSEUR (" + product.getSupplierName() + ")", valueFont);
                } else {
                    String packaging = String.valueOf(dest.getPackagingFromBasket());
                    if (dest.getPackagingFromBasket() == 0) {
                        packaging = "-";
                    } else if (dest.getPackagingToBasket() != 0 && dest.getPackagingFromBasket() != dest.getPackagingToBasket()) {
                        packaging = "De " + packaging + " à " + dest.getPackagingToBasket();
                    }
                    addLabelValue(table, "Colis : ", packaging, labelFont, labelFont);
                    addLabelValue(table, "Destinataire : ", dest.getCustomerName(), labelFont, valueFont);
                }
            }

            showEmptyReportMessage = showEmptyReportMessage ? first : false;
            if (!first) {
                document.add(table);
            }
        }
    }

    if (reportDeletedOrder || reportRenewedOrder) {
        List<CustomerOrder> cos = allocation.getCustomerOrdersToAllocates();
        Collections.sort(cos, new Comparator<CustomerOrder>() {
            public int compare(CustomerOrder co0, CustomerOrder co1) {
                if ((co0.isDeletedOrder() && co1.isDeletedOrder()) || (co0.isRenewedOrder() && co1.isRenewedOrder())) {
                    return co0.getProductDescription().compareToIgnoreCase(co1.getProductDescription());
                }
                return -1;
            }
        });

        for (CustomerOrder co : cos) {
            if (!((co.isDeletedOrder() && reportDeletedOrder) || (co.isRenewedOrder() && reportRenewedOrder))) {
                continue;
            }

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100f);
            table.setSpacingBefore(10f);
            table.setWidths(new float[]{45f, 55f});

            PdfPCell header = new PdfPCell(new Phrase(allocation.getSupplierName(co.getSupplierCode()) + " - " + co.getProductDescription(), headerFont));
            header.setColspan(2);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setBorder(Rectangle.BOTTOM);
            header.setPadding(6f);
            table.addCell(header);

            addLabelValue(table, "Nombre et conditionnement commandé: ", co.getNumberOfProduct() + " X " + Util.displayContitionnement(co.getNumberOfUnit(), co.getUnit()), labelFont, valueFont);

            if (co.getNumberGets() > 0) {
                addLabelValue(table, "Nombre et conditionnement reçu: ", Math.round(co.getNumberGets()) + " X " + co.getUnit(), labelFont, valueFont);
            }
            if (co.isDeletedOrder()) {
                addSpan(table, "SUPPRESSION DE LA COMMANDE CLIENT (" + co.getCustomer().getName() + ")", valueFont);
            } else if (co.isRenewedOrder()) {
                addSpan(table, "COMMANDE FOURNISSEUR A REFAIRE (" + co.getCustomer().getName() + ")", valueFont);
            }

            document.add(table);
            showEmptyReportMessage = false;
        }
    }

    if (showEmptyReportMessage) {
        Paragraph empty = new Paragraph(
                "Aucun colis n'est disponible pour cette demande. Soit il n'y a aucun article de substitution soit cette demande est issue d'une arrivée encodée avec la version antérieure de ouistiti.",
                emptyFont);
        empty.setSpacingBefore(10f);
        empty.setAlignment(Element.ALIGN_CENTER);
        document.add(empty);
    }

    document.close();
    response.getOutputStream().flush();
    out.clear();
    out = pageContext.pushBody();
%>
<%!
    private void addLabelValue(com.lowagie.text.pdf.PdfPTable table, String label, String value, com.lowagie.text.Font labelFont, com.lowagie.text.Font valueFont) {
        com.lowagie.text.pdf.PdfPCell labelCell = new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Phrase(label, labelFont));
        labelCell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        labelCell.setPadding(4f);
        table.addCell(labelCell);

        com.lowagie.text.pdf.PdfPCell valueCell = new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Phrase(value == null ? "" : value, valueFont));
        valueCell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        valueCell.setPadding(4f);
        table.addCell(valueCell);
    }

    private void addSpan(com.lowagie.text.pdf.PdfPTable table, String text, com.lowagie.text.Font font) {
        com.lowagie.text.pdf.PdfPCell cell = new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Phrase(text, font));
        cell.setColspan(2);
        cell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        cell.setPadding(4f);
        table.addCell(cell);
    }
%>
