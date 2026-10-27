package db.entry.treated.temp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreatedEntryTemp  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

  	private int id;

	private String supplierCode;
	private String arrivalDate;
	private String arrivalTime;
	private String productCode;
	private String ean;
	private String unitConditionnement;
	private int numberOfUnit;
	private int numberOfProduct;
	private String lotNumber;
	private String supplierProductCode;
	private String supplierOrderNumbers;
	private String supplierOrderLetters;
	private String validityDate;
	private String additionalData;
	private String description;
	private float numberOfProductReceived;
	private String supplierDocumentDescription;
	private int supplierDocumentType;
	private int stockMovement;
	private Integer supplierEntryProductIntegrity;
	private Integer supplierEntryPackagingIntegrity;
	private Integer supplierEntryDlcDdmValidity;
	private Integer supplierEntryTemperatureValidity;
	private String supplierEntryCommentOnQuality;

	public TreatedEntryTemp() {}

}