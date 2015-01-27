package com.gdn.x.scheduler.model;

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
@DiscriminatorValue(value = CommandType.CommandTypeEnum.SS)
public class ShellScriptCommand extends Command {

	private static final long serialVersionUID = -7943757872452903749L;
}
