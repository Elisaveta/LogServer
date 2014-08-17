package com.pravila.samples.logserver.persistence.dao;

import org.springframework.stereotype.Repository;

import com.pravila.samples.logserver.persistence.model.LogMessage;

@Repository
public class LogMessageDao extends AbstractJpaDAO<LogMessage> implements ILogMessage{

	public LogMessageDao() {
        super();
    }
}
