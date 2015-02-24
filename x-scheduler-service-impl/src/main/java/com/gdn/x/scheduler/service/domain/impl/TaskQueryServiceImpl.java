package com.gdn.x.scheduler.service.domain.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gdn.x.scheduler.dao.TaskDAO;
import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.rest.web.model.TaskResponse;
import com.gdn.x.scheduler.service.domain.TaskQueryService;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 * Service Class that contains queries for the Task data.
 * Basically, this class represents Query layer service of CQRS pattern.
 */
@Service("taskQueryService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class TaskQueryServiceImpl implements TaskQueryService {
	
	private TaskDAO taskDAO;
	
	@Autowired
	public TaskQueryServiceImpl(TaskDAO taskDAO) {
		this.taskDAO = taskDAO;
	}

	/**
	 * Used to retrieve all Tasks in the database (exclusive markForDelete).
	 * 
	 * @see <code>findAll()</code> method for comparison.
	 * 
	 * @return list of Task or empty list.
	 */
	@Override
	public List<Task> fetchAll() {
		return taskDAO.fetchAll();
	}

	/**
	 * Used to retrieve all Tasks in the database (exclusive markForDelete).
	 * Use this method if You'd like to apply paging in the result.
	 * 
	 * @see <code>fetchAll(pageNumber, pageSize)</code> method for comparison.
	 * 
	 * @param pageNumber number of current page.
	 * @param pageSize total pages.
	 * @return Page object that contains all tasks for particular page number.
	 */
	@Override
	public Page<Task> fetchAll(int pageNumber, int pageSize) {
		return taskDAO.fetchAll(new PageRequest(pageNumber, pageSize));
	}

	/**
	 * Used to find the Task by it's ID. 
	 * Note that this method will be searching task based on the ID, 
	 * including task that has already been deleted (markForDelete=true).
	 * 
	 * @see <code>findByIdExcl(id)</code> method for comparison.
	 * 
	 * @param id the task ID.
	 * @return task.
	 */
	@Override
	public Task findById(String id) {
		if (id == null || id.trim().length() == 0) {
			return null;
		}
		return taskDAO.findById(id);
	}

	/**
	 * Used to find one or more tasks having name <b>exactly matched</b> the name 
	 * as specified in the query filter (parameter).
	 * This method merely search for tasks with markForDelete=false.
	 * 
	 * @param taskName the name of the tasks.
	 * @return list of tasks those are matched the searching criteria.
	 */
	@Override
	public List<Task> findByTaskName(String taskName) {
		return taskDAO.findByTaskName(taskName != null ? taskName.trim() : taskName);
	}

	/**
	 * Used to find one or more tasks having name <b>exactly matched</b> the name 
	 * as specified in the query filter (parameter).
	 * This method merely search for tasks with markForDelete=false.
	 * Use this method if You'd like to apply paging in the result.
	 * 
	 * @param taskName the name of the tasks.
	 * @param pageNumber number of the current page.
	 * @param pageSize total number of page. 
	 * @return page object contains tasks matched the query filter.
	 */
	@Override
	public Page<Task> findByTaskName(String taskName, int pageNumber,
			int pageSize) {
		return taskDAO.findByTaskName(taskName != null ? taskName.trim() : taskName, 
				new PageRequest(pageNumber, pageSize));
	}

	/**
	 * Used to find one or more tasks having name <b>like</b> (<i>close</i>) with the name 
	 * provided in the query filter (parameter).
	 * This method merely return tasks with markForDelete=false.
	 * 
	 * @param taskName the name of the tasks being searched.
	 * @return list of tasks those are matched the searching criteria.
	 */
	@Override
	public List<Task> findByTaskNameLike(String taskName) {
		if (taskName == null || taskName.trim().length() == 0) {
			return fetchAll();
		}
		return taskDAO.findByTaskNameLike("%" + taskName.trim() + "%");
	}

	/**
	 * Used to find one or more tasks having name <b>like</b> (<i>close</i>) with the name 
	 * provided in the query filter (parameter).
	 * This method merely return tasks with markForDelete=false.
	 * 
	 * @param taskName the name of the tasks being searched.
	 * @param pageNumber number of current page.
	 * @param pageSize total number of page found.
	 * @return page object contains tasks matched the query filter.
	 */
	@Override
	public Page<Task> findByTaskNameLike(String taskName, int pageNumber,
			int pageSize) {
		if (taskName == null || taskName.trim().length() == 0) {
			return fetchAll(pageNumber, pageSize);
		}
		return taskDAO.findByTaskNameLike("%" + taskName.trim() + "%", 
				new PageRequest(pageNumber, pageSize)
	    );
	}

	/**
	 * Use this to wrapping the task entity into TaskResponse object. 
	 * This method is useful in the REST result/response as this method will also return 
	 * a complete object in regards with Task entity (i.e. Command). 
	 * 
	 * @param task 
	 * @return TaskResponse
	 */
	@Override
	public TaskResponse wrapTask(Task task) {
		if (task == null) {
			return null;
		}
		
		TaskResponse taskResponse = new TaskResponse();
		taskResponse.setId(task.getId());
		taskResponse.setCommandId(task.getCommand().getId());
		taskResponse.setCommand(task.getCommand().getCommand());
		taskResponse.setParameters(task.getCommand().getParameters());
		taskResponse.setCommandType(task.getCommand().getCommandType().name());
		taskResponse.setCreatedBy(task.getCreatedBy());
		taskResponse.setCreatedDate(task.getCreatedDate());
		taskResponse.setUpdatedBy(task.getUpdatedBy());
		taskResponse.setUpdatedDate(task.getUpdatedDate());
		
		taskResponse.setStartDate(task.getStartDate());
		taskResponse.setExpiryDate(task.getExpiryDate());
		taskResponse.setMachineId(task.getMachineId());
		if (task.getState() != null) {
			taskResponse.setState(task.getState().name());
		}
		
		return taskResponse;
	}

	/**
	 * Use this method if You'd like to fetch all task records, including the tasks those 
	 * have already marked for delete (i.e. markForDelete = true).
	 *  
	 * @see <code>fetchAll()</code> method.
	 * 
	 * @return list of tasks. 
	 */
	@Override
	public List<Task> findAll() {
		return taskDAO.findAll();
	}

	/**
	 * Use this method if You'd like to fetch all task records, including the tasks those 
	 * have already marked for delete (i.e. markForDelete = true).
	 * Use this method if You'd like to apply paging to the result.
	 * 
	 * @see <code>fetchAll()</code> method.
	 * 
	 * @param pageNumber number of current page.
	 * @param pageSize total number of page found.
	 * @return Page page object contains all tasks in the database (inclusive markForDelete).
	 */
	@Override
	public Page<Task> findAll(int pageNumber, int pageSize) {
		return taskDAO.findAll(new PageRequest(pageNumber, pageSize));
	}

	/**
	 * Similar to <code>findById</code> method, except that this method only find the task that is not being marked 
	 * as delete (i.e. markForDelete = false).
	 * 
	 * @see <code>findById()</code> method.
	 * 
	 * @param id the task ID.
	 * @return task
	 */
	@Override
	public Task findByIdExcl(String id) {
		if (id == null || id.trim().length() == 0) {
			return null;
		}
		return taskDAO.findByIdExclDelete(id);
	}
	
	/**
	 * find all tasks those already expired.
	 * @param expiryDate date of expired
	 * @return tasks those already expired.
	 */
	@Override
	public List<Task> findExpiredTasks(Date expiryDate) {
		if (expiryDate == null) {
			return null;
		}
		return taskDAO.findExpiredTasks(expiryDate);
	}

	/**
	 * check whether the task with particular ID is exist or not in the database. 
	 * (the searching process will cover all tasks, including tasks those being marked as delete).
	 * 
	 * @return <b>true</b> if the task exist, otherwise <b>false</b> will be returned.
	 */
	@Override
	public boolean exists(String id) {
		return taskDAO.exists(id);
	}

	/**
	 * get total record of tasks in the database (including tasks those being marked as delete).
	 * 
	 * @return number of record found.
	 */
	@Override
	public long count() {
		return taskDAO.count();
	}

	/**
	 * similar with <code>count()</code>, except for this method merely calculate tasks those are not 
	 * being marked as delete (i.e. markForDelete=false).
	 * 
	 * @see <code>count()</code> method.
	 * 
	 * @return number of record found.
	 */
	@Override
	public long countExclDelete() {
		return taskDAO.countExclDelete();
	}
}