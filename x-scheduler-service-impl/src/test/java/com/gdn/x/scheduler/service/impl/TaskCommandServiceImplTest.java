package com.gdn.x.scheduler.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.constant.WSMethod;
import com.gdn.x.scheduler.constant.WSRequestHeader;
import com.gdn.x.scheduler.dao.TaskDAO;
import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.model.WebServiceCommand;
import com.gdn.x.scheduler.service.domain.CommandQueryService;
import com.gdn.x.scheduler.service.domain.TaskCommandService;
import com.gdn.x.scheduler.service.domain.impl.CommandQueryServiceImpl;
import com.gdn.x.scheduler.service.domain.impl.TaskCommandServiceImpl;
import com.gdn.x.scheduler.service.schedengine.CoreEngine;

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
@PrepareForTest(TaskCommandService.class)
public class TaskCommandServiceImplTest {
	
	private static final String URL = "http://md5.jsontest.com/?text=yauritux";
	private static final String STORE_ID = "store-123";

	//SUT
	private TaskCommandService taskService;
	
	//Collaborators
	private CommandQueryService commandQueryService;
	private CoreEngine<Task> coreEngine;
	private TaskDAO taskDAO;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		commandQueryService = mock(CommandQueryServiceImpl.class, withSettings().name("Command Query Service").verboseLogging());
		coreEngine = mock(CoreEngine.class, withSettings().name("Core Scheduler Engine").verboseLogging());		
		taskDAO = mock(TaskDAO.class, withSettings().name("Task DAO").verboseLogging());		
		
		taskService = spy(new TaskCommandServiceImpl(commandQueryService, coreEngine, taskDAO));
	}
	
	@Test(timeout = 1000)
	public void save_taskIsNull_nullIsReturned() {
		assertNull(taskService.save(buildTaskSample("1")));
	}
	
	@Test(timeout = 1000)
	public void save_everythingNormal_newTaskIsReturned() {
		Task task = buildTaskSample("1");
		when(taskDAO.save(any(Task.class))).thenReturn(task);
		assertEquals("1", taskService.save(task).getId());
	}
	
	@Test(timeout = 1000)
	public void delete_taskIsNull_falseIsReturned() {
		assertFalse(taskService.delete(null));
	}
	
	@Test(timeout = 1000)
	public void delete_taskAlreadyMarkedAsDelete_falseIsReturned() {
		Task task = buildTaskSample("1");
		task.setMarkForDelete(true);
		assertFalse(taskService.delete(task));
	}
	
	@Test(timeout = 1000)
	public void delete_everythingNormal_trueIsReturned() {
		Task task = buildTaskSample("1");
		when(taskDAO.deleteTask(task.getId())).thenReturn(1);
		assertTrue(taskService.delete(task));
	}
	
	@Test(timeout = 1000)
	public void delete_noRecordsDeleted_falseIsReturned() {
		Task task = buildTaskSample("1");
		when(taskDAO.deleteTask(task.getId())).thenReturn(0);
		assertFalse(taskService.delete(task));
	}
	
	@Test(timeout = 1000, expected = Exception.class)
	public void delete_exceptionRaisedOnDBOperation_exceptionThrown() {
		Task task = buildTaskSample("1");
		when(taskDAO.deleteTask(task.getId()))
			.thenThrow(new Exception("An exception occured while trying to delete the data!"));
		taskService.delete(task);
	}
	
	@Test(timeout = 100)
	public void restore_taskIsNull_falseIsReturned() {
		assertFalse(taskService.restore(null));
	}
	
	@Test(timeout = 100)
	public void restore_taskNotMarkedAsDelete_falseIsReturned() {
		Task task = buildTaskSample("1");
		assertFalse(taskService.restore(task));
	}
	
	@Test(timeout = 1000)
	public void restore_everythingNormal_trueIsReturned() {
		Task task = buildTaskSample("1");
		task.setMarkForDelete(true);
		when(taskDAO.restoreTask(task.getId())).thenReturn(1);
		assertTrue(taskService.restore(task));
	}
	
	@Test(timeout = 1000)
	public void restore_noTaskRestored_falseIsReturned() {
		Task task = buildTaskSample("1");
		task.setMarkForDelete(true);
		when(taskDAO.restoreTask(task.getId())).thenReturn(0);
		assertFalse(taskService.restore(task));
	}
	
	@Test(timeout = 1000, expected = Exception.class)
	public void restore_exceptionRaisedOnRestoration_exceptionIsThrown() {
		Task task = buildTaskSample("1");
		task.setMarkForDelete(true);
		when(taskDAO.restoreTask(task.getId())).thenThrow(new Exception("Cannot restore data!"));
		taskService.restore(task);
	}
	
	@Test(timeout = 100)
	public void batchDelete_nullEntities_zeroIsReturned() {
		assertEquals(0, taskService.batchDelete(null));
	}
	
	@Test(timeout = 100)
	public void batchDelete_emptyEntities_zeroIsReturned() {
		assertEquals(0, taskService.batchDelete(new ArrayList<Task>()));
	}
	
	@Test(timeout = 1000)
	public void batchDelete_allEntitiesNull_zeroIsReturned() {
		assertEquals(0, taskService.batchDelete(buildNullTaskEntities()));
	}
	
	@Test(timeout = 1000)
	public void batchDelete_allEntitiesNull_taskDAOShouldNeverGetCalled() {
		List<Task> tasks = buildNullTaskEntities();
		taskService.batchDelete(tasks);
		verify(taskDAO, never()).deleteTask(any(String.class));
	}
	
	@Test(timeout = 1000)
	public void batchDelete_allEntitiesAlreadyMarkedAsDelete_zeroIsReturned() {
		List<Task> tasks = buildAllTaskEntitiesMarkedAsDelete();
		assertEquals(0, taskService.batchDelete(tasks));
	}
	
	@Test(timeout = 1000)
	public void batchDelete_allEntitiesAlreadyMarkedAsDelete_taskDAOShouldNeverGetCalled() {
		List<Task> tasks = buildAllTaskEntitiesMarkedAsDelete();
		taskService.batchDelete(tasks);
		verify(taskDAO, never()).deleteTask(any(String.class));
	}
	
	@Test(timeout = 1000)
	public void batchDelete_everythingNormal_numOfAffectedEqualsToNumOfEntities() {
		List<Task> tasks = buildTaskEntities();
		when(taskDAO.deleteTask(any(String.class))).thenReturn(1);
		assertEquals(tasks.size(), taskService.batchDelete(tasks));
	}
	
	@Test(timeout = 1000)
	public void batchDelete_someRecordsAlreadyMarkedAsDelete_numOfAffectedLessThanNumOfEntities() {
		List<Task> tasks = buildPartialDeletedTaskEntities();
		when(taskDAO.deleteTask(any(String.class))).thenReturn(1);
		assertTrue(taskService.batchDelete(tasks) < tasks.size());
	}
	
	@Test(timeout = 1000, expected = Exception.class)
	public void batchDelete_exceptionRaisedOnDBDeletion_exceptionCaught() {
		List<Task> tasks = buildTaskEntities();
		when(taskDAO.deleteTask(any(String.class))).thenThrow(new Exception("Exception occured while trying to delete record."));
		taskService.batchDelete(tasks);
	}
	
	@Test(timeout = 100)
	public void batchRestore_nullEntities_zeroIsReturned() {
		assertEquals(0, taskService.batchRestore(null));
	}
	
	@Test(timeout = 100)
	public void batchRestore_emptyEntities_zeroIsReturned() {
		assertEquals(0, taskService.batchRestore(new ArrayList<Task>()));
	}
	
	@Test(timeout = 1000)
	public void batchRestore_allTaskEntitiesContainNull_zeroIsReturned() {
		assertEquals(0, taskService.batchRestore(buildNullTaskEntities()));
	}
	
	@Test(timeout = 1000)
	public void batchRestore_allTaskEntitiesContainNull_taskDAOShouldNeverGetCalled() {
		taskService.batchRestore(buildNullTaskEntities());
		verify(taskDAO, never()).restoreTask(any(String.class));
	}
	
	@Test(timeout = 1000)
	public void batchRestore_allTaskEntitiesNotMarkedAsDelete_zeroIsReturned() {
		assertEquals(0, taskService.batchRestore(buildTaskEntities()));
	}
	
	@Test(timeout = 1000)
	public void batchRestore_allTaskEntitiesNotMarkedAsDelete_taskDAOShouldNeverGetCalled() {
		taskService.batchRestore(buildTaskEntities());
		verify(taskDAO, never()).restoreTask(any(String.class));
	}
	
	@Test(timeout = 1000)
	public void batchRestore_everythingNormal_numOfAffectedEqualsToTotalEntities() {
		List<Task> tasks = buildAllTaskEntitiesMarkedAsDelete();
		when(taskDAO.restoreTask(any(String.class))).thenReturn(1);
		assertEquals(tasks.size(), taskService.batchRestore(tasks));
	}
	
	@Test(timeout = 1000)
	public void batchRestore_someEntitiesNotMarkedAsDelete_numOfAffectedLessThanTotalEntities() {
		List<Task> tasks = buildPartialDeletedTaskEntities();
		when(taskDAO.restoreTask(any(String.class))).thenReturn(1);
		assertTrue(taskService.batchRestore(tasks) < tasks.size());
	}
	
	private Task buildTaskSample(String id) {
		Task task = new Task();
		task.setId(id);
		task.setCommand(buildWSCommandSample(id));
		task.setSeconds("0/15");
		task.setMinutes("*");
		task.setHours("?");
		task.setDayOfMonth("*");
		task.setMonth("*");
		task.setDayOfWeek("L");
		task.setYear("*");
		task.setCreatedBy("yauritux");
		task.setCreatedDate(new Date());
		task.setMarkForDelete(false);
		task.setStoreId(STORE_ID);
		task.setTaskName("MD5 Generator");
		task.setUpdatedBy("yauritux");
		task.setUpdatedDate(new Date());
		return task;
	}
	
	private WebServiceCommand buildWSCommandSample(String id) {
		WebServiceCommand command = new WebServiceCommand();
		command.setId(id);
		command.setCommandType(CommandType.WEB_SERVICE);
		command.setCommand("{\"" + WSRequestHeader.URL.label() + "\":\"" + URL + "\",\"" 
				+ WSRequestHeader.METHOD.label() + "\":\""+ WSMethod.GET.name() + "\"}");
		command.setContents("");
		command.setCreatedBy("yauritux");
		command.setCreatedDate(new Date());
		command.setMarkForDelete(false);
		command.setParameters("");
		command.setStoreId(STORE_ID);
		command.setUpdatedBy("yauritux");
		command.setUpdatedDate(new Date());
		
		return command;
	}
	
	private List<Task> buildNullTaskEntities() {
		List<Task> tasks = new ArrayList<Task>();
		for (int i = 1; i <= 9; i++) {
			tasks.add(null);
		}
		return tasks;
	}
	
	private List<Task> buildTaskEntities() {
		List<Task> tasks = new ArrayList<Task>();
		for (int i = 1; i <= 9; i++) {
			tasks.add(buildTaskSample(Integer.toString(i)));
		}
		return tasks;
	}
	
	private List<Task> buildPartialDeletedTaskEntities() {
		List<Task> tasks = new ArrayList<Task>();
		for (int i = 1; i <= 5; i++) {
			tasks.add(buildTaskSample(Integer.toString(i)));
		}
		for (int i = 6; i <= 9; i++) {
			Task task = buildTaskSample(Integer.toString(i));
			task.setMarkForDelete(true);
			tasks.add(task);
		}
		
		return tasks;
	}
	
	private List<Task> buildAllTaskEntitiesMarkedAsDelete() {
		List<Task> tasks = new ArrayList<Task>();
		for (int i = 1; i <= 9; i++) {
			Task task = buildTaskSample(Integer.toString(i));
			task.setMarkForDelete(true);
			tasks.add(task);
		}
		return tasks;
	}
}