package com.gdn.x.scheduler.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

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
import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.rest.web.model.TaskRequest;
import com.gdn.x.scheduler.rest.web.model.TaskResponse;
import com.gdn.x.scheduler.service.domain.TaskCommandService;
import com.gdn.x.scheduler.service.domain.TaskQueryService;
import com.wordnik.swagger.annotations.Api;

/**
 * 
 * @author yauritux
 *
 */
@RestController
@Api(value = "task", description = "Task Service API")
@RequestMapping(value = "/task", 
	produces = { 
		MediaType.APPLICATION_JSON_VALUE, 
		MediaType.APPLICATION_XML_VALUE 
})
public class TaskController {

	private static final Logger LOG = LoggerFactory.getLogger(TaskController.class);
	
	private TaskCommandService taskCommandService;
	private TaskQueryService taskQueryService;
	
	@Autowired
	public TaskController(TaskCommandService taskCommandService, TaskQueryService taskQueryService) {
		this.taskCommandService = taskCommandService;
		this.taskQueryService = taskQueryService;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public GdnRestSingleResponse<TaskResponse> getTask(@PathVariable("id") String id,
			@RequestParam String storeId, @RequestParam String requestId) {
		try {
			Task task = taskQueryService.findById(id);
			if (task != null) {
				return new GdnRestSingleResponse<TaskResponse>(
						taskQueryService.wrapTask(task), requestId);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		
		return null;
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public GdnRestListResponse<TaskResponse> fetchTasks(
			@RequestParam String storeId, @RequestParam String requestId,
			@RequestParam(defaultValue = "0") Integer pageNumber,
			@RequestParam(defaultValue = "30") Integer pageSize) 
					throws ReflectiveOperationException, NullPointerException, Exception {
		List<TaskResponse> taskResponses = new ArrayList<TaskResponse>();
		
		PageMetaData pageMetaData = null;
		
		try {
			Page<Task> taskList = taskQueryService.fetchAll(pageNumber, pageSize);
			
			List<Task> tasks = taskList.getContent();
			
			LOG.debug("total tasks found: " + (tasks != null ? tasks.size() : 0));
			
			for (Task task : tasks) {
				taskResponses.add((TaskResponse) taskQueryService.wrapTask(task));
			}
			
			pageMetaData = new PageMetaData(pageSize, pageNumber, taskList.getTotalElements());
			
		} catch (NullPointerException e) {
			LOG.error(e.getMessage() ,e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw e;
		}
		
		return new GdnRestListResponse<TaskResponse>(taskResponses, pageMetaData, requestId);		
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public GdnBaseRestResponse submitTask(@Valid @RequestBody TaskRequest taskRequest,
			BindingResult result, @RequestParam String storeId, @RequestParam String requestId) {
		
		if (result.hasErrors()) {
			return errorValidation(result);
		}				
		
		Task task = taskCommandService.buildTaskFromRequest(taskRequest);
		taskCommandService.save(task);
		
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