package com.gdn.x.scheduler.service.init.impl;

import java.util.List;

import javax.annotation.PreDestroy;

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
 */
@Component("startupHouseKeeper")
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class StartupHouseKeeper implements ApplicationStartup {
	
	private static boolean TASKS_LOADED = false;
	
	private CoreEngine schedulerEngine;
	private TaskQueryService taskQueryService;
	
	@Autowired
	public StartupHouseKeeper(CoreEngine schedulerEngine, TaskQueryService taskQueryService) {
		this.schedulerEngine = schedulerEngine;
		this.taskQueryService = taskQueryService;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		System.out.println("onApplicationEvent::START");
		
		System.out.println("schedulerEngine = " + schedulerEngine);
		System.out.println("taskQueryService = " + taskQueryService);
		System.out.println("all tasks have been loaded = " + TASKS_LOADED);
		
		if (!TASKS_LOADED) {			
			List<Task> tasks = taskQueryService.fetchAll();
			System.out.println("total tasks fetched from db: " + (tasks != null ? tasks.size() : 0));
			
			for (Task task : tasks) {
				schedulerEngine.scheduleTask(task);
			}
			
			TASKS_LOADED = true;
		}
		
		System.out.println("onApplicationEvent::END");
	}

	@Override
	@PreDestroy
	public void onDestroy() {
		TASKS_LOADED = false;
	}
}