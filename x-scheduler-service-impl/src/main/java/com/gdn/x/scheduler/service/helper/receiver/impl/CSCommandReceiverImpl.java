package com.gdn.x.scheduler.service.helper.receiver.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdn.x.scheduler.constant.ClientSDKExtension;
import com.gdn.x.scheduler.constant.ClientSDKRequestField;
import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.model.ClientSDKCommand;
import com.gdn.x.scheduler.model.Command;
import com.gdn.x.scheduler.rest.web.model.CSCommandRequest;
import com.gdn.x.scheduler.rest.web.model.CSCommandResponse;
import com.gdn.x.scheduler.rest.web.model.CommandRequest;
import com.gdn.x.scheduler.rest.web.model.CommandResponse;
import com.gdn.x.scheduler.service.common.helper.impl.CommonUtil;
import com.gdn.x.scheduler.service.domain.CommandQueryService;
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
	
	private static final Logger LOG = LoggerFactory.getLogger(CSCommandReceiverImpl.class);
	
	private Command command;
	private CommandQueryService commandQueryService;
	
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
			JsonFactory factory = mapper.getFactory();
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
	
	@Override
	public void executeCommand() throws Exception {
		System.out.println("Calling CLIENT_SDK...");
		
		try {
			CSCommandResponse clientSDK = (CSCommandResponse) commandQueryService.wrapCommand(command);
			
			String[] fileParts = clientSDK.getEntryPoint().split("\\.");
			String extension = fileParts[fileParts.length - 1];
			Process p = null;
			
			if (extension.equalsIgnoreCase(ClientSDKExtension.JAR.extension())) {
				p = Runtime.getRuntime().exec("java -jar " + clientSDK.getEntryPoint());
			} else if (extension.equalsIgnoreCase(ClientSDKExtension.SHELL_SCRIPT.extension())) {
				p = Runtime.getRuntime().exec("bash " + clientSDK.getEntryPoint());
			} else {
				throw new Exception("Unrecognized Task's Command.");
			}
			p.waitFor();
			int exitCode = p.exitValue();
			System.out.println("Done with exit code = " + exitCode);
			
			if (exitCode != 0) {
				throw new RuntimeException("Failed to execute ClientSDK. Exit Code: "
						+ exitCode);
			}	
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			e.printStackTrace();
			throw e;
		}
	}
}