package com.gdn.x.scheduler.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gdn.x.scheduler.model.Workflow;
import com.gdn.x.scheduler.model.WorkflowSchedule;

/**
 * 
 * @author yauritux
 *
 */
public interface WorkflowScheduleDAO extends CrudRepository<WorkflowSchedule, String> {

	public static final String FETCH_ALL 
		= "FROM WorkflowSchedule ws WHERE ws.markForDelete = false ORDER BY createdDate DESC";
	public static final String FIND_BY_WORKFLOW 
		= "SELECT ws, w FROM WorkflowSchedule ws INNER JOIN ws.workflow w WHERE ws.workflow = :workflow AND ws.markForDelete = false ORDER BY ws.createdDate DESC";
	public static final String FIND_BY_ID_EXCL_DELETE
		= "FROM WorkflowSchedule ws WHERE ws.id = :id AND ws.markForDelete = false";
			
	public WorkflowSchedule findById(String id);
	
	public boolean exists(String id);
	
	@Query(FIND_BY_ID_EXCL_DELETE)
	public WorkflowSchedule findByIdExcl(@Param("id") String id);
	
	@Query(FETCH_ALL)
	public List<WorkflowSchedule> fetchAll();
	
	@Query(FETCH_ALL)
	public Page<WorkflowSchedule> fetchAll(Pageable pageable);
	
	public List<WorkflowSchedule> findAll();
	
	public Page<WorkflowSchedule> findAll(Pageable pageable);
	
	public long count();
	
	@Query("SELECT count(ws) FROM WorkflowSchedule ws WHERE ws.markForDelete = false")
	public long countExclDelete();
	
	@Query(FIND_BY_WORKFLOW)
	public Page<WorkflowSchedule> findByWorkflow(@Param("workflow") Workflow workflow, Pageable pageable);
	
	@Query(FIND_BY_WORKFLOW)
	public List<WorkflowSchedule> findByWorkflow(@Param("workflow") Workflow workflow);	
	
	@Modifying
	@Query("UPDATE WorkflowSchedule ws SET ws.markForDelete = true WHERE ws.id = :id")
	public int deleteWorkflowSchedule(@Param("id") String id);
	
	@Modifying
	@Query("UPDATE WorkflowSchedule ws SET ws.markForDelete = false WHERE ws.id = :id")
	public int restoreWorkflowSchedule(@Param("id") String id);
}