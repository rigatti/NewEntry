package org.belex.customer;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CustomerEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	private String assignedSupplierCode;
	
	private String productCode;
	private int priority;
	private String unitConditionnement;
	private int numberOfConditionnement;
	private int numberOfUnit;
	private int totalUnit;
	private boolean needFridge;

	private int fromBasketNumber;
	private int toBasketNumber;
}
