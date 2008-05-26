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

package org.impalaframework.service.registry;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.impalaframework.service.registry.event.ServiceReferenceFilter;

public interface ServiceRegistry {

	void addService(String beanName, String moduleName, Object service);	

    void addService(String beanName, String moduleName, Object service, List<String> tags);
	
	void addService(String beanName, String moduleName, Object service, Map<String,?> attributes);

	void addService(String beanName, String moduleName, Object service, List<String> tags, Map<String,?> attributes);

	void remove(Object service);

	BasicServiceRegistryReference getService(String beanName);
	
	BasicServiceRegistryReference getService(String beanName, Class<?> type);
	
	Collection<BasicServiceRegistryReference> getServices(ServiceReferenceFilter filter);

}