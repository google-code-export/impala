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

package org.impalaframework.spring.module.application;

import org.impalaframework.module.spi.Application;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.Assert;

/**
 * Interface for allowing {@link Application} instance to be injected into bean.
 * @author Phil Zoio
 */
public class ApplicationAwarePostProcessor implements BeanPostProcessor {

    private final Application application;
    
    public ApplicationAwarePostProcessor( Application application) {
        Assert.notNull(application, "application cannot be null");
        this.application = application;
    }
    
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        if (bean instanceof ApplicationAware) {
            ((ApplicationAware) bean).setApplication(application);
        }
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }
}
