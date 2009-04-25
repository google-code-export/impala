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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.spring.service.ContributionEndpointTargetSource;
import org.impalaframework.spring.service.registry.BeanRetrievingServiceRegistryTargetSource;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.util.Assert;

/**
 * Creates proxy factory backed by a dynamically obtained service, where the
 * lookup assumes that the service was exported using a name key entry.
 */
public class BeanRetrievingProxyFactorySource extends BaseProxyFactorySource {
    
    static final Log logger = LogFactory.getLog(BeanRetrievingProxyFactorySource.class);
    
    private final ServiceRegistry serviceRegistry;
    private final Class<?>[] interfaces;
    private final String registryBeanName;

    /**
     * Constructor
     * @param interfaces the types to which the reference should be compatible
     * @param serviceRegistry the service registry
     * @param registryBeanName the name of the bean as registered in the service registry
     */
    public BeanRetrievingProxyFactorySource(
            Class<?>[] interfaces,
            ServiceRegistry serviceRegistry, 
            String registryBeanName) {
        
        super();
        
        this.interfaces = interfaces;
        this.serviceRegistry = serviceRegistry;
        this.registryBeanName = registryBeanName;
    }

    public void init() {
        
        Assert.notNull(this.serviceRegistry, "serviceRegistry cannot be null");
        Assert.notNull(interfaces, "interfaces cannot be null");
        Assert.notEmpty(interfaces, "interfaces cannot be empty");
        
        //this will return a non-null value if single interface which is concrete class
        ContributionEndpointTargetSource targetSource = new BeanRetrievingServiceRegistryTargetSource(registryBeanName, interfaces, this.serviceRegistry);
        ProxyFactory proxyFactory = new ProxyFactory();
        
        if (targetSource.getTargetClass() == null) {
            //not proxying by class, so proxy by interface
            ProxyFactorySourceUtils.addInterfaces(proxyFactory, interfaces);
        }
        
        afterInit(proxyFactory, targetSource, registryBeanName);
    }
}
