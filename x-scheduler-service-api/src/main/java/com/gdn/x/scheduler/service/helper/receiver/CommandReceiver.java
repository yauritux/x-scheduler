package com.gdn.x.scheduler.service.helper.receiver;

import com.gdn.x.scheduler.rest.web.model.CommandResponse;

/**
 * 
 * @author yauritux
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public interface CommandReceiver {

	public CommandResponse wrapCommand() throws Exception;
}
