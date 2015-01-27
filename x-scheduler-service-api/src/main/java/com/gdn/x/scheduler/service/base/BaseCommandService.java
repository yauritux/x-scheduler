package com.gdn.x.scheduler.service.base;

import java.util.List;

/**
 * 
 * @author yauritux (yauritux @gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public interface BaseCommandService<T> {

	public T save(T entity);
	public boolean delete(T entity);
	public boolean restore(T entity);
	public int batchDelete(List<T> entities);
	public int batchRestore(List<T> entities);
}
