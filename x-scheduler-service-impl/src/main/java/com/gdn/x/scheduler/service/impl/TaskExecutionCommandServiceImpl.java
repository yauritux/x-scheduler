package com.gdn.x.scheduler.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gdn.x.scheduler.dao.TaskExecutionDAO;
import com.gdn.x.scheduler.model.TaskExecution;
import com.gdn.x.scheduler.service.TaskExecutionCommandService;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 *
 */
@Service("taskExecutionCommandService")
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class TaskExecutionCommandServiceImpl implements
		TaskExecutionCommandService {
	
	private TaskExecutionDAO taskExecutionDAO;
	
	@Autowired
	public TaskExecutionCommandServiceImpl(TaskExecutionDAO taskExecutionDAO) {
		this.taskExecutionDAO = taskExecutionDAO;
	}

	@Override
	public TaskExecution save(TaskExecution entity) {
		if (entity == null) {
			return null;
		}
		return taskExecutionDAO.save(entity);
	}

	@Override
	public boolean delete(TaskExecution entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean restore(TaskExecution entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int batchDelete(List<TaskExecution> entities) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int batchRestore(List<TaskExecution> entities) {
		// TODO Auto-generated method stub
		return 0;
	}
}