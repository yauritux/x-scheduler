package com.gdn.x.scheduler.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "command_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Command extends GdnBaseEntity {

	private static final long serialVersionUID = 6629422350366619513L;

	@Column(name = "command", nullable = false)
	private String command;
	
	@Column(name = "parameters", nullable = true)
	private String parameters;
		
	@Enumerated(EnumType.STRING)
	@Column(name = "command_type", nullable = false, insertable = false, updatable = false)
	private CommandType commandType = CommandType.WEB_SERVICE;	
	
	public Command() {}
	
	/**
	 * retrieve command that will be executed by task object.
	 * The command will be represented in JSON string which gonna be different 
	 * for each CommandType (i.e. WEB_SERVICE, CLIENT_SDK).
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
	 * along with the URL of the endpoint service. And for CLIENT_SDK, 
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
	 * attribute that is used to distinguish between WEB_SERVICE and CLIENT_SDK.
	 * 
	 * @return CommandType enum
	 */
	public CommandType getCommandType() {
		return commandType;
	}
	
	public void setCommandType(CommandType commandType) {
		this.commandType = commandType;
	}		
}