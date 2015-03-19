package com.gdn.x.scheduler.service.domain.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gdn.x.scheduler.constant.SchedulerIntervalUnit;
import com.gdn.x.scheduler.constant.ThreadState;
import com.gdn.x.scheduler.dao.TaskDAO;
import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.rest.web.model.TaskRequest;
import com.gdn.x.scheduler.service.domain.CommandQueryService;
import com.gdn.x.scheduler.service.domain.TaskCommandService;
import com.gdn.x.scheduler.service.schedengine.CoreEngine;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 * 
 * Service class that contains all logics to manipulate the Task data.
 * Basically, this class represents command layer service of CQRS pattern.
 *
 */
@Service("taskCommandService")
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class TaskCommandServiceImpl implements TaskCommandService {
	
	private static final Logger LOG = LoggerFactory.getLogger(TaskCommandServiceImpl.class);
	
	private CommandQueryService commandQueryService;
	private TaskDAO taskDAO;
	
	private CoreEngine<Task> schedulerEngine;
	
	@Autowired
	public TaskCommandServiceImpl(CommandQueryService commandQueryService, 
			CoreEngine<Task> schedulerEngine, TaskDAO taskDAO) { 
		this.commandQueryService = commandQueryService;
		this.schedulerEngine = schedulerEngine;
		this.taskDAO = taskDAO;
	}

	/**
	 * Save/Persist task into the database.
	 * 
	 * @param task to be saved.
	 * @return the saved entity
	 */	
	@Override
	public Task save(Task entity) {
		if (entity == null || entity.getCommand() == null || entity.getCommand().getCommandType() == null) {
			return null;
		}
		
		if (entity.getDayOfWeek().equalsIgnoreCase("*") && entity.getDayOfMonth().equalsIgnoreCase("*")) {
			entity.setDayOfWeek("?");
		}
		
		Task task = taskDAO.save(entity);
		
		schedulerEngine.scheduleJob(task);
				
		return task;
	}

	/**
	 * Use this method to delete the task from the system (temporary deleted).
	 * Note that the deleted task will be still existed in the database, 
	 * and merely hidden from the system (flagged by markForDelete = true)
	 * 
	 * @param id the task id.
	 * @return true for successful operation, otherwise false is returned. 
	 */	
	@Override
	public boolean delete(Task entity) {
		if (entity == null || entity.isMarkForDelete()) {
			return false;
		}
		try {
			int affected = taskDAO.deleteTask(entity.getId());
			if (affected == 0) {
				return false;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	/**
	 * Use this method to restore the task that previously has been deleted 
	 * with <code>deleteTask</code> method. 
	 * Once the task is restored, it can be seen again (will be displayed) in the system.
	 * 
	 * @param id the task id.
	 * @return true for successful operation, otherwise false is returned.
	 */	
	@Override
	public boolean restore(Task entity) {
		if (entity == null || !entity.isMarkForDelete()) {
			return false;
		}
		try {
			int affected = taskDAO.restoreTask(entity.getId());
			if (affected == 0) {
				return false;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	/**
	 * execute delete as a batch operation (i.e. performed to more than one object).
	 * 
	 * @param list of object/entity to be deleted.
	 * @return number of deleted records.
	 */	
	@Override
	public int batchDelete(List<Task> entities) {
		if (entities == null || entities.isEmpty()) {
			return 0;
		}
		int affected = 0;
		try {
			for (Task task : entities) {
				if (task == null || task.isMarkForDelete()) {
					continue;
				}
				affected += taskDAO.deleteTask(task.getId());
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return 0;
		}
		return affected;
	}

	/**
	 * execute restore as a batch operation (i.e. performed to more than one object).
	 * 
	 * @param list of object/entity to be restored.
	 * @return number of restored objects.
	 */	
	@Override
	public int batchRestore(List<Task> entities) {
		if (entities == null || entities.isEmpty()) {
			return 0;
		}
		int affected = 0;
		try {
			for (Task task : entities) {
				if (task == null || !task.isMarkForDelete()) {
					continue;
				}
				affected += taskDAO.restoreTask(task.getId());
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return 0;
		}
		return affected;
	}
	
	/**
	 * Delete tasks those have already expired.
	 * 
	 * @param date of expired
	 * @return number of deleted tasks.
	 */
	@Override
	public int deleteExpiredTasks(Date date) {
		List<Task> expiredTasks = taskDAO.findExpiredTasks(date);
		int affectedTasks = 0;
		if (expiredTasks != null) {
			try {				
				for (Task task : expiredTasks) {
					taskDAO.deleteTask(task.getId());
					
					System.out.println("processing expired task " + task.getTaskName());
					
					JobDetail jobDetail = schedulerEngine.getSchedulerFactory().getScheduler()
							.getJobDetail(new JobKey(task.getId() + "-JOB", task.getCommand().getCommandType().name()));
					
					schedulerEngine.getSchedulerFactory().getScheduler().deleteJob(jobDetail.getKey());
										
					affectedTasks++;
				}				
			} catch (SchedulerException schedulerException) {
				LOG.error(schedulerException.getMessage(), schedulerException);
				schedulerException.printStackTrace();
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				e.printStackTrace();
			}
		}
		
		if (affectedTasks > 0) {
			System.out.println(affectedTasks + " expired tasks successfully deleted and removed from the scheduler.");			
		}
				
		return affectedTasks;
	}

	/**
	 * build task object from user's HTTP request. 
	 * @param request
	 * @return new task
	 */
	@Override
	public Task buildTaskFromRequest(TaskRequest request) {
		Task task = new Task();
		task.setTaskName(request.getTaskName());
		task.setCommand(commandQueryService.findById(request.getCommandId()));
		
		//TODO: use decorator pattern later
		if (SchedulerIntervalUnit.SECONDS.name().equalsIgnoreCase(request.getIntervalUnit())) {
			task.setSeconds(request.getSeconds() + "/" + request.getInterval());
		} else {
			task.setSeconds(request.getSeconds());
		}
		if (request.getIntervalUnit().equalsIgnoreCase(SchedulerIntervalUnit.MINUTES.name())) {
			task.setMinutes(request.getMinutes() + "/" + request.getInterval());
		} else {
			task.setMinutes(request.getMinutes());
		}
		if (request.getIntervalUnit().equalsIgnoreCase(SchedulerIntervalUnit.HOUR.name())) {
			task.setHours(request.getHour() + "/" + request.getInterval());
		} else {
			task.setHours(request.getHour());
		}		
		if (request.getIntervalUnit().equalsIgnoreCase(SchedulerIntervalUnit.DAY_OF_MONTH.name())) {
			task.setDayOfMonth(request.getDayOfMonth() + "/" + request.getInterval());
		} else {
			task.setDayOfMonth(request.getDayOfMonth());
		}				
		if (request.getIntervalUnit().equalsIgnoreCase(SchedulerIntervalUnit.MONTH.name())) {
			task.setMonth(request.getMonth() + "/" + request.getInterval());
		} else {
			task.setMonth(request.getMonth());
		}						
		if (request.getIntervalUnit().equalsIgnoreCase(SchedulerIntervalUnit.DAY_OF_WEEK.name())) {
			task.setDayOfWeek(request.getDayOfWeek() + "/" + request.getInterval());
		} else {
			task.setDayOfWeek(request.getDayOfWeek());
		}						
		if (request.getIntervalUnit().equalsIgnoreCase(SchedulerIntervalUnit.YEAR.name())) {
			task.setYear(request.getYear() + "/" + request.getInterval());
		} else {
			task.setYear(request.getYear());
		}						
		//end of TODO
		
		//task.setThreadRunningPolicy(ThreadRunningPolicy.valueOf(request.getThreadRunningPolicy()));
		task.setCreatedBy(request.getSubmittedBy());
		task.setCreatedDate(new Date());
		task.setMarkForDelete(false);
		task.setStoreId(request.getStoreId());
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (request.getStartDate() != null && !request.getStartDate().isEmpty()) {
				task.setStartDate(df.parse(request.getStartDate()));				
			} else {
				task.setStartDate(new Date());
			}
			
			if (request.getExpiryDate() != null && !request.getExpiryDate().isEmpty()) {			
				task.setExpiryDate(df.parse(request.getExpiryDate()));
			}
		} catch (java.text.ParseException pe) {
			LOG.error(pe.getMessage(), pe);
			pe.printStackTrace();
			task.setStartDate(new Date());
			task.setExpiryDate(null);
		}
		
		task.setPriority(request.getPriority());
		
		task.setState(ThreadState.ACTIVE);
		
		return task;
	}

	@Override
	public int updateTaskState(ThreadState state, String taskId) {
		return taskDAO.updateTaskState(state, taskId);
	}

	@Override
	public int updateTaskRunningMachine(String machineId, String taskId) {
		return taskDAO.updateTaskMachineId(machineId, taskId);
	}		
}