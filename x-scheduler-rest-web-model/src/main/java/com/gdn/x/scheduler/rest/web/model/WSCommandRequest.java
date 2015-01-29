package com.gdn.x.scheduler.rest.web.model;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 * Wrapper class (in the body of REST request) for WebService command.
 *   
 */
public class WSCommandRequest extends CommandRequest {

	private static final long serialVersionUID = -3269042672628652056L;

	@NotEmpty(message = "command cannot be empty!")
	private String command;
	private String parameters;
	private String contents;
	
	public WSCommandRequest() {
		super();
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
}