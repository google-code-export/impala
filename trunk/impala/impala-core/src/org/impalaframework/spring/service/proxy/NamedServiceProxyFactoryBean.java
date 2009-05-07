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

import org.impalaframework.service.NamedServiceEndpoint;
import org.impalaframework.service.reference.BasicServiceRegistryEntry;
import org.springframework.aop.framework.ProxyFactory;

/**
 * The <code>NamedServiceProxyFactoryBean</code> works under the assumption that the service was exported against a named key.
 * By default, the key is assumed to be the same name as the {@link NamedServiceProxyFactoryBean}'s bean name.
 * However, this can be overridden.
 * 
 * The service is retrieved using dynamic lookup following each invocation on the proxy.
 * 
 * Note that {@link NamedServiceProxyFactoryBean} replaces {@link ContributionProxyFactoryBean}
 * 
 * @see BasicServiceRegistryEntry
 * @author Phil Zoio
 */
public class NamedServiceProxyFactoryBean extends BaseServiceProxyFactoryBean implements NamedServiceEndpoint {

    private static final long serialVersionUID = 1L;

    private Class<?>[] proxyTypes;
    
    private String exportName;

    /* *************** Abstract superclass method implementation ************** */

    protected ProxyFactory createProxyFactory() {
        String registryBeanName = getRegistryExportName();
        
        BeanRetrievingProxyFactorySource source = new BeanRetrievingProxyFactorySource(super.getServiceRegistry(), proxyTypes, registryBeanName, false);
        
        ProxyFactory createDynamicProxyFactory = getProxyFactoryCreator().createProxyFactory(source, getBeanName());
        return createDynamicProxyFactory;
    }

    /* *************** NamedServiceEndpoint implementation ************** */
    
    public String getExportName() {
        return getRegistryExportName();
    }
    
    /* *************** private methods ************** */
    
    private String getRegistryExportName() {
        return (exportName != null ? exportName : getBeanName());
    }

    /* *************** dependency injection setters ************** */

    public void setProxyTypes(Class<?>[] proxyTypes) {
        this.proxyTypes = proxyTypes;
    }

    public void setExportName(String exportedBeanName) {
        this.exportName = exportedBeanName;
    }
    
}
