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

package org.impalaframework.spring.service.proxy;

import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.spring.service.ProxyFactorySource;
import org.impalaframework.spring.service.ServiceEndpointTargetSource;
import org.springframework.aop.framework.ProxyFactory;

/**
 * Base implementation of {@link ProxyFactorySource}. Simply provides access after init to the {@link ProxyFactory},
 * the {@link ServiceEndpointTargetSource} and the registry bean name (which may ben null).
 * 
 * @author Phil Zoio
 */
public abstract class BaseProxyFactorySource implements ProxyFactorySource {

    private ServiceEndpointTargetSource targetSource;
    private ProxyFactory proxyFactory;
    
    protected void afterInit(ProxyFactory proxyFactory, ServiceEndpointTargetSource targetSource) {
        this.proxyFactory = proxyFactory;
        this.targetSource = targetSource;
        this.proxyFactory.setTargetSource(new DelegatingTargetSource(targetSource));
    }

    public final ServiceEndpointTargetSource getTargetSource() {
        return targetSource;
    }

    public final ProxyFactory getProxyFactory() {
        return proxyFactory;
    }
}

class DelegatingTargetSource implements ServiceEndpointTargetSource {
    private ServiceEndpointTargetSource delegate;

    public DelegatingTargetSource(ServiceEndpointTargetSource delegate) {
        super();
        this.delegate = delegate;
    }

    public ServiceRegistryEntry getServiceRegistryReference() {
        return delegate.getServiceRegistryReference();
    }

    public Object getTarget() throws Exception {
        return null;
    }

    public Class<?> getTargetClass() {
        return delegate.getTargetClass();
    }

    public boolean isStatic() {
        return delegate.isStatic();
    }

    public void releaseTarget(Object target) throws Exception {
    }
    
}
