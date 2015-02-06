package com.gdn.x.scheduler.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gdn.x.scheduler.model.TaskExecution;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public interface TaskExecutionDAO extends CrudRepository<TaskExecution, String> {
	
	public static final String FIND_RUNNING_TASK 
		= "FROM TaskExecution te WHERE te.task.id = :taskId AND te.end != NULL order by te.end desc";

	public TaskExecution findById(String id);
	
	@Query(FIND_RUNNING_TASK)
	public List<TaskExecution> findRunningTask(@Param("taskId") String taskId);
}
