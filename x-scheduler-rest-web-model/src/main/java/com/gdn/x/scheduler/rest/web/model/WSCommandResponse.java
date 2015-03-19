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
	private String contents;
	
	public WSCommandResponse() {}
	
	public String getEndPoint() {
		return endPoint;
	}
	
	/**
	 * the endpoint (URL) of this particular WebService.
	 * 
	 * @param endPoint
	 */
	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
	
	/**
	 * WebService method (GET|POST|PUT|DELETE). 
	 * 
	 * @return webservice method
	 */
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
	
	/**
	 * WebService's payload. Please note that only JSON is supported as payload 
	 * on this version.  
	 * 
	 * @return string represents payload in JSON format.
	 */
	public String getContents() {
		return contents;
	}
	
	public void setContents(String contents) {
		this.contents = contents;
	}
}
