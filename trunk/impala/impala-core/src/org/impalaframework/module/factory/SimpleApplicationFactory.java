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

package org.impalaframework.module.factory;

import org.impalaframework.module.application.ImpalaApplication;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ApplicationFactory;
import org.impalaframework.module.spi.ClassLoaderRegistry;
import org.impalaframework.module.spi.ClassLoaderRegistryFactory;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.module.spi.ModuleStateHolderFactory;
import org.impalaframework.module.spi.ServiceRegistryFactory;
import org.impalaframework.service.ServiceRegistry;
import org.springframework.util.Assert;

/**
 * Creates {@link Application} instance, specifically instantiating an
 * {@link ImpalaApplication} using the {@link ClassLoaderRegistry},
 * {@link ModuleStateHolder} and {@link ServiceRegistry} obtained via the
 * wired-in {@link ClassLoaderRegistryFactory}, {@link ModuleStateHolderFactory}
 * and {@link ServiceRegistryFactory}, respectively.
 * @author Phil Zoio
 */
public class SimpleApplicationFactory implements ApplicationFactory {
    
    private ClassLoaderRegistryFactory classLoaderRegistryFactory;
    
    private ModuleStateHolderFactory moduleStateHolderFactory;
    
    private ServiceRegistryFactory serviceRegistryFactory;
    
    /**
     * Returns the {@link Application} instance set up in {@link #afterPropertiesSet()}.
     */
    public Application newApplication(String id) { 
            
        Assert.notNull(classLoaderRegistryFactory, "classLoaderRegistryFactory cannot be null");
        Assert.notNull(moduleStateHolderFactory, "moduleStateHolderFactory cannot be null");
        Assert.notNull(serviceRegistryFactory, "serviceRegistryFactory cannot be null");
        
        ClassLoaderRegistry classLoaderRegistry = classLoaderRegistryFactory.newClassLoaderRegistry();
        ModuleStateHolder moduleStateHolder = moduleStateHolderFactory.newModuleStateHolder();
        ServiceRegistry serviceRegistry = serviceRegistryFactory.newServiceRegistry();
        Application application = new ImpalaApplication(classLoaderRegistry, moduleStateHolder, serviceRegistry, id);
    
        return application;
    }

    public void setClassLoaderRegistryFactory(ClassLoaderRegistryFactory classLoaderRegistryFactory) {
        this.classLoaderRegistryFactory = classLoaderRegistryFactory;
    }

    public void setModuleStateHolderFactory(ModuleStateHolderFactory moduleStateHolderFactory) {
        this.moduleStateHolderFactory = moduleStateHolderFactory;
    }

    public void setServiceRegistryFactory(ServiceRegistryFactory serviceRegistryFactory) {
        this.serviceRegistryFactory = serviceRegistryFactory;
    }
    
}
