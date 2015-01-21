package com.gdn.x.scheduler.service;


/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public interface TaskCommandService {

	public int deleteTask(String id);
	public int restoreTask(String id);
}