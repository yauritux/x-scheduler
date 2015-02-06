package com.gdn.x.scheduler.rest.web.model;

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

import com.gdn.common.web.wrapper.request.SimpleRequestHolder;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public class TaskRequest extends SimpleRequestHolder {

	private static final long serialVersionUID = -582801445350328128L;

	@NotEmpty(message = "Task name should not be empty!")
	private String taskName;
	@NotEmpty(message = "Store ID should not be empty!")
	private String storeId;
	private String commandId;
	private String interval;
	private String intervalUnit; 
	private String seconds;
	private String minutes;
	private String hour;
	private String dayOfMonth;
	private String month;
	private String dayOfWeek;
	private String year;
	private String threadRunningPolicy;
	
	/**
	 * TODO: use enum instead of String
	 * 
	 * @return
	 */
	public String getThreadRunningPolicy() {
		return threadRunningPolicy;
	}

	public void setThreadRunningPolicy(String threadRunningPolicy) {
		this.threadRunningPolicy = threadRunningPolicy;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String submittedBy;
	private Date submittedOn;		
	
	public TaskRequest() {
		super();
	}	
	
	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	/**
	 * TODO: use enum value later instead of plain String.
	 * @return
	 */
	public String getIntervalUnit() {
		return intervalUnit;
	}

	public void setIntervalUnit(String intervalUnit) {
		this.intervalUnit = intervalUnit;
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

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
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

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	public String getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(String submittedBy) {
		this.submittedBy = submittedBy;
	}

	public Date getSubmittedOn() {
		return submittedOn;
	}

	public void setSubmittedOn(Date submittedOn) {
		this.submittedOn = submittedOn;
	}
}
