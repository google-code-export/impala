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

import java.util.List;

import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.spring.service.ContributionEndpointTargetSource;
import org.springframework.beans.factory.FactoryBean;

/**
 * Supports retrieving of target object from service registry for filter
 * Implements {@link ContributionEndpointTargetSource}
 * @author Phil Zoio
 */
public class FilteredServiceRegistryTargetSource implements ContributionEndpointTargetSource {

	private Class<?> clazz;
	private final ServiceReferenceFilter filter;
	private final ServiceRegistry serviceRegistry;

	public FilteredServiceRegistryTargetSource(
			Class<?> clazz,
			ServiceReferenceFilter filter, 
			ServiceRegistry serviceRegistry) {
		super();
		this.clazz = clazz;
		this.filter = filter;
		this.serviceRegistry = serviceRegistry;
	}

	/* *************** TargetSource implementations ************** */
	
	/**
	 * Attempts to return the target object from the service registry, using the provided bean name
	 * First looks up a {@link ServiceRegistryReference} instance. If one is found, and the
	 * contained bean is a {@link FactoryBean}, will dereference this using {@link FactoryBean#getObject()}.
	 * Otherwise, simply returns the bean held by the {@link ServiceRegistryReference}.
	 * 
	 * Each time {@link #getTarget()} is called, the object is looked up from the service registry.
	 * No cacheing is involved.
	 */
	public Object getTarget() throws Exception {
		
		//FIXME abstract into common superclass
		ServiceRegistryReference reference = getServiceRegistryReference();
		if (reference != null) {
			Object bean = reference.getBean();
			if (bean instanceof FactoryBean) {
				FactoryBean fb = (FactoryBean) bean;
				return fb.getObject();
			}
			return bean;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Class getTargetClass() {
		return null;
	}

	public boolean isStatic() {
		//AOP frameworks should not cache returned
		return false;
	}	
	
	public void releaseTarget(Object target) throws Exception {
		//no need to explicitly release as objects of this class don't maintain
		//any reference to target object
	}
	
	/* *************** ContributionEndpointTargetSource implementations ************** */

	public ServiceRegistryReference getServiceRegistryReference() {
		
		//returns list of services matching filter
		final List<ServiceRegistryReference> filteredServices = serviceRegistry.getServices(filter);
		if (filteredServices.isEmpty()) {
			return null;
		}
		else {
			for (ServiceRegistryReference serviceRegistryReference : filteredServices) {
				final Object bean = serviceRegistryReference.getBean();
				if (clazz.isAssignableFrom(bean.getClass())) {
					return serviceRegistryReference;
				}
			}
		}
		return null;
	}

}
