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

package org.impalaframework.service.contribution;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.ServiceRegistryEvent;
import org.impalaframework.service.ServiceRegistryEventListener;
import org.impalaframework.service.event.ServiceAddedEvent;
import org.impalaframework.service.event.ServiceRemovedEvent;
import org.impalaframework.service.registry.ServiceRegistryAware;
import org.impalaframework.util.ArrayUtils;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

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
    
    private static Log logger = LogFactory.getLog(ServiceRegistryMonitor.class);
    
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
        
        Class<?>[] proxyTypes = serviceActivityNotifiable.getProxyTypes();
        Class<?>[] exportTypes = serviceActivityNotifiable.getExportTypes();
        
        final Class<?>[] supportedTypes;
        final boolean exportTypesOnly;
        
        if (!ArrayUtils.isNullOrEmpty(exportTypes)) {
            supportedTypes = exportTypes;
            exportTypesOnly = true;
        } else {
            supportedTypes = proxyTypes;
            exportTypesOnly = false;
        }
        
        ServiceReferenceFilter filter = serviceActivityNotifiable.getServiceReferenceFilter();
        Collection<ServiceRegistryEntry> services = serviceRegistry.getServices(filter, supportedTypes, exportTypesOnly);
        for (ServiceRegistryEntry entry : services) {
            if (matchesTypes(serviceActivityNotifiable, entry)) {
                serviceActivityNotifiable.add(entry);
            }
        }
    }
    
    /* ******************* Private and package method ******************** */
    
    private void handleEventRemoved(ServiceRegistryEvent event) {
        ServiceRegistryEntry ref = event.getServiceRegistryEntry();
        
        //no particular point matching before removing as it is more extra work than 
        //simply calling remove.
        serviceActivityNotifiable.remove(ref);
    }

    void handleEventAdded(ServiceRegistryEvent event) {
        ServiceRegistryEntry entry = event.getServiceRegistryEntry();
        handleReferenceAdded(entry);
    }

    void handleReferenceAdded(ServiceRegistryEntry entry) {
        ServiceReferenceFilter filter = serviceActivityNotifiable.getServiceReferenceFilter();
        
        boolean isStatic = entry.getServiceBeanReference().isStatic();
        if (!isStatic) {
            boolean allowNonStaticReferences = serviceActivityNotifiable.getAllowNonStaticReferences();
            if (!allowNonStaticReferences) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Service entry " + entry + " filtered out as bean reference is static, but target " +
                            ObjectUtils.identityToString(serviceActivityNotifiable) +
                            "does not support non-static reference");
                }
                return;
            }
        }
        
        final boolean typeMatches;
        
        Class<?>[] exportTypes = serviceActivityNotifiable.getExportTypes();
        if (!ArrayUtils.isNullOrEmpty(exportTypes)) {
            
            //do check against export types in registry
            typeMatches = getServiceRegistry().isPresentInExportTypes(entry, exportTypes);
        } else {

            //do check against actual implemented types
            typeMatches = matchesTypes(serviceActivityNotifiable, entry);
        }
        
        if (typeMatches && filter.matches(entry)) {
            serviceActivityNotifiable.add(entry);
        }
    }

    private boolean matchesTypes(ServiceActivityNotifiable serviceActivityNotifiable, ServiceRegistryEntry entry) {
        boolean matchable = true;
        
        Class<?>[] proxyTypes = serviceActivityNotifiable.getProxyTypes();
        
        //check export types
        if (!ArrayUtils.isNullOrEmpty(proxyTypes)) {

            Class<? extends Object> beanClass = entry.getServiceBeanReference().getService().getClass();
            for (int i = 0; i < proxyTypes.length; i++) {
                
                if (!proxyTypes[i].isAssignableFrom(beanClass)) {
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
