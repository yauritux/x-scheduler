package com.gdn.x.scheduler.service;

import com.gdn.x.scheduler.model.TaskExecution;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 * 
 */
public interface TaskExecutionQueryService {

	public TaskExecution findById(String id);
	public TaskExecution findLastRunningTask(String taskId);
}
