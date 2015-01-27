package com.gdn.x.scheduler.service.helper.receiver.impl;

import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.constant.WSRequestHeader;
import com.gdn.x.scheduler.model.Command;
import com.gdn.x.scheduler.rest.web.model.CommandResponse;
import com.gdn.x.scheduler.rest.web.model.WSCommandResponse;
import com.gdn.x.scheduler.service.helper.receiver.CommandReceiver;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public class WSCommandReceiverImpl implements CommandReceiver {
	
	private Command command;
	
	public WSCommandReceiverImpl(Command command) {
		this.command = command;
	}

	@Override
	public CommandResponse wrapCommand() throws Exception {
		WSCommandResponse wsCommandResponse = new WSCommandResponse();
		wsCommandResponse.setId(command.getId());
		wsCommandResponse.setCommandType(CommandType.WEB_SERVICE.name());
		wsCommandResponse.setCreatedBy(command.getCreatedBy());
		wsCommandResponse.setCreatedDate(command.getCreatedDate());
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getJsonFactory();
			JsonParser jp = factory.createJsonParser(command.getCommand());
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
}