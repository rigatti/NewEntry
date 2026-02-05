package org.belex.requestparams;
import java.io.Serializable;

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
	
	public String getSearchExactMatch() {
		return searchExactMatch;
	}
	public void setSearchExactMatch(String searchExactMatch) {
		this.searchExactMatch = searchExactMatch;
	}
	public String getSearchOnSupplier() {
		return searchOnSupplier;
	}
	public void setSearchOnSupplier(String searchOnSupplier) {
		this.searchOnSupplier = searchOnSupplier;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getSearchValue() {
		return searchValue;
	}
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	public String getDetailIndex() {
		return detailIndex;
	}
	public void setDetailIndex(String detailIndex) {
		this.detailIndex = detailIndex;
	}
	public int getEntryId() {
		return entryId;
	}
	public void setEntryId(int entryId) {
		this.entryId = entryId;
	}
	public String getEntryLotNumber() {
		return entryLotNumber;
	}
	public void setEntryLotNumber(String entryLotNumber) {
		this.entryLotNumber = entryLotNumber;
	}
	public int getEntryNumberOfProduct() {
		return entryNumberOfProduct;
	}
	public void setEntryNumberOfProduct(int entryNumberOfUnit) {
		this.entryNumberOfProduct = entryNumberOfUnit;
	}
	public String getEntryValidityDate() {
		return entryValidityDate;
	}
	public void setEntryValidityDate(String entryValidityDate) {
		this.entryValidityDate = entryValidityDate;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getAllocationEntryOrders() {
		return allocationEntryOrders;
	}
	public void setAllocationEntryOrders(String allocationEntryOrders) {
		this.allocationEntryOrders = allocationEntryOrders;
	}
	public String getAllocationEntrySuppliers() {
		return allocationEntrySuppliers;
	}
	public void setAllocationEntrySuppliers(String allocationEntrySuppliers) {
		this.allocationEntrySuppliers = allocationEntrySuppliers;
	}
	public String getAllocCustomerCode() {
		return allocCustomerCode;
	}
	public void setAllocCustomerCode(String allocCustomerCode) {
		this.allocCustomerCode = allocCustomerCode;
	}
	public String getAllocCustomerOrderCode() {
		return allocCustomerOrderCode;
	}
	public void setAllocCustomerOrderCode(String allocCustomerOrderCode) {
		this.allocCustomerOrderCode = allocCustomerOrderCode;
	}
	public int getAllocCustomerOrderNumber() {
		return allocCustomerOrderNumber;
	}
	public void setAllocCustomerOrderNumber(int allocCustomerOrderNumber) {
		this.allocCustomerOrderNumber = allocCustomerOrderNumber;
	}
	public int getAllocNumberOfUnit() {
		return allocNumberOfUnit;
	}
	public void setAllocNumberOfUnit(int allocNumberOfUnit) {
		this.allocNumberOfUnit = allocNumberOfUnit;
	}
	public String getAllocOriginSupplierCode() {
		return allocOriginSupplierCode;
	}
	public void setAllocOriginSupplierCode(String allocOriginSupplierCode) {
		this.allocOriginSupplierCode = allocOriginSupplierCode;
	}
	public String getAllocProductCode() {
		return allocProductCode;
	}
	public void setAllocProductCode(String allocProductCode) {
		this.allocProductCode = allocProductCode;
	}
	public String getAllocSupplierCode() {
		return allocSupplierCode;
	}
	public void setAllocSupplierCode(String allocSupplierCode) {
		this.allocSupplierCode = allocSupplierCode;
	}
	public String getAllocSupplierOrderLetter() {
		return allocSupplierOrderLetter;
	}
	public void setAllocSupplierOrderLetter(String allocSupplierOrderLetter) {
		this.allocSupplierOrderLetter = allocSupplierOrderLetter;
	}
	public int getAllocSupplierOrderNumber() {
		return allocSupplierOrderNumber;
	}
	public void setAllocSupplierOrderNumber(int allocSupplierOrderNumber) {
		this.allocSupplierOrderNumber = allocSupplierOrderNumber;
	}
	public int getAllocOriginNumberOfUnit() {
		return allocOriginNumberOfUnit;
	}
	public void setAllocOriginNumberOfUnit(int allocOriginNumberOfUnit) {
		this.allocOriginNumberOfUnit = allocOriginNumberOfUnit;
	}
	public String getAllocOriginProduct() {
		return allocOriginProduct;
	}
	public void setAllocOriginProduct(String allocOriginProduct) {
		this.allocOriginProduct = allocOriginProduct;
	}
	public String getAllocCustomerName() {
		return allocCustomerName;
	}
	public void setAllocCustomerName(String allocCustomerName) {
		this.allocCustomerName = allocCustomerName;
	}
	public int getAllocOriginNumberOfProduct() {
		return allocOriginNumberOfProduct;
	}
	public void setAllocOriginNumberOfProduct(int allocOriginNumberOfProduct) {
		this.allocOriginNumberOfProduct = allocOriginNumberOfProduct;
	}
	public String getAllocOriginUnitConditionnement() {
		return allocOriginUnitConditionnement;
	}
	public void setAllocOriginUnitConditionnement(
			String allocOriginUnitConditionnement) {
		this.allocOriginUnitConditionnement = allocOriginUnitConditionnement;
	}
	public String getTraceEan() {
		return traceEan;
	}
	public void setTraceEan(String traceEan) {
		this.traceEan = traceEan;
	}
	public String getTraceLot() {
		return traceLot;
	}
	public void setTraceLot(String traceLot) {
		this.traceLot = traceLot;
	}
	public String getTraceProductCode() {
		return traceProductCode;
	}
	public void setTraceProductCode(String traceProductCode) {
		this.traceProductCode = traceProductCode;
	}
	public String getTraceValidityDate() {
		return traceValidityDate;
	}
	public void setTraceValidityDate(String traceValidityDate) {
		this.traceValidityDate = traceValidityDate;
	}
	public String getTraceExportIndexList() {
		return traceExportIndexList;
	}
	public void setTraceExportIndexList(String traceExportIndexList) {
		this.traceExportIndexList = traceExportIndexList;
	}
	public int getAllocPackagingFromBasket() {
		return allocPackagingFromBasket;
	}
	public void setAllocPackagingFromBasket(int allocPackagingFromBasket) {
		this.allocPackagingFromBasket = allocPackagingFromBasket;
	}
	public int getAllocPackagingToBasket() {
		return allocPackagingToBasket;
	}
	public void setAllocPackagingToBasket(int allocPackagingToBasket) {
		this.allocPackagingToBasket = allocPackagingToBasket;
	}
	public String getAllocationStep() {
		return allocationStep;
	}
	public void setAllocationStep(String allocationStep) {
		this.allocationStep = allocationStep;
	}
	public String getAllocReportStandard() {
		return allocReportStandard;
	}
	public void setAllocReportStandard(String allocReportStandard) {
		this.allocReportStandard = allocReportStandard;
	}
	public String getAllocReportStock() {
		return allocReportStock;
	}
	public void setAllocReportStock(String allocReportStock) {
		this.allocReportStock = allocReportStock;
	}
	public String getAllocReportSubstitution() {
		return allocReportSubstitution;
	}
	public void setAllocReportSubstitution(String allocReportSubstitution) {
		this.allocReportSubstitution = allocReportSubstitution;
	}
	public String getAllocReportSupplier() {
		return allocReportSupplier;
	}
	public void setAllocReportSupplier(String allocReportSupplier) {
		this.allocReportSupplier = allocReportSupplier;
	}
	public String getAllocReportDeletedOrder() {
		return allocReportDeletedOrder;
	}
	public void setAllocReportDeletedOrder(String allocReportDeletedOrder) {
		this.allocReportDeletedOrder = allocReportDeletedOrder;
	}
	public String getAllocReportRenewedOrder() {
		return allocReportRenewedOrder;
	}
	public void setAllocReportRenewedOrder(String allocReportRenewedOrder) {
		this.allocReportRenewedOrder = allocReportRenewedOrder;
	}
	public String getAdminNewProductCode() {
		return adminNewProductCode;
	}
	public void setAdminNewProductCode(String adminNewProductCode) {
		this.adminNewProductCode = adminNewProductCode;
	}
	public String getAdminOldProductCode() {
		return adminOldProductCode;
	}
	public void setAdminOldProductCode(String adminOldProductCode) {
		this.adminOldProductCode = adminOldProductCode;
	}
	public String getEntrySupplierDocumentDescription() {
		return entrySupplierDocumentDescription;
	}
	public void setEntrySupplierDocumentDescription(
			String entrySupplierDocumentDescription) {
		this.entrySupplierDocumentDescription = entrySupplierDocumentDescription;
	}
	public String getEntrySupplierDocumentType() {
		return entrySupplierDocumentType;
	}
	public void setEntrySupplierDocumentType(String entrySupplierDocumentType) {
		this.entrySupplierDocumentType = entrySupplierDocumentType;
	}
	public String getCustEmail() {
		return custEmail;
	}
	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}
	public String getCustNewEmail() {
		return custNewEmail;
	}
	public void setCustNewEmail(String custNewEmail) {
		this.custNewEmail = custNewEmail;
	}
	public String getCustCode() {
		return custCode;
	}
	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}
	public String getProductReference() {
		return productReference;
	}
	public void setProductReference(String productReference) {
		this.productReference = productReference;
	}
	public String getProductExportWithImage() {
		return productExportWithImage;
	}
	public void setProductExportWithImage(String productExportWithImage) {
		this.productExportWithImage = productExportWithImage;
	}
	public int getEntrySupplierEntryProductIntegrity() {
		return entrySupplierEntryProductIntegrity;
	}
	public void setEntrySupplierEntryProductIntegrity(int entrySupplierEntryProductIntegrity) {
		this.entrySupplierEntryProductIntegrity = entrySupplierEntryProductIntegrity;
	}
	public int getEntrySupplierEntryPackagingIntegrity() {
		return entrySupplierEntryPackagingIntegrity;
	}
	public void setEntrySupplierEntryPackagingIntegrity(int entrySupplierEntryPackagingIntegrity) {
		this.entrySupplierEntryPackagingIntegrity = entrySupplierEntryPackagingIntegrity;
	}
	public int getEntrySupplierEntryDlcDdmValidity() {
		return entrySupplierEntryDlcDdmValidity;
	}
	public void setEntrySupplierEntryDlcDdmValidity(int entrySupplierEntryDlcDdmValidity) {
		this.entrySupplierEntryDlcDdmValidity = entrySupplierEntryDlcDdmValidity;
	}
	public int getEntrySupplierEntryTemperatureValidity() {
		return entrySupplierEntryTemperatureValidity;
	}
	public void setEntrySupplierEntryTemperatureValidity(int entrySupplierEntryTemperatureValidity) {
		this.entrySupplierEntryTemperatureValidity = entrySupplierEntryTemperatureValidity;
	}
	public String getEntrySupplierEntryCommentOnQuality() {
		return entrySupplierEntryCommentOnQuality;
	}
	public void setEntrySupplierEntryCommentOnQuality(String entrySupplierEntryCommentOnQuality) {
		this.entrySupplierEntryCommentOnQuality = entrySupplierEntryCommentOnQuality;
	}

	public String getTraceEntrySupplierCode() {
		return traceEntrySupplierCode;
	}
	public String getTraceEntryStartDate() {
		return traceEntryStartDate;
	}
	public void setTraceEntryStartDate(String traceEntryStartDate) {
		this.traceEntryStartDate = traceEntryStartDate;
	}
	public String getTraceEntryEndDate() {
		return traceEntryEndDate;
	}
	public void setTraceEntryEndDate(String traceEntryEndDate) {
		this.traceEntryEndDate = traceEntryEndDate;
	}
	public void setTraceEntrySupplierCode(String traceEntrySupplierCode) {
		this.traceEntrySupplierCode = traceEntrySupplierCode;
	}
}
