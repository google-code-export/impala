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

package org.impalaframework.service.registry.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.event.ServiceAddedEvent;
import org.impalaframework.service.event.ServiceRegistryEvent;
import org.impalaframework.service.event.ServiceRegistryEventListener;
import org.impalaframework.service.event.ServiceRemovedEvent;
import org.impalaframework.service.registry.BasicServiceRegistryReference;
import org.impalaframework.service.registry.exporttype.ExportTypeDeriver;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Implementation of {@link ServiceRegistry}, which holds services which can be shared across modules.
 * @author Phil Zoio
 */
public class ServiceRegistryImpl implements ServiceRegistry {

    private static Log logger = LogFactory.getLog(ServiceRegistryImpl.class);
    
    private ClassChecker classChecker = new ClassChecker();
    private ServiceReferenceSorter serviceReferenceSorter = new ServiceReferenceSorter();
    private ExportTypeDeriver exportTypeDeriver;

    private Map<String, List<ServiceRegistryReference>> beanNameToService = new ConcurrentHashMap<String, List<ServiceRegistryReference>>();
    private Map<String, List<ServiceRegistryReference>> moduleNameToServices = new ConcurrentHashMap<String, List<ServiceRegistryReference>>();
    private Map<String, List<ServiceRegistryReference>> classNameToServices = new ConcurrentHashMap<String, List<ServiceRegistryReference>>();
    private Set<ServiceRegistryReference> services = new CopyOnWriteArraySet<ServiceRegistryReference>();
    
    /**
     * For each registry entry, holds a {@link MapTargetInfo} which points to the keys by which the {@link ServiceRegistryReference}
     * instance is held as a value.
     */
    private Map<ServiceRegistryReference, MapTargetInfo> beanTargetInfo = new IdentityHashMap<ServiceRegistryReference, MapTargetInfo>();

    // use CopyOnWriteArrayList to support non-blocking thread-safe iteration
    private List<ServiceRegistryEventListener> listeners = new CopyOnWriteArrayList<ServiceRegistryEventListener>();

    private Object registryLock = new Object();
    private Object listenersLock = new Object();

    /* ************ registry service modification methods * ************** */

    public ServiceRegistryReference addService(
            String beanName, 
            String moduleName, 
            Object service,
            ClassLoader classLoader) {
        
        return addService(beanName, moduleName, service, null, null, classLoader);
    }

    public ServiceRegistryReference addService(
            String beanName, 
            String moduleName, 
            Object service,
            List<Class<?>> classes, 
            Map<String, ?> attributes, 
            ClassLoader classLoader) {
        
        //Note: null checks performed by BasicServiceRegistryReference constructor
        BasicServiceRegistryReference serviceReference = null;
        synchronized (registryLock) {
            
            boolean checkClasses = true;
            
            if (classes == null) {
                classes = deriveExportTypes(service);
                checkClasses = false;
            }
            
            serviceReference = new BasicServiceRegistryReference(
                    service, 
                    beanName,
                    moduleName, 
                    classes, 
                    attributes, classLoader);
            
            if (checkClasses) {
                checkClasses(serviceReference);
            }
            
            if (classes.isEmpty() && beanName == null) {
                throw new InvalidStateException("Attempted to register bean from module '" + moduleName + "'" +
                        " with no bean name and no export types available.");
            }
            
            //deal with the case of overriding and existing bean
            if (beanName != null) {
                addReferenceToMap(beanNameToService, beanName, serviceReference);
            }
            
            addReferenceToMap(moduleNameToServices, moduleName, serviceReference);
            
            for (Class<?> exportType : classes) {
                addReferenceToMap(classNameToServices, exportType.getName(), serviceReference);
            }
            
            MapTargetInfo targetInfo = new MapTargetInfo(classes, beanName, moduleName);
            beanTargetInfo.put(serviceReference, targetInfo);
            
            services.add(serviceReference);
        }
        
        if (logger.isDebugEnabled())
            logger.debug("Added service bean '" + beanName
                    + "' contributed from module '" + moduleName
                    + "' to service registry, with attributes " + attributes);

        ServiceAddedEvent event = new ServiceAddedEvent(serviceReference);
        invokeListeners(event);
        
        Assert.notNull(serviceReference, "Programming error: addService completing without returning non-null service referece");
        
        return serviceReference;
    }

    public void evictModuleServices(String moduleName) {

        Assert.notNull(moduleName, "moduleName cannot be null");
        
        final List<ServiceRegistryReference> list;
        
        synchronized (registryLock) {
            final List<ServiceRegistryReference> tempList = moduleNameToServices.get(moduleName);
            
            if (tempList != null) {
                list = new LinkedList<ServiceRegistryReference>(tempList);
            } else {
                list = null;
            }
        }
        
        //we have new list so can use it outside synchronised block
        if (list != null) {
            for (ServiceRegistryReference serviceRegistryReference : list) {
                remove(serviceRegistryReference);
            }
        }
    }

    public void remove(ServiceRegistryReference serviceReference) {
        
        Assert.notNull(serviceReference, "serviceReference cannot be null");
        
        synchronized (registryLock) {
            
            final MapTargetInfo targetInfo = beanTargetInfo.remove(serviceReference);
            if (targetInfo != null) {
                String beanName = targetInfo.getBeanName();
                if (beanName != null) {
                    final List<ServiceRegistryReference> list = beanNameToService.get(beanName);
                    if (list != null) {
                        list.remove(serviceReference);
                    }
                }
                
                String moduleName = targetInfo.getModuleName();
                if (moduleName != null) {
                    final List<ServiceRegistryReference> list = moduleNameToServices.get(moduleName);
                    if (list != null) {
                        list.remove(serviceReference);
                    }
                }
                
                final List<Class<?>> exportTypes = targetInfo.getExportTypes();
                for (Class<?> exportType : exportTypes) {
                    final List<ServiceRegistryReference> list = classNameToServices.get(exportType.getName());
                    if (list != null) {
                        list.remove(serviceReference);
                    }
                }
                
                services.remove(serviceReference);
            }
        }

        if (serviceReference != null) {
            
            if (logger.isDebugEnabled())
                logger.debug("Removed from service reference '" + serviceReference
                        + "' contributed from module '"
                        + serviceReference.getContributingModule() + "'");
            
            ServiceRemovedEvent event = new ServiceRemovedEvent(serviceReference);
            
            invokeListeners(event);
        }
    }
    
    /* ************ registry service accessor methods * ************** */

    /**
     * Returns named service, which has to implement all of implemenation types specified
     */
    public ServiceRegistryReference getService(String beanName, Class<?>[] supportedTypes) {
        
        Assert.notNull(beanName, "beanName cannot be null");
        List<ServiceRegistryReference> references = beanNameToService.get(beanName);
        
        //FIXME should have option of looking for explicitly named export types rather than implementation types
        
        if (references == null || references.size() == 0) {
            return null;
        }
        
        //sort the service references
        references = serviceReferenceSorter.sort(references);
        
        if (supportedTypes == null) {
            return references.get(0);
        }
        
        for (int i = 0; i < references.size(); i++) {
            final ServiceRegistryReference ref = getMatchingReference(references, supportedTypes, i);
            if (ref != null) {
                return ref;
            }
        }
        
        return null;
    }

    /**
     * Returns filtered services, which has to implement all of implemenation types specified
     */
    public List<ServiceRegistryReference> getServices(ServiceReferenceFilter filter, Class<?>[] supportedTypes) {

        Assert.notNull(filter, "filter cannot be null");

        //FIXME should have option of looking for explicitly named export types rather than implementation types
        
        List<ServiceRegistryReference> serviceList = new LinkedList<ServiceRegistryReference>();
        
        //FIXME check semantics of using CopyOnWriteArraySet
        Collection<ServiceRegistryReference> values = services;
        
        //FIXME should be looking only at export types here
        for (ServiceRegistryReference serviceReference : values) {
            if (classChecker.matchesTypes(serviceReference, supportedTypes) && filter.matches(serviceReference)) {
                serviceList.add(serviceReference);
            }
        }
        
        //sort, reusing existing list
        return serviceReferenceSorter.sort(serviceList, true);
    }
    
    /* ************ listener related methods * ************** */

    /**
     * Adds to global event listeners to which all service registry events will
     * be broadcast. Note that a single event listener instance can only be added once.
     * Any subsequent attempts to add this listener will not have any effect.
     */
    public boolean addEventListener(ServiceRegistryEventListener listener) {
        Assert.notNull(listener);
        
        synchronized (listenersLock) {
            if (this.listeners.contains(listener)) {
                logger.warn("Listener " + ObjectUtils.identityToString(listener) + " already a listener for this service registry");
                return false;
            } else {
                listeners.add(listener);
                if (logger.isDebugEnabled()) {
                    logger.debug("Added service registry listener " + listener);
                }
                return true;
            }
        }
    }
    
    /**
     * Removes global event listeners to which all service registry events will
     * be broadcast
     * @return true if listener was removed.
     */
    public boolean removeEventListener(ServiceRegistryEventListener listener) {
        List<ServiceRegistryEventListener> listeners = getCopyOfListeners();
        
        return removeListener(listener, listeners);
    }
    
    /* ************ registry collection accessor methods * ************** */
    
    List<ServiceRegistryReference> getClassReferences(Class<?> exportType) {
        final List<ServiceRegistryReference> list = classNameToServices.get(exportType.getName());
        if (list == null) {
            return Collections.emptyList();
        } 
        return Collections.unmodifiableList(list);
    }
    
    List<ServiceRegistryReference> getModuleReferences(String moduleName) {
        final List<ServiceRegistryReference> list = moduleNameToServices.get(moduleName);
        if (list == null) {
            return Collections.emptyList();
        } 
        return Collections.unmodifiableList(list);
    }
    
    List<ServiceRegistryReference> getBeanReferences(String beanName) {
        final List<ServiceRegistryReference> list = beanNameToService.get(beanName);
        if (list == null) {
            return Collections.emptyList();
        } 
        return Collections.unmodifiableList(list);
    }

    ServiceRegistryReference getBeanReference(String beanName) {
        final List<ServiceRegistryReference> beanReferences = getBeanReferences(beanName);
        if (beanReferences.isEmpty()) return null;
        return beanReferences.get(0);
    }
    
    boolean hasService(ServiceRegistryReference serviceReference) {     
        return services.contains(serviceReference);
    }
    
    /* ************ helper methods * ************** */
    
    List<Class<?>> deriveExportTypes(Object service) {

        //FIXME if classes are present then use all of these as keys in classes to services map
        //if no classes are present, then find first matching interface, and use this as key in classes to services map
        //if no bean name present, then at least one explicit class reference must be present
        //if no classes are present, then bean name must be present
        
        if (exportTypeDeriver != null) {
            return exportTypeDeriver.deriveExportTypes(service);
        }
        return Collections.emptyList();
    }

    void checkClasses(ServiceRegistryReference serviceReference) {
        classChecker.checkClasses(serviceReference);
    }

    private ServiceRegistryReference getMatchingReference(
            final List<ServiceRegistryReference> list, 
            Class<?>[] supportedTypes,
            int index) {
        
        final ServiceRegistryReference reference = list.get(index);
        if (reference == null) {
            return null;
        }
        
        if (classChecker.matchesTypes(reference, supportedTypes)) {
            return reference;
        } else {
            return null;
        }
    }
    
    private List<ServiceRegistryEventListener> getCopyOfListeners() {
        return new ArrayList<ServiceRegistryEventListener>(listeners);
    }

    private void invokeListeners(ServiceRegistryEvent event) {
        
        List<ServiceRegistryEventListener> listeners = getCopyOfListeners();
        
        //inform all listeners of service listener event
        for (ServiceRegistryEventListener listener : listeners) {
            listener.handleServiceRegistryEvent(event);
        }
    }

    private boolean removeListener(ServiceRegistryEventListener listener, List<ServiceRegistryEventListener> listeners) {
        
        for (ServiceRegistryEventListener currentListener : listeners) {
            
            if (currentListener == listener) {
                boolean removed = this.listeners.remove(currentListener);
                if (logger.isDebugEnabled())
                    logger.debug("Removed service registry listener " + listener + ": " + removed);
                return removed;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private void addReferenceToMap(Map map,
            Object key,
            ServiceRegistryReference serviceReference) {
        
        List<ServiceRegistryReference> list = (List<ServiceRegistryReference>) map.get(key);
        if (list == null) {
            list = new CopyOnWriteArrayList<ServiceRegistryReference>();
            map.put(key, list);
        }
        list.add(serviceReference);
    }
    
    /* ************ dependency injection setters * ************** */

    public void setExportTypeDeriver(ExportTypeDeriver exportTypeDeriver) {
        this.exportTypeDeriver = exportTypeDeriver;
    }
    
    /**
     * Used to hold the keys by which a particular {@link ServiceRegistryReference} instance is held in
     * this service registry as a value.
     * @author Phil Zoio
     */
    class MapTargetInfo {
        
        /**
         * The key by which the {@link ServiceRegistryReference} is held in the bean name map
         */
        private final String beanName;
        
        /**
         * The keys by which the {@link ServiceRegistryReference} is held in the classes map
         */
        private final List<Class<?>> classes;
        
        /**
         * The keys by which the {@link ServiceRegistryReference} is held in the modules map
         */
        private final String moduleName;
        
        private MapTargetInfo(List<Class<?>> classes, String beanName,
                String moduleName) {
            super();
            Assert.notNull(moduleName);
            Assert.notNull(classes);
            this.classes = classes;
            this.beanName = beanName;
            this.moduleName = moduleName;
        }

        public String getBeanName() {
            return beanName;
        }

        public List<Class<?>> getExportTypes() {
            return classes;
        }

        public String getModuleName() {
            return moduleName;
        }
        
    }

}

