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

package org.impalaframework.module.application;

import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ClassLoaderRegistry;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.service.ServiceRegistry;
import org.springframework.util.Assert;

/**
 * Holds mutable application state held by the Impala runtime
 * @author Phil Zoio
 */
public class ImpalaApplication implements Application {
    
    private final ClassLoaderRegistry classLoaderRegistry;
    
    private final ModuleStateHolder moduleStateHolder;
    
    private final ServiceRegistry serviceRegistry;
    
    private final String id;
    
    public ImpalaApplication(
            ClassLoaderRegistry classLoaderRegistry,
            ModuleStateHolder moduleStateHolder, 
            ServiceRegistry serviceRegistry,
            String id) {
        
        super();

        Assert.notNull(classLoaderRegistry, "classLoaderRegistry cannot be null");
        Assert.notNull(moduleStateHolder, "moduleStateHolder cannot be null");
        Assert.notNull(serviceRegistry, "serviceRegistry cannot be null");
        
        this.classLoaderRegistry = classLoaderRegistry;
        this.moduleStateHolder = moduleStateHolder;
        this.serviceRegistry = serviceRegistry;
        this.id = id != null ? id : "";
    }

    public ClassLoaderRegistry getClassLoaderRegistry() {
        return classLoaderRegistry;
    }

    public ModuleStateHolder getModuleStateHolder() {
        return moduleStateHolder;
    }

    public ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }
    
    public String getId() {
        return id;
    }
    
}
