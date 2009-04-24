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

import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryEventListener;
import org.impalaframework.service.ServiceRegistryReference;

/**
 * Interface for receiving service activity calls from {@link ServiceRegistryMonitor}.
 * Implemented by objects which want to receive services from the {@link ServiceRegistry},
 * but don't want the responsibility of directly interacting with it, i.e., by 
 * implementing the {@link ServiceRegistryEventListener} interface.
 * 
 * @author Phil Zoio
 */
public interface ServiceActivityNotifiable {

    /**
     * Called when a service registry entry is removed.
     * Returns true if corresponding entry was actually removed. False if no
     * change was made.
     */
    boolean remove(ServiceRegistryReference ref);

    /**
     * Called when a service registry entry is added, typically following a match using the associated {@link ServiceReferenceFilter}.
     * See {@link #getServiceReferenceFilter()}.
     */
    void add(ServiceRegistryReference ref);
    
    /**
     * Filter which is used by associated {@link ServiceRegistryMonitor} to filter out events from
     * service registry.
     */
    ServiceReferenceFilter getServiceReferenceFilter();
    
    /**
     * Can be used by {@link ServiceActivityNotifiable} to optionally indicate which classes the service instances should be subtypes of.
     */
    public Class<?>[] getSupportedTypes();

}
