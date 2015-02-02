package com.gdn.x.scheduler.rest.web.model;


/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 * Wrapper class (in the body of REST request) for WebService command.
 *   
 */
public class WSCommandRequest extends CommandRequest {

	private static final long serialVersionUID = -3269042672628652056L;

	private String endpoint;
	private String wsMethod;
	private String parameters;
	private String contents;
	
	public WSCommandRequest() {
		super();
	}
	
	public String getEndpoint() {
		return endpoint;
	}
	
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	
	public String getWsMethod() {
		return wsMethod;
	}
	
	public void setWsMethod(String wsMethod) {
		this.wsMethod = wsMethod;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	
	public String getContents() {
		return contents;
	}
	
	public void setContents(String contents) {
		this.contents = contents;
	}
}