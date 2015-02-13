package com.gdn.x.scheduler.dao;

import java.util.Date;
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
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 * Data Access Object (repository) for Task.
 */
public interface TaskDAO extends CrudRepository<Task, String> {
	
	public static final String FETCH_ALL 
		= "FROM Task t WHERE t.markForDelete = false ORDER BY createdDate DESC";	
	public static final String FIND_BY_TASK_NAME 
		= "FROM Task t WHERE t.markForDelete = false AND t.taskName = :taskName ORDER BY createdDate DESC";
    public static final String FIND_BY_TASK_NAME_LIKE
    	= "FROM Task t WHERE t.taskName like :taskName ORDER BY createdDate DESC";
    public static final String FIND_BY_ID_EXCL_DELETE
    	= "FROM Task t WHERE t.id = :id AND t.markForDelete = false";
    public static final String FIND_EXPIRED_TASKS
    	= "FROM Task t WHERE t.expiryDate <= :expiryDate AND t.markForDelete = false";
    public static final String COUNT_RECORDS_EXCL_DELETE
    	= "SELECT count(t) FROM Task t WHERE t.markForDelete = false";

	public Task findById(String id);
	
	@Query(FIND_BY_ID_EXCL_DELETE)
	public Task findByIdExclDelete(@Param("id") String id);	
	
	public List<Task> findAll();
	
	public Page<Task> findAll(Pageable pageable);
	
	public boolean exists(String id);
	
	public long count();
	
	@Query(COUNT_RECORDS_EXCL_DELETE)
	public long countExclDelete();
	
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
	
	@Query(FIND_EXPIRED_TASKS)
	public List<Task> findExpiredTasks(@Param("expiryDate") Date expiryDate);
	
	@Modifying
	@Query(value = "UPDATE Task t SET markForDelete = true WHERE id = :id")
	public int deleteTask(@Param("id") String id);	
	
	@Modifying
	@Query(value = "UPDATE Task t SET markForDelete = false WHERE id = :id")
	public int restoreTask(@Param("id") String id);
	
	@Modifying
	@Query(value = "UPDATE Task t SET markForDelete = true WHERE expiryDate <= :expiryDate")
	public int deleteExpiredTasks(@Param("expiryDate") Date expiryDate);
}