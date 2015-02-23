package com.gdn.x.scheduler.service.domain.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gdn.x.scheduler.constant.ProcessStatus;
import com.gdn.x.scheduler.dao.TaskExecutionDAO;
import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.model.TaskExecution;
import com.gdn.x.scheduler.service.domain.TaskExecutionCommandService;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 * 
 * Service class that contains all logics to manipulate the TaskExecution data.
 * TaskExecution represents the history/log of every task those are being executed or have been executed.
 * Basically, this class represents command layer service of CQRS pattern.
 *
 */
@Service("taskExecutionCommandService")
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class TaskExecutionCommandServiceImpl implements
		TaskExecutionCommandService {
		
	private TaskExecutionDAO taskExecutionDAO;
	
	@Autowired
	public TaskExecutionCommandServiceImpl(
			TaskExecutionDAO taskExecutionDAO) {
		this.taskExecutionDAO = taskExecutionDAO;
	}

	@Override
	public TaskExecution save(TaskExecution entity) {
		if (entity == null) {
			return null;
		}
		return taskExecutionDAO.save(entity);
	}
	
	@Override
	public TaskExecution createTaskExecutionFromTask(Task task, boolean persist) {
		TaskExecution taskExecution = new TaskExecution();
		taskExecution.setCreatedBy(task.getCreatedBy());
		taskExecution.setCreatedDate(new Date());
		taskExecution.setStart(new Date());
		taskExecution.setEnd(null);
		taskExecution.setStatus(ProcessStatus.IN_PROGRESS);
		taskExecution.setTask(task);
		taskExecution.setStoreId(task.getStoreId());
		taskExecution.setMachineId(System.getenv(MACHINE_ID) != null ? System.getenv(MACHINE_ID) : "NOT-SET");
		
		if (persist) {
			taskExecution = this.save(taskExecution);
		}
		
		return taskExecution;
	}

	@Override
	public boolean delete(TaskExecution entity) {
		throw new UnsupportedOperationException("Unsupported on this major version.");
	}

	@Override
	public boolean restore(TaskExecution entity) {
		throw new UnsupportedOperationException("Unsupported on this major version.");
	}

	@Override
	public int batchDelete(List<TaskExecution> entities) {
		throw new UnsupportedOperationException("Unsupported on this major version.");
	}

	@Override
	public int batchRestore(List<TaskExecution> entities) {
		throw new UnsupportedOperationException("Unsupported on this major version.");
	}
}