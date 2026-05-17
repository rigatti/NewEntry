package org.belex.arrival;


import org.belex.product.Product;
import org.belex.requestparams.RequestParams;
import org.belex.supplier.Supplier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.webflow.execution.Event;

import java.util.Vector;

public interface ArrivalBusiness {
	public static final int STORE_ERROR = -1;
	public static final int STORE_WARNING = 0;
	public static final int STORE_UPDATE = 9;
	public static final int STORE_SUCCESS = 1;

	String forceStoreEntry(Arrival arrival);
	String storeEntry(Arrival arrival);
	String storeEntry(Arrival arrival, String forceStoreEntry);

	Event modifyEntry(Arrival arrival, RequestParams requestParams);

	@Transactional
	Event removeEntry(Arrival arrival, RequestParams requestParams);

	@Transactional
	Event saveEntry(Arrival arrival);

	Event checkPending(Arrival arrival);

	Event getPending(Arrival arrival);

	@Transactional
	boolean deletePending(Arrival arrival);

	Arrival getPlannedSuppliers(Arrival arrival);

	Event getPlannedSupplier(Arrival arrival);

	Supplier fillSupplierOrder(Supplier supplier);

	Arrival productSelection(Arrival arrival, Vector<Product> products);

	void selectBasket(Arrival arrival);
}
