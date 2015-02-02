package com.gdn.x.scheduler.service.helper.invoker.impl;

import com.gdn.x.scheduler.model.Command;
import com.gdn.x.scheduler.rest.web.model.CommandRequest;
import com.gdn.x.scheduler.rest.web.model.CommandResponse;
import com.gdn.x.scheduler.service.helper.factory.impl.CommandFactory;
import com.gdn.x.scheduler.service.helper.invoker.CommandInvoker;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 * 
 * Service class which is representing a gateway to build the DTO 
 * that can be used as an HTTP response object.
 * Invoker term came from <a href="http://en.wikipedia.org/wiki/Command_pattern">Command Pattern</a>.
 * Basically, the main job of this invoker is to wrapping the request from the user/system 
 * and mapping it into the appropriate command to be invoked later by the receiver.   
 *
 */
public class CommandInvokerImpl implements CommandInvoker {
	
	private Command command;
	
	/**
	 * The default constructor
	 */
	public CommandInvokerImpl() {
		super();
	}
	
	/**
	 * constructor which receives command entity as a parameter.
	 *  
	 * @param command
	 */
	public CommandInvokerImpl(Command command) {
		this.command = command;
	}
	
	/**
	 * retrieves CommandResponse object as a wrapper/DTO for one of the command type 
	 * defined in the system (i.e. WebService, ClientSDK, or ShellScript). 
	 * 
	 */
	@Override
	public CommandResponse getCommandResponse() {
		com.gdn.x.scheduler.service.helper.command.Command schedulerCommand
			= CommandFactory.getCommandFromEntity(command);
		return schedulerCommand.buildCommandResponse();
	}

	@Override
	public Command buildFromCommandRequest(CommandRequest request) {
		com.gdn.x.scheduler.service.helper.command.Command schedulerCommand
			= CommandFactory.getCommandFromEntity(request);
		return schedulerCommand.buildFromCommandRequest(request);
	}
}