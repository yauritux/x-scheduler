package com.gdn.x.scheduler.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
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
		= "FROM TaskExecution te WHERE te.task.id = :taskId AND te.end IS NULL order by te.end desc";
	
	public static final String FIND_OBSOLETE_TASK_EXECUTION
		= "FROM TaskExecution te WHERE te.end > :lookupDate";
	
	public static final String DELETE_OBSOLETE_TASK_EXECUTION
		= "DELETE FROM TaskExecution te WHERE te.end > :lookupDate";
	
	public TaskExecution findById(String id);
	
	@Query(FIND_RUNNING_TASK)
	public List<TaskExecution> findRunningTask(@Param("taskId") String taskId);
	
	@Query(FIND_OBSOLETE_TASK_EXECUTION)
	public List<TaskExecution> findObsoleteTaskExecutions(@Param("lookupDate") Date dateToLookBackAfter);
	
	@Modifying
	@Query(DELETE_OBSOLETE_TASK_EXECUTION)
	public int deleteObsoleteTaskExecutions(@Param("lookupDate") Date dateToLookBackAfter);
}
