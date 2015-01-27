package com.gdn.x.scheduler.service.helper.command.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gdn.x.scheduler.rest.web.model.CommandResponse;
import com.gdn.x.scheduler.service.helper.command.Command;
import com.gdn.x.scheduler.service.helper.receiver.CommandReceiver;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public class WSCommand implements Command {
	
	private static final Logger LOG = LoggerFactory.getLogger(WSCommand.class);
	
	private CommandReceiver receiver;
	
	public WSCommand(CommandReceiver receiver) {
		this.receiver = receiver;
	}

	@Override
	public CommandResponse buildCommandResponse() {
		CommandResponse commandResponse = null;
		try {
			commandResponse = receiver.wrapCommand();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return commandResponse;		
	}	
}