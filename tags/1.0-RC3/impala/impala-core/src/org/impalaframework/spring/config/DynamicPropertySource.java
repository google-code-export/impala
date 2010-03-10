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

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.config.PropertySource;
import org.impalaframework.exception.ExecutionException;
import org.springframework.util.Assert;

/**
 * Implementation of {@link PropertySource} which uses {@link DynamicPropertiesFactoryBean} to load properties.
 * Holds an instance of {@link Properties} which is used as the source for returning a value when 
 * {@link #getValue(String)} is called.
 * 
 * @author Phil Zoio
 */
public class DynamicPropertySource extends BaseDynamicPropertySource {
    
    private static final Log logger = LogFactory.getLog(DynamicPropertySource.class);   

    private Properties properties;
    
    private DynamicPropertiesFactoryBean factoryBean;
    
    public synchronized String getValue(String name) {
        return properties.getProperty(name);
    }
    
    /* ********************* initializing bean implementation ******************** */

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(factoryBean);
        super.afterPropertiesSet();
    }
    
    /* ********************* disposable bean implementation ******************** */
   
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
    
    public void setFactoryBean(DynamicPropertiesFactoryBean factoryBean) {
        this.factoryBean = factoryBean;
    } 

}
