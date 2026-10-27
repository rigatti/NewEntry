package org.belex.requestparams;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class RequestParams implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String searchValue = "";
	private String searchExactMatch = "";
	private String searchType = "";
	private String searchOnSupplier = "";

	private String supplierCode = "";
	private int orderNumber = -1;

	private String traceProductCode = "";
	private String traceEan = "";
	private String traceLot = "";
	private String traceValidityDate = "";
	private String traceExportIndexList = "";
	
	private String traceEntrySupplierCode = "";
	private String traceEntryStartDate = "";
	private String traceEntryEndDate = "";
	
	private int entryId = 0;
	private int entryNumberOfProduct = 0;
	private String entryValidityDate = "";
	private String entryLotNumber = "";
	private String entrySupplierDocumentDescription = "";
	private String entrySupplierDocumentType = "";
	
	private int entrySupplierEntryProductIntegrity;
	private int entrySupplierEntryPackagingIntegrity;
	private int entrySupplierEntryDlcDdmValidity;
	private int entrySupplierEntryTemperatureValidity;
	private String entrySupplierEntryCommentOnQuality;
	
	// represented by a Stringtokenizer delim by "#" 
	// each element is a Stringtokenizer containing a supplierCode, number and letter
	// ex : Supplier=CT;Number=1;Letter=DFR#Supplier=RLL;Number=2;Letter=DFX
	private String allocationEntryOrders = "";
	private String allocationEntrySuppliers = "";
	
	// substitution / supplier / stock
	private String allocationStep = "";
	
	private int allocPackagingFromBasket;
	private int allocPackagingToBasket;
	private String allocOriginSupplierCode = "";
	private int allocSupplierOrderNumber;
	private String allocSupplierOrderLetter = "";
	private String allocCustomerOrderCode = "";
	private int allocCustomerOrderNumber;
	private String allocOriginProduct = "";
	private int allocOriginNumberOfUnit;
	private int allocOriginNumberOfProduct;
	private String allocOriginUnitConditionnement = "";

	private String allocSupplierCode = "";
	private int allocNumberOfUnit;
	private String allocProductCode = ""; 
	private String allocCustomerCode = "";
	private String allocCustomerName = "";

	// rtf related stuff
	// indicate which part of the products must be founded in the report
	private String allocReportSubstitution = "";
	private String allocReportStock = "";
	private String allocReportSupplier = "";
	private String allocReportStandard = "";
	private String allocReportDeletedOrder = "";
	private String allocReportRenewedOrder = "";

	private String adminOldProductCode = "";
	private String adminNewProductCode = "";
	
	private String detailIndex = "";
	
	private String date = "";
	
	private String custEmail = "";
	private String custNewEmail = "";
	private String custCode = "";
	
	private String productReference = "";
	private String productExportWithImage = "";
	

}
