package com.gdn.x.scheduler.service.helper.schedengine;

import java.util.Map;

import org.quartz.Trigger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 
 * @author yauritux (yauritux@gmail.com)
 * @version 1.0.0.RC1
 * @since 1.0.0.RC1
 *
 */
public class AutoRegisteringSchedulerFactoryBean extends SchedulerFactoryBean
		implements BeanFactoryAware {
	
	private Map<String, Trigger> triggerBeans;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		DefaultListableBeanFactory listable = (DefaultListableBeanFactory) beanFactory;
		triggerBeans = listable.getBeansOfType(Trigger.class);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		setTriggers(triggerBeans.values().toArray(new Trigger[triggerBeans.size()]));
		super.afterPropertiesSet();
	}
}
