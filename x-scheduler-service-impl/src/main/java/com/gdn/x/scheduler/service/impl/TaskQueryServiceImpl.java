package com.gdn.x.scheduler.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdn.x.scheduler.dao.TaskDAO;
import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.rest.web.model.TaskResponse;
import com.gdn.x.scheduler.service.TaskQueryService;

/**
 * 
 * @author yauritux
 *
 */
@Service
@Transactional(readOnly = true)
public class TaskQueryServiceImpl implements TaskQueryService {
	
	private TaskDAO taskDAO;
	
	@Autowired
	public TaskQueryServiceImpl(TaskDAO taskDAO) {
		this.taskDAO = taskDAO;
	}

	@Override
	public List<Task> fetchAll() {
		return taskDAO.fetchAll();
	}

	@Override
	public Page<Task> fetchAll(int pageNumber, int pageSize) {
		return taskDAO.fetchAll(new PageRequest(pageNumber, pageSize));
	}

	@Override
	public Task findById(String id) {
		return taskDAO.findById(id);
	}

	@Override
	public List<Task> findByTaskName(String taskName) {
		return taskDAO.findByTaskName(taskName != null ? taskName.trim() : taskName);
	}

	@Override
	public Page<Task> findByTaskName(String taskName, int pageNumber,
			int pageSize) {
		return taskDAO.findByTaskName(taskName != null ? taskName.trim() : taskName, 
				new PageRequest(pageNumber, pageSize));
	}

	@Override
	public List<Task> findByTaskNameLike(String taskName) {
		return taskDAO.findByTaskName(
				taskName != null ? ("%" + taskName.trim() + "%") : taskName
		);
	}

	@Override
	public Page<Task> findByTaskNameLike(String taskName, int pageNumber,
			int pageSize) {
		return taskDAO.findByTaskNameLike(taskName != null ? ("%" + taskName.trim() + "%") : taskName, 
				new PageRequest(pageNumber, pageSize)
	    );
	}

	@Override
	public TaskResponse wrapTask(Task task) {
		if (task == null) {
			return null;
		}
		
		TaskResponse taskResponse = new TaskResponse();
		taskResponse.setTaskId(task.getId());
		taskResponse.setCommandId(task.getCommand().getId());
		taskResponse.setCommand(task.getCommand().getCommand());
		taskResponse.setParameters(task.getCommand().getParameters());
		taskResponse.setContents(task.getCommand().getContents());
		taskResponse.setCommandType(task.getCommand().getCommandType().name());
		taskResponse.setCreatedBy(task.getCreatedBy());
		taskResponse.setCreatedDate(task.getCreatedDate());
		taskResponse.setUpdatedBy(task.getUpdatedBy());
		taskResponse.setUpdatedDate(task.getUpdatedDate());
		
		return taskResponse;
	}
}