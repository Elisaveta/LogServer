package com.pravila.samples.logserver.persistence.dao;

import java.util.List;

import com.pravila.samples.logserver.persistence.model.LogMessage;

public interface ILogMessage {

	LogMessage findOne(long id);

	List<LogMessage> findAll();

	void create(LogMessage entity);

	LogMessage update(LogMessage entity);

	void delete(LogMessage entity);

	void deleteById(long entityId);

}
