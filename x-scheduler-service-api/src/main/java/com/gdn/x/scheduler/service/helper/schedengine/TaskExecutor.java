package com.gdn.x.scheduler.service.helper.schedengine;

import com.gdn.x.scheduler.model.Task;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public interface TaskExecutor {
	
	public void setTask(Task task);
	public void run();
}
