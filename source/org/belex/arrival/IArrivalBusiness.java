package org.belex.arrival;


public interface IArrivalBusiness {
	String forceStoreEntry(Arrival arrival);
	String storeEntry(Arrival arrival);
	String storeEntry(Arrival arrival, String forceStoreEntry);
}
