package com.gdn.x.scheduler.service.helper.invoker.impl;

import com.gdn.x.scheduler.model.Command;
import com.gdn.x.scheduler.rest.web.model.CommandResponse;
import com.gdn.x.scheduler.service.helper.factory.impl.CommandFactory;
import com.gdn.x.scheduler.service.helper.invoker.CommandInvoker;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public class CommandInvokerImpl implements CommandInvoker {
	
	private Command command;
		
	public CommandInvokerImpl(Command command) {
		this.command = command;
	}
	
	@Override
	public CommandResponse getCommandResponse() {
		com.gdn.x.scheduler.service.helper.command.Command schedulerCommand = null;
		schedulerCommand = CommandFactory.getCommandFromEntity(command);
		return schedulerCommand.buildCommandResponse();
	}
}