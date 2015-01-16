package com.gdn.x.scheduler.rest.web.model;

import java.util.Date;

import com.gdn.common.web.base.BaseResponse;

/**
 * 
 * @author yauritux
 *
 */
public class TaskResponse extends BaseResponse {

	private static final long serialVersionUID = -2137190859234821682L;
	
	private String taskId;
	private String taskName;
	private String commandId;
	private String command;
	private String parameters;
	private String contents;
	private String commandType;
	private Date createdDate;
	private Date updatedDate;

	public TaskResponse() {}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getCommandType() {
		return commandType;
	}

	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}