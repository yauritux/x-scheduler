package com.gdn.x.scheduler.service.helper.command;

import com.gdn.x.scheduler.rest.web.model.CommandResponse;


/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public interface Command {

	public CommandResponse buildCommandResponse();
}
