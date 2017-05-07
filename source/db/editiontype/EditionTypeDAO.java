package db.editiontype;

import java.util.ArrayList;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class EditionTypeDAO extends HibernateDaoSupport implements IEditionTypeDAO {

	private static final Category log = Logger.getLogger(EditionType.class);
	
	public EditionType get(String id) {
		EditionType editionType = new EditionType(id);
		
		log.debug("Get Edition Type for :" + id);
		
		try {

			boolean itemfound = false;
			String sql = "from EditionType where id='" + id + "'";
			
			Object objList = getHibernateTemplate().find(sql); 
			
			if (objList instanceof ArrayList) {

				ArrayList<EditionType> listOfEditionType = (ArrayList<EditionType>) objList;

				if (listOfEditionType.size() > 0) {
					itemfound = true;
					// List size should be 1
					if (listOfEditionType.size() == 1) {
						editionType = listOfEditionType.get(0);
					} else {
						log.error("List of edition type is bigger than 1 : " + sql);
					}
				}
			}

			if (! itemfound) {
				log.debug("No edition type found");
			}

			log.debug("end of get");

		} catch (Exception e) {
			log.error("Error while getting edition type :" + id, e);
		}
		return editionType;
	}
}