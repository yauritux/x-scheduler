package com.gdn.x.scheduler.service.helper.factory.impl;

import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.service.helper.command.Command;
import com.gdn.x.scheduler.service.helper.command.impl.ClientSDKCommand;
import com.gdn.x.scheduler.service.helper.command.impl.WSCommand;
import com.gdn.x.scheduler.service.helper.receiver.impl.CSCommandReceiverImpl;
import com.gdn.x.scheduler.service.helper.receiver.impl.WSCommandReceiverImpl;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public class CommandFactory {

	public static final Command getCommandFromEntity(
			com.gdn.x.scheduler.model.Command command) {
		if (command.getCommandType() == CommandType.WEB_SERVICE) {
			return new WSCommand(new WSCommandReceiverImpl(command));
		} else if (command.getCommandType() == CommandType.CLIENT_SDK) {
			return new ClientSDKCommand(new CSCommandReceiverImpl(command));
		}
		
		return null;
	}
}