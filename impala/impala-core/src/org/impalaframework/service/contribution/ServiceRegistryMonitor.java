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
    
    private ServiceRegistry serviceRegistry;
    
    private ServiceActivityNotifiable serviceActivityNotifiable;
    
    private Class<?>[] implementationTypes;
    
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
    
    public void init() {
        Assert.notNull(serviceRegistry);
        Assert.notNull(serviceActivityNotifiable);
        
        ServiceReferenceFilter filter = serviceActivityNotifiable.getServiceReferenceFilter();
        Collection<ServiceRegistryReference> services = serviceRegistry.getServices(filter, null);
        for (ServiceRegistryReference serviceReference : services) {
            if (matchesTypes(serviceReference)) {
            	serviceActivityNotifiable.add(serviceReference);
            }
        }
    }
    
    /* ******************* Private and package method ******************** */
    
    private void handleEventRemoved(ServiceRegistryEvent event) {
        ServiceRegistryReference ref = event.getServiceReference();
        
        //FIXME do we need to match before removing
        serviceActivityNotifiable.remove(ref);
    }

    void handleEventAdded(ServiceRegistryEvent event) {
        ServiceRegistryReference ref = event.getServiceReference();
        handleReferenceAdded(ref);
    }

    void handleReferenceAdded(ServiceRegistryReference serviceReference) {
        ServiceReferenceFilter filter = serviceActivityNotifiable.getServiceReferenceFilter();
        
        if (matchesTypes(serviceReference) && filter.matches(serviceReference)) {
            serviceActivityNotifiable.add(serviceReference);
        }
    }

	private boolean matchesTypes(ServiceRegistryReference serviceReference) {
		boolean matchable = true;
        
        //check export types
        if (implementationTypes != null && implementationTypes.length > 0) {

    		Class<? extends Object> beanClass = serviceReference.getBean().getClass();
        	for (int i = 0; i < implementationTypes.length; i++) {
				
        		if (!implementationTypes[i].isAssignableFrom(beanClass)) {
        			matchable = false;
        			break;
				}
			}
        }
		return matchable;
	}

    /* ******************* Protected getters ******************** */
    
    protected ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }

    /* ******************* Injected setters ******************** */

    public void setServiceActivityNotifiable(ServiceActivityNotifiable serviceActivityNotifiable) {
        this.serviceActivityNotifiable = serviceActivityNotifiable;
    }

	public void setImplementationTypes(Class<?>[] implementationTypes) {
		this.implementationTypes = implementationTypes;
	}

    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }
    
}
