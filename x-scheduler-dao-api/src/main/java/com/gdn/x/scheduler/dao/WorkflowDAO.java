package com.gdn.x.scheduler.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gdn.x.scheduler.model.Workflow;

/**
 * 
 * @author yauritux
 *
 */
public interface WorkflowDAO extends CrudRepository<Workflow, String>{

	public static final String FETCH_ALL 
		= "FROM Workflow w WHERE w.markForDelete = false ORDER BY createdDate DESC";
	public static final String FIND_BY_WORKFLOW_NAME
		= "FROM Workflow w WHERE w.markForDelete = false AND w.workflowName = :workflowName ORDER BY createdDate DESC";
	
	public Workflow findById(String id);
	
	@Query(FIND_BY_WORKFLOW_NAME)
	public List<Workflow> findByWorkflowName(@Param("workflowName") String workflowName);
	
	@Query(FIND_BY_WORKFLOW_NAME)
	public Page<Workflow> findByWorkflowName(@Param("workflowName") String workflowName, Pageable pageable);
	
	@Query(FETCH_ALL)
	public List<Workflow> fetchAll();
	
	@Query(FETCH_ALL)
	public Page<Workflow> fetchAll(Pageable pageable);
	
	@Modifying
	@Query("UPDATE Workflow w SET w.markForDelete = true WHERE w.id = :id")
	public int deleteWorkflow(@Param("id") String id);
}
