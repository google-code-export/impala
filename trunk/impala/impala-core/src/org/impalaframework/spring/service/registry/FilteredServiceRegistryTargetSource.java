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

package org.impalaframework.spring.service.registry;

import java.util.List;

import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.spring.service.ContributionEndpointTargetSource;

/**
 * Supports retrieving of target object from service registry for filter.
 * Implements {@link ContributionEndpointTargetSource}
 * @author Phil Zoio
 */
public class FilteredServiceRegistryTargetSource extends BaseServiceRegistryTargetSource {

    private Class<?> clazz;
    private final ServiceReferenceFilter filter;
    private final ServiceRegistry serviceRegistry;

    public FilteredServiceRegistryTargetSource(
            //FIXME should pass in array of classes
            Class<?> clazz,
            //FIXME should also pass in explicit export types
            ServiceReferenceFilter filter, 
            ServiceRegistry serviceRegistry) {
        super();
        this.clazz = clazz;
        this.filter = filter;
        this.serviceRegistry = serviceRegistry;
    }
    
    /* *************** ContributionEndpointTargetSource implementations ************** */

    public ServiceRegistryReference getServiceRegistryReference() {
        
        //returns list of services matching filter
        final List<ServiceRegistryReference> filteredServices = serviceRegistry.getServices(filter, null, false);
        if (filteredServices.isEmpty()) {
            return null;
        }
        else {
            for (ServiceRegistryReference serviceRegistryReference : filteredServices) {
                final Object bean = serviceRegistryReference.getBean();
                if (clazz.isAssignableFrom(bean.getClass())) {
                    return serviceRegistryReference;
                }
            }
        }
        return null;
    }

}
