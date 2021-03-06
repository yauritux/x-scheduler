package com.gdn.x.scheduler.service.helper.invoker;

import com.gdn.x.scheduler.model.Command;
import com.gdn.x.scheduler.rest.web.model.CommandRequest;
import com.gdn.x.scheduler.rest.web.model.CommandResponse;


/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public interface CommandInvoker {

	public CommandResponse getCommandResponse();
	public Command buildFromCommandRequest(CommandRequest request);
}
