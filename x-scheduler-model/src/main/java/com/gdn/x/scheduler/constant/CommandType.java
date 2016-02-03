package com.gdn.x.scheduler.constant;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public enum CommandType {
	
	WEB_SERVICE(CommandTypeEnum.WS), 
	CLIENT_SDK(CommandTypeEnum.CS);
	
	private String label;
	
	private CommandType(final String label) {
		this.label = label;
	}
	
	public final String label() {
		return label;
	}	
	
	@Override
	public String toString() {
		return label();
	}
	
	public class CommandTypeEnum {
		public static final String WS = "WEB_SERVICE";
		public static final String CS = "CLIENT_SDK";
	}
}
