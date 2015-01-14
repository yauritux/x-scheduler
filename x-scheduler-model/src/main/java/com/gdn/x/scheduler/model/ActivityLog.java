package com.gdn.x.scheduler.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gdn.common.base.entity.GdnBaseEntity;
import com.gdn.x.scheduler.constant.Activity;

/**
 * 
 * @author yauritux
 *
 */
@Entity
@Table(name = "activity_log")
public class ActivityLog extends GdnBaseEntity {

	private static final long serialVersionUID = 7621347865785168510L;

	@Enumerated(EnumType.STRING)
	private Activity name;
	
	@Column(name = "description", nullable = true, length = 255)
	private String description;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "log_date", nullable = false)
	private Date date;
	
	@Column(name = "job_class", nullable = false, length = 255)
	private String jobClass;
	
	public ActivityLog() {}

	public Activity getName() {
		return name;
	}

	public void setName(Activity name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getJobClass() {
		return jobClass;
	}

	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}
}
