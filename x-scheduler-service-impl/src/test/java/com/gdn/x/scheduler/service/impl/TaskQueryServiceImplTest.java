package com.gdn.x.scheduler.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.withSettings;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.dao.TaskDAO;
import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.model.WebServiceCommand;
import com.gdn.x.scheduler.service.TaskQueryService;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("org.slf4j.Logger")
@SuppressStaticInitializationFor("org.slf4j.Logger")
public class TaskQueryServiceImplTest {

	//SUT
	private TaskQueryService taskService;
	
	//Collaborator
	private TaskDAO taskDAO;
	
	@Before
	public void setup() {
		taskDAO = mock(TaskDAO.class, withSettings().name("Task DAO").verboseLogging());
		taskService = spy(new TaskQueryServiceImpl(taskDAO));
	}
	
	@Test(timeout = 1000)
	public void findById_idIsNull_nullIsReturned() {
		assertNull(taskService.findById(null));
	}
	
	@Test(timeout = 1000) 
	public void findById_idExists_taskObjectIsReturned() {
		Task task = buildTaskSample("1");
		when(taskDAO.findById(task.getId())).thenReturn(task);
		assertNotNull(taskService.findById(task.getId()));
	}
	
	@Test(timeout = 1000)
	public void findByTaskNameLike_nullTaskName_fetchAllShouldBeCalled() {
		taskService.findByTaskNameLike(null);
		verify(taskDAO, atLeastOnce()).fetchAll();
	}
	
	@Test(timeout = 1000)
	public void findByTaskNameLike_taskNameNotEmpty_findByTaskNameLikeGetCalled() {
		taskService.findByTaskNameLike("WS");
		verify(taskDAO, atLeastOnce()).findByTaskNameLike(any(String.class));
	}
	
	@Test(timeout = 1000)
	public void findByTaskNameLike_taskNameNotEmpty_fetchAllShouldNeverGetCalled() {
		taskService.findByTaskNameLike("WS");
		verify(taskDAO, never()).fetchAll();
	}
	
	@Test(timeout = 100)
	public void wrapTask_nullTask_nullIsReturned() {
		assertNull(taskService.wrapTask(null));
	}
	
	@Test(timeout = 1000)
	public void wrapTask_taskNotNull_taskResponseIdEqualsToTaskId() {
		Task task = buildTaskSample("3");
		assertEquals(task.getId(), taskService.wrapTask(task).getId());
	}
	
	@Test(timeout = 1000)
	public void findAll_everythingNormal_taskDAOFindAllShouldGetCalled() {
		taskService.findAll();
		verify(taskDAO, atLeastOnce()).findAll();
	}
	
	private Task buildTaskSample(String id) {
		Task task = new Task();
		task.setId(id);
		task.setTaskName("WS Task");
		task.setCommand(buildWSCommandSample(id));
		task.setMarkForDelete(false);
		
		return task;
	}
	
	private WebServiceCommand buildWSCommandSample(String id) {
		WebServiceCommand wsCommand = new WebServiceCommand();
		wsCommand.setId(id);
		wsCommand.setCommandType(CommandType.WEB_SERVICE);
		wsCommand.setMarkForDelete(false);
		return wsCommand;
	}
}
