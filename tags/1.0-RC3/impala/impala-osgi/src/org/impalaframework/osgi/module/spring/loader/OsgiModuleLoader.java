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

package org.impalaframework.osgi.module.spring.loader;

import java.util.List;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionUtils;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModuleLoader;
import org.impalaframework.osgi.spring.ImpalaOsgiApplicationContext;
import org.impalaframework.osgi.util.OsgiUtils;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.spring.module.ModuleDefinitionPostProcessor;
import org.impalaframework.spring.module.SpringModuleLoader;
import org.impalaframework.spring.module.loader.ModuleLoaderUtils;
import org.impalaframework.spring.service.ProxyFactoryCreator;
import org.impalaframework.spring.service.registry.config.ServiceRegistryPostProcessor;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.util.ResourceUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.osgi.context.BundleContextAware;
import org.springframework.osgi.context.DelegatedExecutionOsgiBundleApplicationContext;
import org.springframework.osgi.io.OsgiBundleResource;
import org.springframework.util.Assert;

/**
 * {@link ModuleLoader} whose purpose is to represent an Impala module loaded within OSGi. 
 * Accesses {@link BundleContext} by implementing {@link BundleContextAware}
 */
public class OsgiModuleLoader implements SpringModuleLoader, BundleContextAware {

    private BundleContext bundleContext;
    
    private ModuleLocationResolver moduleLocationResolver;
    
    private ProxyFactoryCreator serviceProxyFactoryCreator;

    /* ************************* ModuleLoader implementation ************************ */    
    
    /**
     * Returns the class resource locations as determined by the wired in {@link ModuleLocationResolver#getApplicationModuleClassLocations(String)}.
     */
    public Resource[] getClassLocations(String applicationId, ModuleDefinition moduleDefinition) {
        Assert.notNull(moduleDefinition, "moduleDefinition cannot be null");
        
        List<Resource> locations = moduleLocationResolver.getApplicationModuleClassLocations(moduleDefinition.getName());
        return ResourceUtils.toArray(locations);
    }

    /**
     * Finds the bundle whose name is the same as the module name. Then returns the context locations as an array of 
     * {@link OsgiBundleResource} instances.
     */
    public Resource[] getSpringConfigResources(String applicationId, ModuleDefinition moduleDefinition, ClassLoader classLoader) {
        Assert.notNull(moduleDefinition, "moduleDefinition cannot be null");
        
        Bundle bundle = findAndCheckBundle(moduleDefinition);
        
        List<String> configLocations = moduleDefinition.getConfigLocations();
        if (configLocations.isEmpty()) {
            configLocations = ModuleDefinitionUtils.defaultContextLocations(moduleDefinition.getName());
        }
        
        Resource[] resources = OsgiUtils.getBundleResources(bundle, configLocations);
        
        return resources;
    }

    /**
     * Returns an instance of {@link ImpalaOsgiApplicationContext}. Also
     * registers {@link ServiceRegistryPostProcessor} and
     * {@link ModuleDefinitionPostProcessor} {@link BeanPostProcessor}
     * instances. Wires in resources obtained suing
     * {@link #getSpringConfigResources(String, ModuleDefinition, ClassLoader)}, and
     * calls the application context
     * {@link DelegatedExecutionOsgiBundleApplicationContext#startRefresh()}
     * method. Note that the rest of the refresh operation is called in 
     * {@link #handleRefresh(String, ConfigurableApplicationContext, ModuleDefinition)}.
     */
    public ConfigurableApplicationContext newApplicationContext(
            Application application, 
            ApplicationContext parent,
            final ModuleDefinition moduleDefinition, 
            ClassLoader classLoader) {

        Bundle bundle = findBundle(moduleDefinition);
        final ImpalaOsgiApplicationContext applicationContext = newApplicationContext(application, parent, moduleDefinition);
        
        final BundleContext bc = bundle.getBundleContext();
        applicationContext.setBundleContext(bc);
        
        final Resource[] springConfigResources = getSpringConfigResources("id", moduleDefinition, classLoader);
        
        applicationContext.setClassLoader(classLoader);
        applicationContext.setConfigResources(springConfigResources);

        applicationContext.setDisplayName(ModuleLoaderUtils.getDisplayName(moduleDefinition, applicationContext));

        DelegatedExecutionOsgiBundleApplicationContext dc = ObjectUtils.cast(applicationContext, DelegatedExecutionOsgiBundleApplicationContext.class);
        dc.startRefresh();
        
        return applicationContext;
    }
    
    /**
     * Nothing to do in this implementation
     */
    public void beforeClose(String applicationId, ApplicationContext applicationContext, ModuleDefinition moduleDefinition) {
    }

    ImpalaOsgiApplicationContext newApplicationContext(
            Application application, ApplicationContext parent, final ModuleDefinition moduleDefinition) {
        
        final ServiceRegistry serviceRegistry = application.getServiceRegistry();
        
        final ImpalaOsgiApplicationContext applicationContext = new ImpalaOsgiApplicationContext(parent) {

            @Override
            protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
                //need to add these here because don't get the chance after startRefresh() has been called
                beanFactory.addBeanPostProcessor(new ServiceRegistryPostProcessor(serviceRegistry, serviceProxyFactoryCreator));
                beanFactory.addBeanPostProcessor(new ModuleDefinitionPostProcessor(moduleDefinition));
                super.registerBeanPostProcessors(beanFactory);
            }
        };
        return applicationContext;
    }   

    public BeanDefinitionReader newBeanDefinitionReader(
            String applicationId,
            ConfigurableApplicationContext context, ModuleDefinition moduleDefinition) {
        //don't implement this as this is handled internally within ImpalaOsgiApplicationContext
        return null;
    }

    /**
     * Completes the second part of the refresh process by calling {@link DelegatedExecutionOsgiBundleApplicationContext#completeRefresh()}
     */
    public void handleRefresh(String applicationId, ConfigurableApplicationContext context, ModuleDefinition moduleDefinition) {
        DelegatedExecutionOsgiBundleApplicationContext dc = ObjectUtils.cast(context, DelegatedExecutionOsgiBundleApplicationContext.class);
        dc.completeRefresh();
    }
    
    /**
     * No operation is performed in this method implementation
     */
    public void afterRefresh(String applicationId,
            ConfigurableApplicationContext context, ModuleDefinition definition) {
    }

    /* ************************* helper methods ************************ */

    Bundle findBundle(ModuleDefinition moduleDefinition) {
        Bundle bundle = OsgiUtils.findBundle(bundleContext, moduleDefinition.getName());
        return bundle;
    }

    private Bundle findAndCheckBundle(ModuleDefinition moduleDefinition) {
        Bundle bundle = findBundle(moduleDefinition);
        OsgiUtils.checkBundle(moduleDefinition, bundle);
        return bundle;
    }

    public void setModuleLocationResolver(ModuleLocationResolver moduleLocationResolver) {
        this.moduleLocationResolver = moduleLocationResolver;
    }

    public void setServiceProxyFactoryCreator(ProxyFactoryCreator serviceProxyFactoryCreator) {
        this.serviceProxyFactoryCreator = serviceProxyFactoryCreator;
    }
    
    /* ************************* BundleContextAware implementation ************************ */

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }
}
