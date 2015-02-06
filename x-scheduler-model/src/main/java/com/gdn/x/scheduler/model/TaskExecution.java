package com.gdn.x.scheduler.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.gdn.common.base.entity.GdnBaseEntity;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
@Entity
@Table(name = "task_execution")
public class TaskExecution extends GdnBaseEntity {

	private static final long serialVersionUID = 5939804868063560055L;

	@ManyToOne
	private Task task;
	
	@Column(name = "start_time", nullable = false)
	private Date start;
	
	@Column(name = "end_time", nullable = true)
	private Date end;
	
	public TaskExecution() {}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
}
