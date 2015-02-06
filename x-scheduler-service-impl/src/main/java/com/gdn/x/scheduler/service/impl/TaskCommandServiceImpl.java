package com.gdn.x.scheduler.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gdn.common.base.shade.org.apache.http.HttpResponse;
import com.gdn.common.base.shade.org.apache.http.client.methods.HttpGet;
import com.gdn.common.base.shade.org.apache.http.impl.client.DefaultHttpClient;
import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.constant.SchedulerIntervalUnit;
import com.gdn.x.scheduler.constant.ThreadRunningPolicy;
import com.gdn.x.scheduler.constant.WSMethod;
import com.gdn.x.scheduler.dao.TaskDAO;
import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.model.WebServiceCommand;
import com.gdn.x.scheduler.rest.web.model.TaskRequest;
import com.gdn.x.scheduler.rest.web.model.WSCommandResponse;
import com.gdn.x.scheduler.service.CommandQueryService;
import com.gdn.x.scheduler.service.TaskCommandService;

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
	
	@Autowired
	public TaskCommandServiceImpl(CommandQueryService commandQueryService, TaskDAO taskDAO) {
		this.commandQueryService = commandQueryService;
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
		if (entity == null) {
			return null;
		}
		return taskDAO.save(entity);
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
		if (request.getIntervalUnit().equalsIgnoreCase(SchedulerIntervalUnit.SECONDS.name())) {
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
		task.setCreatedDate(request.getSubmittedOn() == null ? new Date() : request.getSubmittedOn());
		task.setMarkForDelete(false);
		task.setStoreId(request.getStoreId());
		return task;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void executeTask(Task task) throws Exception {
		if (task == null || task.getCommand() == null) {
			throw new RuntimeException("No Task to execute...");
		}
		
		if (task.getCommand().getCommandType() == CommandType.WEB_SERVICE) {
			System.out.println("Calling web service...");
			HttpResponse response = null;
			WebServiceCommand command = (WebServiceCommand) task.getCommand();
			WSCommandResponse webService = (WSCommandResponse) commandQueryService.wrapCommand(command);
			@SuppressWarnings({ "resource" })
			DefaultHttpClient httpClient = new DefaultHttpClient();
			if (webService.getHttpMethod().equalsIgnoreCase(WSMethod.GET.name())) {
				StringBuilder strRequest = new StringBuilder();
				strRequest.append(webService.getEndPoint());
				if (webService.getParameters() != null && !webService.getParameters().isEmpty()) {
					strRequest.append("?" + webService.getParameters());
				}
				HttpGet getRequest = new HttpGet(strRequest.toString());
				getRequest.addHeader("accept", "application/json");
				
				response = httpClient.execute(getRequest);
				
				System.out.println("Response Code = " + response.getStatusLine().getStatusCode());
				
				if (response.getStatusLine().getStatusCode() != 200) {
					throw new RuntimeException("Failed to execute WS. Status Code: "
							+ response.getStatusLine().getStatusCode());
				}
			}
			httpClient.getConnectionManager().shutdown();
		}
	}
}