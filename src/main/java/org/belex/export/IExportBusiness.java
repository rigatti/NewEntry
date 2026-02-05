package org.belex.export;

public interface IExportBusiness {
	Export productExport(Export export, String productReference, String productExportWithImage);
	//Export customerExport(Export export, String custCode, String custEmail, String custNewEmail);
}
