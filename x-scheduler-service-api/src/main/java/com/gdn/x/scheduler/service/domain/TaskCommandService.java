package com.gdn.x.scheduler.service.domain;

import java.util.Date;

import com.gdn.x.scheduler.constant.ThreadState;
import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.rest.web.model.TaskRequest;
import com.gdn.x.scheduler.service.domain.base.BaseCommandService;


/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public interface TaskCommandService extends BaseCommandService<Task> {
	
	String MACHINE_ID = "MACHINE_ID";
	String UPLOAD_DIR = "UPLOAD_DIR";
	
	public Task buildTaskFromRequest(TaskRequest request);
	public int deleteExpiredTasks(Date date);
	public int updateTaskState(ThreadState state, String taskId);
	public int updateTaskRunningMachine(String machineId, String taskId);
}