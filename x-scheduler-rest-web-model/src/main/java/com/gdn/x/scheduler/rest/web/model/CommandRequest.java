package com.gdn.x.scheduler.rest.web.model;

import org.hibernate.validator.constraints.NotEmpty;

import com.gdn.common.web.wrapper.request.SimpleRequestHolder;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 * This class will be representing as a parent for all command's request.
 * Please do not use this class directly as a request object. Instead of this, 
 * you can use class that directly extends this class.
 */
public class CommandRequest extends SimpleRequestHolder {

	private static final long serialVersionUID = -7259600644142262832L;

	@NotEmpty(message = "command type cannot be empty")
	private String commandType;
	@NotEmpty(message = "store id cannot be empty")
	private String storeId;
	private String submittedBy;
	
	public CommandRequest() {
		super();
	}
	
	public String getCommandType() {
		return commandType;
	}
	
	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}
	
	public String getStoreId() {
		return storeId;
	}
	
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}	
	
	public String getSubmittedBy() {
		return submittedBy;
	}
	
	public void setSubmittedBy(String submittedBy) {
		this.submittedBy = submittedBy;
	}	
}