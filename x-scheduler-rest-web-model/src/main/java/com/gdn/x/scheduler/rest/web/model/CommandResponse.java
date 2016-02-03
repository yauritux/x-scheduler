package com.gdn.x.scheduler.rest.web.model;

import com.gdn.common.web.base.BaseResponse;

/**
 * 
 * @author yauritux
 * 
 * This class will be representing as a parent for all command's response.
 * every command (could be WEB_SERVICE, SHELL_SCRIPT, etc) that is intended to be returned 
 * as a command should extend this class. 
 * Please do not use this class directly as a response object, instead you can use 
 * class that extends this particular class.
 *
 */
public class CommandResponse extends BaseResponse {

	private static final long serialVersionUID = -3799436865860072318L;

	private String commandType;
	
	public CommandResponse() {
		super();
	}
	
	public String getCommandType() {
		return commandType;
	}
	
	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}
}