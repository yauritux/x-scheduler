package com.gdn.x.scheduler.service.helper.receiver.impl;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.constant.WSMethod;
import com.gdn.x.scheduler.constant.WSRequestHeader;
import com.gdn.x.scheduler.model.Command;
import com.gdn.x.scheduler.model.WebServiceCommand;
import com.gdn.x.scheduler.rest.web.model.CommandRequest;
import com.gdn.x.scheduler.rest.web.model.CommandResponse;
import com.gdn.x.scheduler.rest.web.model.WSCommandRequest;
import com.gdn.x.scheduler.rest.web.model.WSCommandResponse;
import com.gdn.x.scheduler.service.domain.CommandQueryService;
import com.gdn.x.scheduler.service.helper.factory.impl.CommandFactory;
import com.gdn.x.scheduler.service.helper.receiver.CommandReceiver;
import com.gdn.x.scheduler.wrapper.ProcessResponse;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 * 
 * Receiver class for WebServiceCommand.
 * (see <a href="http://en.wikipedia.org/wiki/Command_pattern">Command Pattern</a>)  
 *
 */
public class WSCommandReceiverImpl implements CommandReceiver {
	
	private static final Logger LOG = LoggerFactory.getLogger(WSCommandReceiverImpl.class);
	
	private Command command;
	private CommandQueryService commandQueryService;
	
	/**
	 * default constructor
	 */
	public WSCommandReceiverImpl() {
		super();
	}
		
	/**
	 * constructor for this class that 
	 * receives command entity as the parameter.
	 * 
	 * @param command
	 */
	public WSCommandReceiverImpl(Command command) {
		this.command = command;
	}
	
	@Override
	public void setCommandQueryService(CommandQueryService commandQueryService) {
		this.commandQueryService = commandQueryService;
	}	

	/**
	 * build and return CommandResponse object based on the given command type.
	 * 
	 * @return instance of CommandResponse.
	 * @throws Exception
	 */
	@Override
	public CommandResponse wrapCommand() throws Exception {
		WSCommandResponse wsCommandResponse = new WSCommandResponse();
		wsCommandResponse.setId(command.getId());
		wsCommandResponse.setCommandType(CommandType.WEB_SERVICE.name());
		wsCommandResponse.setStoreId(command.getStoreId());
		wsCommandResponse.setCreatedBy(command.getCreatedBy());
		wsCommandResponse.setCreatedDate(command.getCreatedDate());
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser jp = factory.createParser(command.getCommand());
			JsonNode actualObj = mapper.readTree(jp);
			
			wsCommandResponse.setEndPoint(actualObj.get(WSRequestHeader.URL.label()).asText());
			wsCommandResponse.setHttpMethod(actualObj.get(WSRequestHeader.METHOD.label()).asText());
			wsCommandResponse.setParameters(command.getParameters());
		} catch (JsonParseException e) {			
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
			
		return wsCommandResponse;
	}

	/**
	 * convert CommandRequest to Command object, in order to be persisted 
	 * into the DB.
	 * @param command request object
	 * @return command 
	 */
	@Override
	public Command convertToCommand(CommandRequest commandRequest)
			throws Exception {
		WebServiceCommand wsCommand = new WebServiceCommand();
		wsCommand.setId(((commandRequest.getId() != null && !commandRequest.getId().isEmpty())
				? commandRequest.getId() : null));
		WSCommandRequest wsCommandRequest = (WSCommandRequest) commandRequest;
		wsCommand.setCommand("{\"" + WSRequestHeader.URL.label() + "\":\"" 
				+ wsCommandRequest.getEndpoint() + "\",\"" + WSRequestHeader.METHOD.label() 
				+ "\":\""+ WSMethod.GET.name() + "\"}");
		wsCommand.setCommandType(CommandType.WEB_SERVICE);
		wsCommand.setContents(wsCommandRequest.getContents());
		wsCommand.setParameters(wsCommandRequest.getParameters());		
		wsCommand.setCreatedBy(wsCommandRequest.getSubmittedBy());
		wsCommand.setCreatedDate(new Date());
		wsCommand.setMarkForDelete(false);
		wsCommand.setStoreId(wsCommandRequest.getStoreId());
		
		return wsCommand;
	}
	
	@Override
	public void executeCommand() throws Exception {
		System.out.println("Calling web service...");
		
		try {
			WSCommandResponse webService = (WSCommandResponse) commandQueryService.wrapCommand(command);
			ProcessResponse processResponse = CommandFactory.getExecutorFromWSCommand(webService).callWS(webService, 11000);
			
			if (!"200".equals(processResponse.getCode())) {
				throw new RuntimeException("Failed to execute WS. Status Code: "
						+ processResponse.getCode() + ", message: " + processResponse.getMessage());				
			}			
		} catch (Exception e) {	
			LOG.error(e.getMessage(), e);
			throw e;
		}		
	}
}