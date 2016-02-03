package com.gdn.x.scheduler.service.schedengine;

import org.springframework.scheduling.quartz.SchedulerFactoryBean;


/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public interface CoreEngine<T> {

	public void scheduleJob(T entity);
	public SchedulerFactoryBean getSchedulerFactory();
}