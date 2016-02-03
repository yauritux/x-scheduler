package com.gdn.x.scheduler.constant;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public enum ClientSDKExtension {

	JAR("jar"), 
	SHELL_SCRIPT("sh");
	
	private String extension;
	
	private ClientSDKExtension(String extension) {
		this.extension = extension;
	}
	
	public String extension() {
		return extension;
	}
}
