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

import org.impalaframework.service.ContributionEndpoint;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.reference.BasicServiceRegistryReference;
import org.impalaframework.service.registry.ServiceRegistryAware;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ClassUtils;

/**
 * <code>FactoryBean</code> which creates a proxy which has uses
 * <code>ServiceRegistryTargetSource</code> as a target source and
 * <code>ContributionEndpointInterceptor</code> as interceptor.
 * 
 * The ContributionProxyFactoryBean works under the assumption that the service was exported against a named key.
 * By default, the key is assumed to be the same name as the {@link ContributionProxyFactoryBean}'s bean name.
 * However, this can be overridden.
 * 
 * @see BasicServiceRegistryReference
 * @author Phil Zoio
 */
public class ContributionProxyFactoryBean implements FactoryBean, BeanNameAware, InitializingBean, ContributionEndpoint, ServiceRegistryAware, BeanClassLoaderAware {

    private static final long serialVersionUID = 1L;
    
    private ServiceProxyFactoryCreator proxyFactoryCreator;

    private Class<?>[] proxyInterfaces;

    private String beanName;
    
    private String exportedBeanName;

    private ProxyFactory proxyFactory;

    private ClassLoader beanClassLoader;
    
    private ServiceRegistry serviceRegistry;

    /* *************** BeanNameAware implementation method ************** */

    public void setBeanName(String name) {
        this.beanName = name;
    }

    /* *************** InitializingBean implementation method ************** */

    public void afterPropertiesSet() throws Exception {
        
        if (this.proxyFactoryCreator == null) {
            this.proxyFactoryCreator = new DefaultServiceProxyFactoryCreator();
            this.proxyFactoryCreator.setServiceRegistry(this.serviceRegistry);
        }
        
        String registryBeanName = (exportedBeanName != null ? exportedBeanName : beanName);
        this.proxyFactory = proxyFactoryCreator.createDynamicProxyFactory(proxyInterfaces, registryBeanName);
    }

    /* *************** FactoryBean implementation methods ************** */

    public Object getObject() throws Exception {
        return proxyFactory.getProxy(beanClassLoader != null ? beanClassLoader : ClassUtils.getDefaultClassLoader());
    }

    @SuppressWarnings("unchecked")
    public Class getObjectType() {
        // no specific awareness of object type, so return null
        return null;
    }

    public boolean isSingleton() {
        // prototype currently not supported
        return true;
    }
    
    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    /* *************** dependency injection setters ************** */

    public void setProxyInterfaces(Class<?>[] proxyInterfaces) {
        this.proxyInterfaces = proxyInterfaces;
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    public void setExportedBeanName(String exportedBeanName) {
        this.exportedBeanName = exportedBeanName;
    }

    public void setProxyFactoryCreator(ServiceProxyFactoryCreator proxyFactoryCreator) {
        this.proxyFactoryCreator = proxyFactoryCreator;
    }
    
}
