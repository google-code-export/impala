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
import org.impalaframework.service.reference.BasicServiceRegistryEntry;
import org.impalaframework.service.registry.ServiceRegistryAware;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ClassUtils;

/**
 * Base implementation for <code>FactoryBean</code> which represents a service obtained from the service registry.
 * Defines an abstract {@link #createProxyFactory()} methods which subclasses should implement to define exactly how 
 * the {@link ProxyFactory} gets created. Implementations may for example hold a static reference to a bean obtained from the 
 * service registry, or use some kind of dynamic lookup.
 * 
 * @see BasicServiceRegistryEntry
 * @author Phil Zoio
 */
public abstract class BaseServiceProxyFactoryBean implements FactoryBean, BeanNameAware, InitializingBean, ContributionEndpoint, ServiceRegistryAware, BeanClassLoaderAware {

    private static final long serialVersionUID = 1L;
    
    private ServiceProxyFactoryCreator proxyFactoryCreator;

    private ProxyFactory proxyFactory;

    private ClassLoader beanClassLoader;
    
    private ServiceRegistry serviceRegistry;

    private String beanName;

    /* *************** InitializingBean implementation method ************** */

    public void afterPropertiesSet() throws Exception {
        
        if (this.proxyFactoryCreator == null) {
            this.proxyFactoryCreator = new DefaultServiceProxyFactoryCreator();
        }
        
        this.proxyFactory = createProxyFactory();
    }

    /* *************** BeanNameAware implementation method ************** */

    public void setBeanName(String name) {
        this.beanName = name;
    }

    /* *************** Protected methods ************** */
    
    protected abstract ProxyFactory createProxyFactory();
    
    protected ServiceProxyFactoryCreator getProxyFactoryCreator() {
        return proxyFactoryCreator;
    }
    
    protected ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }
    
    protected String getBeanName() {
        return beanName;
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
    
    /* *************** ServiceRegistryAware implementation ************** */
    
    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    /* *************** dependency injection setters ************** */

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    public void setProxyFactoryCreator(ServiceProxyFactoryCreator proxyFactoryCreator) {
        this.proxyFactoryCreator = proxyFactoryCreator;
    }
    
}
