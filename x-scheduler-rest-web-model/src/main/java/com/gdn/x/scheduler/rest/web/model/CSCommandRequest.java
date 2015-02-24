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

	private String entryPoint;
	
	public CSCommandRequest() {
		super();
	}
	
	public String getEntryPoint() {
		return entryPoint;
	}
	
	public void setEntryPoint(String entryPoint) {
		this.entryPoint = entryPoint;
	}
}
