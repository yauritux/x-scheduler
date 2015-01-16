package com.gdn.x.scheduler.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdn.x.scheduler.dao.CommandDAO;
import com.gdn.x.scheduler.service.CommandCommandService;

/**
 * 
 * @author yauritux
 *
 */
@Service
@Transactional(readOnly = false)
public class CommandCommandServiceImpl implements CommandCommandService {

	private CommandDAO commandDAO;
	
	@Autowired
	public CommandCommandServiceImpl(CommandDAO commandDAO) {
		this.commandDAO = commandDAO;
	}
	
	@Override
	public int deleteCommand(String id) {
		return commandDAO.deleteCommand(id);
	}
}