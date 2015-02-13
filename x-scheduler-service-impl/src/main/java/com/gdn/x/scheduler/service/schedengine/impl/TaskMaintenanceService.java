package com.gdn.x.scheduler.service.schedengine.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.gdn.x.scheduler.service.domain.TaskCommandService;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
@Service("taskMaintenanceService")
@EnableScheduling
public class TaskMaintenanceService {
	
	private TaskCommandService taskCommandService;
	
	@Autowired
	public TaskMaintenanceService(TaskCommandService taskCommandService) {
		this.taskCommandService = taskCommandService;
	}

	@Scheduled(fixedRate = 5000)
	public void run() {
		taskCommandService.deleteExpiredTasks(new Date());
	}
}
