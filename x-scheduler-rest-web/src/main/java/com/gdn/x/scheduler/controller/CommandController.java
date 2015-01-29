package com.gdn.x.scheduler.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.codehaus.jackson.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gdn.common.enums.ErrorCategory;
import com.gdn.common.web.wrapper.response.GdnBaseRestResponse;
import com.gdn.common.web.wrapper.response.GdnRestListResponse;
import com.gdn.common.web.wrapper.response.GdnRestSingleResponse;
import com.gdn.common.web.wrapper.response.PageMetaData;
import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.model.Command;
import com.gdn.x.scheduler.model.WebServiceCommand;
import com.gdn.x.scheduler.rest.web.model.CommandResponse;
import com.gdn.x.scheduler.rest.web.model.WSCommandRequest;
import com.gdn.x.scheduler.rest.web.model.WSCommandResponse;
import com.gdn.x.scheduler.service.CommandCommandService;
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
	private CommandCommandService commandActionService;
		
	@Autowired
	public CommandController(CommandQueryService commandQueryService, CommandCommandService commandActionService) {
		this.commandQueryService = commandQueryService;
		this.commandActionService = commandActionService;
	}
	
	@RequestMapping(value = "/ws/{id}", method = RequestMethod.GET)
	public GdnRestSingleResponse<WSCommandResponse> getWSCommand(@PathVariable("id") String id,
			@RequestParam String storeId, @RequestParam String requestId) throws Exception {
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
			throw e;
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw e;
		}
		
		return null;
	}
	
	@RequestMapping(value = "/ws", method = RequestMethod.GET)
	public GdnRestListResponse<WSCommandResponse> getWSCommands(
			@RequestParam String storeId, @RequestParam String requestId,
			@RequestParam(defaultValue = "0") Integer pageNumber,
			@RequestParam(defaultValue = "30") Integer pageSize) 
					throws ReflectiveOperationException, NullPointerException, 
						JsonParseException, IOException {

		List<WSCommandResponse> listResponse = new ArrayList<WSCommandResponse>();
		
		PageMetaData pageMetaData = null;
		
		try {		
			Page<Command> pageList = commandQueryService.fetchAll(pageNumber, pageSize);
		
			List<Command> commands = pageList.getContent();
		
			LOG.debug("total commands found: " + (commands != null ? commands.size() : 0));
				
			for (Command command : commands) {
				listResponse.add((WSCommandResponse) commandQueryService.wrapCommand(command));
			}
			
			pageMetaData = new PageMetaData(pageSize, pageNumber, pageList.getTotalElements());
			
		} catch (NullPointerException e) { 
			LOG.error(e.getMessage(), e);
			throw e;
		} catch (JsonParseException e) {
			LOG.error(e.getMessage(), e);
			throw e;
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
				
		return new GdnRestListResponse<WSCommandResponse>(listResponse, pageMetaData, requestId);
	}
	
	@RequestMapping(value = "/ws", method = RequestMethod.POST)
	public GdnBaseRestResponse submitCommand(@Valid @RequestBody WSCommandRequest wsCommandRequest,
			BindingResult result, @RequestParam String storeId, @RequestParam String requestId) {		
		if (result.hasErrors()) {
			return errorValidation(result);
		}		
		
		WebServiceCommand wsCommand = new WebServiceCommand();
		if (wsCommandRequest.getId() != null) {
			wsCommand.setId(wsCommandRequest.getId());
		}
		wsCommand.setStoreId(storeId);
		wsCommand.setCreatedDate(new Date());
		wsCommand.setCommand(wsCommandRequest.getCommand());
		wsCommand.setCommandType(CommandType.WEB_SERVICE);
		wsCommand.setMarkForDelete(false);
		wsCommand.setParameters(wsCommandRequest.getParameters());
		
		commandActionService.save(wsCommand);
		
		GdnBaseRestResponse response = new GdnBaseRestResponse(true);
		response.setRequestId(requestId);
		
		return response;
	}
	
	private GdnBaseRestResponse errorValidation(BindingResult result) {
		GdnBaseRestResponse response = new GdnBaseRestResponse(false);
		response.setErrorCode(ErrorCategory.VALIDATION.getCode());
		
		List<FieldError> errors = result.getFieldErrors();
		StringBuffer sb = new StringBuffer();
		for (FieldError error : errors) {
			sb.append(error.getObjectName() + ":" + error.getDefaultMessage() + ", ");
		}
		LOG.debug(sb.toString());
		
		response.setErrorMessage("not valid parameters");
		return response;
	}
}