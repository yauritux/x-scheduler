package com.gdn.x.scheduler.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gdn.x.scheduler.dao.TaskDAO;
import com.gdn.x.scheduler.model.Task;
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
@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class TaskCommandServiceImpl implements TaskCommandService {
	
	private TaskDAO taskDAO;
	
	@Autowired
	public TaskCommandServiceImpl(TaskDAO taskDAO) {
		this.taskDAO = taskDAO;
	}

	/**
	 * Use this method to delete the task from the system (temporary deleted).
	 * Note that the deleted task will be still existed in the database, 
	 * and merely hidden from the system (flagged by markForDelete = true)
	 * 
	 * @param id the task id.
	 * @return number of task deleted. 
	 */
	@Override
	public int deleteTask(String id) {
		Task task = taskDAO.findById(id);
		if (task == null || task.isMarkForDelete()) {
			return 0;
		}
		return taskDAO.deleteTask(id);
	}
	
	/**
	 * Use this method to restore the task that previously has been deleted 
	 * with <code>deleteTask</code> method. 
	 * Once the task is restored, it can be seen again (will be displayed) in the system.
	 * 
	 * @param id the task id.
	 * @return number of task restored.
	 */
	@Override
	public int restoreTask(String id) {
		Task task = taskDAO.findById(id);
		if (task == null || !task.isMarkForDelete()) {
			return 0;
		}
		return taskDAO.restoreTask(id);
	}	
}