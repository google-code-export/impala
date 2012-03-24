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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.util.ArrayUtils;

/**
 * Package internal class responsible for class checking operations
 * @author Phil Zoio
 */
class ClassChecker {
    
    private static Log logger = LogFactory.getLog(ClassChecker.class);
    
    void checkClasses(ServiceRegistryEntry reference) {

        final List<Class<?>> exportTypes = reference.getExportTypes();
        
        if (!exportTypes.isEmpty()) { 
            checkClassesForClassLoader(exportTypes, reference);
            checkClassesForImplements(exportTypes, reference);
        }
    }

    /**
     * Checks that the class loader and classes are compatible. In other words, it must be possible to load each of the classes
     * using the supplied class loader.
     */
    private void checkClassesForClassLoader(List<Class<?>> classes, ServiceRegistryEntry reference) {
        
        final ClassLoader classLoader = reference.getBeanClassLoader();
        
        //check that classes are valid
        for (Class<?> clz : classes) {
            try {
                final Class<?> loaded = Class.forName(clz.getName(), false, classLoader);
                if (loaded != clz) {
                    throw new InvalidStateException("Class entry '" + clz.getName() + "'" 
                            + " contributed from module '" + reference.getContributingModule() + "'"
                            + " with bean name '" + reference.getBeanName() + "'"
                            + " is incompatible with class loader " + classLoader);
                }
            } catch (ClassNotFoundException e) {
                throw new InvalidStateException("Class entry '" + clz.getName() + "'" 
                        + " contributed from module '" + reference.getContributingModule() + "'"
                        + " with bean name '" + reference.getBeanName() + "'"
                        + " could not be found using class loader " + classLoader);
            }
        }
    }

    /**
     * Checks that the class of the service is assignable to 
     * @param classes
     * @param reference
     */
    private void checkClassesForImplements(List<Class<?>> classes, ServiceRegistryEntry reference) {
        
        Object service = reference.getServiceBeanReference().getService();
        
        //check that classes are valid
        for (Class<?> clz : classes) {
            Class<?> serviceClass = service.getClass();
            
            if (!clz.isAssignableFrom(serviceClass)) {
                throw new InvalidStateException("Service class '" + serviceClass.getName() 
                        + " contributed from module '" + reference.getContributingModule() + "'"
                        + " with bean name '" + reference.getBeanName() + "'"
                        + " is not assignable declared export type " + clz.getName());
            }           
        }
    }
    
    /**
     * Indicates whether a particular service reference is compatible with the array of implementation types.
     * Used during the operation to retrieve service references, potentially filtered by implementation type.
     */
    boolean matchesTypes(
            ServiceRegistryEntry reference,
            Class<?>[] implementationTypes) {
        
        boolean matches = true;
        
        if (ArrayUtils.isNullOrEmpty(implementationTypes)) {
            return matches;
        }
        
        Object service = reference.getServiceBeanReference().getService();

        //check that the the target implements all the interfaces
        final Class<? extends Object> targetClass = service.getClass();
        
        for (Class<?> clazz : implementationTypes) {
            if (!clazz.isAssignableFrom(targetClass)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Not matching '" + reference.getBeanName() + 
                            "' from module '" + reference.getContributingModule() +
                            "' as its target class " + targetClass + " cannot be assigned to " + clazz);
                }
                return false;
            }
        }
        return matches;
    }

}
