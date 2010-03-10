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

package org.impalaframework.module.spi;

import org.impalaframework.service.ServiceRegistry;

/**
 * Holds mutable application state held by the Impala runtime. The Impala runtime can 
 * manage multiple {@link Application} instances at a time, but typically only one of these will
 * be active at a time.
 * 
 * There are a number of potential use cases for the capability. One is to allow for seamless implementation of
 * module upgrades. Intead of changing the currently {@link Application} instance, a new {@link Application}
 * instance can be loaded and populated in the background. If and when this is loaded successfully, 
 * requests can be routed to this instance, after which the old instance can be shut down.
 * 
 * Another use case is to allow for multiple virtual applications hosted within the same web container.
 * 
 * @author Phil Zoio
 */
public interface Application {
    
    /**
     * Returns reference to {@link ClassLoaderRegistry} for current {@link Application} instance
     */
    public ClassLoaderRegistry getClassLoaderRegistry();
    
    /**
     * Returns reference to {@link ModuleStateHolder} for current {@link Application} instance
     */
    public ModuleStateHolder getModuleStateHolder();
    
    /**
     * Returns reference to {@link ServiceRegistry} for current {@link Application} instance
     */
    public ServiceRegistry getServiceRegistry();
    
    /**
     * Returns identifier for this {@link Application} instance
     */
    public String getId();

}
