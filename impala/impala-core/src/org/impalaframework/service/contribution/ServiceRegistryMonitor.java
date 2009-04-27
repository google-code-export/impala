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
import org.impalaframework.service.ServiceRegistryEvent;
import org.impalaframework.service.ServiceRegistryEventListener;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.event.ServiceAddedEvent;
import org.impalaframework.service.event.ServiceRemovedEvent;
import org.impalaframework.service.registry.ServiceRegistryAware;
import org.springframework.util.Assert;

/**
 * Class with responsibility for for logic of picking up changes to service registry and matching these against filter.
 * Delegates to {@link ServiceActivityNotifiable} once it has done it's job.
 * 
 * {@link ServiceRegistryMonitor} implements the {@link ServiceRegistryEventListener}. However, it does not 
 * register itself as a listener. Instead, listener method calls are received from a delegator.
 * 
 * @author Phil Zoio
 */
public class ServiceRegistryMonitor implements 
    ServiceRegistryEventListener, 
    ServiceRegistryAware {
    
    private ServiceRegistry serviceRegistry;
    
    /**
     * Represents target which can be notified of service registry changes.
     */
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
    
    public void init() {
        Assert.notNull(serviceRegistry);
        Assert.notNull(serviceActivityNotifiable);
        
        Class<?>[] supportedTypes = serviceActivityNotifiable.getSupportedTypes();
        ServiceReferenceFilter filter = serviceActivityNotifiable.getServiceReferenceFilter();
        Collection<ServiceRegistryReference> services = serviceRegistry.getServices(filter, supportedTypes, false);
        for (ServiceRegistryReference serviceReference : services) {
            if (matchesTypes(serviceActivityNotifiable, serviceReference)) {
                serviceActivityNotifiable.add(serviceReference);
            }
        }
    }
    
    /* ******************* Private and package method ******************** */
    
    private void handleEventRemoved(ServiceRegistryEvent event) {
        ServiceRegistryReference ref = event.getServiceReference();
        
        //no particular point matching before removing as it is more extra work than 
        //simply calling remove.
        serviceActivityNotifiable.remove(ref);
    }

    void handleEventAdded(ServiceRegistryEvent event) {
        ServiceRegistryReference ref = event.getServiceReference();
        handleReferenceAdded(ref);
    }

    void handleReferenceAdded(ServiceRegistryReference serviceReference) {
        ServiceReferenceFilter filter = serviceActivityNotifiable.getServiceReferenceFilter();
        
        if (matchesTypes(serviceActivityNotifiable, serviceReference) && filter.matches(serviceReference)) {
            serviceActivityNotifiable.add(serviceReference);
        }
    }

    private boolean matchesTypes(ServiceActivityNotifiable serviceActivityNotifiable, ServiceRegistryReference serviceReference) {
        boolean matchable = true;
        
        Class<?>[] implementationTypes = serviceActivityNotifiable.getSupportedTypes();
        
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
    
    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }
    
}
