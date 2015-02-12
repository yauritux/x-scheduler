package com.gdn.x.scheduler.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.model.WebServiceCommand;
import com.gdn.x.scheduler.rest.web.model.TaskRequest;
import com.gdn.x.scheduler.service.domain.TaskCommandService;
import com.gdn.x.scheduler.service.domain.TaskQueryService;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public class TaskControllerTest {
	
	private static final String STORE_ID = "store-123";
	private static final String REQUEST_ID = "7348344OXBC255";

	@Mock
	TaskCommandService taskCommandService;
	
	@Mock
	TaskQueryService taskQueryService;
	
	MockMvc mockMvc;
	
	@Before
	public void setup() {
		initMocks(this);
		
		TaskController taskController = new TaskController(taskCommandService, taskQueryService);
		mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
	}
	
	@Test(timeout = 1000)
	public void getTask_taskNotFound_nullIsReturned() throws Exception {
		//@formatter:off
		mockMvc.perform(get("/task/1")
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
		).equals(null);
		//@formatter:on
	}
	
	@Test(timeout = 1000)
	public void getTask_taskFound_responseStatusIsOK() throws Exception {
		when(taskQueryService.findById(any(String.class))).thenReturn(buildTaskSample("1"));
		//@formatter:off
		mockMvc.perform(get("/task/1")
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
		).andExpect(status().isOk());
		//@formatter:on
	}
	
	@Test(timeout = 1000)
	public void getTask_taskFound_responseContainsRequestId() throws Exception {
		when(taskQueryService.findById(any(String.class))).thenReturn(buildTaskSample("1"));
		//@formatter:off
		mockMvc.perform(get("/task/1")
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
		).andDo(print())
		.andExpect(jsonPath("$requestId", equalTo(REQUEST_ID)));
		//@formatter:on
	}
	
	@Test(timeout = 1000)
	public void fetchTasks_emptyRecords_noRecordsFound() throws Exception {
		//@formatter:off
		when(taskQueryService.fetchAll(any(Integer.class), any(Integer.class)))
			.thenReturn(new PageImpl<>(new ArrayList<Task>()));
		
		mockMvc.perform(get("/task")
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
				.param("pageNumber", String.valueOf(0))
				.param("pageSize", String.valueOf(30))
		).andDo(print())
		.andExpect(jsonPath("$pageMetaData.totalRecords", equalTo(0)));
		//@formatter:on
	}
	
	@Test(timeout = 1000, expected = NestedServletException.class)
	public void fetchTasks_nullRecords_NPERaised() throws Exception {
		//@formatter:off
		when(taskQueryService.fetchAll(any(Integer.class), any(Integer.class)))
			.thenReturn(null);
		mockMvc.perform(get("/task")
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
				.param("pageNumber", String.valueOf(0))
				.param("pageSize", String.valueOf(30))
		);
		//@formatter:on
	}
	
	@Test(timeout = 1000)
	public void submitTask_everythingNormal_successIsTrue() throws Exception {
		TaskRequest request = buildTaskRequestSample("1");
		
		//@formatter:off
		mockMvc.perform(post("/task")
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
	public void submitTask_everythingNormal_saveMethodShouldBeCalled() throws Exception {
		TaskRequest request = buildTaskRequestSample("1");
		
		//@formatter:off
		mockMvc.perform(post("/task")
				.content(getJsonAsByte(request))
				.contentType(MediaType.APPLICATION_JSON)
				.param("storeId", STORE_ID)
				.param("requestId", REQUEST_ID)
				.param("version", String.valueOf(1L))				
		).andDo(print());		
		//@formatter:on
		
		verify(taskCommandService, atLeastOnce()).save(any(Task.class));
	}
	
	private Task buildTaskSample(String id) {
		Task task = new Task();
		task.setId(id);
		task.setTaskName("Fraud Checking");
		task.setCommand(buildWSCommandSample(id));
		task.setMarkForDelete(false);
		return task;
	}
	
	private WebServiceCommand buildWSCommandSample(String id) {
		WebServiceCommand wsCommand = new WebServiceCommand();
		wsCommand.setId(id);
		wsCommand.setCommandType(CommandType.WEB_SERVICE);
		wsCommand.setMarkForDelete(false);
		return wsCommand;
	}
	
	private TaskRequest buildTaskRequestSample(String id) {
		TaskRequest request = new TaskRequest();
		request.setId(id);
		request.setCommandId(id);
		request.setStoreId(STORE_ID);
		request.setSubmittedBy("yauritux");
		request.setSubmittedOn(new Date());
		request.setTaskName("Fraud Checking");
		
		return request;
	}
	
	private byte[] getJsonAsByte(Object object) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsBytes(object);
	}	
}
