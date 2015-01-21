package com.gdn.x.scheduler.service;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public interface CommandCommandService {

	public int deleteCommand(String id);
	public int restoreCommand(String id);
}
