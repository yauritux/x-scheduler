package com.gdn.x.scheduler.service.helper.command.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gdn.x.scheduler.rest.web.model.CommandRequest;
import com.gdn.x.scheduler.rest.web.model.CommandResponse;
import com.gdn.x.scheduler.service.helper.command.Command;
import com.gdn.x.scheduler.service.helper.receiver.CommandReceiver;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 * @see <a href="http://en.wikipedia.org/wiki/Command_pattern">Command Pattern</a>.
 * @see {@link com.gdn.x.scheduler.rest.web.model.CommandResponse}
 * @see {@link com.gdn.x.scheduler.rest.web.model.WSCommandResponse}
 * @see {@link com.gdn.x.scheduler.service.helper.receiver.CommandReceiver}
 * 
 * This command service bound to WebService type of command.
 */
public class WSCommand implements Command {
	
	private static final Logger LOG = LoggerFactory.getLogger(WSCommand.class);
	
	private CommandReceiver receiver;
	
	/**
	 * The default constructor that receives CommandReceiver as the parameter.
	 * 
	 * @param receiver
	 */
	public WSCommand(CommandReceiver receiver) {
		this.receiver = receiver;
	}

	/**
	 * Build and return CommandResponse object.
	 * The process will be handled by particular receiver that has already been 
	 * passed via the default constructor earlier.
	 * This method will return WSCommandResponse (one of the CommandResponse subclass). 
	 *  
	 * @return instance of WSCommandResponse.
	 */
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

	@Override
	public com.gdn.x.scheduler.model.Command buildFromCommandRequest(CommandRequest request) {
		com.gdn.x.scheduler.model.Command command = null;
		try {
			command = receiver.convertToCommand(request);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return command;
	}	
}