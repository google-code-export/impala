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

package org.impalaframework.service.contribution;

import java.util.Collection;

import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.event.ServiceAddedEvent;
import org.impalaframework.service.event.ServiceRegistryEvent;
import org.impalaframework.service.event.ServiceRegistryEventListener;
import org.impalaframework.service.event.ServiceRemovedEvent;
import org.impalaframework.service.registry.ServiceRegistryAware;
import org.springframework.util.Assert;

/**
 * Class with responsibility for for logic of picking up changes to service registry and matching these against filter.
 * Delegates to {@link ServiceActivityNotifiable} once it has done it's job.
 * 
 * @author Phil Zoio
 */
public class ServiceRegistryMonitor implements 
	ServiceRegistryEventListener, 
	ServiceRegistryAware {
	
	//FIXME test
	private ServiceRegistry serviceRegistry;
	
	private ServiceActivityNotifiable serviceActivityNotifiable;
	
	/* **************** ServiceRegistryEventListener implementation *************** */

	public void handleServiceRegistryEvent(ServiceRegistryEvent event) {
		//add or remove from external contribution map
		if (event instanceof ServiceAddedEvent) {
			handleEventAdded(event);
		} else if (event instanceof ServiceRemovedEvent) {
			handleEventRemoved(event);
		}
	}
	
	/* **************** Initializing method *************** */
	
	public void init() throws Exception {
		Assert.notNull(serviceRegistry);
		Assert.notNull(serviceActivityNotifiable);
		
		ServiceReferenceFilter filter = serviceActivityNotifiable.getServiceReferenceFilter();
		Collection<ServiceRegistryReference> services = serviceRegistry.getServices(filter);
		for (ServiceRegistryReference serviceReference : services) {
			serviceActivityNotifiable.add(serviceReference);
		}
	}
	
	/* ******************* Private and package method ******************** */
	
	private void handleEventRemoved(ServiceRegistryEvent event) {
		ServiceRegistryReference ref = event.getServiceReference();
		
		//FIXME do we need to match before removing
		serviceActivityNotifiable.remove(ref);
	}

	private void handleEventAdded(ServiceRegistryEvent event) {
		ServiceRegistryReference ref = event.getServiceReference();
		handleEventAdded(ref);
	}

	private void handleEventAdded(ServiceRegistryReference serviceReference) {
		ServiceReferenceFilter filter = serviceActivityNotifiable.getServiceReferenceFilter();
		if (filter.matches(serviceReference)) {
			serviceActivityNotifiable.add(serviceReference);
		}
	}

	/* ******************* Protected getters ******************** */
	
	protected ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}

	/* ******************* Injected setters ******************** */

	public void setServiceActivityNotifiable(ServiceActivityNotifiable serviceActivityNotifiable) {
		this.serviceActivityNotifiable = serviceActivityNotifiable;
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}
	
}
