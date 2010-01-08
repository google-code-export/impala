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

import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.reference.BasicServiceRegistryEntry;
import org.impalaframework.service.registry.ServiceRegistryAware;
import org.impalaframework.spring.service.ProxyFactoryCreator;
import org.impalaframework.spring.service.SpringServiceEndpoint;
import org.impalaframework.util.ArrayUtils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
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
public abstract class BaseServiceProxyFactoryBean 
    implements FactoryBean, 
    BeanNameAware, 
    InitializingBean,
    ServiceRegistryAware,
    ProxyFactoryCreatorAware,
    BeanClassLoaderAware,
    SpringServiceEndpoint {

    private static final long serialVersionUID = 1L;
    
    private ProxyFactoryCreator proxyFactoryCreator;

    private ProxyFactory proxyFactory;

    private ClassLoader beanClassLoader;
    
    private ServiceRegistry serviceRegistry;

    private Class<?>[] proxyTypes;

    private String beanName;

    /* *************** InitializingBean implementation method ************** */

    public void afterPropertiesSet() throws Exception {
        
        if (this.proxyFactoryCreator == null) {
            this.proxyFactoryCreator = new DefaultProxyFactoryCreator();
        }
        
        this.proxyFactory = createProxyFactory();
    }

    /* *************** BeanNameAware implementation method ************** */

    public void setBeanName(String name) {
        this.beanName = name;
    }

    /* *************** Protected methods ************** */
    
    protected abstract ProxyFactory createProxyFactory();
    
    protected abstract Class<?>[] getExportTypes();
    
    protected ProxyFactoryCreator getProxyFactoryCreator() {
        return proxyFactoryCreator;
    }
    
    protected ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }
    
    protected String getBeanName() {
        return beanName;
    }
    
    protected Class<?>[] getProxyTypes() {
        return proxyTypes;
    }
    
    protected Class<?>[] getProxyTypesToUse(boolean allowEmpty) {
        final Class<?>[] proxyTypes = getProxyTypes();
        final Class<?>[] exportTypes = getExportTypes();
        
        final Class<?>[] proxyTypesToUse;
        if (ArrayUtils.isNullOrEmpty(proxyTypes)) {
            if (!allowEmpty) Assert.isTrue(!ArrayUtils.isNullOrEmpty(exportTypes), "exportTypes and proxyTypes cannot both be empty");
            proxyTypesToUse = exportTypes;
        } else {
            proxyTypesToUse = proxyTypes;
        }
        return proxyTypesToUse;
    }
    
    /* *************** FactoryBean implementation methods ************** */

    public Object getObject() throws Exception {
        return proxyFactory.getProxy(beanClassLoader != null ? beanClassLoader : ClassUtils.getDefaultClassLoader());
    }

    @SuppressWarnings("unchecked")
    public Class getObjectType() {
        final Class<?>[] proxyTypesToUse = getProxyTypesToUse(true);
        if (proxyTypesToUse != null && proxyTypesToUse.length > 0)
            return proxyTypesToUse[0];
        return null;
    }

    public boolean isSingleton() {
        // prototype currently not supported
        return true;
    }

    /* ******************** ServiceProxyFactoryCreatorAware implementation ******************** */
    
    public void setProxyFactoryCreator(ProxyFactoryCreator serviceProxyFactoryCreator) {
        if (this.proxyFactoryCreator == null) {
            this.proxyFactoryCreator = serviceProxyFactoryCreator;
        }
    }
    
    /* *************** ServiceRegistryAware implementation ************** */
    
    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    /* *************** dependency injection setters ************** */

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }
    
    /**
     * Sets proxy types for exposed bean. Proxy for service exposed using the types provided.
     * If single type is provided, and this type is a non-final concrete class, then 
     * class-based proxy using CGLIB is used.
     * 
     * Can only be null or empty if {@link #exportTypes} is not null or empty.
     */
    public final void setProxyTypes(Class<?>[] proxyTypes) {
        this.proxyTypes = proxyTypes;
    }
    
}
