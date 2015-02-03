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
	private String submittedBy;
	private Date submittedOn;		
	
	public TaskRequest() {
		super();
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
