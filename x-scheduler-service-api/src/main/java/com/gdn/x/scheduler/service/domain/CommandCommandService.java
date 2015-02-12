package com.gdn.x.scheduler.service.domain;

import com.gdn.x.scheduler.model.Command;
import com.gdn.x.scheduler.rest.web.model.CommandRequest;
import com.gdn.x.scheduler.service.domain.base.BaseCommandService;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public interface CommandCommandService extends BaseCommandService<Command> {
	
	public Command buildCommandFromRequest(CommandRequest request);
}
