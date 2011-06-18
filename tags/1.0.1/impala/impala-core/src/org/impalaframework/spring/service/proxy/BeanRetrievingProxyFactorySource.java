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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.spring.service.ServiceEndpointTargetSource;
import org.impalaframework.spring.service.registry.BeanRetrievingServiceRegistryTargetSource;
import org.impalaframework.util.ArrayUtils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.util.Assert;

/**
 * Creates proxy factory backed by a dynamically obtained service, where the
 * lookup assumes that the service was exported using a name key entry.
 */
public class BeanRetrievingProxyFactorySource extends BaseProxyFactorySource {
    
    static final Log logger = LogFactory.getLog(BeanRetrievingProxyFactorySource.class);
    
    private final ServiceRegistry serviceRegistry;
    private final Class<?>[] proxyTypes;
    private final Class<?>[] exportTypes;
    private final String registryBeanName;

    /**
     * Constructor
     * @param serviceRegistry the service registry
     * @param proxyTypes the types to which the reference should be compatible
     * @param registryBeanName the name of the bean as registered in the service registry
     */
    public BeanRetrievingProxyFactorySource(
            ServiceRegistry serviceRegistry,
            Class<?>[] proxyTypes, 
            Class<?>[] exportTypes, 
            String registryBeanName) {
        
        super();
        
        this.exportTypes = exportTypes;
        this.serviceRegistry = serviceRegistry;
        this.registryBeanName = registryBeanName;
        
        if (ArrayUtils.isNullOrEmpty(proxyTypes)) {
            Assert.isTrue(ArrayUtils.notNullOrEmpty(exportTypes));
            this.proxyTypes = exportTypes;
        } else {
            this.proxyTypes = proxyTypes;
        }
    }

    public void init() {
        
        Assert.notNull(this.serviceRegistry, "serviceRegistry cannot be null");
        
        //BeanRetrievingServiceRegistryTargetSource implements rules on whether bean name, proxyTypes or exportTypes can and cannot be null
        
        //this will return a non-null value if single interface which is concrete class
        ServiceEndpointTargetSource targetSource = new BeanRetrievingServiceRegistryTargetSource(
                this.serviceRegistry, 
                registryBeanName, 
                proxyTypes, 
                exportTypes);
        
        ProxyFactory proxyFactory = new ProxyFactory();
        
        if (targetSource.getTargetClass() == null) {
            //not proxying by class, so proxy by interface
            ProxyFactorySourceUtils.addInterfaces(proxyFactory, proxyTypes);
        }
        
        afterInit(proxyFactory, targetSource);
    }
}
