package com.gdn.x.scheduler.service.domain.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gdn.x.scheduler.dao.TaskExecutionDAO;
import com.gdn.x.scheduler.model.TaskExecution;
import com.gdn.x.scheduler.service.domain.TaskExecutionQueryService;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
@Service("taskExecutionQueryService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class TaskExecutionQueryServiceImpl implements TaskExecutionQueryService {
	
	private TaskExecutionDAO taskExecutionDAO;
	
	@Autowired
	public TaskExecutionQueryServiceImpl(TaskExecutionDAO taskExecutionDAO) {
		this.taskExecutionDAO = taskExecutionDAO;
	}

	@Override
	public TaskExecution findById(String id) {
		if (id == null) {
			return null;
		}
		return taskExecutionDAO.findById(id);
	}

	@Override
	public TaskExecution findLastRunningTask(String taskId) {
		if (taskId == null) {
			return null;
		}
		List<TaskExecution> taskExecutions = taskExecutionDAO.findRunningTask(taskId);
		return (taskExecutions != null && !taskExecutions.isEmpty()) ? taskExecutions.get(0) : null;
	}
}