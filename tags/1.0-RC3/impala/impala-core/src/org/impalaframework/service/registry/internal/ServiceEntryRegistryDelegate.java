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

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.service.ServiceBeanReference;
import org.impalaframework.service.ServiceEntryRegistry;
import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.reference.BasicServiceRegistryEntry;
import org.impalaframework.service.reference.ServiceReferenceSorter;
import org.impalaframework.service.registry.exporttype.ExportTypeDeriver;
import org.impalaframework.util.ArrayUtils;
import org.springframework.util.Assert;

/**
 * Implementation of {@link ServiceRegistry}, which holds services which can be shared across modules.
 * @author Phil Zoio
 */
public class ServiceEntryRegistryDelegate implements ServiceEntryRegistry {

    private static Log logger = LogFactory.getLog(ServiceEntryRegistryDelegate.class);
    
    static ServiceReferenceFilter IDENTIFY_FILTER = new IdentityServiceReferenceFilter();
    
    private ClassChecker classChecker = new ClassChecker();
    private ServiceReferenceSorter serviceReferenceSorter = new ServiceReferenceSorter();
    private ExportTypeDeriver exportTypeDeriver = new EmptyExportTypeDeriver();

    private Map<String, List<ServiceRegistryEntry>> beanNameToService = new ConcurrentHashMap<String, List<ServiceRegistryEntry>>();
    private Map<String, List<ServiceRegistryEntry>> moduleNameToServices = new ConcurrentHashMap<String, List<ServiceRegistryEntry>>();
    private Map<String, List<ServiceRegistryEntry>> classNameToServices = new ConcurrentHashMap<String, List<ServiceRegistryEntry>>();
    private Set<ServiceRegistryEntry> services = new CopyOnWriteArraySet<ServiceRegistryEntry>();
    
    /**
     * For each registry entry, holds a {@link MapTargetInfo} which points to the keys by which the {@link ServiceRegistryEntry}
     * instance is held as a value.
     */
    private Map<ServiceRegistryEntry, MapTargetInfo> beanTargetInfo = new IdentityHashMap<ServiceRegistryEntry, MapTargetInfo>();
    
    private ReentrantReadWriteLock reentrantRegistryLock = new ReentrantReadWriteLock();
    private ReadLock registryReadLock = reentrantRegistryLock.readLock();
    private WriteLock registryWriteLock = reentrantRegistryLock.writeLock();
    

    /* ************ registry service modification methods * ************** */

    public ServiceRegistryEntry addService(
            String beanName, 
            String moduleName, 
            ServiceBeanReference beanReference,
            List<Class<?>> classes, 
            Map<String, ?> attributes, 
            ClassLoader classLoader) {
        
        Object service = beanReference.getService();
        
        final boolean checkClasses;
        
        if (classes == null) {
            classes = deriveExportTypes(service, beanName, classes);
            checkClasses = false;
        } else {
            checkClasses = true;
        }
        
        if (classes.isEmpty() && beanName == null) {
            logger.warn("Registering service with no explicit name or service classes. One of these is recommended");
        }

        //Note: null checks performed by BasicServiceRegistryReference constructor
        BasicServiceRegistryEntry serviceReference = new BasicServiceRegistryEntry(
                beanReference, 
                beanName,
                moduleName, 
                classes, 
                attributes, classLoader);
        
        //no need to check classes as these were derived
        if (checkClasses) {
            checkClasses(serviceReference);
        }
        
        try {
            registryWriteLock.lock();
            
            //deal with the case of overriding and existing bean
            if (beanName != null) {
                addReferenceToMap(beanNameToService, beanName, serviceReference, true);
            }
            
            addReferenceToMap(moduleNameToServices, moduleName, serviceReference, false);
            
            for (Class<?> exportType : classes) {
                addReferenceToMap(classNameToServices, exportType.getName(), serviceReference, true);
            }
            
            //associate service reference with keys used to hold it in service registry
            MapTargetInfo targetInfo = new MapTargetInfo(classes, beanName, moduleName);
            beanTargetInfo.put(serviceReference, targetInfo);
            
            services.add(serviceReference);
            
        } finally {
            registryWriteLock.unlock();
        }
        
        if (logger.isDebugEnabled())
            logger.debug("Added service bean '" + beanName
                    + "' contributed from module '" + moduleName
                    + "' to service registry, with attributes " + attributes);

        /*
        ServiceAddedEvent event = new ServiceAddedEvent(serviceReference);
        invokeListeners(event);
        */
        
        Assert.notNull(serviceReference, "Programming error: addService completing without returning non-null service referece");
        return serviceReference;
    }

    public List<ServiceRegistryEntry> evictModuleServices(String moduleName) {

        Assert.notNull(moduleName, "moduleName cannot be null");
        
        final List<ServiceRegistryEntry> list;
        
        //get copy of services within read lock protected block
        try {
            registryReadLock.lock();
            
            final List<ServiceRegistryEntry> tempList = moduleNameToServices.get(moduleName);
            
            if (tempList != null) {
                list = new ArrayList<ServiceRegistryEntry>(tempList);
            } else {
                list = Collections.emptyList();
            }
            
        } finally {
            registryReadLock.unlock();
        }
        
        //we have new list so can use it outside synchronised block
        Iterator<ServiceRegistryEntry> iterator = list.iterator();
        while (iterator.hasNext()) {
            ServiceRegistryEntry serviceRegistryEntry = iterator.next();
            
            boolean removed = remove(serviceRegistryEntry);
            if (!removed) {
                //filter out the entries that weren't actually removed
                iterator.remove();
            }
        }
        
        return list;
    }

    public boolean remove(ServiceRegistryEntry serviceReference) {
        
        Assert.notNull(serviceReference, "serviceReference cannot be null");
        boolean removed = false;
        
        //use write lock protected section to remove service references
        try {
            registryWriteLock.lock();
            
            final MapTargetInfo targetInfo = beanTargetInfo.remove(serviceReference);
            if (targetInfo != null) {
                String beanName = targetInfo.getBeanName();
                if (beanName != null) {
                    final List<ServiceRegistryEntry> list = beanNameToService.get(beanName);
                    if (list != null) {
                        list.remove(serviceReference);
                    }
                }
                
                String moduleName = targetInfo.getModuleName();
                if (moduleName != null) {
                    final List<ServiceRegistryEntry> list = moduleNameToServices.get(moduleName);
                    if (list != null) {
                        list.remove(serviceReference);
                    }
                }
                
                final List<Class<?>> exportTypes = targetInfo.getExportTypes();
                for (Class<?> exportType : exportTypes) {
                    final List<ServiceRegistryEntry> list = classNameToServices.get(exportType.getName());
                    if (list != null) {
                        list.remove(serviceReference);
                    }
                }
                
                removed = services.remove(serviceReference);
            }
            
        } finally {
            registryWriteLock.unlock();
        }

        if (removed) {
            if (logger.isDebugEnabled()) {
                logger.debug("Removed from service reference '" + serviceReference
                        + "' contributed from module '"
                        + serviceReference.getContributingModule() + "'");
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("No service '" + serviceReference
                        + "' present to remove from service registry");
            }
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
        
        final List<ServiceRegistryEntry> references = getServicesInternal(beanName, exportTypesOnly ? supportedTypes : null);
        
        if (references == null || references.isEmpty()) {
            return null;
        }
        
        //more than one result returned
        if (exportTypesOnly) {
            
            //only one supported type - we've done all the type checking we need to do
            if (supportedTypes.length <  2) {
                return references.get(0);
            }
            
            for (ServiceRegistryEntry serviceReference : references) {
                
                if (isPresentInExportTypes(serviceReference, supportedTypes))
                {
                    return serviceReference;
                }
            }
            
        } else {
            for (int i = 0; i < references.size(); i++) {
                final ServiceRegistryEntry ref = getMatchingReference(references, supportedTypes, i);
                if (ref != null) {
                    return ref;
                }
            }
        }
        
        return null;
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

        List<ServiceRegistryEntry> references = getServicesInternal(beanName, exportTypesOnly ? supportedTypes : null);
        
        if (references.isEmpty()) {
            return references;
        }
        
        if (exportTypesOnly) {
            //filter only export types
            filterReferenceByExportTypes(references, supportedTypes);
        } else {
            for (Iterator<ServiceRegistryEntry> iterator = references.iterator(); iterator.hasNext();) {
                ServiceRegistryEntry serviceReference = iterator.next();
                if (!classChecker.matchesTypes(serviceReference, supportedTypes)) {
                    iterator.remove();
                }
            }
        }
        
        return references;
    }
    
    /**
     * Returns named service, which has to implement all of implementation types specified
     */
    @SuppressWarnings("unchecked")
    private List<ServiceRegistryEntry> getServicesInternal(String beanName, Class<?>[] exportTypes) {
        
        final boolean useBeanLookup;
        final boolean exportTypesSet;
        
        if (ArrayUtils.isNullOrEmpty(exportTypes)) {
            Assert.notNull(beanName, "Either bean name must be not null, or export types must be non-empty");
            useBeanLookup = true;
            exportTypesSet = false;
        }
        else {
            exportTypesSet = true;
            
            if (beanName == null) {
                Assert.notEmpty(exportTypes, "Either bean name must be not null, or export types must be non-empty");
                useBeanLookup = false;
            }
            else {
                //bean lookup and export types are available - use bean lookup
                useBeanLookup = true;
            }
        }
        
        List<ServiceRegistryEntry> references;
        
        //use read lock protected section to get services
        try {
            registryReadLock.lock();
            
            final List<ServiceRegistryEntry> list;
            
            if (useBeanLookup) {
                list = beanNameToService.get(beanName);
                
                //if we have explicit types and list is not empty, we get the exported list use this to filter the 
                //list to be returned
                if (exportTypesSet && list != null && !list.isEmpty()) {
                    
                    final List<ServiceRegistryEntry> exportList = classNameToServices.get(exportTypes[0].getName());
                    if (exportList == null) {
                        return Collections.EMPTY_LIST;
                    } else {
                        list.retainAll(exportList);
                    }
                }
            } else {
                //get list from export types
                list = classNameToServices.get(exportTypes[0].getName());
            }
            references = (list == null ? Collections.EMPTY_LIST : new ArrayList<ServiceRegistryEntry>(list));
            
        } finally {
            registryReadLock.unlock();
        }
        
        //no need to sort here, as it is already sorted
        return references;
    }

    /**
     * Returns filtered services, which has to implement all of implemenation types specified.
     * @param filter the filter which is used to match service references. Can be null. If so, {@link IdentityServiceReferenceFilter} is used,
     * which always returns true. See {@link IdentityServiceReferenceFilter#matches(ServiceRegistryEntry)}.
     * @param types the types with which returned references should be compatible
     * @param if true only matches against explicit export types
     */
    public List<ServiceRegistryEntry> getServices(ServiceReferenceFilter filter, Class<?>[] types, boolean exportTypesOnly) {

        if (filter == null) {
            filter = IDENTIFY_FILTER;
        }
        
        if (exportTypesOnly) {
            
            Assert.notNull(types, "exportTypesOnly is true but types is null");
            Assert.notEmpty(types, "exportTypesOnly is true but types is empty");
            
            final List<ServiceRegistryEntry> values; 
            
            //build a copy of values within read lock protected block
            try {
                registryReadLock.lock();
                
                final List<ServiceRegistryEntry> tempValues = classNameToServices.get(types[0].getName());
                
                if (tempValues == null) {
                    return Collections.emptyList();
                } else {
                    values = new ArrayList<ServiceRegistryEntry>(tempValues);
                }
            } finally {
                registryReadLock.unlock();
            }
                
            //filter only export types
            filterReferenceByExportTypes(values, types);
                
            //check each entry against filter
            for (Iterator<ServiceRegistryEntry> iterator = values.iterator(); iterator.hasNext();) {
                ServiceRegistryEntry serviceReference = iterator.next();
               
                if (!filter.matches(serviceReference)) {
                    iterator.remove();
                }
            }
            
            return serviceReferenceSorter.sort(values, true);
        } else {
        
            //Create new list of services for concurrency protection
            final List<ServiceRegistryEntry> values;
            
            try {
                registryReadLock.lock();
                values = new LinkedList<ServiceRegistryEntry>(this.services);
            } finally {
                registryReadLock.unlock();
            }
            
            for (Iterator<ServiceRegistryEntry> iterator = values.iterator(); iterator.hasNext();) {
                ServiceRegistryEntry serviceReference = iterator.next();
               
                if (!(classChecker.matchesTypes(serviceReference, types) && filter.matches(serviceReference))) {
                    iterator.remove();
                }
            }
            //sort, reusing existing list
            return serviceReferenceSorter.sort(values, true);
        }
        
    }

    /**
     * Returns non-null reference if service reference is present in 
     * @param serviceReference the {@link ServiceRegistryEntry} under examination
     * @param exportTypes an array of {@link Class} instances
     * @return true if service reference is present in service registry against all of the passed in export types
     */
    public boolean isPresentInExportTypes(ServiceRegistryEntry serviceReference, Class<?>[] exportTypes) {
        return isPresentInExportTypes(serviceReference, exportTypes, 0);
    }

    private boolean isPresentInExportTypes(
            ServiceRegistryEntry serviceReference,
            Class<?>[] exportTypes, int startIndex) {
        for (int i = startIndex; i < exportTypes.length; i++) {
            String name = exportTypes[i].getName();
            boolean found = isPresentAsExportedType(serviceReference, name);
            
            if (!found) {
                return false;
            }
        }
        return true;
    }
    
    /* ************ private service registry lookup related methods * ************** */

    private void filterReferenceByExportTypes(final List<ServiceRegistryEntry> values, Class<?>[] types) {
        
        Iterator<ServiceRegistryEntry> iterator = values.iterator();
        while (iterator.hasNext()) {
            
            ServiceRegistryEntry serviceReference = iterator.next();
            
            //note start index is 1 because we've already checked first type
            boolean isPresent = isPresentInExportTypes(serviceReference, types, 1);
            if (!isPresent) {
                iterator.remove();
            }
        }
    }

    private boolean isPresentAsExportedType(ServiceRegistryEntry serviceReference,
            String name) {
        
        List<ServiceRegistryEntry> secondaryValues = classNameToServices.get(name);
        boolean found = false;
        
        if (secondaryValues != null) {
            if (secondaryValues.contains(serviceReference)) {
                found = true;
            }
        }
        return found;
    }
    
    /* ************ registry collection accessor methods * ************** */
    
    List<ServiceRegistryEntry> getClassReferences(Class<?> exportType) {
        final List<ServiceRegistryEntry> list = classNameToServices.get(exportType.getName());
        if (list == null) {
            return Collections.emptyList();
        } 
        return Collections.unmodifiableList(list);
    }
    
    List<ServiceRegistryEntry> getModuleReferences(String moduleName) {
        final List<ServiceRegistryEntry> list = moduleNameToServices.get(moduleName);
        if (list == null) {
            return Collections.emptyList();
        } 
        return Collections.unmodifiableList(list);
    }
    
    List<ServiceRegistryEntry> getBeanReferences(String beanName) {
        final List<ServiceRegistryEntry> list = beanNameToService.get(beanName);
        if (list == null) {
            return Collections.emptyList();
        } 
        return Collections.unmodifiableList(list);
    }

    ServiceRegistryEntry getBeanReference(String beanName) {
        final List<ServiceRegistryEntry> beanReferences = getBeanReferences(beanName);
        if (beanReferences.isEmpty()) return null;
        return beanReferences.get(0);
    }
    
    boolean hasService(ServiceRegistryEntry serviceReference) {     
        return services.contains(serviceReference);
    }
    
    /* ************ helper methods * ************** */
    
    List<Class<?>> deriveExportTypes(Object service, String beanName, List<Class<?>> classes) {

        //pass on job of figuring out export types to ExportTypeDeriver      
        if (exportTypeDeriver != null) {
            return exportTypeDeriver.deriveExportTypes(service, beanName, classes);
        }
        return Collections.emptyList();
    }

    void checkClasses(ServiceRegistryEntry serviceReference) {
        classChecker.checkClasses(serviceReference);
    }

    private ServiceRegistryEntry getMatchingReference(
            final List<ServiceRegistryEntry> list, 
            Class<?>[] supportedTypes,
            int index) {
        
        final ServiceRegistryEntry reference = list.get(index);
        if (reference == null) {
            return null;
        }
        
        if (classChecker.matchesTypes(reference, supportedTypes)) {
            return reference;
        } else {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    private void addReferenceToMap(Map map,
            Object key,
            ServiceRegistryEntry serviceReference, boolean sort) {
        
        List<ServiceRegistryEntry> list = (List<ServiceRegistryEntry>) map.get(key);
        if (list == null) {
            list = new ArrayList<ServiceRegistryEntry>();
            map.put(key, list);
        }
        list.add(serviceReference);
        if (sort) {
            this.serviceReferenceSorter.sort(list, true);
        }
    }
    
    /* ************ dependency injection setters * ************** */

    public void setExportTypeDeriver(ExportTypeDeriver exportTypeDeriver) {
        this.exportTypeDeriver = exportTypeDeriver;
    }
    
    /**
     * Used to hold the keys by which a particular {@link ServiceRegistryEntry} instance is held in
     * this service registry as a value.
     * @author Phil Zoio
     */
    class MapTargetInfo {
        
        /**
         * The key by which the {@link ServiceRegistryEntry} is held in the bean name map
         */
        private final String beanName;
        
        /**
         * The keys by which the {@link ServiceRegistryEntry} is held in the classes map
         */
        private final List<Class<?>> classes;
        
        /**
         * The keys by which the {@link ServiceRegistryEntry} is held in the modules map
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

    private static class IdentityServiceReferenceFilter implements ServiceReferenceFilter {

        public boolean matches(ServiceRegistryEntry reference) {
            return true;
        }
        
    }

}

