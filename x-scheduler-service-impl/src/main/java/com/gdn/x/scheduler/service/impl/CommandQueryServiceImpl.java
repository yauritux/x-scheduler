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
 * @author yauritux
 *
 */
@Service
@Transactional(readOnly = true)
public class CommandQueryServiceImpl implements CommandQueryService {

	private CommandDAO commandDAO;
	
	@Autowired
	public CommandQueryServiceImpl(CommandDAO commandDAO) {
		this.commandDAO = commandDAO;
	}
	
	@Override
	public Command findById(String id) {
		return commandDAO.findById(id != null ? id.trim() : id);
	}

	@Override
	public List<Command> fetchAll() {
		return commandDAO.fetchAll();
	}

	@Override
	public Page<Command> fetchAll(int pageNumber, int pageSize) {
		return commandDAO.fetchAll(new PageRequest(pageNumber, pageSize));
	}

	@Override
	public List<Command> findByCommandType(CommandType commandType) {
		return commandDAO.findByCommandType(commandType);
	}

	@Override
	public Page<Command> findByCommandType(CommandType commandType,
			int pageNumber, int pageSize) {
		return commandDAO.findByCommandType(commandType,
				new PageRequest(pageNumber, pageSize));
	}

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