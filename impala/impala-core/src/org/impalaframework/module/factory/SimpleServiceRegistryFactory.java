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

package org.impalaframework.module.factory;

import org.impalaframework.module.spi.ServiceRegistryFactory;
import org.impalaframework.service.ServiceEntryRegistry;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.registry.exporttype.ExportTypeDeriver;
import org.impalaframework.service.registry.internal.DelegatingServiceRegistry;
import org.impalaframework.service.registry.internal.EmptyExportTypeDeriver;
import org.impalaframework.service.registry.internal.InvokingServiceEventListenerRegistry;
import org.impalaframework.service.registry.internal.ServiceEntryRegistryDelegate;
import org.impalaframework.service.registry.internal.ServiceEventListenerRegistryDelegate;

/**
 * Represents mechanism for creating a {@link ServiceRegistry} instance.
 * @author Phil Zoio
 */
public class SimpleServiceRegistryFactory implements ServiceRegistryFactory {
    
    public ServiceRegistry newServiceRegistry() {
        
        DelegatingServiceRegistry serviceRegistry = new DelegatingServiceRegistry();
        ServiceEntryRegistry entryRegistryDelegate = newServiceEntryRegistry();
        serviceRegistry.setEntryRegistryDelegate(entryRegistryDelegate);
        
        InvokingServiceEventListenerRegistry listenerRegistryDelegate = newInvokingServiceEventListenerRegistry();
        serviceRegistry.setListenerRegistryDelegate(listenerRegistryDelegate);
        return serviceRegistry;
    }

    protected InvokingServiceEventListenerRegistry newInvokingServiceEventListenerRegistry() {
        InvokingServiceEventListenerRegistry listenerRegistryDelegate = new ServiceEventListenerRegistryDelegate();
        return listenerRegistryDelegate;
    }

    protected ServiceEntryRegistry newServiceEntryRegistry() {
        ServiceEntryRegistryDelegate serviceEntryRegistryDelegate = new ServiceEntryRegistryDelegate();
        ExportTypeDeriver exportTypeDeriver = newExportTypeDeriver();
        serviceEntryRegistryDelegate.setExportTypeDeriver(exportTypeDeriver);
        return serviceEntryRegistryDelegate;
    }

    protected ExportTypeDeriver newExportTypeDeriver() {
        return new EmptyExportTypeDeriver();
    }
    
}
