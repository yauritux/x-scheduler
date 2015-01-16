package com.gdn.x.scheduler.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.JsonParseException;
import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.model.Command;
import com.gdn.x.scheduler.rest.web.model.CommandResponse;

/**
 * 
 * @author yauritux
 *
 */
public interface CommandQueryService {

	public Command findById(String id);
	public List<Command> fetchAll();
	public Page<Command> fetchAll(int pageNumber, int pageSize);
	public List<Command> findByCommandType(CommandType commandType); 
	public Page<Command> findByCommandType(CommandType commandType, int pageNumber, int pageSize);
	public CommandResponse wrapCommand(Command command) throws JsonParseException, IOException;
}