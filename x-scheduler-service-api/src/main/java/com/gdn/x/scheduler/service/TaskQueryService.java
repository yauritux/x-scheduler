package com.gdn.x.scheduler.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.rest.web.model.TaskResponse;

/**
 * 
 * @author yauritux
 *
 */
public interface TaskQueryService {

	public List<Task> fetchAll();
	public Page<Task> fetchAll(int pageNumber, int pageSize);
	public Task findById(String id);
	public List<Task> findByTaskName(String taskName);
	public Page<Task> findByTaskName(String taskName, int pageNumber, int pageSize);
	public List<Task> findByTaskNameLike(String taskName);
	public Page<Task> findByTaskNameLike(String taskName, int pageNumber, int pageSize);
	public TaskResponse wrapTask(Task task);
}