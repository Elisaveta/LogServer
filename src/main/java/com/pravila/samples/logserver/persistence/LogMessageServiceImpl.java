package com.pravila.samples.logserver.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Plain JPA implementation of {@link LogMessageService}.
 * 
 */
@Repository
@Transactional
public class LogMessageServiceImpl implements LogMessageService {

	private static Logger logger = Logger.getLogger(LogMessageServiceImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional
	public void save(LogMessage logMessage) {

		if (logMessage.getId() == 0) {
			try {
				em.persist(logMessage);
				logger.info(" " + logMessage.getLevel() + " " +logMessage.getDate() + " " + logMessage.getClassName() + " " + logMessage.getMessage());
				
			} catch (Exception e) {
				logger.info(logMessage);
			}

		} else {
			em.merge(logMessage);
		}
	}

}