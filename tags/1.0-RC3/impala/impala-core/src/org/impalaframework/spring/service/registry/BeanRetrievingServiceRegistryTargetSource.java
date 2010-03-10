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

package org.impalaframework.spring.service.registry;

import java.lang.reflect.Modifier;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.spring.service.ServiceEndpointTargetSource;
import org.impalaframework.util.ArrayUtils;
import org.springframework.util.Assert;

/**
 * Supports retrieving of target object from service registry for particular bean name.
 * 
 * Implements {@link ServiceEndpointTargetSource}
 * @author Phil Zoio
 */
public class BeanRetrievingServiceRegistryTargetSource extends BaseServiceRegistryTargetSource {

    private final String beanName;
    private final ServiceRegistry serviceRegistry;
    private final Class<?>[] proxyTypes;
    private final Class<?>[] exportTypes;
    private final Class<?> concreteClass;

    public BeanRetrievingServiceRegistryTargetSource(
            ServiceRegistry serviceRegistry, 
            String beanName, 
            Class<?>[] proxyTypes, 
            Class<?>[] exportTypes) {
        
        super();
        this.beanName = beanName;
        this.serviceRegistry = serviceRegistry;
        this.proxyTypes = proxyTypes;
        this.exportTypes = exportTypes;
        
        if (beanName == null) {
            Assert.isTrue(ArrayUtils.notNullOrEmpty(exportTypes), "exportTypes and bean name cannot both be null/empty");
        }
        
        if (ArrayUtils.isNullOrEmpty(proxyTypes)) {
            Assert.isTrue(ArrayUtils.notNullOrEmpty(exportTypes), "exportTypes and proxyTypes cannot both be null/empty");
            proxyTypes = exportTypes;
        }
        
        //if we have just a single interface and this is a concrete class, then we can use this 
        //as the return value for getTargetClass
        Class<?> firstClass = proxyTypes[0];
        if (proxyTypes.length == 1 && !firstClass.isInterface()) {
            if (Modifier.isFinal(firstClass.getModifiers())) {
                throw new InvalidStateException("Cannot create proxy for " + firstClass + " as it is a final class");
            }
            this.concreteClass = firstClass;
        } 
        else {
            this.concreteClass = null;
        }
    }
    
    /* *************** ServiceEndpointTargetSource implementation ************** */

    @Override
    public Class<?> getTargetClass() {
        return this.concreteClass;
    }

    public ServiceRegistryEntry getServiceRegistryReference() {
        boolean exportTypesOnly = ArrayUtils.notNullOrEmpty(exportTypes);
        Class<?>[] typesToUse = exportTypesOnly ? exportTypes : proxyTypes;
        return serviceRegistry.getService(beanName, typesToUse, exportTypesOnly);
    }

}
