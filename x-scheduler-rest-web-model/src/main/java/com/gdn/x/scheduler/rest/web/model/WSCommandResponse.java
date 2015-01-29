package com.gdn.x.scheduler.rest.web.model;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 * 
 * Wrapper class (in REST response) for WebService command.  
 *
 */
public class WSCommandResponse extends CommandResponse {

	private static final long serialVersionUID = -8941003179821697064L;
	
	private String endPoint;
	private String httpMethod;
	private String parameters;
	
	public WSCommandResponse() {}
	
	public String getEndPoint() {
		return endPoint;
	}
	
	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
	
	public String getHttpMethod() {
		return httpMethod;
	}
	
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}
	
	public String getParameters() {
		return parameters;
	}
	
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
}
