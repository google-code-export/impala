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

package org.impalaframework.web.spring.integration;

import java.util.Collections;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.ServletContext;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionAware;
import org.impalaframework.web.integration.IntegrationFilterConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

/**
 * <code>FactoryBean</code> which can be used to instantiate an <code>HttpServlet</code> instance.
 * 
 * <i>Init Parameters</i> are passed in via a <code>Map</code>.
 * @author Phil Zoio
 */
public class FilterFactoryBean implements FactoryBean, ServletContextAware, InitializingBean, DisposableBean, ModuleDefinitionAware, ApplicationContextAware {

    private ServletContext servletContext;
    private Map<String,String> initParameters;
    private Filter filter;
    private String filterName;
    private Class<?> filterClass;
    private ApplicationContext applicationContext;
    private ModuleDefinition moduleDefintion;

    public Object getObject() throws Exception {
        return filter;
    }

    public Class<?> getObjectType() {
        return Filter.class;
    }

    public boolean isSingleton() {
        return true;
    }

    /* ***************** InitializingBean implementation **************** */
    
    public final void afterPropertiesSet() throws Exception {
        
        Assert.notNull(servletContext, "servletContext cannot be null - are you sure that the current module is configured as a web module?");      
        Assert.notNull(filterClass, "filterClass cannot be null");
        
        if (filterName == null) {
            filterName = moduleDefintion.getName();
        }
        
        filter = (Filter) BeanUtils.instantiateClass(filterClass);
        Map<String, String> emptyMap = Collections.emptyMap();
        Map<String,String> parameterMap = (initParameters != null ? initParameters : emptyMap);
        IntegrationFilterConfig config = newFilterConfig(parameterMap);
        
        if (filter instanceof ApplicationContextAware) {
            ApplicationContextAware awa = (ApplicationContextAware) filter;
            awa.setApplicationContext(applicationContext);
        }
        
        initFilterProperties(filter);       
        filter.init(config);
    }

    /* ***************** public methods **************** */
    
    public String getFilterName() {
        return filterName;
    }

    /* ***************** protected methods **************** */

    protected IntegrationFilterConfig newFilterConfig(Map<String, String> parameterMap) {
        IntegrationFilterConfig config = new IntegrationFilterConfig(parameterMap, this.servletContext, this.filterName);
        return config;
    }

    /**
     * Hook for subclasses to customise filter properties
     */
    protected void initFilterProperties(Filter servlet) {
    }

    /* ***************** Protected getters **************** */

    protected ServletContext getServletContext() {
        return servletContext;
    }

    /* ***************** DisposableBean implementation **************** */
    
    public void destroy() throws Exception {
        filter.destroy();
    }

    /* ***************** Protected getters **************** */

    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    protected ModuleDefinition getModuleDefintion() {
        return moduleDefintion;
    }
    
    /* ***************** injection setters **************** */

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }   

    public void setInitParameters(Map<String, String> initParameters) {
        this.initParameters = initParameters;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public void setFilterClass(Class<?> servletClass) {
        this.filterClass = servletClass;
    }

    public void setModuleDefinition(ModuleDefinition moduleDefinition) {
        this.moduleDefintion = moduleDefinition;
    }

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

}
