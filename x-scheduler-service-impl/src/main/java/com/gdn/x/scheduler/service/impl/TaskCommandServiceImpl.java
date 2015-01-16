package com.gdn.x.scheduler.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdn.x.scheduler.dao.TaskDAO;
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

	@Override
	public int deleteTask(String id) {
		return taskDAO.deleteTask(id);
	}
}
