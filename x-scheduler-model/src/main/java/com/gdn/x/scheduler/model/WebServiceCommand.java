package com.gdn.x.scheduler.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.gdn.x.scheduler.constant.CommandType;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
@Entity
@DiscriminatorValue(value = CommandType.CommandTypeEnum.WS)
public class WebServiceCommand extends Command {

	private static final long serialVersionUID = -8583574728030700758L;
	
	@Column(name = "contents", nullable = true)
	private String contents;
	
	/** 
	 * Contents is the request body that is sent usually via HTTP POST method. 
	 * contents will be supplied in the format of JSON.
	 * 
	 * @return JSON string, represents the data being sent as a content.
	 */
	public String getContents() {
		return contents;
	}
	
	public void setContents(String contents) {
		this.contents = contents;
	}	
}
