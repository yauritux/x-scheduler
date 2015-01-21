package com.gdn.x.scheduler.service.impl;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.dao.CommandDAO;
import com.gdn.x.scheduler.model.Command;
import com.gdn.x.scheduler.rest.web.model.CommandResponse;
import com.gdn.x.scheduler.rest.web.model.WSCommandResponse;
import com.gdn.x.scheduler.service.CommandQueryService;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 * Service Class that contains queries for the Command data.
 * Basically, this class represents Query layer service of CQRS pattern.
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class CommandQueryServiceImpl implements CommandQueryService {

	private CommandDAO commandDAO;
	
	@Autowired
	public CommandQueryServiceImpl(CommandDAO commandDAO) {
		this.commandDAO = commandDAO;
	}
	
	/**
	 * Used to find the Command by it's ID. 
	 * Note that this method will be searching command based on the ID, 
	 * including command that has already been deleted (markForDelete=true).
	 * 
	 * @see <code>findByIdExcl(id)</code> method for comparison.
	 * 
	 * @param id the command ID.
	 * @return command.
	 */	
	@Override
	public Command findById(String id) {
		return commandDAO.findById(id != null ? id.trim() : id);
	}

	/**
	 * Used to retrieve all Commands in the database (exclusive markForDelete).
	 * 
	 * @see <code>findAll()</code> method for comparison.
	 * 
	 * @return list of command or empty list.
	 */	
	@Override
	public List<Command> fetchAll() {
		return commandDAO.fetchAll();
	}

	/**
	 * Used to retrieve all Commands in the database (exclusive markForDelete).
	 * Use this method if You'd like to apply paging in the result.
	 * 
	 * @see <code>fetchAll(pageNumber, pageSize)</code> method for comparison.
	 * 
	 * @param pageNumber number of current page.
	 * @param pageSize total pages.
	 * @return Page object that contains all commands for particular page number.
	 */	
	@Override
	public Page<Command> fetchAll(int pageNumber, int pageSize) {
		return commandDAO.fetchAll(new PageRequest(pageNumber, pageSize));
	}

	/**
	 * Used to find one or more commands those belong to <i>command type</i> 
	 * as specified in the query filter (parameter).
	 * This method merely search for commands with markForDelete=false. 
	 * 
	 * @param commandName the name of the commands.
	 * @return list of commands those are matched the searching criteria.
	 */	
	@Override
	public List<Command> findByCommandType(CommandType commandType) {
		return commandDAO.findByCommandType(commandType);
	}

	/**
	 * Used to find one or more commands those belong to <i>command type</i> 
	 * as specified in the query filter (parameter).
	 * This method merely search for commands with markForDelete=false.
	 * Use this one instead if You'd like to apply paging in the result. 
	 * 
	 * @param commandName the name of the commands.
	 * @return list of commands those are matched the searching criteria.
	 */		
	@Override
	public Page<Command> findByCommandType(CommandType commandType,
			int pageNumber, int pageSize) {
		return commandDAO.findByCommandType(commandType,
				new PageRequest(pageNumber, pageSize));
	}

	/**
	 * Use this to wrapping the command entity into CommandResponse object. 
	 * This method is useful in the REST result/response as this method will also parse and  
	 * return a complete object content specified in the JSON content's field.  
	 * 
	 * @param command 
	 * @return CommandResponse
	 */	
	@Override
	public CommandResponse wrapCommand(Command command) throws JsonParseException, IOException {
		if (command == null) {
			return null;
		}
		
		if (command.getCommandType() == CommandType.WEB_SERVICE) {
			WSCommandResponse wsCommandResponse = new WSCommandResponse();
			wsCommandResponse.setCommandId(command.getId());
			wsCommandResponse.setCommandType("WEB_SERVICE");
			wsCommandResponse.setCreatedBy(command.getCreatedBy());
			wsCommandResponse.setCreatedDate(command.getCreatedDate());
				
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getJsonFactory();
			JsonParser jp = factory.createJsonParser(command.getCommand());
			JsonNode actualObj = mapper.readTree(jp);
				
			wsCommandResponse.setEndPoint(actualObj.get("url").asText());
			wsCommandResponse.setHttpMethod(actualObj.get("method").asText());
			wsCommandResponse.setParameters(command.getParameters());
				
			return wsCommandResponse;
		}
		
		return null;
	}	
}