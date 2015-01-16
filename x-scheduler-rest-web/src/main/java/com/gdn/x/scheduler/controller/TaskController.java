package com.gdn.x.scheduler.controller;

import javax.ws.rs.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gdn.common.web.wrapper.response.GdnRestSingleResponse;
import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.rest.web.model.TaskResponse;
import com.gdn.x.scheduler.service.TaskQueryService;
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
	
	private TaskQueryService taskQueryService;
	
	@Autowired
	public TaskController(TaskQueryService taskQueryService) {
		this.taskQueryService = taskQueryService;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public GdnRestSingleResponse<TaskResponse> getTask(@PathParam("id") String id,
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
}