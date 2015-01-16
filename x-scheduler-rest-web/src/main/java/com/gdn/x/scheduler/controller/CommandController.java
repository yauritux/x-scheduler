package com.gdn.x.scheduler.controller;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gdn.common.web.wrapper.response.GdnRestSingleResponse;
import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.model.Command;
import com.gdn.x.scheduler.rest.web.model.CommandResponse;
import com.gdn.x.scheduler.rest.web.model.WSCommandResponse;
import com.gdn.x.scheduler.service.CommandQueryService;
import com.wordnik.swagger.annotations.Api;

/**
 * 
 * @author yauritux
 *
 */
@RestController
@Api(value = "command", description = "Command Service API")
@RequestMapping(value = "/command", 
	produces = { 
		MediaType.APPLICATION_JSON_VALUE, 
		MediaType.APPLICATION_XML_VALUE 
})
public class CommandController {
	
	private static final Logger LOG = LoggerFactory.getLogger(CommandController.class);

	private CommandQueryService commandQueryService;
		
	@Autowired
	public CommandController(CommandQueryService commandQueryService) {
		this.commandQueryService = commandQueryService;
	}
	
	@RequestMapping(value = "/ws/{id}", method = RequestMethod.GET)
	public GdnRestSingleResponse<WSCommandResponse> getWSCommand(@PathVariable("id") String id,
			@RequestParam String storeId, @RequestParam String requestId) {
		try {
			Command command = commandQueryService.findById(id);
			if (command.getCommandType() == CommandType.WEB_SERVICE) {
				CommandResponse commandResponse = commandQueryService.wrapCommand(command);
				if (commandResponse == null) {
					return null;
				}
		    
				return new GdnRestSingleResponse<WSCommandResponse>(
					(WSCommandResponse) commandResponse, requestId
				);	
			}
		} catch (JsonParseException e) {
			LOG.error(e.getMessage(), e);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		
		return null;
	}
}