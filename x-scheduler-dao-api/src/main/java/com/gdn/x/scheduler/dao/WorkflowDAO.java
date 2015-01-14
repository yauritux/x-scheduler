package com.gdn.x.scheduler.dao;

import org.springframework.data.repository.CrudRepository;

import com.gdn.x.scheduler.model.Workflow;

/**
 * 
 * @author yauritux
 *
 */
public interface WorkflowDAO extends CrudRepository<Workflow, Long>{

	public Workflow findByWorkflowId(Long workflowId);
}
