package com.gdn.x.scheduler.dao;

import org.springframework.data.repository.CrudRepository;

import com.gdn.x.scheduler.model.ActivityLog;

/**
 * 
 * @author yauritux
 *
 */
public interface ActivityLogDAO extends CrudRepository<ActivityLog, String> {
}
