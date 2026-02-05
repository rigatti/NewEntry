package org.belex.cleaning;

import db.entry.treated.ITreatedEntryDAO;
import db.entry.treated.ITreatedEntryDetailDAO;
import db.entry.treated.ITreatedEntryDetailDestinationDAO;
import db.entry.treated.temp.ITreatedEntryCustomerTempDAO;
import db.entry.treated.temp.ITreatedEntryTempDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class CleaningBusiness implements ICleaningBusiness {
	
	ITreatedEntryDAO treatedEntryDAO;
	ITreatedEntryDetailDAO treatedEntryDetailDAO;
	ITreatedEntryDetailDestinationDAO treatedEntryDetailDestinationDAO;
	
	ITreatedEntryTempDAO treatedEntryTempDAO;
	ITreatedEntryCustomerTempDAO treatedEntryCustomerTempDAO;

	@Transactional
	public Cleaning cleanDb(Cleaning cleaning) {

		/*
		log.info("temp tables cleaning - START");
		transaction.begin();
		// remove all entries from db without corresponding foreign keys
		treatedEntryTempDAO.cleanDb(session, treatedEntryCustomerTempDAO);
		transaction.commit();
		log.info("temp tables cleaning - DONE");
		*/
		
		java.util.Calendar cal = java.util.Calendar.getInstance();
		java.text.SimpleDateFormat sdtf = new java.text.SimpleDateFormat("yyyyMMdd");
		
		// clean all older than 3 years
		cal.add(java.util.Calendar.YEAR, -3);
		String currentMaxDate = sdtf.format(cal.getTime());
		
		log.info("tables cleaning < " + currentMaxDate + " - START");
		treatedEntryDAO.cleanDb(currentMaxDate, treatedEntryDetailDAO, treatedEntryDetailDestinationDAO);
		log.info("tables cleaning < " + currentMaxDate + " - DONE");

		// for now - 3 years, clean 6 x 3 months. The cleaning will be done until now - 1,5 years
		for (int i = 0 ; i < 6 ; i++) {
			cal.add(java.util.Calendar.MONTH, 3);
			currentMaxDate = sdtf.format(cal.getTime());
			log.info("tables cleaning < " + currentMaxDate + " - START");
			treatedEntryDAO.cleanDb(currentMaxDate, treatedEntryDetailDAO, treatedEntryDetailDestinationDAO);
			log.info("tables cleaning < " + currentMaxDate + " - DONE");
		}

		return cleaning;
	}

	public void setTreatedEntryDAO(ITreatedEntryDAO treatedEntryDAO) {
		this.treatedEntryDAO = treatedEntryDAO;
	}

	public void setTreatedEntryDetailDAO(
			ITreatedEntryDetailDAO treatedEntryDetailDAO) {
		this.treatedEntryDetailDAO = treatedEntryDetailDAO;
	}

	public void setTreatedEntryDetailDestinationDAO(
			ITreatedEntryDetailDestinationDAO treatedEntryDetailDestinationDAO) {
		this.treatedEntryDetailDestinationDAO = treatedEntryDetailDestinationDAO;
	}

	public ITreatedEntryCustomerTempDAO getTreatedEntryCustomerTempDAO() {
		return treatedEntryCustomerTempDAO;
	}

	public void setTreatedEntryCustomerTempDAO(
			ITreatedEntryCustomerTempDAO treatedEntryCustomerTempDAO) {
		this.treatedEntryCustomerTempDAO = treatedEntryCustomerTempDAO;
	}

	public ITreatedEntryTempDAO getTreatedEntryTempDAO() {
		return treatedEntryTempDAO;
	}

	public void setTreatedEntryTempDAO(ITreatedEntryTempDAO treatedEntryTempDAO) {
		this.treatedEntryTempDAO = treatedEntryTempDAO;
	}
}
