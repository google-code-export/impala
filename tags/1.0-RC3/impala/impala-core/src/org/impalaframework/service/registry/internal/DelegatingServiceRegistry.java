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

package org.impalaframework.service.registry.internal;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.service.ServiceBeanReference;
import org.impalaframework.service.ServiceEntryRegistry;
import org.impalaframework.service.ServiceEventListenerRegistry;
import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.ServiceRegistryEventListener;
import org.impalaframework.service.event.ServiceAddedEvent;
import org.impalaframework.service.event.ServiceRemovedEvent;
import org.springframework.util.Assert;

/**
 * Implementation of {@link ServiceRegistry}, which hides the implementation details of managing interactions with the service registry.
 * These are handled respectively by {@link ServiceEntryRegistryDelegate} for service entry registration and removal, and 
 * {@link ServiceEventListenerRegistryDelegate} for listener registration, removal and invocation.
 * 
 * @author Phil Zoio
 */
public class DelegatingServiceRegistry implements ServiceRegistry {
    
    private ServiceEntryRegistry entryRegistryDelegate = new ServiceEntryRegistryDelegate();
    
    private InvokingServiceEventListenerRegistry listenerRegistryDelegate = new ServiceEventListenerRegistryDelegate();

    private static Log logger = LogFactory.getLog(DelegatingServiceRegistry.class);
    
    /* ************ registry service modification methods * ************** */

    public ServiceRegistryEntry addService(
            String beanName, 
            String moduleName, 
            ServiceBeanReference service,
            ClassLoader classLoader) {
        
        return this.addService(beanName, moduleName, service, null, null, classLoader);
    }

    public ServiceRegistryEntry addService(
            String beanName, 
            String moduleName, 
            ServiceBeanReference beanReference,
            List<Class<?>> classes, 
            Map<String, ?> attributes, 
            ClassLoader classLoader) {
        
        ServiceRegistryEntry serviceEntry = entryRegistryDelegate.addService(beanName, moduleName, beanReference, classes, attributes, classLoader);
        
        if (logger.isDebugEnabled())
            logger.debug("Added service bean '" + beanName
                    + "' contributed from module '" + moduleName
                    + "' to service registry, with attributes " + attributes);

        ServiceAddedEvent event = new ServiceAddedEvent(serviceEntry);
        listenerRegistryDelegate.invokeListeners(event);
        
        Assert.notNull(serviceEntry, "Programming error: addService completing without returning non-null service referece");
        return serviceEntry;
    }

    public List<ServiceRegistryEntry> evictModuleServices(String moduleName) {

        Assert.notNull(moduleName, "moduleName cannot be null");
        
        //list contains only entries actually removed, and will not be null
        final List<ServiceRegistryEntry> list = entryRegistryDelegate.evictModuleServices(moduleName);
        
        //we have new list so can use it outside synchronised block
            for (ServiceRegistryEntry serviceRegistryEntry : list) {
                ServiceRemovedEvent event = new ServiceRemovedEvent(serviceRegistryEntry);
                listenerRegistryDelegate.invokeListeners(event);
            }
        
        return list;
    }

    public boolean remove(ServiceRegistryEntry serviceRegistryEntry) {
        
        boolean removed = entryRegistryDelegate.remove(serviceRegistryEntry);
        
        if (removed) {
            ServiceRemovedEvent event = new ServiceRemovedEvent(serviceRegistryEntry);
            listenerRegistryDelegate.invokeListeners(event);
        }
            
        return removed;
    }
    
    /* ************ registry service accessor methods * ************** */

    /**
     * Returns service, either from name or set of types, which has to implement all of implementation types specified.
     * If exportTypesOnly is true, the will only return service explicitly registered against the 
     * supportedTypes.
     * @param bean name, which cannot be false if supportedTypes is null or empty and exportTypes is false
     * @param supportedTypes, which cannot be null or empty if bean name is null
     * @param exportTypesOnly which cannot be false if beanName is null
     */
    public ServiceRegistryEntry getService(String beanName, Class<?>[] supportedTypes, boolean exportTypesOnly) {
        
        return entryRegistryDelegate.getService(beanName, supportedTypes, exportTypesOnly);
    }
    
    /**
     * Returns list of services, each of which has to implement all of implementation types specified.
     * If exportTypesOnly is true, the will only return service explicitly registered against the 
     * supportedTypes.
     * @param bean name, which cannot be false if supportedTypes is null or empty and exportTypes is false
     * @param supportedTypes, which cannot be null or empty if bean name is null
     * @param exportTypesOnly which cannot be false if beanName is null
     */
    public List<ServiceRegistryEntry> getServices(String beanName, Class<?>[] supportedTypes, boolean exportTypesOnly) {

        return entryRegistryDelegate.getServices(beanName, supportedTypes, exportTypesOnly);
    }

    /**
     * Returns filtered services, which has to implement all of implemenation types specified.
     * @param filter the filter which is used to match service references. Can be null. If so, {@link IdentityServiceReferenceFilter} is used,
     * which always returns true. See {@link IdentityServiceReferenceFilter#matches(ServiceRegistryEntry)}.
     * @param types the types with which returned references should be compatible
     * @param if true only matches against explicit export types
     */
    public List<ServiceRegistryEntry> getServices(ServiceReferenceFilter filter, Class<?>[] types, boolean exportTypesOnly) {

        return entryRegistryDelegate.getServices(filter, types, exportTypesOnly);
        
    }

    /**
     * Returns non-null reference if service reference is present in 
     * @param serviceReference the {@link ServiceRegistryEntry} under examination
     * @param exportTypes an array of {@link Class} instances
     * @return true if service reference is present in service registry against all of the passed in export types
     */
    public boolean isPresentInExportTypes(ServiceRegistryEntry serviceReference, Class<?>[] exportTypes) {
        
        return entryRegistryDelegate.isPresentInExportTypes(serviceReference, exportTypes);
    }
    
    /* ************ listener related methods * ************** */

    /**
     * Adds to global event listeners to which all service registry events will
     * be broadcast. Note that a single event listener instance can only be added once.
     * Any subsequent attempts to add this listener will not have any effect.
     */
    public boolean addEventListener(ServiceRegistryEventListener listener) {
        
        return listenerRegistryDelegate.addEventListener(listener);
    }
    
    /**
     * Removes global event listeners to which all service registry events will
     * be broadcast
     * @return true if listener was removed.
     */
    public boolean removeEventListener(ServiceRegistryEventListener listener) {
        
        return listenerRegistryDelegate.removeEventListener(listener);
    }
    
    /* ************ package level accessor methods * ************** */


    ServiceEntryRegistry getEntryRegistryDelegate() {
        return entryRegistryDelegate;
    }

    ServiceEventListenerRegistry getListenerRegistryDelegate() {
        return listenerRegistryDelegate;
    }
    
    /* ************ injected setter methods * ************** */

    public void setEntryRegistryDelegate(ServiceEntryRegistry entryRegistryDelegate) {
        this.entryRegistryDelegate = entryRegistryDelegate;
    }

    public void setListenerRegistryDelegate(InvokingServiceEventListenerRegistry listenerRegistryDelegate) {
        this.listenerRegistryDelegate = listenerRegistryDelegate;
    }
    
    
}

