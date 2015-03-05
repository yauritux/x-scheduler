package com.gdn.x.scheduler.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.constant.WSMethod;
import com.gdn.x.scheduler.constant.WSRequestHeader;
import com.gdn.x.scheduler.model.ClientSDKCommand;
import com.gdn.x.scheduler.model.Command;
import com.gdn.x.scheduler.model.WebServiceCommand;
import com.gdn.x.scheduler.rest.web.model.WSCommandRequest;
import com.gdn.x.scheduler.rest.web.model.WSCommandResponse;
import com.gdn.x.scheduler.service.domain.CommandCommandService;
import com.gdn.x.scheduler.service.domain.CommandQueryService;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public class CommandControllerTest {
	
	private static final String[] URL 
		= { "http://md5.jsontest.com/?text=yauritux", 
		"http://api.geonames.org/citiesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&lang=de&username=demo" };
	private static final String STORE_ID = "store-123";
	private static final String REQUEST_ID = "7348344OXBC234";

	@Mock
	CommandQueryService commandQueryService;
	
	@Mock
	CommandCommandService commandActionService;
	
	MockMvc mockMvc;
	
	@Before
	public void setup() {
		initMocks(this);
		
		CommandController commandController = new CommandController(
				commandQueryService, commandActionService);
		mockMvc = MockMvcBuilders.standaloneSetup(commandController).build();
	}
	
	@Test(timeout = 1000)
	public void getWSCommand_notWebService_nullIsReturned() throws Exception {
		when(commandQueryService.findById("2")).thenReturn(buildCSCommand());
		//@formatter:off
		mockMvc.perform(get("/command/ws/2")
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
		).equals(null);
		//@formatter:on
	}
	
	@Test(timeout = 1000)
	public void getWSCommand_nullCommandResponse_nullIsReturned() throws Exception {
		when(commandQueryService.findById("1")).thenReturn(buildWSCommand("1", URL[0]));
		when(commandQueryService.wrapCommand(any(Command.class))).thenReturn(null);
		//@formatter:off
		mockMvc.perform(get("/command/ws/1")
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
		).equals(null);
		//@formatter:on
	}
	
	@Test(timeout = 1000)
	public void getWSCommand_everythingNormal_responseStatusIsOK() throws Exception {
		Command command = buildWSCommand("1", URL[0]);
		when(commandQueryService.findById("1")).thenReturn(command);
		when(commandQueryService.wrapCommand(command)).thenReturn(buildWSCommandResponse(command.getId(), URL[0]));
		//@formatter:off
		mockMvc.perform(get("/command/ws/1")
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
		).andExpect(status().isOk());
		//@formatter:on
	}
	
	@Test(timeout = 1000)
	public void getWSCommand_everythingNormal_responseIsJSON() throws Exception {
		Command command = buildWSCommand("1", URL[0]);
		when(commandQueryService.findById("1")).thenReturn(command);
		when(commandQueryService.wrapCommand(command)).thenReturn(buildWSCommandResponse(command.getId(), URL[0]));
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
		Command command = buildWSCommand("1", URL[0]);
		when(commandQueryService.findById("1")).thenReturn(command);
		when(commandQueryService.wrapCommand(command)).thenReturn(buildWSCommandResponse(command.getId(), URL[0]));
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
		Command command = buildWSCommand("1", URL[0]);
		when(commandQueryService.findById("1")).thenReturn(command);
		when(commandQueryService.wrapCommand(command)).thenReturn(buildWSCommandResponse(command.getId(), URL[0]));
		//@formatter:off
		mockMvc.perform(get("/command/ws/1")
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
		).andDo(print())
		.andReturn().getResponse().getContentAsString().contains("endPoint");
		//@formatter:on
	}
		
	@SuppressWarnings("unchecked")
	@Test(timeout = 1000)
	public void getWSCommands_everythingNormal_responseStatusIsOK() throws Exception {
		List<? extends Command> wsCommands = populateWSCommands();
		//@formatter:off
		when(commandQueryService.findByCommandType(any(CommandType.class), any(Integer.class), any(Integer.class)))
			.thenReturn((Page<Command>) new PageImpl<>(wsCommands));
		when(commandQueryService.wrapCommand(any(Command.class))).thenReturn(buildWSCommandResponse("1", URL[0]));
		
		mockMvc.perform(get("/command/ws")
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
		).andDo(print())
		.andExpect(status().isOk());
		//@formatter:on
	}
	
	@SuppressWarnings("unchecked")
	@Test(timeout = 1000)
	public void getWSCommands_everythingNormal_twoRecordsFound() throws Exception {
		List<? extends Command> wsCommands = populateWSCommands();
		//@formatter:off
		when(commandQueryService.findByCommandType(any(CommandType.class), any(Integer.class), any(Integer.class)))
			.thenReturn((Page<Command>) new PageImpl<>(wsCommands));
		when(commandQueryService.wrapCommand(any(Command.class))).thenReturn(buildWSCommandResponse("2", URL[1]));
		
		mockMvc.perform(get("/command/ws")
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
				.param("pageNumber", String.valueOf(0))
				.param("pageSize", String.valueOf(30))
		).andDo(print())
		.andExpect(jsonPath("$pageMetaData.totalRecords", equalTo(2)));
		//@formatter:on
	}
	
	@Test(timeout = 1000)
	public void getWSCommands_emptyRecords_noRecordsFound() throws Exception {
		//@formatter:off
		when(commandQueryService.findByCommandType(any(CommandType.class), any(Integer.class), any(Integer.class)))
			.thenReturn(new PageImpl<>(new ArrayList<Command>()));
		
		mockMvc.perform(get("/command/ws")
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
				.param("pageNumber", String.valueOf(0))
				.param("pageSize", String.valueOf(30))
		).andDo(print())
		.andExpect(jsonPath("$pageMetaData.totalRecords", equalTo(0)));
		//@formatter:on
	}
	
	@Test(timeout = 1000, expected = NestedServletException.class)
	public void getWSCommands_nullRecords_NPERaised() throws Exception {
		when(commandQueryService.fetchAll(any(Integer.class), any(Integer.class))).thenReturn(null);
		
		//@formatter:off
		mockMvc.perform(get("/command/ws")
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
				.param("pageNumber", String.valueOf(0))
				.param("pageSize", String.valueOf(30)));
		//@formatter:on
	}
	
	@Test(timeout = 1000)
	public void submitCommand_everythingNormal_successIsTrue() throws Exception {
		Command command = buildWSCommand("1", URL[0]);
		WSCommandRequest request = buildWSCommandRequest(command);
				
		//@formatter:off
		mockMvc.perform(post("/command/ws")
				.content(getJsonAsByte(request))
				.contentType(MediaType.APPLICATION_JSON)
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
				.param("version", String.valueOf(1L))
		).andDo(print())
		.andExpect(jsonPath("$success", equalTo(true)));
		//@formatter:on
	}
	
	@Test(timeout = 1000)
	public void submitCommand_everythingNormal_saveMethodShouldBeCalled() throws Exception {
		Command command = buildWSCommand("1", URL[0]);
		WSCommandRequest request = buildWSCommandRequest(command);
		
		//@formatter:off
		mockMvc.perform(post("/command/ws")
				.content(getJsonAsByte(request))
				.contentType(MediaType.APPLICATION_JSON)
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
				.param("version", String.valueOf(1L))
		).andDo(print());
		//@formatter:on
		
		verify(commandActionService).save(any(Command.class));
	}
	
	@Test(timeout = 1000)
	public void submitCommand_everythingNormal_requestIdShouldNotChanged() throws Exception {
		Command command = buildWSCommand("1", URL[0]);
		WSCommandRequest request = buildWSCommandRequest(command);
		
		//@formatter:off
		mockMvc.perform(post("/command/ws")
				.content(getJsonAsByte(request))
				.contentType(MediaType.APPLICATION_JSON)
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
				.param("version", String.valueOf(1L))
		).andDo(print())
		.andExpect(jsonPath("requestId", equalTo(REQUEST_ID)));
		//@formatter:on
		//@formatter:on
	}
	
	private WebServiceCommand buildWSCommand(String id, String URL) {
		WebServiceCommand wsCommand = new WebServiceCommand();
		wsCommand.setId(id);
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
	
	private WSCommandRequest buildWSCommandRequest(Command command) {
		WSCommandRequest request = new WSCommandRequest();
		request.setEndpoint(URL[0]);
		request.setWsMethod(WSMethod.GET.name());
		request.setSubmittedBy("yauritux");
		request.setSubmittedOn(new Date());
		request.setCommandType(command.getCommandType().name());
		request.setContents(((WebServiceCommand) command).getContents());
		request.setId(command.getId());
		request.setParameters(command.getParameters());
		return request;
	}
	
	private WSCommandResponse buildWSCommandResponse(String id, String URL) {
		WSCommandResponse response = new WSCommandResponse();
		response.setCommandType(CommandType.WEB_SERVICE.name());
		response.setCreatedBy("yauritux");
		response.setCreatedDate(new Date());
		response.setEndPoint(URL);
		response.setHttpMethod(WSMethod.GET.name());
		response.setId(id);
		response.setParameters("");
		response.setStoreId(STORE_ID);
		response.setUpdatedBy("yauritux");
		response.setUpdatedDate(new Date());
		
		return response;
	}
	
	private ClientSDKCommand buildCSCommand() {
		ClientSDKCommand csCommand = new ClientSDKCommand();
		csCommand.setId("2");
		csCommand.setCommandType(CommandType.CLIENT_SDK);
		csCommand.setCommand("{\"entry_point\": \"hello.jar\"}");
		csCommand.setCreatedBy("yauritux");
		csCommand.setCreatedDate(new Date());
		csCommand.setStoreId(STORE_ID);
		csCommand.setMarkForDelete(false);
		
		return csCommand;
	}
	
	private List<? extends Command> populateWSCommands() {
		List<WebServiceCommand> commands = new ArrayList<WebServiceCommand>();
		for (int i = 0; i < URL.length; i++) {
			commands.add(buildWSCommand(Integer.toString(i), URL[i]));
		}
		return commands;
	}
	
	private byte[] getJsonAsByte(Object object) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsBytes(object);
	}
}