package org.belex.export;

public interface IExportBusiness {
	Export productExport(Export export, String productReference, String productExportWithImage);
	Export productsExport(Export export);
	Export customersExport(Export export);
	//Export customerExport(Export export, String custCode, String custEmail, String custNewEmail);
}
