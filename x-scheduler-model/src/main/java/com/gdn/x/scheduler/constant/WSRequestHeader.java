package com.gdn.x.scheduler.constant;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public enum WSRequestHeader {

	URL("url"),
	METHOD("method"),
	ACCEPT("Accept"),
	ACCEPT_CHARSET("Accept-Charset"),
	ACCEPT_ENCODING("Accept-Encoding"),
	CACHE_CONTROL("Cache-Control"),
	CONNECTION("Connection"),
	CONTENT_TYPE("Content-Type"),
	DATE("Date"),
	HOST("Host");
	
	private String label;
	
	private WSRequestHeader(String label) {
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
