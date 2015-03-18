package com.gdn.x.scheduler.service.helper.receiver.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdn.common.base.shade.org.apache.http.client.config.RequestConfig;
import com.gdn.common.base.shade.org.apache.http.client.methods.CloseableHttpResponse;
import com.gdn.common.base.shade.org.apache.http.client.methods.HttpGet;
import com.gdn.common.base.shade.org.apache.http.impl.client.CloseableHttpClient;
import com.gdn.common.base.shade.org.apache.http.impl.client.HttpClientBuilder;
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
		wsCommand.setCreatedDate(wsCommandRequest.getSubmittedOn());
		wsCommand.setMarkForDelete(false);
		wsCommand.setStoreId(wsCommandRequest.getStoreId());
		
		return wsCommand;
	}
	
	@Override
	public void executeCommand() throws Exception {
		System.out.println("Calling web service...");
		
		CloseableHttpClient httpClient = null;
		HttpGet getRequest = null;
		CloseableHttpResponse response = null;
		
		try {
			WSCommandResponse webService = (WSCommandResponse) commandQueryService.wrapCommand(command);
			httpClient = HttpClientBuilder.create().build();
			if (webService.getHttpMethod().equalsIgnoreCase(WSMethod.GET.name())) {
				StringBuilder strRequest = new StringBuilder();
				strRequest.append(webService.getEndPoint());
				if (webService.getParameters() != null && !webService.getParameters().isEmpty()) {
					strRequest.append("?" + webService.getParameters());
				}
				RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(11000)
						.setConnectTimeout(11000).setSocketTimeout(11000).build();
				getRequest = new HttpGet(strRequest.toString());
				getRequest.addHeader("accept", "application/json");
				getRequest.setConfig(requestConfig);
				
				response = httpClient.execute(getRequest);
				
				System.out.println("Response Code = " + response.getStatusLine().getStatusCode());
				
				if (response.getStatusLine().getStatusCode() != 200) {
					throw new RuntimeException("Failed to execute WS. Status Code: "
							+ response.getStatusLine().getStatusCode());
				}
			}	
		} catch (Exception e) {
			try {
				if (getRequest != null) {
					getRequest.releaseConnection();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException ioe) {
				LOG.error(ioe.getMessage(), ioe);
				ioe.printStackTrace();
			} catch (Exception e1) {
				LOG.error(e.getMessage(), e1);
				e1.printStackTrace();
			}	
			
			throw e;
		}		
	}
}