package com.gdn.x.scheduler.service.schedengine.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gdn.x.scheduler.service.domain.TaskCommandService;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
@Service("taskMaintenanceService")
@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
public class TaskMaintenanceService {
	
	private TaskCommandService taskCommandService;
	
	@Autowired
	public TaskMaintenanceService(SchedulerFactoryBean schedulerFactory, TaskCommandService taskCommandService) {
		this.taskCommandService = taskCommandService;
	}

	@Async
	public void run() {
		taskCommandService.deleteExpiredTasks(new Date());
	}
}
