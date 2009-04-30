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

import org.impalaframework.service.reference.BasicServiceRegistryReference;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanNameAware;

/**
 * The <code>ContributionProxyFactoryBean</code> works under the assumption that the service was exported against a named key.
 * By default, the key is assumed to be the same name as the {@link ContributionProxyFactoryBean}'s bean name.
 * However, this can be overridden.
 * 
 * The service is retrieved using dynamic lookup following each invocation on the proxy.
 * 
 * @see BasicServiceRegistryReference
 * @author Phil Zoio
 */
public class ContributionProxyFactoryBean extends BaseContributionProxyFactoryBean implements BeanNameAware {

    private static final long serialVersionUID = 1L;

    private Class<?>[] proxyInterfaces;
    
    private String beanName;
    
    private String exportedBeanName;

    /* *************** BeanNameAware implementation method ************** */

    public void setBeanName(String name) {
        this.beanName = name;
    }

    /* *************** Abstract superclass method implementation ************** */

    protected ProxyFactory createProxyFactory() {
        String registryBeanName = (exportedBeanName != null ? exportedBeanName : beanName);
        
        BeanRetrievingProxyFactorySource source = new BeanRetrievingProxyFactorySource(super.getServiceRegistry(), proxyInterfaces, registryBeanName, false);
        
        ProxyFactory createDynamicProxyFactory = getProxyFactoryCreator().createProxyFactory(source, beanName);
        return createDynamicProxyFactory;
    }

    /* *************** dependency injection setters ************** */

    public void setProxyInterfaces(Class<?>[] proxyInterfaces) {
        this.proxyInterfaces = proxyInterfaces;
    }

    public void setExportedBeanName(String exportedBeanName) {
        this.exportedBeanName = exportedBeanName;
    }
    
}
