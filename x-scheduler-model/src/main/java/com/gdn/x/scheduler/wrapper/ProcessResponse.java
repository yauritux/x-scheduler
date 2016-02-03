package com.gdn.x.scheduler.wrapper;

import java.io.Serializable;

/**
 * 
 * @author yauritux@gmail.com
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public class ProcessResponse implements Serializable {

	private static final long serialVersionUID = 3439837736386966282L;

	private String code;
	private String message;
	
	public ProcessResponse(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "[code=" + code + ", message=" + message + "]";
	}
}
