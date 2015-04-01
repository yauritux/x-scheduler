package com.gdn.x.scheduler.service.schedengine.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.constant.WSMethod;
import com.gdn.x.scheduler.constant.WSRequestHeader;
import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.model.WebServiceCommand;

/**
 * 
 * @author yauritux@gmail.com
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(MethodInvokingEngine.class)
@PowerMockIgnore("org.slf4j.Logger")
@SuppressStaticInitializationFor("org.slf4j.Logger")
public class MethodInvokingEngineTest {
	
	private static final String STORE_ID = "store-123";
	private static final String URL = "http://www.google.com";

	// SUT
	private MethodInvokingEngine methodInvokingEngine;
	
	// Collaborators
	private BeanFactory mockBeanFactory;
	private SchedulerFactoryBean mockSchedulerFactoryBean;
	
	@Before
	public void setup() {
		mockBeanFactory = mock(BeanFactory.class, withSettings().name("Bean Factory"));
		mockSchedulerFactoryBean = mock(SchedulerFactoryBean.class, withSettings().name("Scheduler Factory Bean"));
		methodInvokingEngine = spy(new MethodInvokingEngine(mockSchedulerFactoryBean));
	}
	
	@Test(timeout = 1000)
	public void scheduleJob_MethodInvokingJobDetailFactoryBeanIsBeingInstantiated() throws Exception {
		MethodInvokingJobDetailFactoryBean mockMethodInvokingJobDetailFactoryBean
			= mock(MethodInvokingJobDetailFactoryBean.class);
		whenNew(MethodInvokingJobDetailFactoryBean.class).withNoArguments().thenReturn(
				mockMethodInvokingJobDetailFactoryBean);
		methodInvokingEngine.scheduleJob(any(Task.class));
		verifyNew(MethodInvokingJobDetailFactoryBean.class, atMost(1));
	}
	
	@Test(timeout = 1000)
	public void scheduleJob_CronTriggerFactoryBeanIsBeingInstantiated() throws Exception {
		Task task = new Task();
		task.setStartDate(new Date());
		CronTriggerFactoryBean mockCronTrigger = mock(CronTriggerFactoryBean.class);
		whenNew(CronTriggerFactoryBean.class).withNoArguments().thenReturn(mockCronTrigger);
		methodInvokingEngine.scheduleJob(task);
		verifyNew(CronTriggerFactoryBean.class, atMost(1));
	}
	
	@Test(timeout = 1000)
	public void scheduleJob_scheduleJobIsCalled() throws Exception {
		TaskExecutorImpl mockTaskExecutor = mock(TaskExecutorImpl.class);
		when(mockBeanFactory.getBean("taskExecutor", TaskExecutorImpl.class)).thenReturn(mockTaskExecutor);
		methodInvokingEngine.setBeanFactory(mockBeanFactory);		
		Scheduler mockScheduler = mock(Scheduler.class);
		when(mockSchedulerFactoryBean.getScheduler()).thenReturn(mockScheduler);
		methodInvokingEngine.scheduleJob(buildTaskSample("1", new Date()));
		verify(mockScheduler, atLeastOnce()).scheduleJob(any(JobDetail.class), any(CronTrigger.class));
	}
	
	@Test(timeout = 1000)
	public void scheduleJob_schedulerNotStartedYet_startTheScheduler() throws Exception {
		TaskExecutorImpl mockTaskExecutor = mock(TaskExecutorImpl.class);
		when(mockBeanFactory.getBean("taskExecutor", TaskExecutorImpl.class)).thenReturn(mockTaskExecutor);
		methodInvokingEngine.setBeanFactory(mockBeanFactory);
		Scheduler mockScheduler = mock(Scheduler.class);
		when(mockSchedulerFactoryBean.getScheduler()).thenReturn(mockScheduler);
		when(mockScheduler.isStarted()).thenReturn(false);
		methodInvokingEngine.scheduleJob(buildTaskSample("1", new Date()));
		verify(mockScheduler, atLeastOnce()).start();
	}
	
	@Test(timeout = 1000)
	public void scheduleJob_getCronExpressionFromTaskIsCalled() throws Exception {
		TaskExecutorImpl mockTaskExecutor = mock(TaskExecutorImpl.class);
		when(mockBeanFactory.getBean("taskExecutor", TaskExecutorImpl.class)).thenReturn(mockTaskExecutor);
		methodInvokingEngine.setBeanFactory(mockBeanFactory);
		Scheduler mockScheduler = mock(Scheduler.class);
		when(mockSchedulerFactoryBean.getScheduler()).thenReturn(mockScheduler);
		Task task = buildTaskSample("1", new Date());
		methodInvokingEngine.scheduleJob(task);
		PowerMockito.verifyPrivate(methodInvokingEngine, atLeastOnce()).invoke("getCronExpressionFromTask", task);
	}
	
	private Task buildTaskSample(String id, Date startingDate) {
		Task task = new Task();
		task.setId(id);
		task.setCommand(buildWSCommandSample(id));
		task.setSeconds("0/15");
		task.setMinutes("*");
		task.setHours("*");
		task.setDayOfMonth("*");
		task.setMonth("*");
		task.setDayOfWeek("?");
		task.setYear("*");
		task.setPriority(1);
		task.setStartDate(startingDate);
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
}