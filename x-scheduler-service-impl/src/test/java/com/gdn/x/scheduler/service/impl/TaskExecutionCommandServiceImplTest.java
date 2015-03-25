package com.gdn.x.scheduler.service.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.withSettings;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.constant.ProcessStatus;
import com.gdn.x.scheduler.constant.WSMethod;
import com.gdn.x.scheduler.constant.WSRequestHeader;
import com.gdn.x.scheduler.dao.TaskExecutionDAO;
import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.model.TaskExecution;
import com.gdn.x.scheduler.model.WebServiceCommand;
import com.gdn.x.scheduler.service.domain.TaskExecutionCommandService;
import com.gdn.x.scheduler.service.domain.impl.TaskExecutionCommandServiceImpl;

/**
 * 
 * @author yauritux@gmail.com
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("org.slf4j.Logger")
@SuppressStaticInitializationFor("org.slf4j.Logger")
public class TaskExecutionCommandServiceImplTest {
	
	private static final String URL = "http://md5.jsontest.com/?text=yauritux";
	private static final String STORE_ID = "store-123";	
	
	//SUT
	private TaskExecutionCommandService taskExecutionCommandService;
	
	//Collaborator
	private TaskExecutionDAO taskExecutionDAO;
	
	@Before
	public void setup() {
		taskExecutionDAO = mock(TaskExecutionDAO.class, withSettings().name("TaskExecutionDAO"));
		taskExecutionCommandService = spy(new TaskExecutionCommandServiceImpl(taskExecutionDAO));
	}
	
	@Test(timeout = 1000)
	public void save_entityIsNull_nullIsReturned() {
		assertNull(taskExecutionCommandService.save(null));
	}
	
	@Test(timeout = 1000)
	public void save_everythingNormal_newTaskExecutionIsGeneratedAndReturned() {
		TaskExecution te = buildTaskExecutionSample("1");
		when(taskExecutionDAO.save(te)).thenReturn(te);
		assertNotNull(taskExecutionCommandService.save(te));
	}
	
	@Test(timeout = 1000)
	public void createTaskExecutionFromTask_taskIsNull_noTaskExecutionCreatedNullIsReturned() {
		assertNull(taskExecutionCommandService.createTaskExecutionFromTask(null, true));
	}
	
	@Test(timeout = 1000)
	public void createTaskExecutionFromTask_createTaskExecutionWithoutPersisted_newTaskExecutionIsGeneratedAndReturned() {
		assertNotNull(taskExecutionCommandService.createTaskExecutionFromTask(buildTaskSample("1"), false));
	}
	
	@Test(timeout = 1000)
	public void createTaskExecutionFromTask_createTaskExecutionWithoutPersisted_taskExecutionDAOSaveNeverGetCalled() {
		taskExecutionCommandService.createTaskExecutionFromTask(buildTaskSample("1"), false);
		verify(taskExecutionDAO, never()).save(any(TaskExecution.class));
	}
	
	@Test(timeout = 1000)
	public void createTaskExecutionFromTask_createTaskExecutionWithPersisted_taskExecutionDAOSaveShouldGetCalledOnce() {
		taskExecutionCommandService.createTaskExecutionFromTask(buildTaskSample("1"), true);
		verify(taskExecutionDAO, atLeastOnce()).save(any(TaskExecution.class));
	}
	
	private TaskExecution buildTaskExecutionSample(String id) {
		TaskExecution taskExecution = new TaskExecution();
		taskExecution.setId(id);
		taskExecution.setCreatedBy("yauritux");
		taskExecution.setCreatedDate(new Date());
		taskExecution.setUpdatedBy("yauritux");
		taskExecution.setUpdatedDate(new Date());
		taskExecution.setMachineId("DEV_TEST");
		taskExecution.setMarkForDelete(false);
		taskExecution.setStart(new Date());
		taskExecution.setEnd(new Date());
		taskExecution.setStatus(ProcessStatus.FINISHED);
		taskExecution.setStoreId(STORE_ID);
		taskExecution.setTask(new Task());
		return taskExecution;
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
		task.setTaskName("MD5 Generateor");
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
}