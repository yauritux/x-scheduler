package com.gdn.x.scheduler.service.init.impl;

import java.util.List;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.service.domain.TaskQueryService;
import com.gdn.x.scheduler.service.init.ApplicationStartup;
import com.gdn.x.scheduler.service.schedengine.CoreEngine;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 * 
 * This class represents bootstrap service that will be fired up at the beginning 
 * to inject all tasks in the database into the job scheduler engine.
 *
 */
@Component("schedulerBootstrap")
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class SchedulerBootstrap implements ApplicationStartup {
	
	private static final Logger LOG = LoggerFactory.getLogger(SchedulerBootstrap.class);
	
	private static boolean TASKS_LOADED = false;
	
	private CoreEngine<Task> schedulerEngine;
	private TaskQueryService taskQueryService;
	
	/**
	 * Default constructor which takes two service beans in the parameters.
	 * 
	 * @param schedulerEngine
	 * @param taskQueryService
	 */
	@Autowired
	public SchedulerBootstrap(CoreEngine<Task> schedulerEngine, TaskQueryService taskQueryService) {
		this.schedulerEngine = schedulerEngine;
		this.taskQueryService = taskQueryService;
	}

	/**
	 * Invoke periodically by container to check any occured event.
	 * 
	 * @param event
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (!TASKS_LOADED) {			
			List<Task> tasks = taskQueryService.fetchAll();
		    LOG.debug("total tasks fetched from db: " + (tasks != null ? tasks.size() : 0));
			
			for (Task task : tasks) {
				schedulerEngine.scheduleJob(task);
			}
			
			TASKS_LOADED = true;
		}		
	}

	/**
	 * Cleanup method which is executed during the shutdown process of the container.
	 * 
	 */
	@Override
	@PreDestroy
	public void onDestroy() {
		TASKS_LOADED = false;
	}
}