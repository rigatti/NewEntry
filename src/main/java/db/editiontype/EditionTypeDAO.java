package db.editiontype;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
public class EditionTypeDAO implements IEditionTypeDAO {

	private final SessionFactory sessionFactory;

	public EditionTypeDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Transactional(readOnly = true)
	public EditionType get(String id) {
		log.debug("Fetching EditionType with id: {}", id);

		try {

			EditionType editionType = currentSession().get(EditionType.class, id);

			if (editionType == null) {
				log.debug("No edition type found for id: {}", id);
				return new EditionType(id);
			}

			log.debug("EditionType found successfully");
			return editionType;

		} catch (Exception e) {
			log.error("Error while getting edition type: {}", id, e);
			return new EditionType(id);
		}
	}
}