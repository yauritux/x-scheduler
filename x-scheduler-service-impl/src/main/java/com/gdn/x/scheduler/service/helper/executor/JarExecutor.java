package com.gdn.x.scheduler.service.helper.executor;

import java.io.IOException;

import com.gdn.x.scheduler.wrapper.ProcessResponse;

/**
 * 
 * @author yauritux@gmail.com
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public class JarExecutor extends ClientSDKExecutor {
	
	public JarExecutor(String app) throws IOException {
		super();
		this.process = Runtime.getRuntime().exec("java -jar " + app);
	}
	
	@Override
	public ProcessResponse execute(long timeoutInSeconds) {
		return executeCommand(timeoutInSeconds);
	}
}