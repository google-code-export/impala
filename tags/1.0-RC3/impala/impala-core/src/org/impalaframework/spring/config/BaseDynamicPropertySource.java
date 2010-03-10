/*
 * Copyright 2007-2010 the original author or authors.
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

package org.impalaframework.spring.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.config.PropertySource;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Implementation of {@link PropertySource} sets up a {@link ScheduledExecutorService} to periodically
 * call update.
 * @author Phil Zoio
 */
public abstract class BaseDynamicPropertySource implements PropertySource, InitializingBean, Runnable, DisposableBean {
    
    private static final Log logger = LogFactory.getLog(BaseDynamicPropertySource.class);   
    
    private int reloadInterval = 100;
    
    private int reloadInitialDelay = 10;

    private ScheduledExecutorService executorService;
 
    /* ********************* initializing bean implementation ******************** */

    public void afterPropertiesSet() throws Exception {
        
        run();
        
        //if we don't already have an executor service, create a single threaded executor
        if (executorService == null) {
            logger.info("No executor service wired in for '" + this.getClass().getName() + "'. Creating new single threaded executor");
            executorService = Executors.newSingleThreadScheduledExecutor();
        }

        logger.info("Starting executor service for for '" + this.getClass().getName() + "'. Initial delay: " + reloadInitialDelay + " seconds, interval: " + reloadInterval + " seconds");
        executorService.scheduleWithFixedDelay(this, reloadInitialDelay, reloadInterval, TimeUnit.SECONDS);
    }
    
    /* ********************* disposable bean implementation ******************** */

    public void destroy() throws Exception {
        try {
            logger.info("Shutting down executor service for " + this.getClass().getName());
            executorService.shutdown();
        } catch (RuntimeException e) {
            logger.error("Error shutting down service for " + this.getClass().getName() + ": " + e.getMessage(), e);
        }
    }
    
    public void run() {
        update();
    }

    public abstract void update();

    public void setReloadInterval(int reloadInterval) {
        this.reloadInterval = reloadInterval;
    }

    public void setReloadInitialDelay(int reloadInitialDelay) {
        this.reloadInitialDelay = reloadInitialDelay;
    }

    public void setExecutorService(ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }   

}
