package com.gdn.x.scheduler.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gdn.x.scheduler.model.Task;

/**
 * 
 * @author yauritux
 *
 */
public interface TaskDAO extends CrudRepository<Task, String> {
	
	public static final String FETCH_ALL 
		= "FROM Task t WHERE t.markForDelete = false ORDER BY createdDate DESC";	
	public static final String FIND_BY_TASK_NAME 
		= "FROM Task t WHERE t.markForDelete = false AND t.taskName = :taskName ORDER BY createdDate DESC";
    public static final String FIND_BY_TASK_NAME_LIKE
    	= "FROM Task t WHERE t.taskName like :taskName ORDER BY createdDate DESC";

	public Task findById(String id);
	
	@Query(FETCH_ALL)
	public List<Task> fetchAll();
	
	@Query(FETCH_ALL)
	public Page<Task> fetchAll(Pageable pageable);

	@Query(FIND_BY_TASK_NAME)
	public List<Task> findByTaskName(@Param("taskName") String taskName);
	
	@Query(FIND_BY_TASK_NAME)
	public Page<Task> findByTaskName(@Param("taskName") String taskName, Pageable pageable);
	
	@Query(FIND_BY_TASK_NAME_LIKE)
	public List<Task> findByTaskNameLike(@Param("taskName") String taskName);
	
	@Query(FIND_BY_TASK_NAME_LIKE)
	public Page<Task> findByTaskNameLike(@Param("taskName") String taskName, Pageable pageable);
	
	@Modifying
	@Query(value = "UPDATE Task t SET markForDelete = true WHERE id = :id")
	public int deleteTask(@Param("id") String id);	
}
