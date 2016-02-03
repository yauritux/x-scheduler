package com.gdn.x.scheduler.rest.web.model;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 * 
 * Wrapper class (in the body of REST request) for ClientSDK command.
 *
 */
public class CSCommandRequest extends CommandRequest {

	private static final long serialVersionUID = 1859897177734304258L;

	private String sdkName;
	
	public CSCommandRequest() {
		super();
	}
	
	public String getSdkName() {
		return sdkName;
	}
	
	public void setSdkName(String sdkName) {
		this.sdkName = sdkName;
	}
}
