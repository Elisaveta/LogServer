package com.pravila.samples.logserver.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Plain JPA implementation of {@link LogMessageService}.
 * 
 */
@Repository
@Transactional
public class LogMessageServiceImpl implements LogMessageService {
	

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional
	public LogMessage save(LogMessage logMessage) {

		if (logMessage.getId() == 0) {
			em.persist(logMessage);
			return logMessage;
		} else {
			return em.merge(logMessage);
		}
	}

}