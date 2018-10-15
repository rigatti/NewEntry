package org.belex.traceability;

import org.belex.requestparams.RequestParams;


public interface ITraceabilityBusiness {
	Traceability searchProductEntry(Traceability traceability, RequestParams requestParams);
	Traceability searchSupplierEntry(Traceability traceability, RequestParams requestParams);
}
