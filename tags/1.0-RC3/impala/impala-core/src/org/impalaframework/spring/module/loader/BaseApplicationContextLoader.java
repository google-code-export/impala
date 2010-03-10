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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.runtime.ModuleRuntimeUtils;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModuleLoader;
import org.impalaframework.spring.module.ApplicationContextLoader;
import org.impalaframework.spring.module.DelegatingContextLoader;
import org.impalaframework.spring.module.SpringModuleLoader;
import org.impalaframework.util.ObjectUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * @author Phil Zoio
 */
public class BaseApplicationContextLoader implements ApplicationContextLoader {
    
    private static Log logger = LogFactory.getLog(BaseApplicationContextLoader.class);
    
    private ModuleLoaderRegistry moduleLoaderRegistry;
    
    private DelegatingContextLoaderRegistry delegatingContextLoaderRegistry;

    public BaseApplicationContextLoader() {
    }

    public ConfigurableApplicationContext loadContext(Application application, ClassLoader classLoader, ModuleDefinition definition, ApplicationContext parent) {

        Assert.notNull(moduleLoaderRegistry, ModuleLoaderRegistry.class.getName() + " cannot be null");
        Assert.notNull(delegatingContextLoaderRegistry, DelegatingContextLoaderRegistry.class.getName() + " cannot be null");
        ConfigurableApplicationContext context = null;
        
        final ModuleLoader loader = moduleLoaderRegistry.getModuleLoader(ModuleRuntimeUtils.getModuleLoaderKey(definition), false);
        final SpringModuleLoader moduleLoader = ObjectUtils.cast(loader, SpringModuleLoader.class);
        
        final DelegatingContextLoader delegatingLoader = delegatingContextLoaderRegistry.getDelegatingLoader(definition.getType());

        try {

            if (moduleLoader != null) {
                if (logger.isDebugEnabled()) logger.debug("Loading module " + definition + " using ModuleLoader " + moduleLoader);
                context = loadApplicationContext(application, classLoader, moduleLoader, parent, definition);
                
                final String applicationId = application.getId();
                moduleLoader.afterRefresh(applicationId, context, definition);
            }
            else if (delegatingLoader != null) {
                if (logger.isDebugEnabled()) logger.debug("Loading module " + definition + " using DelegatingContextLoader " + moduleLoader);
                context = delegatingLoader.loadApplicationContext(parent, definition);
            }
            else {
                throw new ConfigurationException("No " + ModuleLoader.class.getName() + " or "
                        + DelegatingContextLoader.class.getName() + " specified for module definition type " + definition.getType());
            }

        }
        finally {
            if (moduleLoader != null) {
                afterContextLoaded(definition, moduleLoader);
            }
        }

        return context;
    }

    /**
     * If {@link ApplicationContext} is an instance of
     * {@link ConfigurableApplicationContext}, then calls
     * {@link ConfigurableApplicationContext#close()}. Also, prior to this, if
     * module is associated with {@link SpringModuleLoader}, then
     * {@link SpringModuleLoader#beforeClose(String, ApplicationContext, ModuleDefinition)}
     * is called.
     */
    public void closeContext(String applicationId, ModuleDefinition moduleDefinition, ApplicationContext applicationContext) {
        
        Assert.notNull(moduleLoaderRegistry, ModuleLoaderRegistry.class.getName() + " cannot be null");        
        final ModuleLoader loader = moduleLoaderRegistry.getModuleLoader(ModuleRuntimeUtils.getModuleLoaderKey(moduleDefinition), false);
        
        if (loader instanceof SpringModuleLoader) {
            SpringModuleLoader springModuleLoader = (SpringModuleLoader) loader;
            springModuleLoader.beforeClose(applicationId, applicationContext, moduleDefinition);
        }
        
        if (applicationContext instanceof ConfigurableApplicationContext) {
            ConfigurableApplicationContext configurableContext = (ConfigurableApplicationContext) applicationContext;
            configurableContext.close();
        }
    }

    protected void afterContextLoaded(ModuleDefinition definition,
            final ModuleLoader moduleLoader) {
    }

    private ConfigurableApplicationContext loadApplicationContext(
            Application application,
            ClassLoader classLoader, 
            SpringModuleLoader moduleLoader, 
            ApplicationContext parent, 
            ModuleDefinition definition) {

        ClassLoader existing = ClassUtils.getDefaultClassLoader();

        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            
            if (logger.isDebugEnabled()) logger.debug("Setting class loader to " + classLoader);

            ConfigurableApplicationContext context = moduleLoader.newApplicationContext(application, parent, definition, classLoader);
            ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
            addBeanPostProcessors(application, definition, beanFactory);
            
            final String applicationId = application.getId();
            BeanDefinitionReader reader = moduleLoader.newBeanDefinitionReader(applicationId, context, definition);

            if (reader != null) {
                //if this is null, then we assume moduleLoader or refresh takes care of this
                
                if (reader instanceof AbstractBeanDefinitionReader) {
                    ((AbstractBeanDefinitionReader) reader).setBeanClassLoader(classLoader);
                }
    
                final Resource[] resources = moduleLoader.getSpringConfigResources(applicationId, definition, classLoader);
                reader.loadBeanDefinitions(resources);
            }

            moduleLoader.handleRefresh(applicationId, context, definition);
            return context;
        }
        finally {
            Thread.currentThread().setContextClassLoader(existing);
        }
    }

    protected void addBeanPostProcessors(Application application, ModuleDefinition definition, ConfigurableListableBeanFactory beanFactory) {
    }
    
    /* **************************** injected setters ************************** */

    public void setModuleLoaderRegistry(ModuleLoaderRegistry moduleLoaderRegistry) {
        this.moduleLoaderRegistry = moduleLoaderRegistry;
    }

    public void setDelegatingContextLoaderRegistry(DelegatingContextLoaderRegistry delegatingContextLoaderRegistry) {
        this.delegatingContextLoaderRegistry = delegatingContextLoaderRegistry;
    }

}
