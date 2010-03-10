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

import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.spring.service.ServiceEndpointTargetSource;
import org.springframework.beans.factory.FactoryBean;

/**
 * Abstract class providing implementation of some of methods of {@link ServiceEndpointTargetSource}
 * @author Phil Zoio
 */
public abstract class BaseServiceRegistryTargetSource implements ServiceEndpointTargetSource {

    /* *************** TargetSource implementations ************** */
    
    /**
     * Attempts to return the target object from the service registry, using the provided bean name
     * First looks up a {@link ServiceRegistryEntry} instance. If one is found, and the
     * contained bean is a {@link FactoryBean}, will dereference this using {@link FactoryBean#getObject()}.
     * Otherwise, simply returns the bean held by the {@link ServiceRegistryEntry}.
     * 
     * Each time {@link #getTarget()} is called, the object is looked up from the service registry.
     * No cacheing is involved.
     */
    public Object getTarget() throws Exception {
        ServiceRegistryEntry reference = getServiceRegistryReference();
        if (reference != null) {
            return reference.getServiceBeanReference().getService();
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

}
