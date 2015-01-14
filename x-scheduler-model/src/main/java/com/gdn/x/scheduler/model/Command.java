package com.gdn.x.scheduler.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.gdn.common.base.entity.GdnBaseEntity;
import com.gdn.x.scheduler.constant.CommandType;

/**
 * 
 * @author yauritux
 *
 */
@Entity
@Table(name = "command")
public class Command extends GdnBaseEntity {

	private static final long serialVersionUID = 6629422350366619513L;

	@Column(name = "command", nullable = false)
	private String command;
	
	@Column(name = "parameters", nullable = true)
	private String parameters;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "command_type", nullable = false)
	private CommandType commandType = CommandType.WEB_SERVICE;
	
	public Command() {}
	
	/**
	 * retrieve command that will be executed by task object.
	 * The command will be representing in JSON string which will be different 
	 * between each CommandType (i.e. WEB_SERVICE, JAR_API, COMMAND_SCRIPT).
	 * e.g. for WEB_SERVICE, the JSON string will contain some keys such as URL, WS method, etc.
	 *  
	 * @return command in JSON string.
	 */
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
	
	public CommandType getCommandType() {
		return commandType;
	}
	
	public void setCommandType(CommandType commandType) {
		this.commandType = commandType;
	}
}