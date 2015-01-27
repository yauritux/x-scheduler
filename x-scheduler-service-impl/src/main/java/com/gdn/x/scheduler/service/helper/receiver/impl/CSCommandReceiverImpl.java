package com.gdn.x.scheduler.service.helper.receiver.impl;

import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import com.gdn.x.scheduler.constant.ClientSDKRequestField;
import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.model.Command;
import com.gdn.x.scheduler.rest.web.model.CSCommandResponse;
import com.gdn.x.scheduler.rest.web.model.CommandResponse;
import com.gdn.x.scheduler.service.helper.receiver.CommandReceiver;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public class CSCommandReceiverImpl implements CommandReceiver {
	
	private Command command;
	
	public CSCommandReceiverImpl(Command command) {
		this.command = command;
	}

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
			JsonFactory factory = mapper.getJsonFactory();
			JsonParser jp = factory.createJsonParser(command.getCommand());
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
}