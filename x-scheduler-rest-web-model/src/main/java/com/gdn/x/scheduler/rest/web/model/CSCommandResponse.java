package com.gdn.x.scheduler.rest.web.model;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public class CSCommandResponse extends CommandResponse {

	private static final long serialVersionUID = 4989661261448931698L;
	
	private String entryPoint;	
	
	public CSCommandResponse() {}
	
	public String getEntryPoint() {
		return entryPoint;
	}
	
	public void setEntryPoint(String entryPoint) {
		this.entryPoint = entryPoint;
	}
}
