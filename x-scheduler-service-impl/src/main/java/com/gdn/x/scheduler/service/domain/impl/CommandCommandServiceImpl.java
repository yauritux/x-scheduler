package com.gdn.x.scheduler.service.domain.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gdn.x.scheduler.dao.CommandDAO;
import com.gdn.x.scheduler.model.Command;
import com.gdn.x.scheduler.rest.web.model.CommandRequest;
import com.gdn.x.scheduler.service.domain.CommandCommandService;
import com.gdn.x.scheduler.service.helper.invoker.CommandInvoker;
import com.gdn.x.scheduler.service.helper.invoker.impl.CommandInvokerImpl;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 * 
 * Service class that contains all logics to manipulate the Command data.
 * Basically, this class represents command layer service in term of CQRS pattern.
 */
@Service("commandCommandService")
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class CommandCommandServiceImpl implements CommandCommandService {

	private static final Logger LOG = LoggerFactory.getLogger(CommandCommandServiceImpl.class);
	
	private CommandDAO commandDAO;
	
	@Autowired
	public CommandCommandServiceImpl(CommandDAO commandDAO) {
		this.commandDAO = commandDAO;
	}
		
	/**
	 * Save/Persist command into the database.
	 * 
	 * @param Command to be saved.
	 * @return the saved command
	 */
	@Override
	public Command save(Command entity) {
		if (entity == null) {
			return null;
		}
		return commandDAO.save(entity);
	}

	/**
	 * Use this method to delete the command from the system (temporary deleted).
	 * Note that the deleted command will still exist in the database, 
	 * and merely hidden from the system (flagged by markForDelete = true)
	 * 
	 * @param entity command object to be deleted.
	 * @return true for successful deletion, otherwise false will be returned. 
	 */	
	@Override
	public boolean delete(Command entity) {
		if (entity == null || entity.isMarkForDelete()) {
			return false;
		}
		try {
			int affected = commandDAO.deleteCommand(entity.getId());
			if (affected == 0) {
				return false;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	/**
	 * Use this method to restore the command that previously has been deleted 
	 * with {@link CommandCommandServiceImpl#deleteCommand} method. 
	 * Once the command is restored, it can be seen again (will be shown) in the system.
	 * 
	 * @param entity command object to be restored.
	 * @return true for successful operation, otherwise false will be returned.
	 */		
	@Override
	public boolean restore(Command entity) {
		if (entity == null || !entity.isMarkForDelete()) {
			return false;
		}
		try {
			int affected = commandDAO.restoreCommand(entity.getId());
			if (affected == 0) {
				return false;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	/**
	 * execute delete as a batch operation (i.e. performed to more than one object).
	 * 
	 * @param list of object/entity to be deleted.
	 * @return number of deleted records.
	 */
	@Override
	public int batchDelete(List<Command> entities) {
		if (entities == null || entities.isEmpty()) {
			return 0;
		}
		int affected = 0;
		try {
			for (Command command : entities) {
				if (command == null || command.isMarkForDelete()) {
					continue;
				}
				affected += commandDAO.deleteCommand(command.getId());
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return 0;
		}
		return affected;
	}

	/**
	 * execute restore as a batch operation (i.e. performed to more than one object).
	 * 
	 * @param list of object/entity to be restored.
	 * @return number of restored objects.
	 */
	@Override
	public int batchRestore(List<Command> entities) {
		if (entities == null || entities.isEmpty()) {
			return 0;
		}
		int affected = 0;
		try {
			for (Command command : entities) {
				if (command == null || !command.isMarkForDelete()) {
					continue;
				}
				affected += commandDAO.restoreCommand(command.getId());
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return 0;
		}
		return affected;
	}

	/**
	 * build Command specific object (i.e. WebService, ClientSDK, or ShellScript) 
	 * based on the command JSON request.
	 * 
	 * @param commandRequest
	 * @return specific command object.
	 */
	@Override
	public Command buildCommandFromRequest(CommandRequest commandRequest) {
		if (commandRequest == null) {
			return null;
		}
		CommandInvoker invoker = new CommandInvokerImpl();
		Command command = invoker.buildFromCommandRequest(commandRequest);
		
		return command;
	}
}