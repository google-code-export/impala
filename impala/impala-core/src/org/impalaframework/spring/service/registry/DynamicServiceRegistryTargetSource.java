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

package org.impalaframework.spring.service.registry;

import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.spring.service.ContributionEndpointTargetSource;

/**
 * Supports retrieving of target object from service registry for particular bean name.
 * Implements {@link ContributionEndpointTargetSource}
 * @author Phil Zoio
 */
public class DynamicServiceRegistryTargetSource extends BaseServiceRegistryTargetSource {

	private final String beanName;
	private final ServiceRegistry serviceRegistry;
	private final Class<?>[] interfaces;

	public DynamicServiceRegistryTargetSource(String beanName, Class<?>[] interfaces, ServiceRegistry serviceRegistry) {
		super();
		this.beanName = beanName;
		this.serviceRegistry = serviceRegistry;
		this.interfaces = interfaces;
	}
	
	/* *************** ContributionEndpointTargetSource implementation ************** */

	public ServiceRegistryReference getServiceRegistryReference() {
		return serviceRegistry.getService(beanName, interfaces);
	}

}
