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

package org.impalaframework.service;

import java.util.List;
import java.util.Map;

/**
 * Interface for shared registry for services used by Impala to share beans between modules.
 * 
 * @author Phil Zoio
 */
public interface ServiceRegistry {

	/**
	 * Registers a service in the Impala service registry. Equivalent to {@link #addService(String, String, Object, List, Map, ClassLoader)}
	 * with the export types and attributes set to null.
	 * @param beanName the name under which the service is registered. Can be null
	 * @param moduleName the module from which the service is registered. Cannot be null
	 * @param service the service instance
	 * @param classLoader the classloader associated with the loading module
	 */
	ServiceRegistryReference addService(String beanName, String moduleName, Object service, ClassLoader classLoader);	

	/**
	 * 
	 * @param beanName the name under which the service is registered. Can be null
	 * @param moduleName the module from which the service is registered. Cannot be null
	 * @param service the service instance
	 * @param exportTypes the export types against which the service will be registered
	 * @param attributes attributes associated with the service instance. Can be used to filter services when 
	 * looking up services from the service registry
	 * @param classLoader the classloader associated with the loading module
	 */
	ServiceRegistryReference addService(String beanName, String moduleName, Object service, 
			List<Class<?>> exportTypes, Map<String,?> attributes, ClassLoader classLoader);

	/**
	 * Removes a service instance from the service registry
	 */
	void remove(ServiceRegistryReference serviceReference);
	
	/**
	 * Evicts the services contributing from a particular module
	 */
	void evictModuleServices(String moduleName);

	/**
	 * Retrieves a service from the service registry
	 * @param beanName the name under which the service was registered
	 * @param exportTypes the export types for the service. The service must be class compatible with all of these
	 * types to be returned.
	 * @return a {@link ServiceRegistryReference} instance
	 */
	ServiceRegistryReference getService(String beanName, Class<?>[] exportTypes);
	
	/**
	 * Gets all services from the service registry which match the provided filter
	 * @param filter a {@link ServiceReferenceFilter} instance
	 * @return a list of service references.
	 */
	List<ServiceRegistryReference> getServices(ServiceReferenceFilter filter);

}