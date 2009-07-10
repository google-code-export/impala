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

package org.impalaframework.web.servlet.invocation;

import java.util.Map;

import javax.servlet.ServletContext;

import org.impalaframework.util.ObjectUtils;
import org.impalaframework.web.spring.integration.FilterFactoryBean;
import org.impalaframework.web.spring.integration.ServletFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.web.context.ServletContextAware;

/**
 * Bean which ties into the Spring life-cycle to create a {@link ModuleHttpServiceInvoker} for the module.
 * Collects all suffix to bean name and servlet filter mapping contributions by finding all beans 
 * registered within the application context of type {@link ModuleInvokerContributor}.
 * @author Phil Zoio
 */
public class ModuleHttpServiceInvokerBuilder implements BeanFactoryAware, InitializingBean, DisposableBean, ServletContextAware {

    private BeanFactory beanFactory;
    
    private ServletContext servletContext;

    /**
     * Goes through the contributors, and for each suffix, find the relevant
     * servlets and filter registered with the module. Instantiates a
     * {@link ModuleHttpServiceInvoker}, set the servlet and filter mapping
     * mappings, and bind to the {@link ServletContext} context using the
     * attribute XXX
     */
    public void afterPropertiesSet() throws Exception {

        // FIXME implement and test
        
        ListableBeanFactory beanFactory = ObjectUtils.cast(this.beanFactory, ListableBeanFactory.class);
        Map<String, ModuleInvokerContributor> contributors = beanFactory.getBeansOfType(ModuleInvokerContributor.class);
        Map<String, ServletFactoryBean> servletFactoryBeans = beanFactory.getBeansOfType(ServletFactoryBean.class);
        Map<String, FilterFactoryBean> filterFactoryBeans = beanFactory.getBeansOfType(FilterFactoryBean.class);
        
        System.out.println("Contributors: " + contributors.values());
        System.out.println("Servlets: " + servletFactoryBeans.values());
        System.out.println("Filters: " + filterFactoryBeans.values());
        
        //now go through the contributors, and for each suffix, find the relevant servlets and filters
        //instantiate a ModuleHttpServiceInvoker, set the mappings, and bind to the servlet context
    }
    
    /**
     * Unbinds the {@link ModuleHttpServiceInvoker} registered with the
     * {@link ServletContext}
     */
    public void destroy() throws Exception {

        // FIXME implement and test
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
    
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
