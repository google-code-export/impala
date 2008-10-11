/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.config;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.ExecutionException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Implementation of {@link PropertySource} which uses {@link DynamicPropertiesFactoryBean} to load properties.
 * Holds an instance of {@link Properties} which is used as the source for returning a value when 
 * {@link #getValue(String)} is called.
 * 
 * It also sets up a {@link ScheduledExecutorService} to periodically update the {@link Properties}.
 * @author Phil Zoio
 */
public class DynamicPropertySource implements PropertySource, InitializingBean, Runnable {
	
	private static final Log logger = LogFactory.getLog(DynamicPropertySource.class);	
	
	private int reloadInterval = 100;
	
	private int reloadInitialDelay = 10;

	private Properties properties;
	
	private DynamicPropertiesFactoryBean factoryBean;

	private ScheduledExecutorService executorService;
	
	public synchronized String getValue(String name) {
		return properties.getProperty(name);
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(factoryBean);
		
		run();
		
		//if we don't already have an executor service, create a single threaded executor
		if (executorService == null) {
			executorService = Executors.newSingleThreadScheduledExecutor();
		}
		
		executorService.scheduleWithFixedDelay(this, reloadInitialDelay, reloadInterval, TimeUnit.SECONDS);
	}
	
	public void run() {
		update();
	}

	public synchronized void update() {
		if (properties != null) {
			logger.info("Checking for updates to properties from " + factoryBean);
		}
		try {
			Properties props = (Properties) factoryBean.getObject();
			properties = props;
		} catch (IOException e) {
			if (properties != null) {
				throw new ExecutionException("Unable to load properties from " + factoryBean, e);
			}
		}
	}

	public void setReloadInterval(int reloadInterval) {
		this.reloadInterval = reloadInterval;
	}

	public void setReloadInitialDelay(int reloadInitialDelay) {
		this.reloadInitialDelay = reloadInitialDelay;
	}

	public void setFactoryBean(DynamicPropertiesFactoryBean factoryBean) {
		this.factoryBean = factoryBean;
	}

	public void setExecutorService(ScheduledExecutorService executorService) {
		this.executorService = executorService;
	}	

}
