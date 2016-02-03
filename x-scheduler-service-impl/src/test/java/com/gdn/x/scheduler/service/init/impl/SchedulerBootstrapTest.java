package com.gdn.x.scheduler.service.init.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.event.ContextRefreshedEvent;

import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.service.domain.TaskQueryService;
import com.gdn.x.scheduler.service.schedengine.CoreEngine;

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
public class SchedulerBootstrapTest {

	// SUT
	private SchedulerBootstrap schedulerBootstrap;
	
	// Collaborators
	private CoreEngine<Task> mockCoreEngine;
	private TaskQueryService mockTaskQueryService;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		mockCoreEngine = mock(CoreEngine.class, withSettings().name("Core Engine"));
		mockTaskQueryService = mock(TaskQueryService.class, withSettings().name("Task Query Service"));
		schedulerBootstrap = spy(new SchedulerBootstrap(mockCoreEngine, mockTaskQueryService));
	}
	
	@Test(timeout = 1000)
	public void onApplicationEvent_firstTimeCalled_allTaskFetchedFromDB() {
		schedulerBootstrap.onApplicationEvent(any(ContextRefreshedEvent.class));
		verify(mockTaskQueryService, atLeastOnce()).fetchAll();
	}
	
	@Test(timeout = 1000)
	public void onApplicationEvent_firstTimeCalled_jobIsScheduled() {
		when(mockTaskQueryService.fetchAll()).thenReturn(populateTaskSample());
		schedulerBootstrap.onApplicationEvent(any(ContextRefreshedEvent.class));		
		verify(mockCoreEngine, atMost(3)).scheduleJob(any(Task.class));
	}
	
	@Test(timeout = 1000)
	public void onApplicationEvent_secondTimeCalled_taskNotFetchedFromDB() {
		schedulerBootstrap.onApplicationEvent(any(ContextRefreshedEvent.class));
		schedulerBootstrap.onApplicationEvent(any(ContextRefreshedEvent.class));
		verify(mockTaskQueryService, never()).fetchAll();
	}
	
	@Test(timeout = 1000)
	public void onApplicationEvent_secondTimeCalled_noJobScheduleExecuted() {
		schedulerBootstrap.onApplicationEvent(any(ContextRefreshedEvent.class));
		schedulerBootstrap.onApplicationEvent(any(ContextRefreshedEvent.class));
		verify(mockCoreEngine, never()).scheduleJob(any(Task.class));
	}
	
	private List<Task> populateTaskSample() {
		List<Task> tasks = new ArrayList<>();
		tasks.add(new Task());
		tasks.add(new Task());
		tasks.add(new Task());
		return tasks;
	}
	
	@After
	public void tearDown() {
		mockCoreEngine = null;
		mockTaskQueryService = null;
		schedulerBootstrap = null;
	}
}
