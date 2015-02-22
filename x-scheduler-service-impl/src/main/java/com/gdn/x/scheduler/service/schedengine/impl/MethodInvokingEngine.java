package com.gdn.x.scheduler.service.schedengine.impl;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gdn.x.scheduler.model.Task;
import com.gdn.x.scheduler.service.schedengine.CoreEngine;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
@Service("methodInvokingEngine")
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class MethodInvokingEngine implements CoreEngine<Task>, BeanFactoryAware {
	
	private static final Logger LOG = LoggerFactory.getLogger(MethodInvokingEngine.class);
	
	private BeanFactory beanFactory;
	private SchedulerFactoryBean schedulerFactory;
	
	@Autowired
	public MethodInvokingEngine(SchedulerFactoryBean schedulerFactory) {
		this.schedulerFactory = schedulerFactory;
	}

	@Override
	public void scheduleJob(Task task) {		
		try {
			// set the task to run
			/* 
			 * since we are calling taskExecutor (which is prototype scope) 
			 * from the scope of this class (which is singleton by default)
			 * then we need to force the DI here, otherwise the prototype bean 
			 * won't be acting as it should. 
			 * see my comment on linkedIn discussion group 
			 * at https://www.linkedin.com/groupItem?view=&gid=46964&type=member&item=212949099&commentID=5971684518195658753
			 * &report.success=8ULbKyXO6NDvmoK7o030UNOYGZKrvdhBhypZ_w8EpQrrQI-BBjkmxwkEOwBjLE28YyDIxcyEO7_TA_giuRN#commentID_5971684518195658753 
			 * regarding this matter.
			 *  
			 */
			TaskExecutorImpl taskExecutorImpl = this.beanFactory.getBean("taskExecutor", TaskExecutorImpl.class);
			taskExecutorImpl.setTask(task);		
			
			// create job			
			MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
			jobDetail.setTargetObject(taskExecutorImpl);
			jobDetail.setTargetMethod("run");
			jobDetail.setName(task.getTaskName() + "-JOB");
			jobDetail.setGroup(task.getCommand().getCommandType().name());
			jobDetail.setConcurrent(false);
			jobDetail.afterPropertiesSet();
			
			// create trigger
			CronTriggerFactoryBean cronTrigger = new CronTriggerFactoryBean();
			cronTrigger.setBeanName(task.getTaskName());
			cronTrigger.setJobDetail((JobDetail) jobDetail.getObject());
			cronTrigger.setName(task.getTaskName() + "-TRIGGER");
			cronTrigger.setGroup(task.getCommand().getCommandType().name());
			cronTrigger.setCronExpression(getCronExpressionFromTask(task));
			cronTrigger.setStartTime(task.getStartDate()); // activate scheduler on specific date
			cronTrigger.afterPropertiesSet();
			
			Scheduler scheduler = schedulerFactory.getScheduler();
			
			// add to schedule
			if (!scheduler.isStarted()) {
				scheduler.start();
			}
			
			scheduler.scheduleJob(jobDetail.getObject(), cronTrigger.getObject());									
						
		} catch (NoSuchMethodException e) {
			LOG.error(e.getMessage(), e);		
		} catch (ClassNotFoundException e) {
			LOG.error(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}		
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	
	private String getCronExpressionFromTask(Task task) {
		StringBuilder builder = new StringBuilder();
		builder.append(task.getSeconds());
		builder.append(" ");
		builder.append(task.getMinutes());
		builder.append(" ");
		builder.append(task.getHours());
		builder.append(" ");
		builder.append(task.getDayOfMonth());
		builder.append(" ");
		builder.append(task.getMonth());
		builder.append(" ");
		builder.append(task.getDayOfWeek());
		if (task.getYear() != null && !task.getYear().isEmpty()) {
			builder.append(" ");
			builder.append(task.getYear());
		}
		
		return builder.toString();
	}
}
