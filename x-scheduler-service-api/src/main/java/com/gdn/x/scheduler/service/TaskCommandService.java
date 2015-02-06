package com.gdn.x.scheduler.service;

import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.rest.web.model.TaskRequest;
import com.gdn.x.scheduler.service.base.BaseCommandService;


/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public interface TaskCommandService extends BaseCommandService<Task> {
	
	public Task buildTaskFromRequest(TaskRequest request);
	public void executeTask(Task task) throws Exception;
}