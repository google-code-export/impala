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

package org.impalaframework.spring.service.proxy;

import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.registry.ServiceRegistryAware;
import org.springframework.aop.framework.ProxyFactory;

/**
 * Encapsulates the mechanism for creating a {@link ProxyFactory} which backs a service/bean
 * obtained from the Impala service registry.
 * 
 * @author Phil Zoio
 */
public interface ServiceProxyFactoryCreator extends ServiceRegistryAware {

    /**
     * Creates a {@link ProxyFactory} instance for a service which needs to be
     * dynamically retrieved from the service registry
     * @param proxyTypes the supported types for the proxy. These will typically
     * be interfaces, but can also be just a single concrete class, in which
     * case a CGLIB-based class proxy will be created.
     * @param registryKeyName the name identifier for the service in the service
     * registry
     * @return a {@link ProxyFactory} instance
     */
    // FIXME should registryKeyName support filters
    ProxyFactory createDynamicProxyFactory(Class<?>[] proxyTypes, String registryKeyName);
    
    /**
     * Creates {@link ProxyFactory} for a service which has already been obtained from the service registry
     * @param types the supported types for the proxy. These will typically be
     * interfaces, but can also be just a single concrete class, in which case a
     * CGLIB-based class proxy will be created.
     * @param the {@link ServiceRegistryReference} instance which backs the service entry
     */
    ProxyFactory createStaticProxyFactory(Class<?>[] types, ServiceRegistryReference reference);

}
