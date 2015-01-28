package com.gdn.x.scheduler.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.constant.WSMethod;
import com.gdn.x.scheduler.constant.WSRequestHeader;
import com.gdn.x.scheduler.model.Command;
import com.gdn.x.scheduler.model.ShellScriptCommand;
import com.gdn.x.scheduler.model.WebServiceCommand;
import com.gdn.x.scheduler.rest.web.model.WSCommandResponse;
import com.gdn.x.scheduler.service.CommandQueryService;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public class CommandControllerTest {
	
	private static final String URL = "http://www.google.com";
	private static final String STORE_ID = "store-123";
	private static final String REQUEST_ID = "7348344OXBC234";

	@Mock
	CommandQueryService commandQueryService;
	
	MockMvc mockMvc;
	
	@Before
	public void setup() {
		initMocks(this);
		
		CommandController commandController = new CommandController(commandQueryService);
		mockMvc = MockMvcBuilders.standaloneSetup(commandController).build();
	}
	
	@Test(timeout = 1000)
	public void getWSCommand_notWebService_nullIsReturned() throws Exception {
		when(commandQueryService.findById("2")).thenReturn(buildSSCommand());
		//@formatter:off
		mockMvc.perform(get("/command/ws/2")
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
		).equals(null);
		//@formatter:on
	}
	
	@Test(timeout = 1000)
	public void getWSCommand_nullCommandResponse_nullIsReturned() throws Exception {
		when(commandQueryService.findById("1")).thenReturn(buildWSCommand());
		when(commandQueryService.wrapCommand(any(Command.class))).thenReturn(null);
		//@formatter:off
		mockMvc.perform(get("/command/ws/1")
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
		).equals(null);
		//@formatter:on
	}
	
	@Test(timeout = 1000)
	public void getWSCommand_everythingNormal_statusResponseIsOK() throws Exception {
		Command command = buildWSCommand();
		when(commandQueryService.findById("1")).thenReturn(command);
		when(commandQueryService.wrapCommand(command)).thenReturn(buildWSCommandResponse());
		//@formatter:off
		mockMvc.perform(get("/command/ws/1")
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
		).andExpect(status().isOk());
		//@formatter:on
	}
	
	@Test(timeout = 1000)
	public void getWSCommand_everythingNormal_responseIsJSON() throws Exception {
		Command command = buildWSCommand();
		when(commandQueryService.findById("1")).thenReturn(command);
		when(commandQueryService.wrapCommand(command)).thenReturn(buildWSCommandResponse());
		//@formatter:off
		mockMvc.perform(get("/command/ws/1")
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
		).andDo(print())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
		//@formatter:on
	}
	
	@Test(timeout = 1000)
	public void getWSCommand_everythingNormal_requestIdDoesNotChange() throws Exception {
		Command command = buildWSCommand();
		when(commandQueryService.findById("1")).thenReturn(command);
		when(commandQueryService.wrapCommand(command)).thenReturn(buildWSCommandResponse());
		//@formatter:off
		mockMvc.perform(get("/command/ws/1")
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
		).andDo(print())
		.andExpect(jsonPath("$requestId", equalTo(REQUEST_ID)));
		//@formatter:on
	}
	
	@Test(timeout = 1000)
	public void getWSCommand_everythingNormal_containsEndPoint() throws Exception {
		Command command = buildWSCommand();
		when(commandQueryService.findById("1")).thenReturn(command);
		when(commandQueryService.wrapCommand(command)).thenReturn(buildWSCommandResponse());
		//@formatter:off
		mockMvc.perform(get("/command/ws/1")
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
		).andDo(print())
		.andReturn().getResponse().getContentAsString().contains("endPoint");
		//@formatter:on
	}
	
	private WebServiceCommand buildWSCommand() {
		WebServiceCommand wsCommand = new WebServiceCommand();
		wsCommand.setId("1");
		wsCommand.setCommandType(CommandType.WEB_SERVICE);
		wsCommand.setCommand("{\"" + WSRequestHeader.URL.label() + "\":\"" + URL + "\",\"" 
				+ WSRequestHeader.METHOD.label() + "\":\""+ WSMethod.GET.name() + "\"}");
		wsCommand.setContents("");
		wsCommand.setCreatedBy("yauritux");
		wsCommand.setCreatedDate(new Date());
		wsCommand.setMarkForDelete(false);
		wsCommand.setParameters("");
		wsCommand.setStoreId(STORE_ID);
		wsCommand.setUpdatedBy("yauritux");
		wsCommand.setUpdatedDate(new Date());
		
		return wsCommand;
	}
	
	private WSCommandResponse buildWSCommandResponse() {
		WSCommandResponse response = new WSCommandResponse();
		response.setCommandType(CommandType.WEB_SERVICE.name());
		response.setCreatedBy("yauritux");
		response.setCreatedDate(new Date());
		response.setEndPoint(URL);
		response.setHttpMethod(WSMethod.GET.name());
		response.setId("1");
		response.setParameters("");
		response.setStoreId(STORE_ID);
		response.setUpdatedBy("yauritux");
		response.setUpdatedDate(new Date());
		
		return response;
	}
	
	private ShellScriptCommand buildSSCommand() {
		ShellScriptCommand ssCommand = new ShellScriptCommand();
		ssCommand.setId("2");
		ssCommand.setCommandType(CommandType.SHELL_SCRIPT);
		ssCommand.setCommand("setup.sh");
		ssCommand.setCreatedBy("yauritux");
		ssCommand.setCreatedDate(new Date());
		ssCommand.setStoreId(STORE_ID);
		ssCommand.setMarkForDelete(false);
		
		return ssCommand;
	}
}