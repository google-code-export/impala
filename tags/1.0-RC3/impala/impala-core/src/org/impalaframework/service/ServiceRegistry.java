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

package org.impalaframework.service;

import java.util.List;
import java.util.Map;


/**
 * Interface for shared registry for services used by Impala to share beans between modules.
 * 
 * @author Phil Zoio
 */
public interface ServiceRegistry extends ServiceEntryRegistry, ServiceEventListenerRegistry {
    
    /**
     * Registers a service in the Impala service registry. Equivalent to {@link #addService(String, String, Object, List, Map, ClassLoader)}
     * with the export types and attributes set to null.
     * @param beanName the name under which the service is registered. Can be null
     * @param moduleName the module from which the service is registered. Cannot be null
     * @param service the service instance
     * @param classLoader the classloader associated with the loading module
     */
    ServiceRegistryEntry addService(String beanName, String moduleName, ServiceBeanReference service, ClassLoader classLoader);  
    
}
