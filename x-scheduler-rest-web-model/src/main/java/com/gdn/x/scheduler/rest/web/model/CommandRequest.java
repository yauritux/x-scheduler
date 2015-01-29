package com.gdn.x.scheduler.rest.web.model;

import org.hibernate.validator.constraints.NotEmpty;

import com.gdn.common.web.wrapper.request.SimpleRequestHolder;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 * This class will be representing as a parent for all command's request.
 * every command (could be WEB_SERVICE, SHELL_SCRIPT, etc) that is intended to be passed
 * as a request should extend this class. 
 * Please do not use this class directly as a request object, instead you can use 
 * class that extends this particular class.
 */
public class CommandRequest extends SimpleRequestHolder {

	private static final long serialVersionUID = -7259600644142262832L;

	@NotEmpty(message = "command type should not be empty")
	private String commandType;
	
	public CommandRequest() {
		super();
	}
	
	public String getCommandType() {
		return commandType;
	}
	
	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}
}