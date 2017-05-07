package org.belex.traceability;

import org.belex.requestparams.RequestParams;


public interface ITraceabilityBusiness {
	Traceability searchEntry(Traceability traceability, RequestParams requestParams);
}
