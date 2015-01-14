package com.gdn.x.scheduler.dao;

import org.springframework.data.repository.CrudRepository;

import com.gdn.x.scheduler.model.Task;

/**
 * 
 * @author yauritux
 *
 */
public interface TaskDAO extends CrudRepository<Task, Long> {

	public Task findByTaskId(Long taskId);
}
