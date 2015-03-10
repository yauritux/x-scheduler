package com.gdn.x.scheduler.service.helper.receiver.impl;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdn.x.scheduler.constant.ClientSDKRequestField;
import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.model.ClientSDKCommand;
import com.gdn.x.scheduler.model.Command;
import com.gdn.x.scheduler.rest.web.model.CSCommandRequest;
import com.gdn.x.scheduler.rest.web.model.CSCommandResponse;
import com.gdn.x.scheduler.rest.web.model.CommandRequest;
import com.gdn.x.scheduler.rest.web.model.CommandResponse;
import com.gdn.x.scheduler.service.common.helper.impl.CommonUtil;
import com.gdn.x.scheduler.service.helper.receiver.CommandReceiver;

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
public class CSCommandReceiverImpl implements CommandReceiver {
	
	private Command command;
	
	/**
	 * default constructor
	 */
	public CSCommandReceiverImpl() {
		super();
	}

	/**
	 * The constructor for this class that receives
	 * command entity as the parameter.
	 * 
	 * @param command
	 */
	public CSCommandReceiverImpl(Command command) {
		this.command = command;
	}

	/**
	 * build and return CommandResponse object based on the given command type.
	 * 
	 * @return instance of CommandResponse.
	 * @throws Exception
	 */
	@Override
	public CommandResponse wrapCommand() throws Exception {
		CSCommandResponse commandResponse = new CSCommandResponse();
		commandResponse.setId(command.getId());
		commandResponse.setCommandType(CommandType.CLIENT_SDK.name());
		commandResponse.setCreatedBy(command.getCreatedBy());
		commandResponse.setCreatedDate(command.getCreatedDate());
		commandResponse.setStoreId(command.getStoreId());
		commandResponse.setUpdatedBy(command.getUpdatedBy());
		commandResponse.setUpdatedDate(command.getUpdatedDate());
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			//JsonFactory factory = mapper.getJsonFactory();
			JsonFactory factory = mapper.getFactory();
			//JsonParser jp = factory.createJsonParser(command.getCommand());
			JsonParser jp = factory.createParser(command.getCommand());
			JsonNode actualObj = mapper.readTree(jp);
				
			commandResponse.setEntryPoint(actualObj.get(ClientSDKRequestField.ENTRY_POINT.label()).asText());
		} catch (JsonParseException e) {			
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		
		return commandResponse;
	}

	@Override
	public Command convertToCommand(CommandRequest commandRequest)
			throws Exception {
		ClientSDKCommand csCommand = new ClientSDKCommand();
		csCommand.setId(((commandRequest.getId() != null && !commandRequest.getId().isEmpty())
				? commandRequest.getId() : null));
		CSCommandRequest csCommandRequest = (CSCommandRequest) commandRequest;
		String uploadedDir = (CommonUtil.getUploadDir() == null ? "" : CommonUtil.getUploadDir());
		csCommand.setCommand("{\"" + ClientSDKRequestField.ENTRY_POINT.label()
				+ "\":\""
				+ uploadedDir + "/"  								
				+ csCommandRequest.getEntryPoint() 
				+ "\"}");
		csCommand.setCommandType(CommandType.CLIENT_SDK);
		csCommand.setCreatedBy(csCommandRequest.getSubmittedBy());
		csCommand.setCreatedDate(csCommandRequest.getSubmittedOn());
		csCommand.setMarkForDelete(false);
		csCommand.setStoreId(csCommandRequest.getStoreId());
		
		return csCommand;		
	}
}