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
@DiscriminatorValue(value = CommandType.CommandTypeEnum.CS)
public class ClientSDKCommand extends Command {
	
	private static final long serialVersionUID = -9077076940684050060L;
	
	@Column(name = "entry_point")
	private String entryPoint;
	
	/**
	 * return entry point of the class executed. 
	 * Entry point here refers to the method in the class.
	 * 
	 * @return string represent method name in the class.
	 */
	public String getEntryPoint() {
		return entryPoint;
	}
	
	public void setEntryPoint(String entryPoint) {
		this.entryPoint = entryPoint;
	}
}
