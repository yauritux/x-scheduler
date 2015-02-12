package com.gdn.x.scheduler.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
@Table(name = "task")
public class Task extends GdnBaseEntity {

	private static final long serialVersionUID = -60028310176870786L;

	@Column(name = "task_name", nullable = false, length = 55, unique = true)
	private String taskName;
	
	@ManyToOne
	@JoinColumn(referencedColumnName = "ID", name = "command_id", nullable = false)
	private Command command;
	
	@Column(name = "secs", nullable = false)
	private String seconds;
	
	@Column(name = "mins", nullable = false)
	private String minutes;
	
	@Column(name = "hrs", nullable = false)
	private String hours;

	@Column(name = "day_of_month", nullable = false)
	private String dayOfMonth;
	
	@Column(name = "mth", nullable = false)
	private String month; 
	
	@Column(name = "day_of_week", nullable = false)
	private String dayOfWeek;
	
	@Column(name = "yr", nullable = true)
	private String year;
	
	/*
	@Enumerated(EnumType.STRING)
	private ThreadRunningPolicy threadRunningPolicy = ThreadRunningPolicy.SERIAL;
	*/
	
	public Task() {}

	/*
	public ThreadRunningPolicy getThreadRunningPolicy() {
		return threadRunningPolicy;
	}

	public void setThreadRunningPolicy(ThreadRunningPolicy threadRunningPolicy) {
		this.threadRunningPolicy = threadRunningPolicy;
	}
	*/

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

	public String getSeconds() {
		return seconds;
	}

	public void setSeconds(String seconds) {
		this.seconds = seconds;
	}

	public String getMinutes() {
		return minutes;
	}

	public void setMinutes(String minutes) {
		this.minutes = minutes;
	}

	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	public String getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}	
}