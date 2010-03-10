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

package org.impalaframework.spring.service.bean;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionAware;
import org.impalaframework.service.ServiceBeanReference;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.spring.service.SpringServiceBeanUtils;
import org.impalaframework.spring.service.proxy.DefaultProxyFactoryCreator;
import org.impalaframework.spring.service.proxy.StaticServiceReferenceProxyFactorySource;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

/**
 * {@link FactoryBean} which exposes existing named bean via a proxy. The proxy can be used in a static way, allowing the client to
 * treat the source bean as if it is a singleton.
 * @author Phil Zoio
 */
public class ProxiedNamedFactoryBean extends BaseExistingBeanExposingFactoryBean implements BeanClassLoaderAware, ModuleDefinitionAware {

    private String beanName;
    
    private Class<?>[] proxyTypes;

    private ClassLoader beanClassLoader;

    private ModuleDefinition moduleDefinition;
    
    private ServiceBeanReference serviceBeanReference;

    private ProxyFactory proxyFactory;
    
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(beanName, "beanName cannot be null");
        
        BeanFactory parentFactory = findBeanFactory();
        this.serviceBeanReference = SpringServiceBeanUtils.newServiceBeanReference(parentFactory, getBeanNameToSearchFor());
       
        final String moduleName;
        if (moduleDefinition != null) {
            moduleName = moduleDefinition.getName();
        } else {
            moduleName = "internal";
        }
        ServiceRegistryEntry entry = new LocalOnlyServiceRegistryEntry(serviceBeanReference, getBeanNameToSearchFor(), moduleName, this.beanClassLoader);
        
        //no need to set context class loader as this is not being used to call across modules
        DefaultProxyFactoryCreator proxyFactoryCreator = new DefaultProxyFactoryCreator();
        proxyFactoryCreator.setSetContextClassLoader(false);
        
        final StaticServiceReferenceProxyFactorySource proxyFactorySource = new StaticServiceReferenceProxyFactorySource(proxyTypes, entry);
        proxyFactory = proxyFactoryCreator.createProxyFactory(proxyFactorySource, beanName, null);
    }

    @Override
    protected String getBeanNameToSearchFor() {
        return beanName;
    }

    @Override
    protected boolean getIncludeCurrentBeanFactory() {
        return true;
    }    

    public Object getObject() throws Exception {
        return proxyFactory.getProxy();
    }

    public boolean isSingleton() {
        return serviceBeanReference.isStatic();
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    public void setModuleDefinition(ModuleDefinition moduleDefinition) {
        this.moduleDefinition = moduleDefinition;
    }
    
    public void setProxyTypes(Class<?>[] proxyTypes) {
        this.proxyTypes = proxyTypes;
    }
    
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

}
