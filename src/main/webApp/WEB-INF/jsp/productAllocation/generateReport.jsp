<%@page import="org.belex.allocation.Allocation"%>
<%@page import="org.belex.allocation.AllocationEntry"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.belex.product.ProductTraceable"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.text.SimpleDateFormat"%>
<% 
SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmss");
String fileName = "Colisage" + sdf.format(new Date())+ ".rtf";
%>
<%@ include file="/WEB-INF/jspf/rtfGeneratorHeader.jspf" %>
<%
	Allocation allocation = (Allocation) request.getAttribute("allocation");
	RequestParams requestParams = (RequestParams) request.getAttribute("requestParams");

	List<AllocationEntry> entries = allocation.getEntries();

	ArrayList<ProductTraceable> products = new ArrayList<ProductTraceable>();

	boolean reportAllocated = requestParams.getAllocReportStandard().equals("on");
	boolean reportAssigned = requestParams.getAllocReportSubstitution().equals("on");
	boolean reportStock = requestParams.getAllocReportStock().equals("on");
	boolean reportSupplier = requestParams.getAllocReportSupplier().equals("on");
	boolean reportDeletedOrder = requestParams.getAllocReportDeletedOrder().equals("on");
	boolean reportRenewedOrder = requestParams.getAllocReportRenewedOrder().equals("on");

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
		titleFilled = true;
	}

	// TITLE
	paragraph = new Paragraph( title.toString(),
		FontFactory.getFont(FontFactory.TIMES_ITALIC, 18, Font.BOLDITALIC, new Color(0, 0, 0)));
		paragraph.setSpacingBefore(10.0f);
		paragraph.setSpacingAfter(0.0f);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
	document.add(paragraph);

	boolean showEmptyReportMessage = true;

	if ( reportAllocated || reportAssigned || reportStock || reportSupplier ) {
	
		for ( AllocationEntry entry : allocation.getEntries() ) {
			if (entry.isSelected()) {
				products.addAll(entry.getProducts());
			}
		}
	
		// sort products array by product description
		Collections.sort(products, new Comparator<ProductTraceable>(){
			public int compare(ProductTraceable pt0, ProductTraceable pt1) {
				int result = 0;
				
				int supplierCodeComp = pt0.getSupplierCode().compareToIgnoreCase(pt1.getSupplierCode());
				if ( supplierCodeComp == 0 ) {
					result = pt0.getDescription().compareToIgnoreCase(pt1.getDescription());
				} else {
					result = supplierCodeComp;
				}
	
				return result;
			}
		});
		
		// remove duplicate products
		for ( int i=0 ; i+1 < products.size() ; i++ ) {

			ProductTraceable currentProduct = products.get(i);
			ProductTraceable nextProduct = products.get(i+1);
			if ( currentProduct.getProductCode().equals(nextProduct.getProductCode()) ) {
				// check destinations
				//if ( currentProduct.getDestinations().size() < nextProduct.getDestinations().size() ) {
					for (ProductTraceable.Destination nextProductDest : nextProduct.getDestinations()) {
						boolean similarDestinationDetected = false;
						for (int j = 0 ; j < currentProduct.getDestinations().size() ; j++) {
							if ( currentProduct.getDestinations().get(j).equals(nextProductDest)) {
								similarDestinationDetected = true;
							}
						}
						if ( ! similarDestinationDetected) {
							currentProduct.addDestination(nextProductDest);
						}
					}
				//}

				// remove the next similar product
				products.remove(i+1);
				i = i-1;
			}
		}
		
		for (int i=0; i < products.size() ; i++ ) {
	
			ProductTraceable product = products.get(i);
	
			Table table = new Table(2);
			table.setBorderWidth(20.0f);
			table.setPadding(5.0f);
			table.setSpacing(5.0f);

			RtfCell rtfCell = new RtfCell();

			boolean first = true;
			for(ProductTraceable.Destination dest : product.getDestinations()) {
				boolean isRequestedDestination = false;
				if (reportAllocated) {
					if ( ! dest.isStockItem() && ! dest.isSubstitutionItem() && ! dest.isSupplierReturnItem() && !dest.getCustomerCode().equals("") ) {
						isRequestedDestination = true;
					}
				}
	
				if (reportAssigned) {
					if ( dest.isSubstitutionItem() ) {
						isRequestedDestination = true;
					}
				}
				
				if (reportStock) {
					if ( dest.isStockItem() ) {
						isRequestedDestination = true;
					}
				}
				
				if (reportSupplier) {
					if ( dest.isSupplierReturnItem() ) {
						isRequestedDestination = true;
					}
				}
				if ( isRequestedDestination ) {

					if ( first ) {
						paragraph = new Paragraph(product.getSupplierName() + " - " + product.getDescription(), FontFactory.getFont(FontFactory.TIMES_ITALIC, 12, Font.BOLD, new Color(0, 0, 0)));
						paragraph.setSpacingAfter(3.0f);
						
						rtfCell.add(paragraph);
						
						rtfCell.setBorders(new RtfBorderGroup(Rectangle.BOTTOM, RtfBorder.BORDER_SINGLE,5.0f, new Color(0, 0, 0)));
						rtfCell.setColspan(2);
						rtfCell.setHeader(true);
						rtfCell.setVerticalAlignment(RtfCell.ALIGN_CENTER);
						
						table.addCell(rtfCell);
						table.endHeaders();
					}

					if ( ! first) {
						rtfCell = new RtfCell("");
						rtfCell.setColspan(2);
						table.addCell(rtfCell);
					}

					first = false;
					if ( dest.isStockItem() ) {
						rtfCell = new RtfCell();
						paragraph = new Paragraph("Code Produit : ", labelFont);
						rtfCell.add(paragraph);
						table.addCell(rtfCell);
			
						rtfCell = new RtfCell(String.valueOf(product.getProductCode()));
						table.addCell(rtfCell);
					}

					rtfCell = new RtfCell();
					paragraph = new Paragraph("Nombre et conditionnement : ", labelFont);
					rtfCell.add(paragraph);
					table.addCell(rtfCell);
		
					rtfCell = new RtfCell(String.valueOf(dest.getNumberOfUnit() + " X " + product.getUnitConditionnement()));
					table.addCell(rtfCell);
	
					if ( dest.isStockItem() ) {
						// if multiple report has to be generated, print the destination
						if (
							reportAllocated ||
							reportAssigned ||
							reportSupplier ||
							reportDeletedOrder ||
							reportRenewedOrder
						) {
							rtfCell = new RtfCell("POUR STOCKAGE");
							rtfCell.setColspan(2);
							table.addCell(rtfCell);
						}
	
					} else if ( dest.isSupplierReturnItem() ) {
	
						rtfCell = new RtfCell("RETOUR FOURNISSEUR (" + product.getSupplierName() + ")" );
						rtfCell.setColspan(2);
						table.addCell(rtfCell);
	
					} else {
					
						rtfCell = new RtfCell();
						paragraph = new Paragraph("Colis : ", labelFont);	
						rtfCell.add(paragraph);
						table.addCell(rtfCell);
			
						String packaging = String.valueOf(dest.getPackagingFromBasket());
						if (dest.getPackagingFromBasket() == 0) {
							packaging = "-";
						} else if (dest.getPackagingToBasket() != 0 && dest.getPackagingFromBasket() != dest.getPackagingToBasket()) {
							packaging = "De " + packaging + " à " + dest.getPackagingToBasket();
						}
			
						rtfCell = new RtfCell();
						paragraph = new Paragraph(packaging, labelFont);	
						rtfCell.add(paragraph);
						table.addCell(rtfCell);
						
						rtfCell = new RtfCell();
						paragraph = new Paragraph("Destinataire : ", labelFont);	
						rtfCell.add(paragraph);
						table.addCell(rtfCell);
			
						rtfCell = new RtfCell(dest.getCustomerName());
						table.addCell(rtfCell);
	
					}
				}
			}
			
	
			showEmptyReportMessage = showEmptyReportMessage?first:false;
			if ( ! first ) {
				document.add(table);
			}
		}
		
	} // end if entries related request
	
	if (reportDeletedOrder || reportRenewedOrder) {

		List<CustomerOrder> cos = allocation.getCustomerOrdersToAllocates();
		
		// sort products array by product description
		Collections.sort(cos, new Comparator<CustomerOrder>(){
			public int compare(CustomerOrder co0, CustomerOrder co1) {
				int result = -1;
				
				if ( (co0.isDeletedOrder() && co1.isDeletedOrder()) || co0.isRenewedOrder() && co1.isRenewedOrder()) {
					result = co0.getProductDescription().compareToIgnoreCase(co1.getProductDescription());
				}

				return result;
			}
		});
	
		for (CustomerOrder co : cos) {
			if ( (co.isDeletedOrder() && reportDeletedOrder) || (co.isRenewedOrder() && reportRenewedOrder) ) {
	
				Table table = new Table(2);
				table.setBorderWidth(20.0f);
				table.setPadding(5.0f);
				table.setSpacing(5.0f);
				
				RtfCell rtfCell = new RtfCell();
				paragraph = new Paragraph(allocation.getSupplierName(co.getSupplierCode()) + " - " + co.getProductDescription(), FontFactory.getFont(FontFactory.TIMES_ITALIC, 12, Font.BOLD, new Color(0, 0, 0)));
				paragraph.setSpacingAfter(3.0f);
				
				rtfCell.add(paragraph);
			
				rtfCell.setBorders(new RtfBorderGroup(Rectangle.BOTTOM, RtfBorder.BORDER_SINGLE,5.0f, new Color(0, 0, 0)));
				rtfCell.setColspan(2);
				rtfCell.setHeader(true);
				rtfCell.setVerticalAlignment(RtfCell.ALIGN_CENTER);
			
				table.addCell(rtfCell);
				table.endHeaders();
			
				rtfCell = new RtfCell();
				paragraph = new Paragraph("Nombre et conditionnement commandé: ", labelFont);
				rtfCell.add(paragraph);
				table.addCell(rtfCell);
		
				rtfCell = new RtfCell(String.valueOf(co.getNumberOfProduct() + " X " + Util.displayContitionnement(co.getNumberOfUnit(), co.getUnit())));
				table.addCell(rtfCell);
				
				if (co.getNumberGets() > 0) {
		
					rtfCell = new RtfCell();
					paragraph = new Paragraph("Nombre et conditionnement reçu: ", labelFont);
					rtfCell.add(paragraph);
					table.addCell(rtfCell);
			
					rtfCell = new RtfCell(String.valueOf(Math.round(co.getNumberGets())) + " X " + co.getUnit());
					table.addCell(rtfCell);
				}

				if ( co.isDeletedOrder() ) {

					rtfCell = new RtfCell("SUPPRESSION DE LA COMMANDE CLIENT (" + co.getCustomer().getName() + ")");
					rtfCell.setColspan(2);
					table.addCell(rtfCell);

				} else if ( co.isRenewedOrder() ) {

					rtfCell = new RtfCell("COMMANDE FOURNISSEUR A REFAIRE (" + co.getCustomer().getName() + ")");
					rtfCell.setColspan(2);
					table.addCell(rtfCell);
				}

				document.add(table);
				
				showEmptyReportMessage = false;
			}
			
		}

	}
	
	if (showEmptyReportMessage) {
		paragraph = new Paragraph(
			"Aucun colis n'est disponible pour cette demande. Soit il n'y a aucun article de substitution soit cette demande est issue d'une arrivée encodée avec la version antérieure de ouistiti.",
			FontFactory.getFont(FontFactory.TIMES_ITALIC, 18,
			Font.ITALIC, new Color(0, 0, 0)));
		paragraph.setSpacingBefore(10.0f);
		paragraph.setSpacingAfter(0.0f);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph);
	}

	document.close();
%>

<%@page import="org.belex.requestparams.RequestParams"%>
<%@page import="org.belex.customer.CustomerOrder"%>
<%@page import="org.belex.util.Util"%>
<%@page import="org.belex.product.ProductTraceable.Destination"%>
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