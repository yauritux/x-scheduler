package com.gdn.x.scheduler.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.gdn.common.base.entity.GdnBaseEntity;

/**
 * 
 * @author yauritux
 *
 */
@Entity
@Table(name = "WORKFLOW_SCHEDULE")
public class WorkflowSchedule extends GdnBaseEntity {

	private static final long serialVersionUID = -552674980041464211L;

	@ManyToOne
	private Workflow workflow;
	
	@Column(name = "MINUTES", nullable = true)
	private int minutes;
	
	@Column(name = "HOURS", nullable = true)
	private int hours;
	
	@Column(name = "DAY_OF_MONTH", nullable = true)
	private int dayOfMonth;
	
	@Column(name = "MONTHS", nullable = true)
	private int months;
	
	@Column(name = "DAY_OF_WEEK", nullable = true)
	private int dayOfWeek;
	
	public WorkflowSchedule() {}

	public Workflow getWorkflow() {
		return workflow;
	}

	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public int getMonths() {
		return months;
	}

	public void setMonths(int months) {
		this.months = months;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
}
