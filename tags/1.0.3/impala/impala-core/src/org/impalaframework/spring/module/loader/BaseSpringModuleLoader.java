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

package org.impalaframework.spring.module.loader;

import java.util.ArrayList;
import java.util.Collection;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.loader.ModuleUtils;
import org.impalaframework.module.resource.ModuleLocationsResourceLoader;
import org.impalaframework.module.spi.Application;
import org.impalaframework.spring.module.SpringModuleLoader;
import org.impalaframework.spring.resource.ClassPathResourceLoader;
import org.impalaframework.spring.resource.CompositeResourceLoader;
import org.impalaframework.spring.resource.ResourceLoader;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Base implementation of {@link SpringModuleLoader} which provides implementations for methods defined in {@link SpringModuleLoader}.
 * 
 * @author Phil Zoio
 */
public abstract class BaseSpringModuleLoader implements SpringModuleLoader {

    private Collection<ResourceLoader> springLocationResourceLoaders;
    
    public GenericApplicationContext newApplicationContext(Application application, ApplicationContext parent, ModuleDefinition definition, ClassLoader classLoader) {
        Assert.notNull(classLoader, "classloader cannot be null");
        
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.setBeanClassLoader(classLoader);

        // create the application context, and set the class loader
        GenericApplicationContext context = new GenericApplicationContext(beanFactory, parent);
        context.setClassLoader(classLoader);
        context.setDisplayName(ModuleLoaderUtils.getDisplayName(definition, context));
        return context;
    }

    public final Resource[] getSpringConfigResources(String applicationId, ModuleDefinition moduleDefinition, ClassLoader classLoader) {
        
        ModuleLocationsResourceLoader loader = new ModuleLocationsResourceLoader();
        Collection<ResourceLoader> resourceLoaders = getSpringLocationResourceLoaders();
        ResourceLoader compositeResourceLoader = new CompositeResourceLoader(resourceLoaders);
        loader.setResourceLoader(compositeResourceLoader);
        return loader.getSpringLocations(moduleDefinition, classLoader);
    }

    protected Collection<ResourceLoader> getSpringLocationResourceLoaders() {
        
        Collection<ResourceLoader> injectedLocationResourceLoaders = getInjectedSpringLocationResourceLoaders();
        if (injectedLocationResourceLoaders != null) {
            return injectedLocationResourceLoaders;
        }
        
        Collection<ResourceLoader> resourceLoaders = new ArrayList<ResourceLoader>();
        resourceLoaders.add(new ClassPathResourceLoader());
        return resourceLoaders;
    }
    
    public XmlBeanDefinitionReader newBeanDefinitionReader(String applicationId, ConfigurableApplicationContext context, ModuleDefinition definition) {
        final ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        return new XmlBeanDefinitionReader(ModuleUtils.castToBeanDefinitionRegistry(beanFactory));
    }

    public void afterRefresh(String applicationId, ConfigurableApplicationContext context, ModuleDefinition definition) {
    }
    
    public void handleRefresh(String applicationId, ConfigurableApplicationContext context, ModuleDefinition moduleDefinition) {
        // refresh the application context - now we're ready to go
        context.refresh();
    }
    
    /**
    * Nothing to do in this implementation
    */
    public void beforeClose(String applicationId, ApplicationContext applicationContext, ModuleDefinition moduleDefinition) {
    }

    protected Collection<ResourceLoader> getInjectedSpringLocationResourceLoaders() {
        return this.springLocationResourceLoaders;
    }

    public void setSpringLocationResourceLoaders(Collection<ResourceLoader> resourceLoaders) {
        this.springLocationResourceLoaders = resourceLoaders;
    }

}
