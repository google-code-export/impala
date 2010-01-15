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

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.web.spring.integration.ModuleHttpServiceInvokerBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.web.context.support.GenericWebApplicationContext;

/**
 * Extends {@link BaseWebModuleLoader}, providing a concrete implementation of
 * {@link #configureBeanFactoryAndApplicationContext(ModuleDefinition, DefaultListableBeanFactory, GenericWebApplicationContext)}, 
 * in which a {@link ModuleHttpServiceInvokerBuilder} is automatically
 * registered with the bean factory. This is to allow request URI suffix to
 * filter and servlet mappings to be contributed to build a
 * {@link org.impalaframework.web.servlet.invocation.ModuleHttpServiceInvoker} instance.
 * 
 * @author Phil Zoio
 */
public class WebModuleLoader extends BaseWebModuleLoader {

    public WebModuleLoader() {
        super();
    }

    public WebModuleLoader(ServletContext servletContext) {
        super(servletContext);
    }

    /**
     * Hook for subclasses to perform additional configuration on application context
     */
    protected void configureBeanFactoryAndApplicationContext(
            ModuleDefinition moduleDefinition,
            final DefaultListableBeanFactory beanFactory,
            final GenericWebApplicationContext context) {
        //register invoker builder definition
        RootBeanDefinition invokerBuilderDefinition = new RootBeanDefinition(ModuleHttpServiceInvokerBuilder.class);
        beanFactory.registerBeanDefinition(ModuleHttpServiceInvokerBuilder.class.getName()+".for." + moduleDefinition.getName(), invokerBuilderDefinition);
    }
    
}
