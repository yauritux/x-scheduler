package com.gdn.x.scheduler.service.schedengine;


/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public interface CoreEngine<T> {

	public void scheduleJob(T entity);
}