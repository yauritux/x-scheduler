package com.gdn.x.scheduler.service.helper.factory.impl;

import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.rest.web.model.CommandRequest;
import com.gdn.x.scheduler.service.helper.command.Command;
import com.gdn.x.scheduler.service.helper.command.impl.ClientSDKCommand;
import com.gdn.x.scheduler.service.helper.command.impl.WSCommand;
import com.gdn.x.scheduler.service.helper.receiver.CommandReceiver;
import com.gdn.x.scheduler.service.helper.receiver.impl.CSCommandReceiverImpl;
import com.gdn.x.scheduler.service.helper.receiver.impl.WSCommandReceiverImpl;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 * Collection of Command's factory.
 */
public class CommandFactory {

	/**
	 * Build specific command object (i.e. WebService, ClientSDK) 
	 * based on the command provided in the method argument.
	 * 
	 * @param command
	 * @return specific command object 
	 */
	public static final Command getCommandFromEntity(
			com.gdn.x.scheduler.model.Command command) {		
		try {
			if (command.getCommandType() == CommandType.WEB_SERVICE) {
				return new WSCommand(new WSCommandReceiverImpl(command));
			} else if (command.getCommandType() == CommandType.CLIENT_SDK) {
				return new ClientSDKCommand(new CSCommandReceiverImpl(command));
			}
		} catch (NullPointerException ignored) {}
		
		return null;
	}
	
	/**
	 * Build specific command helper object (i.e. WebService, ClientSDK) 
	 * based on the CommandRequest object.
	 * 
	 * @param request
	 * @return specific command helper 
	 */
	public static final Command getCommandFromEntity(CommandRequest request) {
		try {
			if (request.getCommandType().equalsIgnoreCase(CommandType.WEB_SERVICE.name())) {
				return new WSCommand(new WSCommandReceiverImpl());
			} else if (request.getCommandType().equalsIgnoreCase(CommandType.CLIENT_SDK.name())) {
				return new ClientSDKCommand(new CSCommandReceiverImpl());
			}
		} catch (NullPointerException ignored) {}
		
		return null;
	}
	
	/**
	 * get specific implementation of CommandReceiver (i.e. WebService, ClientSDK) 
	 * based on the command provided in the argument.
	 * 
	 * @param command
	 * @return CommandReceiver
	 */
	public static final CommandReceiver getCommandReceiverFromEntity(com.gdn.x.scheduler.model.Command command) {
		try {
			if (command.getCommandType() == CommandType.WEB_SERVICE) {
				return new WSCommandReceiverImpl(command);
			} 
			if (command.getCommandType() == CommandType.CLIENT_SDK) {
				return new CSCommandReceiverImpl(command);
			}
		} catch (NullPointerException ignored) {}
		
		return null;
	}
}