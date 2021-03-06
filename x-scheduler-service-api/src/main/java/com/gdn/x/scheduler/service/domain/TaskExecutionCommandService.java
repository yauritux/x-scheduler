package com.gdn.x.scheduler.service.domain;

import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.model.TaskExecution;
import com.gdn.x.scheduler.service.domain.base.BaseCommandService;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 *
 */
public interface TaskExecutionCommandService extends BaseCommandService<TaskExecution> {
	
	String MACHINE_ID = "MACHINE_ID";
	
	public TaskExecution createTaskExecutionFromTask(Task task, boolean persist);
	public int deleteObsoleteTasks(int interval, int calendarField);
}
