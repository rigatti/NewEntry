package db.entry.treated;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TreatedEntryDetail  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int treatedEntryDetailId;
	private int treatedEntryId;
	private String supplierOrderNumbers;
	private String supplierOrderLetters;
	private String additionalData;
	private String description;
	private String unitConditionnement;
	private int numberOfUnit;
	private int numberOfProduct;
	private String supplierDocumentDescription;
	private int supplierDocumentType;
	private int stockMovement;
	private Integer supplierEntryProductIntegrity;
	private Integer supplierEntryPackagingIntegrity;
	private Integer supplierEntryDlcDdmValidity;
	private Integer supplierEntryTemperatureValidity;
	private String supplierEntryCommentOnQuality;

	private List<TreatedEntryDetailDestination> treatedEntryDetailDestinations  = new ArrayList<TreatedEntryDetailDestination>();
	
	public TreatedEntryDetail() {}

}