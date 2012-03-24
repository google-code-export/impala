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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionAware;
import org.impalaframework.util.ObjectMapUtils;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.web.listener.ServletContextListenerFactoryBean;
import org.impalaframework.web.servlet.invocation.ModuleHttpServiceInvoker;
import org.impalaframework.web.servlet.invocation.ModuleInvokerContributor;
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
public class ModuleHttpServiceInvokerBuilder implements BeanFactoryAware, InitializingBean, DisposableBean, ServletContextAware, ModuleDefinitionAware {

    //TODO make so that this is automatically registered with application context
    
    private Log logger = LogFactory.getLog(ModuleHttpServiceInvokerBuilder.class);
    
    private BeanFactory beanFactory;
    
    private ServletContext servletContext;
    
    private String moduleName;

    /**
     * Goes through the contributors, and for each suffix, find the relevant
     * servlets and filter registered with the module. Instantiates a
     * {@link ModuleHttpServiceInvoker}, set the servlet and filter mapping
     * mappings, and bind to the {@link ServletContext} context using the
     * attribute <code>ModuleHttpServiceInvoker.class.getName()+"."+moduleDefinition.getName()</code>
     */
    public void afterPropertiesSet() throws Exception {
        
        ModuleHttpServiceInvoker invoker = buildInvoker();
        servletContext.setAttribute(getAttributeName(), invoker);
    }

    /**
     * Unbinds the {@link ModuleHttpServiceInvoker} registered with the
     * {@link ServletContext}
     */
    public void destroy() throws Exception {
        servletContext.removeAttribute(getAttributeName());
    }

    /* ***************** package level methods **************** */

    @SuppressWarnings("unchecked")
    ModuleHttpServiceInvoker buildInvoker() throws Exception {
        
        ListableBeanFactory beanFactory = ObjectUtils.cast(this.beanFactory, ListableBeanFactory.class);
        beanFactory.getBeansOfType(ServletContextListenerFactoryBean.class);
        Map<String, FilterFactoryBean> filterFactoryBeans = beanFactory.getBeansOfType(FilterFactoryBean.class);
        Map<String, ServletFactoryBean> servletFactoryBeans = beanFactory.getBeansOfType(ServletFactoryBean.class);
        Map<String, ModuleInvokerContributor> contributors = beanFactory.getBeansOfType(ModuleInvokerContributor.class);
        
        if (logger.isDebugEnabled()) {
            logger.debug("Contributors: " + contributors.values());
            logger.debug("Servlets: " + servletFactoryBeans.values());
            logger.debug("Filters: " + filterFactoryBeans.values());
        }
        
        Map<String, ServletFactoryBean> servletsByName = buildServletsByName(servletFactoryBeans);
        Map<String, FilterFactoryBean> filtersByName = buildFiltersByName(filterFactoryBeans);

        Map<String, List<FilterFactoryBean>> suffixFiltersMapping = new HashMap<String, List<FilterFactoryBean>>();
        Map<String, ServletFactoryBean> suffixServletMapping = new HashMap<String, ServletFactoryBean>();
        
        if (!contributors.isEmpty()) {
            Collection<ModuleInvokerContributor> values = contributors.values();
    
            for (ModuleInvokerContributor contributor : values) {
                String suffix = contributor.getSuffix();
                String[] filterNames = contributor.getFilterNames();
                
                if (filterNames != null) {
                    List<FilterFactoryBean> filterFactories = new ArrayList<FilterFactoryBean>();
                    for (String filterName : filterNames) {
                        FilterFactoryBean filterFactoryBean = filtersByName.get(filterName);
                        if (filterFactoryBean == null) {
                            throw new ConfigurationException("Suffix '" + suffix + "' has mapping for filter '" + filterName + "' for which no named filter definition is present in the current module.");
                        }
                        filterFactories.add(filterFactoryBean);
                    }
                    suffixFiltersMapping.put(suffix, filterFactories);
                }
                
                String servletName = contributor.getServletName();
                if (servletName != null) {
                    ServletFactoryBean servletFactoryBean = servletsByName.get(servletName);
                    if (servletFactoryBean == null) {
                        throw new ConfigurationException("Suffix '" + suffix + "' has mapping for servlet '" + servletName + "' for which no named servlet definition is present in the current module.");
                    }
                    suffixServletMapping.put(suffix, servletFactoryBean);
                }
            }
        } else {      
 
            maybeDebug("Module '" + moduleName + "' has no contributions. Looking for servlet matching module name ...");
            
            //if no contributions, first look for servlet whose name is same as module name
            FilterFactoryBean filter = null;
            ServletFactoryBean servlet = servletsByName.get(this.moduleName);
            
            if (servlet == null) {

                maybeDebug("Looking for filter matching module name ...");

                //if not found, look for filter whose name is same as module name
                filter = filtersByName.get(this.moduleName);
                if (filter == null) {
                    
                    maybeDebug("Looking for single servlet definition ...");

                    //check that there is only one servlet, if > 1, throw exception
                    if (servletsByName.size() > 1) {
                        throw new ConfigurationException("Cannot determine default servlet for module '" + moduleName + "' as more than one servlet is registered for this module.");
                    }
                    if (servletsByName.size() == 1) {
                        servlet = ObjectMapUtils.getFirstValue(servletsByName);
                    }
                    
                    if (servlet == null) {
                        
                        maybeDebug("Looking for single filter definition ...");

                        //check that there is only one filter, if > 1, throw exception
                        if (filtersByName.size() > 1) {
                            throw new ConfigurationException("Cannot determine default filter for module '" + moduleName + "' as more than one filter is registered for this module.");
                        }
                        if (filtersByName.size() == 1) {
                            filter = ObjectMapUtils.getFirstValue(filtersByName);
                        }
                    }
                }
            }
            
            if (servlet != null) {
                suffixServletMapping.put("*", servlet);
                
                maybeDebug("Mapping servlet " + servlet + " to all paths (*)");
            }
            else {
                if (filter != null) {
                    suffixFiltersMapping.put("*", Collections.singletonList(filter));
                    
                    maybeDebug("Mapping filter " + filter + " to all paths (*)");
                    
                } else {
                    maybeDebug("No servlet or filters registered for module '" + moduleName + "'");
                }
            }          
        }
        
        Map<String, List<Filter>> suffixFilters = initializeFilters(suffixFiltersMapping);
        Map<String, Servlet> suffixServlets = initializeServlets(suffixServletMapping);
        
        ModuleHttpServiceInvoker invoker = new ModuleHttpServiceInvoker(suffixFilters, suffixServlets);
        
        if (logger.isDebugEnabled()) {
            logger.debug("Module '" + moduleName + "' returning " + ModuleHttpServiceInvoker.class.getSimpleName() + ": " + invoker);
        }
        
        return invoker;
    }

    private void maybeDebug(String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    /* ***************** private methods **************** */

    private Map<String, Servlet> initializeServlets(
            Map<String, ServletFactoryBean> suffixServletMapping)
            throws Exception {
        Map<String, Servlet> suffixServlets = new HashMap<String, Servlet>();
        Set<String> servletSuffixes = suffixServletMapping.keySet();
        for (String suffix : servletSuffixes) {
            ServletFactoryBean servletFactoryBean = suffixServletMapping.get(suffix);
            suffixServlets.put(suffix, (Servlet) servletFactoryBean.getObject());
        }
        return suffixServlets;
    }

    private Map<String, List<Filter>> initializeFilters(
            Map<String, List<FilterFactoryBean>> suffixFiltersMapping)
            throws Exception {
        //now initialize the filters
        Map<String, List<Filter>> suffixFilters = new HashMap<String, List<Filter>>();
        Set<String> filterSuffixes = suffixFiltersMapping.keySet();
        for (String suffix : filterSuffixes) {
            List<FilterFactoryBean> list = suffixFiltersMapping.get(suffix);
            List<Filter> filterList = new ArrayList<Filter>(list.size());
            for (FilterFactoryBean filterFactoryBean : list) {
                filterList.add((Filter) filterFactoryBean.getObject());
            }
            suffixFilters.put(suffix, filterList);
        }
        return suffixFilters;
    }

    private String getAttributeName() {
        return "shared:" + ModuleHttpServiceInvoker.class.getName()+ "." + moduleName;
    }
    
    private Map<String, FilterFactoryBean> buildFiltersByName(Map<String, FilterFactoryBean> filterFactoryBeans) {
        Map<String,FilterFactoryBean> filtersByName = new HashMap<String, FilterFactoryBean>();
        
        Set<String> keySet = filterFactoryBeans.keySet();
        for (String key : keySet) {
            FilterFactoryBean bean = filterFactoryBeans.get(key);
            filtersByName.put(bean.getFilterName(), bean);
        }
        return filtersByName;
    }

    private Map<String, ServletFactoryBean> buildServletsByName(Map<String, ServletFactoryBean> servletFactoryBeans) {

        Map<String,ServletFactoryBean> servletsByName = new HashMap<String, ServletFactoryBean>();
        
        Set<String> keySet = servletFactoryBeans.keySet();
        for (String key : keySet) {
            ServletFactoryBean bean = servletFactoryBeans.get(key);
            servletsByName.put(bean.getServletName(), bean);
        }
        return servletsByName;
    }

    /* ***************** automatic property setter methods **************** */

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
    
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setModuleDefinition(ModuleDefinition moduleDefinition) {
        this.moduleName = moduleDefinition.getName();
    }
}
