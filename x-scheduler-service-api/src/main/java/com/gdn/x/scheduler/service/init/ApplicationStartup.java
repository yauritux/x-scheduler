package com.gdn.x.scheduler.service.init;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public interface ApplicationStartup extends ApplicationListener<ContextRefreshedEvent> {
	
	public void onDestroy();
}
