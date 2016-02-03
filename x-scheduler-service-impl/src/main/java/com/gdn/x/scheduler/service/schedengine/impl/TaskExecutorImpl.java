package com.gdn.x.scheduler.service.schedengine.impl;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gdn.x.scheduler.constant.ProcessStatus;
import com.gdn.x.scheduler.constant.ThreadState;
import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.model.TaskExecution;
import com.gdn.x.scheduler.service.common.helper.impl.CommonUtil;
import com.gdn.x.scheduler.service.domain.CommandQueryService;
import com.gdn.x.scheduler.service.domain.TaskCommandService;
import com.gdn.x.scheduler.service.domain.TaskExecutionCommandService;
import com.gdn.x.scheduler.service.helper.factory.impl.CommandFactory;
import com.gdn.x.scheduler.service.helper.receiver.CommandReceiver;
import com.gdn.x.scheduler.service.schedengine.TaskExecutor;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
@Component("taskExecutor")
@Scope("prototype")
public class TaskExecutorImpl implements TaskExecutor {

	private static final Logger LOG = LoggerFactory.getLogger(TaskExecutorImpl.class);
	
	private CommandQueryService commandQueryService;
	private TaskCommandService taskCommandService;
	private TaskExecutionCommandService taskExecutionCommandService;
	private Task task;

	@Autowired
	public TaskExecutorImpl(CommandQueryService commandQueryService,
			TaskCommandService taskCommandService,
			TaskExecutionCommandService taskExecutionCommandService) {
		this.commandQueryService = commandQueryService;
		this.taskCommandService = taskCommandService;
		this.taskExecutionCommandService = taskExecutionCommandService;
	}

	@Override
	public void setTask(Task task) {
		this.task = task;
	}
	
	@Override
	public void run() {
		if (task == null || task.getCommand() == null) {
			LOG.error("No Task to be executed...");
			return;
		}
		
		if (task.isMarkForDelete()) {
			LOG.error("Task " + task.getTaskName() + " is marked as delete. Cancel to run.");
			return;
		}
		
		if (task.getState() == ThreadState.RUNNING) {
			System.out.println("Task " + task.getTaskName() + " is still running on another thread/machine. Pending for now and will be examined again later.");
			LOG.error("Task " + task.getTaskName() + " is still running on another thread/machine. Pending for now and will be examined again later.");
			return;
		}
		
		System.out.println("Running Task " + task.getTaskName() + " [" + task.getCommand().getCommandType().name() + "]");

		TaskExecution taskExecution = null;
		
		try {
			task.setMachineId(CommonUtil.getMachineId() == null ? "NOT-SET" : CommonUtil.getMachineId());
			taskExecution = taskExecutionCommandService.createTaskExecutionFromTask(task, true);
			taskCommandService.updateTaskRunningMachine(task.getMachineId(), task.getId());
			taskCommandService.updateTaskState(ThreadState.RUNNING, task.getId()); // always update (persisted) task whenever state is changed.
			
			CommandReceiver commandReceiver = CommandFactory.getCommandReceiverFromEntity(task.getCommand());
			commandReceiver.setCommandQueryService(commandQueryService);
			commandReceiver.executeCommand();			
			
			taskExecution.setStatus(ProcessStatus.FINISHED);
			taskExecution.setEnd(new Date());
			taskExecutionCommandService.save(taskExecution);			
			taskCommandService.updateTaskState(ThreadState.SCHEDULED, task.getId());		
			
		} catch (InterruptedException ie) { 
			taskExecutionErrorHandling(taskExecution, task, ie);
		} catch (IOException ioe) {
			taskExecutionErrorHandling(taskExecution, task, ioe);
		} catch (Exception e) {
			taskExecutionErrorHandling(taskExecution, task, e);
		}		
	}
	
	private final void taskExecutionErrorHandling(TaskExecution taskExecution, Task task, Exception e) {
		taskCommandService.updateTaskState(ThreadState.TERMINATED, task.getId()); // means task has stopped unexpectedly due to some exceptions 
		
		if (taskExecution != null) {
			taskExecution.setEnd(new Date());
			taskExecution.setStatus(ProcessStatus.FAILED);
			taskExecutionCommandService.save(taskExecution);
		}
		
		LOG.error(e.getMessage(), e);			
		e.printStackTrace();		
	}
}