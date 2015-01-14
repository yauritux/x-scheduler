package com.gdn.x.scheduler.model;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.gdn.common.base.entity.GdnBaseEntity;

/**
 * 
 * @author yauritux
 *
 */
@Entity
@Table(name = "WORKFLOW")
public class Workflow extends GdnBaseEntity {

	private static final long serialVersionUID = 523239817846280840L;

	@Column(name = "WORKFLOW_NAME", nullable = false, length = 55)
	private String workflowName;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "WORKFLOW_TASKS",
			joinColumns = @JoinColumn(name = "WORKFLOW_ID"),
			inverseJoinColumns = @JoinColumn(name = "TASK_ID")
	)	
	private Set<Task> sequentialTasks = new LinkedHashSet<>();
			
	public Workflow() {}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}
	
	public Set<Task> getSequentialTasks() {
		return sequentialTasks;
	}
	
	public void setSequentialTasks(Set<Task> sequentialTasks) {
		this.sequentialTasks = sequentialTasks;
	}
}
