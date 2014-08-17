package com.pravila.samples.logserver.persistence.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pravila.samples.logserver.persistence.dao.ILogMessage;
import com.pravila.samples.logserver.persistence.model.LogMessage;

@Service
@Transactional
public class LogMessageService {

	@Autowired
	private ILogMessage dao;

	public LogMessageService() {
		super();
	}

	// API

	public void create(final LogMessage entity) {
		dao.create(entity);
	}

	public LogMessage findOne(final long id) {
		return dao.findOne(id);
	}

	public List<LogMessage> findAll() {
		return dao.findAll();
	}

}
