package org.belex.allocation;

import org.belex.requestparams.RequestParams;

public interface IAllocationBusiness {
	Allocation getSuppliersEntries(Allocation allocation, RequestParams requestParams);
	Allocation getEntriesToAllocate(Allocation allocation, RequestParams requestParams);
	Allocation updateAllocation(Allocation allocation, RequestParams requestParams);
	Allocation confirmAllocation(Allocation allocation);
}
