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

import org.impalaframework.service.ContributionEndpointTargetSource;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryReference;
import org.springframework.beans.factory.FactoryBean;

/**
 * Supports retrieving of target object from service registry for particular bean name
 * @author Phil Zoio
 */
public class ServiceRegistryTargetSource implements ContributionEndpointTargetSource {

	private final String beanName;
	private final ServiceRegistry serviceRegistry;

	public ServiceRegistryTargetSource(String beanName, ServiceRegistry serviceRegistry) {
		super();
		this.beanName = beanName;
		this.serviceRegistry = serviceRegistry;
	}

	/* *************** TargetSource implementations ************** */
	
	public Object getTarget() throws Exception {
		ServiceRegistryReference service = serviceRegistry.getService(beanName);
		if (service != null) {
			Object bean = service.getBean();
			if (bean instanceof FactoryBean) {
				FactoryBean fb = (FactoryBean) bean;
				return fb.getObject();
			}
			return bean;
		}
		return null;
	}

	public ServiceRegistryReference getServiceRegistryReference() {
		return serviceRegistry.getService(beanName);
	}

	@SuppressWarnings("unchecked")
	public Class getTargetClass() {
		return null;
	}

	public boolean isStatic() {
		return true;
	}	
	
	public void releaseTarget(Object target) throws Exception {
		//no need to explicitly release as
	}
	
	/* *************** ContributionEndpointTargetSource implementations ************** */

	public boolean hasTarget() {
		return ( serviceRegistry.getService(beanName) != null);
	}


	public void deregisterTarget(Object bean) {		
	}
	
	public void registerTarget(Object bean) {		
	}

}
