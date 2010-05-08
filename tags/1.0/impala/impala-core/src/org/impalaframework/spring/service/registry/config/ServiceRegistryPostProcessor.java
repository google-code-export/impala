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

package org.impalaframework.spring.service.registry.config;

import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.registry.ServiceRegistryAware;
import org.impalaframework.spring.service.ProxyFactoryCreator;
import org.impalaframework.spring.service.proxy.ProxyFactoryCreatorAware;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.Assert;

/**
 * Implementation of {@link BeanPostProcessor}, which for each bean passed to
 * {@link #postProcessBeforeInitialization(Object, String)}, checks whether it
 * implements {@link ServiceRegistryAware}. If so, calls the bean's
 * {@link ServiceRegistryAware} method.
 * 
 * @author Phil Zoio
 */
public class ServiceRegistryPostProcessor implements BeanPostProcessor {
    
    private final ServiceRegistry serviceRegistry;
    
    private final ProxyFactoryCreator serviceProxyFactoryCreator;

    public ServiceRegistryPostProcessor(ServiceRegistry serviceRegistry, ProxyFactoryCreator serviceProxyFactoryCreator) {
        Assert.notNull(serviceRegistry);
        this.serviceRegistry = serviceRegistry;
        this.serviceProxyFactoryCreator = serviceProxyFactoryCreator;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ServiceRegistryAware) {
            ServiceRegistryAware psa = (ServiceRegistryAware) bean;
            psa.setServiceRegistry(serviceRegistry);
        }
        if (bean instanceof ProxyFactoryCreatorAware) {
            ProxyFactoryCreatorAware spa = (ProxyFactoryCreatorAware) bean;
            spa.setProxyFactoryCreator(serviceProxyFactoryCreator);
        }
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String name) {
        return bean;
    }
}
