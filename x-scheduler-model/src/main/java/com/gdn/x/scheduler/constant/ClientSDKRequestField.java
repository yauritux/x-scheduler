package com.gdn.x.scheduler.constant;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public enum ClientSDKRequestField {

	ENTRY_POINT("entryPoint");
	
	private String label;
	
	private ClientSDKRequestField(String label) {
		this.label = label;
	}
	
	public String label() {
		return label;
	}
	
	@Override
	public String toString() {
		return label();
	}
}
