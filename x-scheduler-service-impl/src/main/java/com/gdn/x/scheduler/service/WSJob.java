package com.gdn.x.scheduler.service;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.model.TaskExecution;
import com.gdn.x.scheduler.model.WebServiceCommand;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 *
 */
public class WSJob extends QuartzJobBean {
	
	private static final Logger LOG = LoggerFactory.getLogger(WSJob.class);
	
	/*
	@Autowired
	public void setTaskCommandService(TaskCommandService taskCommandService,
			TaskQueryService taskQueryService) {
		this.taskCommandService = taskCommandService;
		this.taskQueryService = taskQueryService;
	}
	*/
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		//System.out.println("WSJob Scheduler is running now...");
		try {
			/*
			TaskQueryService taskQueryService = (TaskQueryService) context.getJobDetail().getJobDataMap().get("taskQueryService");	
			TaskCommandService taskCommandService = (TaskCommandService) context.getJobDetail().getJobDataMap().get("taskCommandService");
			Task task = taskQueryService.findById("ac4fc20b-824d-48fc-99d7-9694d0e73be1");
			*/
			//System.out.println("taks name = " + (task != null ? task.getTaskName() : "<<empty>>"));
			/*
			if (task != null) {
				taskCommandService.executeTask(task);
			}
			*/
			CommandQueryService commandQueryService = (CommandQueryService) context.getJobDetail().getJobDataMap().get("commandQueryService");
			TaskExecutionCommandService taskExecutionCommandService 
				= (TaskExecutionCommandService) context.getJobDetail().getJobDataMap().get("taskExecutionCommandService");
			TaskQueryService taskQueryService = (TaskQueryService) context.getJobDetail().getJobDataMap().get("taskQueryService");	
			TaskExecutionQueryService taskExecutionQueryService 
				= (TaskExecutionQueryService) context.getJobDetail().getJobDataMap().get("taskExecutionQueryService");
			ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) context.getJobDetail().getJobDataMap().get("taskExecutorObject");
			List<Task> tasks = taskQueryService.fetchAll();
			for (Task task : tasks) {
				TaskExecution taskExecution = taskExecutionQueryService.findLastRunningTask(task.getId());
				if (taskExecution == null) {
					// start immediately
					System.out.println("invoke " + task.getTaskName());
					taskExecutor.execute(new TaskExecutor(commandQueryService, taskExecutionCommandService, task));
				} else {
					// check end time is null or not
					if (taskExecution.getEnd() != null) {
						//task already finish, check whether the time has reached next schedule
						java.util.Calendar end = java.util.Calendar.getInstance();
						java.util.Calendar current = java.util.Calendar.getInstance();
						end.setTime(taskExecution.getEnd());
						int seconds = 0;
						int minutes = 0;
						try {
							seconds = Integer.parseInt(task.getSeconds());
							minutes = Integer.parseInt(task.getMinutes());
						} catch (NumberFormatException nfe) {
						}
						int minInSeconds = minutes * 60;
						seconds += minInSeconds;
						int milliseconds = seconds * 1000;
						System.out.println("current = " + current.getTimeInMillis());
						System.out.println("end = " + end.getTimeInMillis());
						System.out.println("milliseconds = " + milliseconds);
						//current.add(java.util.Calendar.SECOND, -(end.get(java.util.Calendar.SECOND)));
						if (current.getTimeInMillis() - end.getTimeInMillis() >= milliseconds) {
						//if (current.get(java.util.Calendar.SECOND) >= seconds) {
							System.out.println("invoke next task: " + task.getTaskName());
							taskExecutor.execute(new TaskExecutor(commandQueryService, taskExecutionCommandService, task));
						}						
					} else {
						System.out.println("task " + task.getTaskName() + " is still running. Couldn't run in paralel.");
					}
				}
			}
		} catch (Exception e) {
			throw new JobExecutionException(e);
		}
	}
}