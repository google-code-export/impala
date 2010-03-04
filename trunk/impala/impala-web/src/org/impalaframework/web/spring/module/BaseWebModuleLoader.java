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

package org.impalaframework.web.spring.module;

import javax.servlet.ServletContext;

import org.impalaframework.exception.ExecutionException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModuleLoader;
import org.impalaframework.spring.module.loader.BaseSpringModuleLoader;
import org.impalaframework.spring.module.loader.ModuleLoaderUtils;
import org.impalaframework.web.servlet.qualifier.WebAttributeQualifier;
import org.impalaframework.web.servlet.wrapper.ServletContextWrapper;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

/**
 * Base implementation of {@link ModuleLoader} which extends {@link BaseSpringModuleLoader}
 * providing additional functionality specific to the web environment.
 * Specifically, it uses the {@link ServletContextWrapper} if present, to provide 
 * a wrapped {@link ServletContext} instance as a proxy to the real {@link ServletContext} 
 * provided via the {@link ServletContextAware#setServletContext(ServletContext)} method.
 * Also, it creates an instance of {@link GenericWebApplicationContext} to use as the
 * {@link ConfigurableApplicationContext} instance.
 * 
 * @author Phil Zoio
 */
public class BaseWebModuleLoader extends BaseSpringModuleLoader implements ServletContextAware {

    private ServletContext servletContext;
    
    private ServletContextWrapper servletContextWrapper;
    
    private WebAttributeQualifier webAttributeQualifier;

    private static final String SERVLET_CONTEXT_ATTRIBUTE_NAME = "wrapped_servlet_context";

    public BaseWebModuleLoader() {
    }

    public BaseWebModuleLoader(ServletContext servletContext) {
        Assert.notNull(servletContext, "ServletContext cannot be null");
        this.servletContext = servletContext;
    }

    public final GenericWebApplicationContext newApplicationContext(
            Application application,
            ApplicationContext parent, 
            ModuleDefinition moduleDefinition, 
            ClassLoader classLoader) {
        
        ServletContext wrappedServletContext = servletContext;
        
        if (servletContextWrapper != null) {
            wrappedServletContext = servletContextWrapper.wrapServletContext(servletContext, application.getId(), moduleDefinition, classLoader);
            
            final String wrappedContextAttributeName = getWrappedServletContextAttributeName(application, moduleDefinition);
            servletContext.setAttribute(wrappedContextAttributeName, wrappedServletContext);
        }
        
        final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.setBeanClassLoader(classLoader);
        
        final GenericWebApplicationContext context = newApplicationContext(
                moduleDefinition, 
                parent, 
                classLoader, 
                wrappedServletContext,
                beanFactory);
        
        configureBeanFactoryAndApplicationContext(moduleDefinition, beanFactory, context);
        return context;
    }

    protected GenericWebApplicationContext newApplicationContext(
            ModuleDefinition moduleDefinition, 
            ApplicationContext parent,
            ClassLoader classLoader, 
            ServletContext servletContext,
            final DefaultListableBeanFactory beanFactory) {
        
        final GenericWebApplicationContext context = new GenericWebApplicationContext(beanFactory);
        context.setServletContext(servletContext);
        context.setClassLoader(classLoader);
        
        context.setParent(parent);
        final String displayName = ModuleLoaderUtils.getDisplayName(moduleDefinition, context);
        context.setDisplayName(displayName);
        return context;
    }

    @Override
    public void handleRefresh(String applicationId, ConfigurableApplicationContext context, ModuleDefinition moduleDefinition) {
        
        Assert.notNull(webAttributeQualifier, "webAttributeQualifier cannot be null");        
        String applicationContextAttributeName = webAttributeQualifier.getQualifiedAttributeName(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationId, moduleDefinition.getName());
        String servletContextAttributeName = webAttributeQualifier.getQualifiedAttributeName(SERVLET_CONTEXT_ATTRIBUTE_NAME, applicationId, moduleDefinition.getName());
        
        try {
            servletContext.setAttribute(applicationContextAttributeName, context);
            doHandleRefresh(applicationId, context, moduleDefinition);
        }
        catch (RuntimeException e) {
            servletContext.removeAttribute(applicationContextAttributeName);
            servletContext.removeAttribute(servletContextAttributeName);
            throw e;
        }
        catch (Throwable e) {
            servletContext.removeAttribute(applicationContextAttributeName);
            servletContext.removeAttribute(servletContextAttributeName);
            throw new ExecutionException(e.getMessage(), e);
        }
    }
    
    @Override
    public void beforeClose(String applicationId, ApplicationContext applicationContext, ModuleDefinition moduleDefinition) {
        String servletContextAttributeName = getQualifiedAttributeName(SERVLET_CONTEXT_ATTRIBUTE_NAME, applicationId, moduleDefinition.getName());
        servletContext.removeAttribute(servletContextAttributeName);

        String applicationContextAttributeName = getQualifiedAttributeName(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationId, moduleDefinition.getName());
        servletContext.removeAttribute(applicationContextAttributeName);
    }

    protected void doHandleRefresh(String applicationId, ConfigurableApplicationContext context, ModuleDefinition moduleDefinition) {
        super.handleRefresh(applicationId, context, moduleDefinition);
    }
    
    /**
     * Hook for subclasses to perform additional configuration on application context
     */
    protected void configureBeanFactoryAndApplicationContext(
            ModuleDefinition moduleDefinition,
            final DefaultListableBeanFactory beanFactory,
            final GenericWebApplicationContext context) {
    }
    
    protected ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    private String getWrappedServletContextAttributeName(Application application, ModuleDefinition moduleDefinition) {
        return getWrappedServletContextAttributeName(application.getId(), moduleDefinition.getName());
    }

    private String getWrappedServletContextAttributeName(final String applicationId, final String name) {
        return getQualifiedAttributeName(SERVLET_CONTEXT_ATTRIBUTE_NAME, applicationId, name);
    }

    private String getQualifiedAttributeName(
            final String attributeName,
            final String applicationId, 
            final String moduleName) {
        final String wrappedContextAttributeName = webAttributeQualifier.getQualifiedAttributeName(attributeName, applicationId, moduleName);
        return wrappedContextAttributeName;
    }
    
    /* ********************** Injected setters ************************ */

    public void setServletContextWrapper(ServletContextWrapper servletContextWrapper) {
        this.servletContextWrapper = servletContextWrapper;
    }
    
    public void setWebAttributeQualifier(WebAttributeQualifier webAttributeQualifier) {
        this.webAttributeQualifier = webAttributeQualifier;
    }
    
}