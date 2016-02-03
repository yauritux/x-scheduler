package com.gdn.x.scheduler.service.common.helper.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gdn.x.scheduler.service.domain.TaskCommandService;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public class CommonUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);
	
	private static Properties properties;
	private static boolean propLoaded = false;
	
	static {
		properties = new Properties();
		try {
			properties.load(new FileInputStream(
					System.getenv("X_CONF_DIR") + "/x-scheduler.properties")
			);	
			propLoaded = true;
		} catch (IOException ioe) {
			LOGGER.error(ioe.getMessage(), ioe);
			propLoaded = false;
		}		
	}

	public static String getMachineId() {
		if (propLoaded) {
			return properties.getProperty(XSchedulerProperties.MACHINE_ID.key());
		} 
		if (System.getenv(TaskCommandService.MACHINE_ID) != null) {
			return System.getenv(TaskCommandService.MACHINE_ID);
		} 			
		
		return "NOT-SET";
	}
	
	public static String getUploadDir() {
		if (propLoaded) {
			return properties.getProperty(XSchedulerProperties.UPLOAD_DIR.key());
		}
		
		if (System.getenv(TaskCommandService.UPLOAD_DIR) != null) {
			return System.getenv(TaskCommandService.UPLOAD_DIR);
		}
		
		return "NOT-SET";
	}
	
	public enum XSchedulerProperties {
		MACHINE_ID("machineId"),
		UPLOAD_DIR("uploadDir");
		
		private String key;
		
		private XSchedulerProperties(String key) {
			this.key = key;
		}
		
		public String key() {
			return key;
		}
		
		@Override
		public String toString() {
			return name();
		}
	}
}