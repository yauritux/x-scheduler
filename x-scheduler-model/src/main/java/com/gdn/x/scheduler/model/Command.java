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
	
	@Column(name = "contents", nullable = true)
	private String contents;
	
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

	/**
	 * Parameters are various, depend on the command type.
	 * e.g. parameter for WEB_SERVICE will be query parameters those are supplied 
	 * along with the URL of the endpoint service. And for CLIENT_SDK or SHELL_SCRIPT, 
	 * parameter will be arguments of the command that is being executed.
	 * 
	 * @return parameters in string
	 */
	public String getParameters() {
		return parameters;
	}
	
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * contents merely applied for WEB_SERVICE command. 
	 * contents will be supplied in the format of JSON.
	 * 
	 * @return JSON string, represents the data being sent as a content.
	 */
	public String getContents() {
		return contents;
	}
	
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	public CommandType getCommandType() {
		return commandType;
	}
	
	public void setCommandType(CommandType commandType) {
		this.commandType = commandType;
	}
}