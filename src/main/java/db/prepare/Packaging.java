package db.prepare;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Packaging  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	private int orderNumber;
	@EqualsAndHashCode.Include
	private String orderLetter;
	@EqualsAndHashCode.Include
	private String customerOrderCode;
	@EqualsAndHashCode.Include
	private float customerOrderCodeNumber;
	@EqualsAndHashCode.Include
	private int feesType;
	private int fromPackageNumber;
	private String customerCode;

	
	public Packaging() {}

}