package org.impalaframework.config;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.impalaframework.exception.ExecutionException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertiesFactoryBean;

public class DynamicPropertiesPropertySource implements PropertySource, InitializingBean, Runnable {
	
	private int reloadInterval = 100;
	
	private int reloadInitialDelay = 10;
	
	private PropertiesFactoryBean factoryBean;
	
	private Properties properties;
	
	public synchronized String getValue(String name) {
		return properties.getProperty(name);
	}

	public void afterPropertiesSet() throws Exception {
		factoryBean.setSingleton(false);
		run();
		
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleWithFixedDelay(this, reloadInitialDelay, reloadInterval, TimeUnit.SECONDS);
	}

	public synchronized void run() {
		if (properties != null) {
			System.out.println("Loading properties");
		}
		try {
			Properties props = (Properties) factoryBean.getObject();
			properties = props;
		} catch (IOException e) {
			if (properties != null) {
				//FIXME
				throw new ExecutionException("Unable to load properties", e);
			}
		}
	}

	public void setReloadInterval(int reloadInterval) {
		this.reloadInterval = reloadInterval;
	}

	public void setReloadInitialDelay(int reloadInitialDelay) {
		this.reloadInitialDelay = reloadInitialDelay;
	}

	public void setFactoryBean(PropertiesFactoryBean factoryBean) {
		this.factoryBean = factoryBean;
	}	

}
