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

package org.impalaframework.web.spring.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.web.servlet.qualifier.WebAttributeQualifier;
import org.impalaframework.web.spring.helper.ImpalaServletUtils;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Extension of {@link BaseDelegatingFilterProxy} which passes in login call to
 * user-configured bean and module combination. The target bean must implement
 * the {@link Filter} interface. Inspired by the Spring
 * {@link org.springframework.web.filter.DelegatingFilterProxy}
 * @author Phil Zoio
 */
public abstract class BaseDelegatingFilterProxy extends GenericFilterBean {
    
    private String targetModuleName;
    
    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
    
        Assert.notNull(targetModuleName, "targetModuleName cannot be null");
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Filter delegate = getDelegateFilter(request);
        invokeDelegate(delegate, request, response, filterChain);
    }

    protected Filter getDelegateFilter(ServletRequest request) {
        ServletContext servletContext = getServletContext();

        ModuleManagementFacade moduleManagementFacade = ImpalaServletUtils.getModuleManagementFacade(servletContext);
        WebAttributeQualifier qualifier = getWebAttributeQualifier(moduleManagementFacade);
        String applicationId = getApplicationId(moduleManagementFacade);
        
        String attributeName = qualifier.getQualifiedAttributeName(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationId, targetModuleName);
        
        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext, attributeName);
        
        if (applicationContext == null) {
            throw new ConfigurationException("No root web application context associated with module '" + targetModuleName + "'");
        }
        
        final String beanName = getBeanName(request);
        final Object bean = applicationContext.getBean(beanName, Filter.class);
        if (!(bean instanceof Filter)) {
            throw new ConfigurationException("Delegate bean '" + beanName + "' from module '" + targetModuleName + "' is not an instance of " + Filter.class.getName());
        }
        return ObjectUtils.cast(bean, Filter.class);
    }

    protected WebAttributeQualifier getWebAttributeQualifier(ModuleManagementFacade moduleManagementFacade) {
        return (WebAttributeQualifier) moduleManagementFacade.getBean("webAttributeQualifier", WebAttributeQualifier.class);
    }

    protected String getApplicationId(ModuleManagementFacade moduleManagementFacade) {
        return moduleManagementFacade.getApplicationManager().getCurrentApplication().getId();
    }

    protected abstract String getBeanName(ServletRequest request);
    
    protected void invokeDelegate(Filter delegate, ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        delegate.doFilter(request, response, filterChain);
    }
    
    public void setTargetModuleName(String moduleName) {
        this.targetModuleName = moduleName;
    }
    
}
