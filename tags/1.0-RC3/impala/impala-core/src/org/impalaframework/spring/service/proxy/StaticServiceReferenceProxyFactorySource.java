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

import java.lang.reflect.Modifier;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.spring.service.ServiceEndpointTargetSource;
import org.impalaframework.spring.service.registry.StaticServiceRegistryTargetSource;
import org.impalaframework.util.ArrayUtils;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.util.Assert;

/**
 * Creates proxy factory backed by an already obtained service registry reference. Here the job of the 
 * {@link TargetSource} is not to obtain the service, just to handle aspects of proxying the service.
 */
public class StaticServiceReferenceProxyFactorySource extends BaseProxyFactorySource {
    
    private ServiceRegistryEntry reference;
    private Class<?>[] proxyTypes;
    
    /**
     * Constructor
     * @param proxyTypes the types used for the proxy
     * @param reference the {@link ServiceRegistryEntry} instance to be proxied
     */
    public StaticServiceReferenceProxyFactorySource(Class<?>[] proxyTypes, ServiceRegistryEntry reference) {
        super();
        this.proxyTypes = proxyTypes;
        this.reference = reference;
    }

    public void init() {
        
        Assert.notNull(reference, "reference cannot be null");
        
        ServiceEndpointTargetSource targetSource = new StaticServiceRegistryTargetSource(reference);
        
        ProxyFactory proxyFactory = new ProxyFactory();
        if (!ArrayUtils.isNullOrEmpty(proxyTypes)) {
            ProxyFactorySourceUtils.addInterfaces(proxyFactory, proxyTypes);
        } else {
            boolean isFinal = Modifier.isFinal(reference.getServiceBeanReference().getService().getClass().getModifiers());
            if (isFinal) {
                throw new InvalidStateException("Cannot create proxy for service reference " + reference + " as no interfaces have been " +
                        "specified and the bean class is final, therefore cannot be proxied");
            }
        }

        afterInit(proxyFactory, targetSource);
    }
    
}
