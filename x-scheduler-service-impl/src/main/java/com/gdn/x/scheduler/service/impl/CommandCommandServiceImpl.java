package com.gdn.x.scheduler.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gdn.x.scheduler.dao.CommandDAO;
import com.gdn.x.scheduler.model.Command;
import com.gdn.x.scheduler.service.CommandCommandService;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 * 
 * Service class that contains all logics to manipulate the Command data.
 * Basically, this class represents command layer service of CQRS pattern.
 */
@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class CommandCommandServiceImpl implements CommandCommandService {

	private CommandDAO commandDAO;
	
	@Autowired
	public CommandCommandServiceImpl(CommandDAO commandDAO) {
		this.commandDAO = commandDAO;
	}
	
	/**
	 * Use this method to delete the command from the system (temporary deleted).
	 * Note that the deleted command will be still existed in the database, 
	 * and merely hidden from the system (flagged by markForDelete = true)
	 * 
	 * @param id the command id.
	 * @return number of command deleted. 
	 */	
	@Override
	public int deleteCommand(String id) {
		Command command = commandDAO.findById(id);
		if (command == null || command.isMarkForDelete()) {
			return 0;
		}
		return commandDAO.deleteCommand(id);
	}

	/**
	 * Use this method to restore the command that previously has been deleted 
	 * with <code>deleteCommand</code> method. 
	 * Once the command is restored, it can be seen again (will be displayed) in the system.
	 * 
	 * @param id the command id.
	 * @return number of command restored.
	 */	
	@Override
	public int restoreCommand(String id) {
		Command command = commandDAO.findById(id);
		if (command == null || !command.isMarkForDelete()) {
			return 0;
		}
		return commandDAO.restoreCommand(id);
	}
}