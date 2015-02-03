package com.gdn.x.scheduler.service;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.gdn.x.scheduler.service.impl.TaskCommandServiceImpl;

/*
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
*/

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 * 
 * Class that will schedule every task to be executed as a Job.
 */
//public class TaskJob extends QuartzJobBean {
public class TaskJob {
	
	private TaskCommandService taskCommandService;
	private TaskQueryService taskQueryService;
	private Scheduler scheduler;
	
	@Autowired
	public TaskJob(TaskCommandService taskCommandService,
			TaskQueryService taskQueryService, Scheduler scheduler) throws SchedulerException {
		this.taskCommandService = taskCommandService;
		this.taskQueryService = taskQueryService;
		this.scheduler = scheduler;
		this.scheduler.start();
		runScheduler();
	}

	/*
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {		
		taskCommandService.executeTask(task);
	}
	*/
	
	public void runScheduler() throws SchedulerException {
		JobDetail job = JobBuilder.newJob(TaskCommandServiceImpl.class)
				.withIdentity("TaskScheduler").build();
		
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("TaskTriggerGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule("*/5 * * * * ?")).build();
		
		scheduler.scheduleJob(job, trigger);
	}
}