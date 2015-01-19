package com.gdn.x.scheduler.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdn.x.scheduler.dao.TaskDAO;
import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.service.TaskCommandService;

/**
 * 
 * @author yauritux
 *
 */
@Service
@Transactional(readOnly = false)
public class TaskCommandServiceImpl implements TaskCommandService {
	
	private TaskDAO taskDAO;
	
	@Autowired
	public TaskCommandServiceImpl(TaskDAO taskDAO) {
		this.taskDAO = taskDAO;
	}

	/**
	 * Use this method to temporary delete task from the system.
	 * Note that the deleted task will be still exist in the database, 
	 * and just hidden from the system (flagged by markForDelete = true)
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
	 * with deleteTask method. 
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