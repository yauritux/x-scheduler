package com.gdn.x.scheduler.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.gdn.common.base.entity.GdnBaseEntity;

/**
 * 
 * @author yauritux
 *
 */
@Entity
@Table(name = "task")
public class Task extends GdnBaseEntity {

	private static final long serialVersionUID = -60028310176870786L;

	@Column(name = "task_name", nullable = false, length = 55)
	private String taskName;
	
	@ManyToOne
	@JoinColumn(referencedColumnName = "ID", name = "command_id", nullable = false)
	private Command command;
	
	@ManyToMany(mappedBy = "sequentialTasks")
	private Set<Workflow> workflows = new HashSet<>();
	
	public Task() {}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}
	
	public Set<Workflow> getWorkflows() {
		return workflows;
	}
	
	public void setWorkflows(Set<Workflow> workflows) {
		this.workflows = workflows;
	}
}